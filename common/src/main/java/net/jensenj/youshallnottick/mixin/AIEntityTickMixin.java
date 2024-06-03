package net.jensenj.youshallnottick.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.jensenj.youshallnottick.Utils;
import net.jensenj.youshallnottick.config.ServerConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@SuppressWarnings({"unused", ""})
@Mixin(value = LivingEntity.class, priority = 1100)
public abstract class AIEntityTickMixin {

    @WrapWithCondition(
            method = "tick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;aiStep()V")
    )
    private boolean youshallnottick$CheckAIStep(LivingEntity entity){
        Level level = entity.level();

        //If the tick mixin is disabled, allow ticking
        if(!ServerConfig.shouldEnableAITickMixin.get()){
            return true;
        }

        //If there are enough players, allow ticking
        if (!Utils.enoughPlayers(level)){
            return true;
        }

        //If this is an ignored entity, allow ticking
        if (Utils.isIgnoredEntity(entity)) {
            System.out.println("Allowing AI step");
            return true;
        }

        int playerHorizontalDist = ServerConfig.playerMaxEntityTickHorizontalDist.get();
        int playerVerticalDist = ServerConfig.playerMaxEntityTickVerticalDist.get();
        int totemHorizontalDist = ServerConfig.totemMaxEntityTickHorizontalDist.get();
        int totemVerticalDist = ServerConfig.totemMaxEntityTickVerticalDist.get();

        //If it's near the player, allow ticking
        BlockPos entityPos = entity.blockPosition();
        if (Utils.isNearPlayer(level, entityPos.getX(), entityPos.getY(), entityPos.getZ(), playerHorizontalDist, playerVerticalDist, totemHorizontalDist, totemVerticalDist)) {
            return true;
        }

        //If it is dead or dying, allow ticking
        return entity.isDeadOrDying();
    }
}
