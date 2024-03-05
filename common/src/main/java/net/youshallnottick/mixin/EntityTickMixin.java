package net.youshallnottick.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.youshallnottick.Config;
import net.youshallnottick.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Random;
import java.util.function.Consumer;

@Mixin(Level.class)
public abstract class EntityTickMixin {

    @Overwrite
    public void guardEntityTick(Consumer<Entity> consumer, Entity entity) {
        Level level = ((Level) (Object) this);
        handleEntityTick(consumer, entity, level);
    }

    private static void handleEntityTick(Consumer<Entity> consumer, Entity entity, Level level){
        if (!Utils.enoughPlayers(level)){
            Utils.handleGuardEntityTick(consumer, entity);
            return;
        }

        if(!(entity instanceof LivingEntity) || entity instanceof Player){
            Utils.handleGuardEntityTick(consumer, entity);
            return;
        }

        if (Utils.isIgnoredEntity(entity)) {
            Utils.handleGuardEntityTick(consumer, entity);
            return;
        }

        BlockPos entityPos = entity.blockPosition();
        int maxHeight = Config.maxEntityTickDistanceVertical.get();
        int maxDistanceSquare = Config.maxEntityTickDistanceHorizontal.get();

        if (Utils.isNearPlayer(level, entityPos, maxHeight, maxDistanceSquare)) {
            Utils.handleGuardEntityTick(consumer, entity);
            return;
        }

        boolean isInExemptChunk = Utils.isInExemptChunk(level, entityPos);
        if (isInExemptChunk || ((LivingEntity) entity).isDeadOrDying()) {
            Utils.handleGuardEntityTick(consumer, entity);
        }
    }
}
