package net.jensenj.youshallnottick.mixin;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.jensenj.youshallnottick.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.jensenj.youshallnottick.config.ServerConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Consumer;

@Mixin(value = Level.class, priority = 1100)
public abstract class EntityTickMixin {

    @WrapWithCondition(
            method = "guardEntityTick",
            at = @At(value = "INVOKE", target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V")
    )
    private boolean youshallnottick$onlyTickIfAllowed(Consumer<Entity> consumer, Object obj){
        Level level = ((Level) (Object) this);
        Entity entity = (Entity) obj;

        //If the tick mixin is disabled, allow ticking
        if(!ServerConfig.shouldEnableTickMixin.get()){
            return true;
        }

        //If there are not enough players, allow ticking
        if (!Utils.enoughPlayers(level)){
            return true;
        }

        //If this is not a living entity or is a player, allow ticking
        if(!(entity instanceof LivingEntity) || entity instanceof Player){
            return true;
        }

        //If this is an ignored entity, allow ticking
        if (Utils.isIgnoredEntity(entity)) {
            return true;
        }

        //If it's near the player, allow ticking
        BlockPos entityPos = entity.blockPosition();
        if (Utils.isNearPlayer(level, entityPos)) {
            return true;
        }

        //If it is dead or dying, allow ticking
        return ((LivingEntity) entity).isDeadOrDying();
    }
}
