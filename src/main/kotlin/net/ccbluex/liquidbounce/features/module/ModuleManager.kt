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
package net.ccbluex.liquidbounce.features.module

import net.ccbluex.liquidbounce.features.module.modules.custom.ModuleFishGuard
import net.ccbluex.liquidbounce.features.module.modules.custom.ModuleSimmcChannel
import net.ccbluex.liquidbounce.features.module.modules.custom.ModuleAdminActionLog
import net.ccbluex.liquidbounce.features.module.modules.custom.ModuleArcaneRange
import net.ccbluex.liquidbounce.features.module.modules.custom.ModuleEntitySizeNormalize
import net.ccbluex.liquidbounce.features.module.modules.custom.ModulePacketCapture
import net.ccbluex.liquidbounce.features.module.modules.movement.ModuleSprint
import net.ccbluex.liquidbounce.features.module.modules.render.ModuleFullBright
import it.unimi.dsi.fastutil.objects.ObjectRBTreeSet
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap
import net.ccbluex.fastutil.mapToArray
import net.ccbluex.liquidbounce.config.ConfigSystem
import net.ccbluex.liquidbounce.config.autoconfig.AutoConfig
import net.ccbluex.liquidbounce.config.types.VALUE_NAME_ORDER
import net.ccbluex.liquidbounce.event.EventListener
import net.ccbluex.liquidbounce.event.events.DisconnectEvent
import net.ccbluex.liquidbounce.event.events.KeyboardKeyEvent
import net.ccbluex.liquidbounce.event.events.MouseButtonEvent
import net.ccbluex.liquidbounce.event.events.WorldChangeEvent
import net.ccbluex.liquidbounce.event.handler
import net.ccbluex.liquidbounce.event.sequenceHandler
import net.ccbluex.liquidbounce.event.tickUntil
import net.ccbluex.liquidbounce.features.module.modules.exploit.ModulePlugins
import net.ccbluex.liquidbounce.features.module.modules.exploit.ModulePortalMenu
import net.ccbluex.liquidbounce.features.module.modules.exploit.ModuleYggdrasilSignatureFix
import net.ccbluex.liquidbounce.features.module.modules.misc.ModuleBetterTab
import net.ccbluex.liquidbounce.features.module.modules.misc.ModuleGUICloser
import net.ccbluex.liquidbounce.features.module.modules.misc.ModuleItemScroller
import net.ccbluex.liquidbounce.features.module.modules.misc.ModuleMiddleClickAction
import net.ccbluex.liquidbounce.features.module.modules.misc.ModuleNotifier
import net.ccbluex.liquidbounce.features.module.modules.misc.ModulePacketLogger
import net.ccbluex.liquidbounce.features.module.modules.misc.betterchat.ModuleBetterChat
import net.ccbluex.liquidbounce.features.module.modules.misc.debugrecorder.ModuleDebugRecorder
import net.ccbluex.liquidbounce.features.module.modules.misc.reporthelper.ModuleReportHelper
import net.ccbluex.liquidbounce.features.module.modules.render.ModuleAnimations
import net.ccbluex.liquidbounce.features.module.modules.render.ModuleAspect
import net.ccbluex.liquidbounce.features.module.modules.render.ModuleAutoF5
import net.ccbluex.liquidbounce.features.module.modules.render.ModuleBetterInventory
import net.ccbluex.liquidbounce.features.module.modules.render.ModuleBlockOutline
import net.ccbluex.liquidbounce.features.module.modules.render.ModuleBreadcrumbs
import net.ccbluex.liquidbounce.features.module.modules.render.ModuleClickGui
import net.ccbluex.liquidbounce.features.module.modules.render.ModuleCombineMobs
import net.ccbluex.liquidbounce.features.module.modules.render.ModuleCustomAmbience
import net.ccbluex.liquidbounce.features.module.modules.render.ModuleDamageParticles
import net.ccbluex.liquidbounce.features.module.modules.render.ModuleDebug
import net.ccbluex.liquidbounce.features.module.modules.render.ModuleFreeCam
import net.ccbluex.liquidbounce.features.module.modules.render.ModuleFreeLook
import net.ccbluex.liquidbounce.features.module.modules.render.ModuleHud
import net.ccbluex.liquidbounce.features.module.modules.render.ModuleItemTags
import net.ccbluex.liquidbounce.features.module.modules.render.ModuleNoBob
import net.ccbluex.liquidbounce.features.module.modules.render.ModuleNoFov
import net.ccbluex.liquidbounce.features.module.modules.render.ModuleNoHurtCam
import net.ccbluex.liquidbounce.features.module.modules.render.ModuleNoSwing
import net.ccbluex.liquidbounce.features.module.modules.render.ModuleParticles
import net.ccbluex.liquidbounce.features.module.modules.render.ModuleQuickPerspectiveSwap
import net.ccbluex.liquidbounce.features.module.modules.render.ModuleSkinChanger
import net.ccbluex.liquidbounce.features.module.modules.render.ModuleSmoothCamera
import net.ccbluex.liquidbounce.features.module.modules.render.ModuleZoom
import net.ccbluex.liquidbounce.features.module.modules.render.crosshair.ModuleCrosshair
import net.ccbluex.liquidbounce.features.module.modules.render.hats.ModuleHats
import net.ccbluex.liquidbounce.features.module.modules.render.hitfx.ModuleHitFX
import net.ccbluex.liquidbounce.features.module.modules.world.ModuleAutoDisable
import net.ccbluex.liquidbounce.script.ScriptApiRequired
import net.ccbluex.liquidbounce.utils.client.clientStartDurationMs
import net.ccbluex.liquidbounce.utils.client.inGame
import net.ccbluex.liquidbounce.utils.client.logger
import net.ccbluex.liquidbounce.utils.client.mc
import net.ccbluex.liquidbounce.utils.input.InputBind
import org.lwjgl.glfw.GLFW

private val modules = ObjectRBTreeSet<ClientModule>(VALUE_NAME_ORDER)

/**
 * A fairly simple module manager
 */
object ModuleManager : EventListener, Collection<ClientModule> by modules {

    val modulesConfig = ConfigSystem.root("modules", modules)

    private const val SMART_MOUSE_HOLD_THRESHOLD_MS = 200L

    private enum class SmartBindKeyboardState {
        PENDING_ENABLED, PENDING_DISABLED, HOLDING,
    }
    private class SmartBindMouseState(val pendingEnabled: Boolean, val pressTimestamp: Long)

    private val smartKeyboardStates = Reference2ObjectArrayMap<ClientModule, SmartBindKeyboardState>()
    private val smartMouseStates = Reference2ObjectArrayMap<ClientModule, SmartBindMouseState>()

    /**
     * Handles keystrokes for module binds.
     * This also runs in GUIs, so that if a GUI is opened while a key is pressed,
     * any modules that need to be disabled on key release will be properly disabled.
     */
    @Suppress("unused")
    private val keyboardKeyHandler = handler<KeyboardKeyEvent> { event ->
        when (event.action) {
            GLFW.GLFW_PRESS -> if (mc.screen == null) {
                // Usually nobody actually wants a module to activate when they press the Minecraft debug key combo.
                if (mc.options.keyDebugModifier.isDown) return@handler
                for (m in modules) {
                    if (!m.bind.matchesKeyPress(event)) {
                        continue
                    }

                    when (m.bind.action) {
                        InputBind.BindAction.TOGGLE -> m.enabled = !m.enabled
                        InputBind.BindAction.HOLD -> m.enabled = true
                        InputBind.BindAction.SMART -> {
                            smartKeyboardStates[m] = if (m.enabled) {
                                SmartBindKeyboardState.PENDING_ENABLED
                            } else {
                                SmartBindKeyboardState.PENDING_DISABLED
                            }
                            m.enabled = true
                        }
                    }
                }
            }

            GLFW.GLFW_REPEAT -> {
                for (m in modules) {
                    if (m.bind.action != InputBind.BindAction.SMART ||
                        !m.bind.matchesKey(event.keyCode, event.scanCode) ||
                        m !in smartKeyboardStates
                    ) {
                        continue
                    }

                    smartKeyboardStates[m] = SmartBindKeyboardState.HOLDING
                }
            }

            GLFW.GLFW_RELEASE -> {
                for (m in modules) {
                    if (!m.bind.matchesKeyRelease(event)) {
                        continue
                    }

                    when (m.bind.action) {
                        InputBind.BindAction.HOLD -> m.enabled = false

                        InputBind.BindAction.SMART -> {
                            val stateBeforePress = smartKeyboardStates.remove(m) ?: continue
                            m.enabled = stateBeforePress == SmartBindKeyboardState.PENDING_DISABLED
                        }

                        InputBind.BindAction.TOGGLE -> {}
                    }
                }
            }
        }
    }

    @Suppress("unused")
    private val mouseButtonHandler = handler<MouseButtonEvent> { event ->
        when (event.action) {
            GLFW.GLFW_PRESS -> if (mc.screen == null) {
                for (m in modules) {
                    if (!m.bind.matchesMousePress(event)) {
                        continue
                    }

                    when (m.bind.action) {
                        InputBind.BindAction.TOGGLE -> m.enabled = !m.enabled
                        InputBind.BindAction.HOLD -> m.enabled = true
                        InputBind.BindAction.SMART -> {
                            smartMouseStates[m] = SmartBindMouseState(m.enabled, clientStartDurationMs)
                            m.enabled = true
                        }
                    }
                }
            }

            GLFW.GLFW_RELEASE -> {
                for (m in modules) {
                    if (!m.bind.matchesMouseRelease(event)) {
                        continue
                    }

                    when (m.bind.action) {
                        InputBind.BindAction.HOLD -> m.enabled = false

                        InputBind.BindAction.SMART -> {
                            val state = smartMouseStates.remove(m) ?: continue

                            // Mouse button events do not emit GLFW_REPEAT, so SMART falls back to:
                            // - hold if the press was long enough
                            // - toggle otherwise
                            val shouldFallbackToHold =
                                clientStartDurationMs - state.pressTimestamp >= SMART_MOUSE_HOLD_THRESHOLD_MS

                            if (shouldFallbackToHold) {
                                m.enabled = false
                            } else {
                                m.enabled = !state.pendingEnabled
                            }
                        }

                        InputBind.BindAction.TOGGLE -> {}
                    }
                }
            }
        }
    }

    /**
     * Handles world change and enables modules that are not enabled yet
     */
    @Suppress("unused")
    private val handleWorldChange = sequenceHandler<WorldChangeEvent> { event ->
        // Delayed start handling
        if (event.world != null) {
            tickUntil { inGame }
            AutoConfig.withLoading {
                for (module in modules) {
                    if (!module.enabled || module.calledSinceStartup) continue

                    try {
                        module.calledSinceStartup = true
                        // inGame is false here, so use onToggle0
                        module.onToggled(true)
                    } catch (e: Exception) {
                        logger.error("Failed to enable module ${module.name}", e)
                    }
                }
            }
        }

        // Store modules configuration after world change, happens on disconnect as well
        ConfigSystem.store(modulesConfig)
    }

    /**
     * Handles disconnect and if [ClientModule.disableOnQuit] is true disables module
     */
    @Suppress("unused")
    private val handleDisconnect = handler<DisconnectEvent> {
        for (module in modules) {
            if (module.disableOnQuit) {
                try {
                    module.enabled = false
                } catch (e: Exception) {
                    logger.error("Failed to disable module ${module.name}", e)
                }
            }
        }
    }

    /**
     * Register inbuilt client modules
     */
    @Suppress("LongMethod")
    fun registerInbuilt() {
        val builtin = arrayOf(
            // Custom
            ModuleFishGuard,
            ModuleSimmcChannel,
            ModuleAdminActionLog,
            ModuleArcaneRange,
            ModuleEntitySizeNormalize,
            ModulePacketCapture,

            // Combat

            // Exploit
            ModulePlugins,
            ModulePortalMenu,
            ModuleYggdrasilSignatureFix,

            // Fun

            // Misc
            ModuleGUICloser,
            ModuleBetterTab,
            ModuleItemScroller,
            ModuleBetterChat,
            ModuleMiddleClickAction,
            ModuleNotifier,
            ModuleReportHelper,
            ModulePacketLogger,
            ModuleDebugRecorder,

            // Movement
            ModuleSprint,

            // Player

            // Render
            ModuleFullBright,
            ModuleAnimations,
            ModuleBetterInventory,
            ModuleBlockOutline,
            ModuleBreadcrumbs,
            ModuleClickGui,
            ModuleDamageParticles,
            ModuleParticles,
            ModuleFreeCam,
            ModuleSmoothCamera,
            ModuleFreeLook,
            ModuleHud,
            ModuleHats,
            ModuleItemTags,
            ModuleHitFX,
            ModuleCombineMobs,
            ModuleAspect,
            ModuleAutoF5,
            ModuleNoBob,
            ModuleNoFov,
            ModuleNoHurtCam,
            ModuleNoSwing,
            ModuleCustomAmbience,
            ModuleQuickPerspectiveSwap,
            ModuleDebug,
            ModuleZoom,
            ModuleSkinChanger,
            ModuleCrosshair,

            // World
            ModuleAutoDisable,
        )

        builtin.forEach { module ->
            addModule(module)
            module.walkKeyPath()
            module.verifyFallbackDescription()
        }
    }

    fun addModule(module: ClientModule) {
        if (!modules.add(module)) {
            error("Module '${module.name}' is already registered.")
        }
        module.walkInit()
        module.onRegistration()
    }

    fun removeModule(module: ClientModule) {
        if (!modules.remove(module)) {
            error("Module '${module.name}' is not registered.")
        }
        if (module.enabled) {
            module.enabled = false
        }
        module.unregister()
    }

    fun clear() {
        modules.clear()
    }

    /**
     * This is being used by UltralightJS for the implementation of the ClickGUI. DO NOT REMOVE!
     */
    @JvmName("getCategories")
    @ScriptApiRequired
    fun getCategories() = ModuleCategories.entries.mapToArray { it.tag }

    @JvmName("getModules")
    @ScriptApiRequired
    fun getModules(): Collection<ClientModule> = modules

    @JvmName("getModuleByName")
    @ScriptApiRequired
    fun getModuleByName(module: String) = find { it.name.equals(module, true) }

    operator fun get(moduleName: String) = modules.find { it.name.equals(moduleName, true) }

}
