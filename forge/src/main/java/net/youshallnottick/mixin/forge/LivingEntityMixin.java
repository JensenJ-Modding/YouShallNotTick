package net.youshallnottick.mixin.forge;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.fluids.FluidType;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;

import net.youshallnottick.Utils;

@Mixin(value = LivingEntity.class, priority = 10100)
public abstract class LivingEntityMixin {

    @WrapWithCondition(
            method = "aiStep",
            at =
                    @At(
                            value = "INVOKE",
                            target =
                                    "Lnet/minecraft/world/entity/LivingEntity;jumpInFluid(Lnet/minecraftforge/fluids/FluidType;)V"))
    private boolean youshallnottick$handleJumpInLiquid(LivingEntity entity, FluidType fluidType) {
        return Utils.shouldProcessEntityTick(entity);
    }
}
