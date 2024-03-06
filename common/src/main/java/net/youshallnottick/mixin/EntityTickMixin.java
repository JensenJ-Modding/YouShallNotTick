package net.youshallnottick.mixin;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.youshallnottick.Config;
import net.youshallnottick.Utils;
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

        if (!Utils.enoughPlayers(level)){
            return true;
        }

        if(!(entity instanceof LivingEntity) || entity instanceof Player){
            return true;
        }

        if (Utils.isIgnoredEntity(entity)) {
            return true;
        }

        BlockPos entityPos = entity.blockPosition();
        int maxHorizontalDist = Config.maxEntityTickDistanceHorizontal.get();
        int maxVerticalDist = Config.maxEntityTickDistanceVertical.get();

        if (Utils.isNearPlayer(level, entityPos, maxHorizontalDist, maxVerticalDist)) {
            return true;
        }

        boolean isInExemptChunk = Utils.isInExemptChunk(level, entityPos);
        if (isInExemptChunk || ((LivingEntity) entity).isDeadOrDying()) {
            return true;
        }

        return false;
    }
}
