package net.youshallnottick.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.youshallnottick.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = LivingEntity.class, priority = 10100)
public class LivingEntityMixin {

    @WrapWithCondition(
            method = "aiStep",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;serverAiStep()V")
    )
    private boolean youshallnottick$handleAI(LivingEntity entity){
        return Utils.shouldProcessEntityTick(entity);
    }

    @WrapWithCondition(
            method = "aiStep",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;pushEntities()V")
    )
    private boolean youshallnottick$handleCollisions(LivingEntity entity){
        return Utils.shouldProcessEntityTick(entity);
    }

    @WrapWithCondition(
            method = "aiStep",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;travel(Lnet/minecraft/world/phys/Vec3;)V")
    )
    private boolean youshallnottick$handleTravel(LivingEntity entity, Vec3 vec3){
        return Utils.shouldProcessEntityTick(entity);
    }

    @WrapWithCondition(
            method = "aiStep",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;updateFallFlying()V")
    )
    private boolean youshallnottick$handleFall(LivingEntity entity){
        return Utils.shouldProcessEntityTick(entity);
    }

    @WrapWithCondition(
            method = "aiStep",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;jumpFromGround()V")
    )
    private boolean youshallnottick$handleJump(LivingEntity entity){
        return Utils.shouldProcessEntityTick(entity);
    }
}
