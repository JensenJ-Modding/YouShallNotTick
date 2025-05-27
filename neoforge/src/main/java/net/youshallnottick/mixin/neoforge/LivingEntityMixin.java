package net.youshallnottick.mixin.neoforge;

import net.minecraft.world.entity.LivingEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;

import net.neoforged.neoforge.fluids.FluidType;
import net.youshallnottick.Utils;

@Mixin(value = LivingEntity.class, priority = 10100)
public abstract class LivingEntityMixin {

    @WrapWithCondition(
            method = "aiStep",
            at =
                    @At(
                            value = "INVOKE",
                            target =
                                    "Lnet/minecraft/world/entity/LivingEntity;jumpInFluid(Lnet/neoforged/neoforge/fluids/FluidType;)V"))
    private boolean youshallnottick$handleJumpInLiquid(LivingEntity entity, FluidType fluidType) {
        return Utils.shouldProcessEntityTick(entity);
    }
}
