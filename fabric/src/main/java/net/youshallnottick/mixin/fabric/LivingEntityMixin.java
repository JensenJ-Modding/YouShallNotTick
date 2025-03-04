package net.youshallnottick.mixin.fabric;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.Fluid;
import net.youshallnottick.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = LivingEntity.class, priority = 10100)
public abstract class LivingEntityMixin {

    @WrapWithCondition(
            method = "aiStep",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;jumpInLiquid(Lnet/minecraft/tags/TagKey;)V")
    )
    private boolean youshallnottick$handleJumpInLiquid(LivingEntity entity, TagKey<Fluid> tagKey){
        return Utils.shouldProcessEntityTick(entity);
    }
}
