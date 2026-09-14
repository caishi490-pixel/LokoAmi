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

package net.ccbluex.liquidbounce.injection.mixins.minecraft.entity;

import net.ccbluex.liquidbounce.features.module.modules.movement.ModuleSprint;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.ccbluex.liquidbounce.event.EventManager;
import net.ccbluex.liquidbounce.event.events.PlayerSafeWalkEvent;
import net.ccbluex.liquidbounce.features.command.commands.ingame.fakeplayer.FakePlayer;
import net.ccbluex.liquidbounce.features.module.modules.render.hitfx.ModuleHitFX;
import net.ccbluex.liquidbounce.utils.aiming.RotationManager;
import net.ccbluex.liquidbounce.utils.aiming.features.MovementCorrection;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class MixinPlayer extends MixinLivingEntity {

    @Shadow
    public abstract void tick();

    @Shadow
    public abstract SoundSource getSoundSource();

    /**
     * Hook safe walk event
     */
    @ModifyReturnValue(method = "isStayingOnGroundSurface", at = @At("RETURN"))
    private boolean hookSafeWalk(boolean original) {
        final var event = EventManager.INSTANCE.callEvent(new PlayerSafeWalkEvent());
        return original || event.isSafeWalk();
    }

    /**
     * Hook velocity rotation modification
     * <p>
     * There are a few velocity changes when attacking an entity, which could be easily detected by anti-cheats when a different server-side rotation is applied.
     */
    @ModifyExpressionValue(method = {"causeExtraKnockback",
        "doSweepAttack"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getYRot()F"))
    private float hookFixRotation(float original) {
        if ((Object) this != Minecraft.getInstance().player) {
            return original;
        }

        var rotationManager = RotationManager.INSTANCE;
        var rotation = rotationManager.getCurrentRotation();
        var rotationTarget = rotationManager.getActiveRotationTarget();

        if (rotationTarget == null || rotationTarget.getMovementCorrection() == MovementCorrection.OFF || rotation == null) {
            return original;
        }

        return rotation.yRot();
    }

    @Inject(method = "isReducedDebugInfo", at = @At("HEAD"), cancellable = true)
    private void injectReducedDebugInfo(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
    }
    @Inject(method = "isMobilityRestricted", at = @At("HEAD"), cancellable = true)
    private void hookSprintIgnoreBlindness(CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this == Minecraft.getInstance().player && ModuleSprint.INSTANCE.getShouldIgnoreBlindness()) {
            cir.setReturnValue(false);
        }
    }


    /*
     * Sadly, mixins don't allow capturing parameters when redirecting,
     * so there needs to be an extra injection for every sound.
     */
    @Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;playServerSideSound(Lnet/minecraft/sounds/SoundEvent;)V", ordinal = 0))
    private void hookPlaySound(Entity target, CallbackInfo ci) {
        if (!ModuleHitFX.INSTANCE.getRunning()) {
            liquid_bounce$playSoundIfFakePlayer(target, SoundEvents.PLAYER_ATTACK_KNOCKBACK);
        }
    }

    @Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;playServerSideSound(Lnet/minecraft/sounds/SoundEvent;)V", ordinal = 1))
    private void hookPlaySound1(Entity target, CallbackInfo ci) {
        if (!ModuleHitFX.INSTANCE.getRunning()) {
            liquid_bounce$playSoundIfFakePlayer(target, SoundEvents.PLAYER_ATTACK_NODAMAGE);
        }
    }

    @Inject(method = "attackVisualEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;playServerSideSound(Lnet/minecraft/sounds/SoundEvent;)V", ordinal = 0))
    private void hookPlaySound2(Entity target, boolean criticalHit, boolean sweeping, boolean cooldownPassed, boolean pierce, float enchantDamage, CallbackInfo ci) {
        if (!ModuleHitFX.INSTANCE.getRunning()) {
            liquid_bounce$playSoundIfFakePlayer(target, SoundEvents.PLAYER_ATTACK_CRIT);
        }

    }

    @Inject(method = "attackVisualEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;playServerSideSound(Lnet/minecraft/sounds/SoundEvent;)V", ordinal = 1))
    private void hookPlaySound3(Entity target, boolean criticalHit, boolean sweeping, boolean cooldownPassed, boolean pierce, float enchantDamage, CallbackInfo ci) {
        if(!ModuleHitFX.INSTANCE.getRunning()) {
            liquid_bounce$playSoundIfFakePlayer(target, cooldownPassed ? SoundEvents.PLAYER_ATTACK_STRONG : SoundEvents.PLAYER_ATTACK_WEAK);
        }
    }

    @Inject(method = "doSweepAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;playServerSideSound(Lnet/minecraft/sounds/SoundEvent;)V", ordinal = 0))
    private void hookPlaySound4(Entity target, float damage, DamageSource damageSource, float cooldownProgress, CallbackInfo ci) {
        if(!ModuleHitFX.INSTANCE.getRunning()) {
            liquid_bounce$playSoundIfFakePlayer(target, SoundEvents.PLAYER_ATTACK_SWEEP);
        }
    }

    /**
     * When the target is a fake player, this method will play a client side sound.
     */
    @Unique
    private void liquid_bounce$playSoundIfFakePlayer(Entity target, SoundEvent soundEvent) {
        if (target instanceof FakePlayer) {
            level().playSound(Player.class.cast(this), getX(), getY(), getZ(), soundEvent, getSoundSource(), 1F, 1F);
        }
    }
}
