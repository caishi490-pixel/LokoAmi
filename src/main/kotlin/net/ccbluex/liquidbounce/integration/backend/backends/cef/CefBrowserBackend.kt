/*
 * This file is part of LiquidBounce (https://github.com/CCBlueX/LiquidBounce)
 *
 * Copyright (c) 2015 - 2026 CCBlueX
 *
 * LiquidBounce is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LiquidBounce is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LiquidBounce. If not, see <https://www.gnu.org/licenses/>.
 */
package net.ccbluex.liquidbounce.integration.backend.backends.cef

import net.ccbluex.liquidbounce.api.core.HttpClient
import net.ccbluex.liquidbounce.config.ConfigSystem
import net.ccbluex.liquidbounce.event.EventListener
import net.ccbluex.liquidbounce.integration.backend.BrowserAccelerationFlags
import net.ccbluex.liquidbounce.integration.backend.BrowserBackend
import net.ccbluex.liquidbounce.integration.backend.browser.BrowserSettings
import net.ccbluex.liquidbounce.integration.backend.browser.BrowserState
import net.ccbluex.liquidbounce.integration.backend.browser.BrowserViewport
import net.ccbluex.liquidbounce.integration.backend.input.InputAcceptor
import net.ccbluex.liquidbounce.integration.task.MCEFProgressForwarder
import net.ccbluex.liquidbounce.integration.task.TaskManager
import net.ccbluex.liquidbounce.integration.task.type.Task
import net.ccbluex.liquidbounce.mcef.MCEF
import net.ccbluex.liquidbounce.mcef.MCEFAccelerationSupport
import net.ccbluex.liquidbounce.mcef.MCEFDownloadManager
import net.ccbluex.liquidbounce.mcef.MCEFPlatform
import net.ccbluex.liquidbounce.mcef.download.MCEFProvidedResourceManager
import net.ccbluex.liquidbounce.utils.client.error.ErrorHandler
import net.ccbluex.liquidbounce.utils.client.error.QuickFix
import net.ccbluex.liquidbounce.utils.client.error.errors.JcefIsntCompatible
import net.ccbluex.liquidbounce.utils.client.formatAsCapacity
import net.ccbluex.liquidbounce.utils.client.logger
import net.ccbluex.liquidbounce.utils.client.mc
import net.ccbluex.liquidbounce.utils.kotlin.sortedInsert
import net.ccbluex.liquidbounce.utils.validation.HashValidator
import org.cef.browser.CefFrame
import org.cef.handler.CefLifeSpanHandlerAdapter
import org.cef.handler.CefLoadHandler
import org.cef.handler.CefLoadHandlerAdapter
import org.cef.network.CefRequest
import java.io.File
import java.util.zip.ZipFile

/**
 * The time threshold for cleaning up old cache directories.
 */
private const val CACHE_CLEANUP_THRESHOLD = 1000 * 60 * 60 * 24 * 7 // 7 days

/**
 * Jar prefix of the JCEF runtime shipped by the offline build (`./gradlew assemble -Poffline`).
 * Everything below it mirrors a commit directory, so extraction is a plain prefix strip.
 */
private const val BUNDLED_JCEF_PREFIX = "mcef/"

/**
 * Uses a modified fork of the JCEF library browser backend made for Minecraft.
 * This browser backend is based on Chromium and is the most advanced browser backend.
 * JCEF is available through the MCEF library, which provides a Minecraft compatible version of JCEF.
 *
 * @see <a href="https://github.com/CCBlueX/java-cef/">JCEF</a>
 * @see <a href="https://github.com/CCBlueX/mcef/">MCEF</a>
 *
 * @author Izuna <izuna.seikatsu@ccbluex.net>
 */
@Suppress("TooManyFunctions")
class CefBrowserBackend : BrowserBackend, EventListener {

    private val mcefFolder = ConfigSystem.rootFolder.resolve("mcef")
    private val librariesFolder = mcefFolder.resolve("libraries")
    private val cacheFolder = mcefFolder.resolve("cache")

    override val isInitialized: Boolean
        get() = MCEF.INSTANCE.isInitialized
    override var browsers = mutableListOf<CefBrowser>()
    override var accelerationFlags = BrowserAccelerationFlags.UNSUPPORTED

    @Suppress("ThrowingExceptionsWithoutMessageOrCause")
    override fun makeDependenciesAvailable(taskManager: TaskManager, whenAvailable: () -> Unit) {
        // Clean up old cache directories
        cleanup()

        if (!MCEF.INSTANCE.isInitialized) {
            MCEF.INSTANCE.settings.apply {
                userAgent = HttpClient.DEFAULT_AGENT
                cacheDirectory = cacheFolder.resolve(System.currentTimeMillis().toString(16)).apply {
                    deleteOnExit()
                }
                librariesDirectory = librariesFolder

                // CEF Switches
                appendCefSwitches("--no-proxy-server")
            }

            val platform = MCEFPlatform.getPlatform()
            val commit = MCEF.INSTANCE.javaCefCommit

            // A jar built with -Poffline carries the JCEF runtime inside itself. Extract it once so
            // even a fresh install never has to reach mcef's download hosts.
            if (commit != null && bundledJcefNeedsExtraction(platform, commit)) {
                bundledJcefExtractionAttempted = true

                taskManager.launch("MCEF") { task ->
                    runCatching { extractBundledJcef(commit, task) }
                        .onFailure { logger.error("Failed to extract the bundled JCEF runtime", it) }

                    // Re-enter to pick up the freshly extracted runtime. The attempt flag above
                    // stops this from looping when the payload turned out to be unusable.
                    mc.execute { makeDependenciesAvailable(taskManager, whenAvailable) }
                }

                return
            }

            // Prefer a JCEF runtime that is already on disk; only fall back to mcef's downloader
            // when there is nothing usable locally.
            val resourceManager = localJcefResourceManager() ?: MCEF.INSTANCE.newResourceManager()

            // Check if system is compatible with MCEF (JCEF)
            if (!resourceManager.isSystemCompatible) {
                throw JcefIsntCompatible()
            }

            HashValidator.validateFolder(resourceManager.commitDirectory)

            if (resourceManager.requiresDownload()) {
                taskManager.launch("MCEF") { task ->
                    resourceManager.registerProgressListener(MCEFProgressForwarder(task))

                    runCatching {
                        resourceManager.downloadJcef()
                        mc.execute(whenAvailable)
                    }.onFailure {
                        ErrorHandler.fatal(
                            error = it,
                            quickFix = QuickFix.DOWNLOAD_JCEF_FAILED,
                            additionalMessage = "Downloading jcef"
                        )
                    }
                }
            } else {
                whenAvailable()
            }
        }
    }

    /**
     * Whether the bundled JCEF runtime was already extracted during this session.
     *
     * Keeps [makeDependenciesAvailable] from re-entering itself forever when the jar ships a payload
     * that cannot be turned into a usable runtime.
     */
    @Volatile
    private var bundledJcefExtractionAttempted = false

    /**
     * True when this jar ships a JCEF runtime and the local copy is not complete yet.
     */
    private fun bundledJcefNeedsExtraction(platform: MCEFPlatform, commit: String): Boolean {
        if (bundledJcefExtractionAttempted) {
            return false
        }

        val platformDirectory = librariesFolder.resolve(commit).resolve(platform.normalizedName)

        if (platform.requiredLibraries().all { platformDirectory.resolve(it).isFile }) {
            return false
        }

        val jar = codeSourceJar() ?: return false

        return ZipFile(jar).use { zip ->
            zip.entries().asSequence().any { it.name.startsWith(BUNDLED_JCEF_PREFIX) }
        }
    }

    /**
     * Extracts the JCEF runtime bundled under [BUNDLED_JCEF_PREFIX] into `libraries/<commit>/`.
     *
     * The payload mirrors a commit directory itself — the checksum manifest, the platform checksum
     * file and the platform folder — so only the prefix is stripped and nothing else is transformed.
     * Files therefore stay byte-identical, which is exactly what [HashValidator] verifies afterwards.
     */
    private fun extractBundledJcef(commit: String, task: Task) {
        val jar = codeSourceJar() ?: return
        val target = librariesFolder.resolve(commit)

        ZipFile(jar).use { zip ->
            val entries = zip.entries().asSequence()
                .filter { !it.isDirectory && it.name.startsWith(BUNDLED_JCEF_PREFIX) }
                .toList()

            if (entries.isEmpty()) {
                return
            }

            val progress = MCEFProgressForwarder(task)
            val taskName = "Extracting JCEF"
            val total = entries.sumOf { it.size }
            var written = 0L

            progress.onFileStart(taskName)

            entries.forEach { entry ->
                val outFile = File(target, entry.name.removePrefix(BUNDLED_JCEF_PREFIX))

                // Skipping files that already match in size keeps repeat launches cheap.
                if (!outFile.isFile || outFile.length() != entry.size) {
                    outFile.parentFile?.mkdirs()

                    zip.getInputStream(entry).use { input ->
                        outFile.outputStream().use { output -> input.copyTo(output) }
                    }
                }

                written += entry.size
                progress.onFileProgress(taskName, written, total, false)
            }

            progress.onFileEnd(taskName)
        }

        logger.info("Extracted bundled JCEF into {}", target)
    }

    /**
     * The jar this class was loaded from, or `null` in a development environment where classes are
     * plain files on disk and nothing is bundled.
     */
    private fun codeSourceJar(): File? {
        val location = runCatching {
            File(CefBrowserBackend::class.java.protectionDomain.codeSource.location.toURI())
        }.getOrNull()

        return location?.takeIf { it.isFile }
    }

    /**
     * Returns a resource manager backed by an already-installed JCEF runtime, or `null` when the
     * local copy is missing/incomplete and mcef has to take over.
     *
     * mcef's default resource manager always fetches the java-cef checksum over the network on
     * **every** launch, even when the runtime is already installed. That is what makes startup sit
     * there for minutes whenever the API hosts are slow or unreachable. [MCEFProvidedResourceManager]
     * skips both the checksum check and the download, so a complete local runtime means the client
     * starts fully offline.
     */
    private fun localJcefResourceManager(): MCEFDownloadManager? {
        val platform = MCEFPlatform.getPlatform()
        val commit = MCEF.INSTANCE.javaCefCommit ?: return null
        val platformDirectory = librariesFolder.resolve(commit).resolve(platform.normalizedName)

        if (!platformDirectory.isDirectory) {
            return null
        }

        val missing = platform.requiredLibraries().filterNot { platformDirectory.resolve(it).isFile }

        if (missing.isNotEmpty()) {
            logger.info(
                "Local JCEF at {} is incomplete (missing {}), falling back to mcef",
                platformDirectory, missing.joinToString()
            )
            return null
        }

        logger.info("Using local JCEF at {} — skipping checksum check and download", platformDirectory)

        return MCEFProvidedResourceManager(
            platformDirectory,
            MCEF.INSTANCE.settings.hosts.toTypedArray(),
            commit,
            platform,
            librariesFolder
        )
    }

    /**
     * Cleans up old cache directories.
     *
     * TODO: Check if we have an active PID using the cache directory, if so, check if the LiquidBounce
     *   process attached to the JCEF PID is still running or not. If not, we could kill the JCEF process
     *   and clean up the cache directory.
     */
    fun cleanup() {
        if (cacheFolder.exists()) {
            runCatching {
                cacheFolder.listFiles()
                    ?.filter { file ->
                        file.isDirectory && System.currentTimeMillis() - file.lastModified() > CACHE_CLEANUP_THRESHOLD
                    }
                    ?.sumOf { file ->
                        try {
                            val fileSize = file.walkTopDown().sumOf { uFile -> uFile.length() }
                            file.deleteRecursively()
                            fileSize
                        } catch (e: Exception) {
                            logger.error("Failed to clean up old cache directory", e)
                            0
                        }
                    } ?: 0
            }.onFailure {
                // Not a big deal, not fatal.
                logger.error("Failed to clean up old JCEF cache directories", it)
            }.onSuccess { size ->
                if (size > 0) {
                    logger.info("Cleaned up ${size.formatAsCapacity()} JCEF cache directories")
                }
            }
        }
    }

    override fun start() {
        if (!MCEF.INSTANCE.isInitialized) {
            MCEF.INSTANCE.initialize()

            MCEF.INSTANCE.client.handle.addLifeSpanHandler(object : CefLifeSpanHandlerAdapter() {
                override fun onAfterCreated(cefBrowser: org.cef.browser.CefBrowser) {
                    markInitialized(cefBrowser)
                    super.onAfterCreated(cefBrowser)
                }
            })

            MCEF.INSTANCE.client.addLoadHandler(object : CefLoadHandlerAdapter() {

                override fun onLoadStart(
                    cefBrowser: org.cef.browser.CefBrowser, frame: CefFrame?,
                    transitionType: CefRequest.TransitionType?
                ) {
                    updateStateForBrowser(cefBrowser, BrowserState.Loading)
                    super.onLoadStart(cefBrowser, frame, transitionType)
                }

                override fun onLoadEnd(cefBrowser: org.cef.browser.CefBrowser, frame: CefFrame?, httpStatusCode: Int) {
                    updateStateForBrowser(cefBrowser, BrowserState.Success(httpStatusCode))
                    super.onLoadEnd(cefBrowser, frame, httpStatusCode)
                }

                override fun onLoadError(
                    cefBrowser: org.cef.browser.CefBrowser, frame: CefFrame?,
                    errorCode: CefLoadHandler.ErrorCode?, errorText: String?, failedUrl: String?
                ) {
                    updateStateForBrowser(
                        cefBrowser,
                        BrowserState.Failure(
                            errorCode?.code ?: -1,
                            errorText ?: "Unknown Error",
                            failedUrl ?: "Unknown URL"
                        )
                    )
                    super.onLoadError(cefBrowser, frame, errorCode, errorText, failedUrl)
                }

            })
        }

        val support = MCEFAccelerationSupport.getAccelerationSupport()
        accelerationFlags = if (support.isSupported) {
            BrowserAccelerationFlags(isSupported = true, isBeta = support.isBeta)
        } else {
            BrowserAccelerationFlags.UNSUPPORTED
        }
    }

    override fun stop() {
        MCEF.INSTANCE.shutdown()
        MCEF.INSTANCE.settings.cacheDirectory?.deleteRecursively()
    }

    override fun update() {
        if (MCEF.INSTANCE.isInitialized) {
            try {
                MCEF.INSTANCE.app.handle.N_DoMessageLoopWork()
            } catch (e: Exception) {
                logger.error("Failed to draw browser globally", e)
            }
        }
    }

    override fun createBrowser(
        url: String,
        position: BrowserViewport,
        settings: BrowserSettings,
        priority: Short,
        inputAcceptor: InputAcceptor?
    ) = CefBrowser(this, url, position, settings, priority, inputAcceptor)
        .apply(::addBrowser)

    private fun addBrowser(browser: CefBrowser) {
        browsers.sortedInsert(browser, CefBrowser::priority)
    }

    internal fun removeBrowser(browser: CefBrowser) {
        browsers.remove(browser)
    }

    fun getBrowserByApi(apiInstance: org.cef.browser.CefBrowser) = browsers.find { it.browserApi == apiInstance }

    private fun markInitialized(apiInstance: org.cef.browser.CefBrowser) {
        val browser = getBrowserByApi(apiInstance)
        if (browser != null) {
            if (!browser.isInitialized) {
                browser.isInitialized = true
            }
        } else {
            logger.warn("[CefBrowser-${apiInstance.hashCode()}] Browser Instance not present in BrowserManager")
        }
    }

    private fun updateStateForBrowser(apiInstance: org.cef.browser.CefBrowser, state: BrowserState) {
        val browser = getBrowserByApi(apiInstance)
        if (browser != null) {
            browser.state = state
        } else {
            logger.warn("[CefBrowser-${apiInstance.hashCode()}] Browser Instance not present in BrowserManager")
        }
    }

}
