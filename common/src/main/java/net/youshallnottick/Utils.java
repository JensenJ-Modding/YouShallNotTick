package net.youshallnottick;

import java.util.List;
import java.util.Set;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;

import dev.architectury.injectables.annotations.ExpectPlatform;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.youshallnottick.config.ServerConfig;
import net.youshallnottick.registry.TickingTotemBlockEntity;

public class Utils {

    public static Object2BooleanMap<EntityType<?>> isIgnored = new Object2BooleanOpenHashMap<>();

    public static boolean enoughPlayers(Level level) {
        return level.players().size() >= ServerConfig.minPlayers.get();
    }

    @ExpectPlatform
    @SuppressWarnings("unused")
    public static ResourceLocation getEntityRegistrationLocation(Entity entity) {
        throw new AssertionError("Override not found for getEntityRegistrationLocation in mod loader.");
    }

    public static boolean shouldProcessEntityTick(LivingEntity entity) {
        Level level = entity.level();

        // Allow ticking on client side, for animations and such
        if (level.isClientSide()) {
            return true;
        }

        // If the tick mixin is disabled, allow ticking
        if (!ServerConfig.shouldEnableAITickMixin.get()) {
            return true;
        }

        // If there are enough players, allow ticking
        if (!Utils.enoughPlayers(level)) {
            return true;
        }

        // If this is an ignored entity, allow ticking
        if (Utils.isIgnoredEntity(entity)) {
            return true;
        }

        int playerHorizontalDist = ServerConfig.playerMaxEntityTickHorizontalDist.get();
        int playerVerticalDist = ServerConfig.playerMaxEntityTickVerticalDist.get();
        int totemHorizontalDist = ServerConfig.totemMaxEntityTickHorizontalDist.get();
        int totemVerticalDist = ServerConfig.totemMaxEntityTickVerticalDist.get();

        // If it's near the player, allow ticking
        BlockPos entityPos = entity.blockPosition();
        if (Utils.isNearPlayer(
                level,
                entityPos.getX(),
                entityPos.getY(),
                entityPos.getZ(),
                playerHorizontalDist,
                playerVerticalDist,
                totemHorizontalDist,
                totemVerticalDist)) {
            return true;
        }

        // If it is dead or dying, allow ticking
        return entity.isDeadOrDying();
    }

    public static boolean isIgnoredEntity(Entity entity) {
        if (entity.level().isClientSide()) {
            return true;
        }
        // If it's not living or is a player, it's ignored
        if (!(entity instanceof LivingEntity) || entity instanceof Player) {
            return true;
        }

        // If this entity is part of a raid, it should be ignored
        if (ServerConfig.shouldRaidParticipantsTick.get()) {
            if (entity instanceof Raider raider) {
                if (raider.hasActiveRaid()) return true;
            }
            if (entity instanceof Villager || entity instanceof IronGolem) {
                Raid raid = ((ServerLevel) entity.level()).getRaidAt(entity.blockPosition());
                if (raid != null && raid.isActive() && !raid.isOver()) return true;
            }
        }

        // Ignore tamed animals
        if (entity instanceof TamableAnimal tamedEntity) {
            if (tamedEntity.getOwner() != null) return true;
        }

        // If the entity list is empty, this entity should not be ignored
        if (ServerConfig.entityIgnoreList.get().isEmpty()) return false;

        EntityType<?> entityType = entity.getType();
        return isIgnored.computeIfAbsent(entityType, (et) -> {
            ResourceLocation entityRegLoc = getEntityRegistrationLocation(entity);
            if (entityRegLoc == null) return false;

            var ignored = false;
            if (!ServerConfig.entityResources.isEmpty()) ignored = ServerConfig.entityResources.contains(entityRegLoc);

            if (!ServerConfig.entityWildcards.isEmpty() && !ignored)
                ignored = ServerConfig.entityWildcards.stream()
                        .anyMatch(e -> entityRegLoc.toString().startsWith(e));

            if (!ServerConfig.entityTagKeys.isEmpty() && !ignored)
                ignored = ServerConfig.entityTagKeys.stream().anyMatch(entityType::is);

            return ignored;
        });
    }

    public static boolean isNearPlayer(
            Level level,
            double posX,
            double posY,
            double posZ,
            int playerHorizontalDist,
            int playerVerticalDist,
            int totemHorizontalDist,
            int totemVerticalDist) {
        boolean isNearPlayer = isNearPlayerInternal(level, posX, posY, posZ, playerHorizontalDist, playerVerticalDist);
        if (isNearPlayer) return true;
        if (ServerConfig.shouldEnableTotemOfTicking.get())
            return isNearTotemOfTickingInternal(level, posX, posY, posZ, totemHorizontalDist, totemVerticalDist);
        return false;
    }

    private static boolean isNearPlayerInternal(
            Level level, double posX, double posY, double posZ, int horizontalDist, int verticalDist) {
        List<? extends Player> players = level.players();
        for (Player player : players) {
            if (player == null) continue;

            if (Math.abs(player.getY() - posY) < verticalDist) {
                double x = player.getX() - posX;
                double z = player.getZ() - posZ;

                if (x * x + z * z < horizontalDist * horizontalDist) return true;
            }
        }
        return false;
    }

    private static boolean isNearTotemOfTickingInternal(
            Level level, double posX, double posY, double posZ, int horizontalDist, int verticalDist) {
        Set<BlockPos> totemsForThisLevel = TickingTotemBlockEntity.TICKING_TOTEM_LOCATIONS.get(
                level.dimension().location());
        if (totemsForThisLevel == null) return false;
        for (BlockPos totemPos : totemsForThisLevel) {
            if (Math.abs(totemPos.getY() - posY) < verticalDist) {
                double x = totemPos.getX() - posX;
                double z = totemPos.getZ() - posZ;

                if (x * x + z * z < horizontalDist * horizontalDist) return true;
            }
        }
        return false;
    }
}
