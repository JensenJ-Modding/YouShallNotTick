package net.jensenj.youshallnottick;

import dev.architectury.injectables.annotations.ExpectPlatform;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.jensenj.youshallnottick.config.ServerConfig;

import java.util.List;

public class Utils {

    public static Object2BooleanMap<EntityType<?>> isIgnored = new Object2BooleanOpenHashMap<>();

    public static boolean enoughPlayers(Level level) {
        return level.players().size() >= ServerConfig.minPlayers.get();
    }

    @ExpectPlatform
    public static ResourceLocation getEntityRegistrationLocation(Entity entity){
        throw new AssertionError("Override not found for getEntityRegistrationLocation in modloader.");
    }

    public static boolean isIgnoredEntity(Entity entity) {
        if (ServerConfig.entityIgnoreList.get().isEmpty()) {
            return false;
        }

        EntityType<?> entityType = entity.getType();
        return isIgnored.computeIfAbsent(entityType, (et) -> {
            ResourceLocation entityRegLoc = getEntityRegistrationLocation(entity);
            if (entityRegLoc == null) {
                return false;
            }

            var ignored = false;
            if (!ServerConfig.entityResources.isEmpty()) {
                ignored = ServerConfig.entityResources.contains(entityRegLoc);
            }

            if (!ServerConfig.entityWildcards.isEmpty() && !ignored) {
                ignored = ServerConfig.entityWildcards.stream().anyMatch(e -> entityRegLoc.toString().startsWith(e));
            }

            if (!ServerConfig.entityTagKeys.isEmpty() && !ignored) {
                ignored = ServerConfig.entityTagKeys.stream().anyMatch(entityType::is);
            }

            return ignored;
        });
    }

    public static boolean isNearPlayer(Level level, BlockPos blockPos) {
        return isNearPlayerInternal(level, blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    public static boolean isEntityWithinDistance(Entity entity, double posX, double posY, double posZ){
        int maxHorizontalDist = ServerConfig.maxEntitySpawnDistanceHorizontal.get();
        int maxVerticalDist = ServerConfig.maxEntitySpawnDistanceVertical.get();
        if (Math.abs(entity.getY() - posY) < maxVerticalDist) {
            double x = entity.getX() - posX;
            double z = entity.getZ() - posZ;

            return x * x + z * z < maxHorizontalDist * maxHorizontalDist;
        }

        return false;
    }

    private static boolean isNearPlayerInternal(Level world, double posx, double posy, double posz) {
        int maxHorizontalDist = ServerConfig.maxEntitySpawnDistanceHorizontal.get();
        int maxVerticalDist = ServerConfig.maxEntitySpawnDistanceVertical.get();
        List<? extends Player> players = world.players();

        for (Player player : players) {
            if (player == null) {
                continue;
            }

            if (Math.abs(player.getY() - posy) < maxVerticalDist) {
                double x = player.getX() - posx;
                double z = player.getZ() - posz;

                if (x * x + z * z < maxHorizontalDist * maxHorizontalDist) {
                    return true;
                }
            }
        }

        return false;

    }
}
