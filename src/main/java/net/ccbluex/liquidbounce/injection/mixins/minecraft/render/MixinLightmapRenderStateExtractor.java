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

package net.ccbluex.liquidbounce.injection.mixins.minecraft.render;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.ccbluex.liquidbounce.features.module.modules.render.ModuleFullBright;
import net.minecraft.client.renderer.LightTexture;
import org.jspecify.annotations.NullMarked;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@NullMarked
@Mixin(LightTexture.class)
public abstract class MixinLightmapRenderStateExtractor {

    /**
     * Gamma value that was last used to force a lightmap refresh for FullBright.
     */
    @Unique
    private double lokoami$lastForcedGamma;

    /**
     * Whether FullBright's gamma mode was active on the previous lightmap update.
     */
    @Unique
    private boolean lokoami$fullBrightActive;

    /**
     * Target:
     * <pre>
     *     float brightnessOption = ((Double)this.minecraft.options.gamma().get()).floatValue();
     * </pre>
     *
     * Original hook also served XRay's FullBright. XRay is removed from this client, so only the
     * FullBright module's gamma override remains.
     */
    @ModifyExpressionValue(method = "updateLightTexture", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/OptionInstance;get()Ljava/lang/Object;", ordinal = 2))
    private Object injectFullBright(Object original) {
        if (ModuleFullBright.FullBrightGamma.INSTANCE.getRunning()) {
            return ModuleFullBright.FullBrightGamma.INSTANCE.getGamma();
        }

        return original;
    }

    /**
     * Keeps the lightmap updating while FullBright's gamma mode is active.
     *
     * <p>{@code updateLightTexture} starts with {@code if (!this.updateLightTexture) return;}, and that
     * flag is only ever set by {@link LightTexture#tick()}. GameRenderer calls {@code updateLightTexture}
     * unconditionally every frame, so overriding this single read is what lets the gamma override above
     * reach the shader.
     *
     * <p>Without this, the gamma override is silently dead on any setup that caches the lightmap. The
     * override only replaces the value returned by {@code options.gamma().get()} inside this method, so
     * the real option never changes — and lightmap caching mods (BadOptimizations'
     * {@code enable_lightmap_caching}, for instance) compare exactly that real value, conclude nothing
     * changed, cancel {@code tick()} and starve this method of updates.
     *
     * <p>The refresh is driven by our own gamma value, which keeps the module's intended fade-in and
     * costs nothing once the value settles. One refresh is also forced on disable so the lightmap
     * reverts to the real gamma.
     */
    @ModifyExpressionValue(
        method = "updateLightTexture",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/renderer/LightTexture;updateLightTexture:Z",
            opcode = Opcodes.GETFIELD
        )
    )
    private boolean forceLightmapRefreshForFullBright(boolean original) {
        if (ModuleFullBright.FullBrightGamma.INSTANCE.getRunning()) {
            double gamma = ModuleFullBright.FullBrightGamma.INSTANCE.getGamma();

            if (!this.lokoami$fullBrightActive || gamma != this.lokoami$lastForcedGamma) {
                this.lokoami$fullBrightActive = true;
                this.lokoami$lastForcedGamma = gamma;
                return true;
            }

            return original;
        }

        if (this.lokoami$fullBrightActive) {
            this.lokoami$fullBrightActive = false;
            return true;
        }

        return original;
    }

}