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
import net.minecraft.world.level.LevelAccessor;

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

    public static ResourceLocation resourceLocationForLevel(LevelAccessor levelAccessor) {
        if (levelAccessor.isClientSide()) return null;
        return ((Level) levelAccessor).dimensionTypeId().location();
    }

    public static boolean shouldProcessEntityTick(LivingEntity entity) {
        Level level = entity.level();
        if (level.isClientSide()) {
            return true;
        }

        if (!Utils.enoughPlayers(level)) {
            return true;
        }

        if (Utils.isIgnoredEntity(entity)) {
            return true;
        }

        if (Utils.isNearPlayer(level, entity.blockPosition())) {
            return true;
        }

        if (ServerConfig.shouldEnableTotemOfTicking.get()
                && Utils.isNearTotemOfTicking(level, entity.blockPosition())) {
            return true;
        }

        return entity.isDeadOrDying();
    }

    public static boolean isIgnoredEntity(Entity entity) {
        if (entity.level().isClientSide()) {
            return true;
        }

        if (!(entity instanceof LivingEntity) || entity instanceof Player) {
            return true;
        }

        if (ServerConfig.shouldRaidParticipantsTick.get() && isEntityRaidParticipant(entity)) {
            return true;
        }

        if (entity instanceof TamableAnimal tamedEntity) {
            if (tamedEntity.getOwner() != null) return true;
        }

        return isEntityOnIgnoreList(entity);
    }

    private static boolean isEntityOnIgnoreList(Entity entity) {
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

    private static boolean isEntityRaidParticipant(Entity entity) {
        if (entity instanceof Raider raider) {
            return raider.hasActiveRaid();
        }
        if (entity instanceof Villager || entity instanceof IronGolem) {
            Raid raid = ((ServerLevel) entity.level()).getRaidAt(entity.blockPosition());
            return raid != null && raid.isActive() && !raid.isOver();
        }
        return false;
    }

    private static boolean isNearPlayer(Level level, BlockPos entityPos) {
        int horizontalDist = ServerConfig.playerMaxEntityTickHorizontalDist.get();
        int verticalDist = ServerConfig.playerMaxEntityTickVerticalDist.get();

        List<? extends Player> players = level.players();
        for (Player player : players) {
            if (player == null) continue;
            if (!ServerConfig.spectatorsAllowTicking.get() && player.isSpectator()) continue;

            if (Math.abs(player.getBlockY() - entityPos.getY()) <= verticalDist) {
                double x = player.getBlockX() - entityPos.getX();
                double z = player.getBlockZ() - entityPos.getZ();

                if (x * x + z * z < horizontalDist * horizontalDist) return true;
            }
        }
        return false;
    }

    private static boolean isNearTotemOfTicking(Level level, BlockPos entityPos) {
        int horizontalDist = ServerConfig.totemMaxEntityTickHorizontalDist.get();
        int verticalDist = ServerConfig.totemMaxEntityTickVerticalDist.get();

        Set<BlockPos> totemsForThisLevel = TickingTotemBlockEntity.ACTIVE_TICKING_TOTEMS.get(
                level.dimension().location());
        if (totemsForThisLevel == null) return false;
        for (BlockPos totemPos : totemsForThisLevel) {
            if (Math.abs(totemPos.getY() - entityPos.getY()) <= verticalDist) {
                double x = totemPos.getX() - entityPos.getX();
                double z = totemPos.getZ() - entityPos.getZ();

                if (x * x + z * z < horizontalDist * horizontalDist) return true;
            }
        }
        return false;
    }
}
