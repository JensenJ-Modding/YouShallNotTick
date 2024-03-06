package net.youshallnottick;

import dev.architectury.injectables.annotations.ExpectPlatform;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.List;

public class Utils {

    public static Object2BooleanMap<EntityType<?>> isIgnored = new Object2BooleanOpenHashMap<>();

    public static boolean enoughPlayers(Level level) {
        if (level.isClientSide){
            return false;
        }
        MinecraftServer server = level.getServer();
        if (server != null) {
            return server.getPlayerList().getPlayerCount() >= Config.minPlayers.get();
        } else {
            return false;
        }
    }

    @ExpectPlatform
    public static ResourceLocation getEntityRegistrationLocation(Entity entity){
        throw new AssertionError("Override not found for getEntityRegistrationLocation in modloader.");
    }

    public static boolean isIgnoredEntity(Entity entity) {
        if (Config.entityIgnoreList.get().isEmpty()) {
            return false;
        }

        EntityType<?> entityType = entity.getType();
        return isIgnored.computeIfAbsent(entityType, (et) -> {
            ResourceLocation entityRegLoc = getEntityRegistrationLocation(entity);
            if (entityRegLoc == null) {
                return false;
            }

            var ignored = false;
            if (!Config.entityResources.isEmpty()) {
                ignored = Config.entityResources.contains(entityRegLoc);
            }

            if (!Config.entityWildcards.isEmpty() && !ignored) {
                ignored = Config.entityWildcards.stream().anyMatch(e -> entityRegLoc.toString().startsWith(e));
            }

            if (!Config.entityTagKeys.isEmpty() && !ignored) {
                ignored = Config.entityTagKeys.stream().anyMatch(entityType::is);
            }

            return ignored;
        });
    }

    public static boolean isInExemptChunk(Level level, BlockPos entityPos) {
        return false;
    }

    public static boolean isNearPlayer(Level level, BlockPos blockPos, int maxHorizontalDist, int maxVerticalDist) {
        return isNearPlayerInternal(level, blockPos.getX(), blockPos.getY(), blockPos.getZ(), maxHorizontalDist, maxVerticalDist);
    }

    private static boolean isNearPlayerInternal(Level world, double posx, double posy, double posz, int maxHorizontalDist, int maxVerticalDist) {
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
