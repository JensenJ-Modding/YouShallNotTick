package net.jensenj.youshallnottick;

import dev.architectury.injectables.annotations.ExpectPlatform;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.jensenj.youshallnottick.registry.TickingTotemBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.jensenj.youshallnottick.config.ServerConfig;

import java.util.List;
import java.util.Set;

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

        //Ignore tamed animals
        if(ServerConfig.shouldTamedMobsBeExempt.get()) {
            if (entity instanceof TamableAnimal tamedEntity) {
                if (tamedEntity.getOwner() != null) {
                    return true;
                }
            }
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

    public static boolean isNearPlayer(Level level, double posx, double posy, double posz, int playerHorizontalDist, int playerVerticalDist, int totemHorizontalDist, int totemVerticalDist) {
        boolean isNearPlayer = isNearPlayerInternal(level, posx, posy, posz, playerHorizontalDist, playerVerticalDist);
        if(isNearPlayer)
            return true;
        if(ServerConfig.shouldEnableTotemOfTicking.get())
            return isNearTotemOfTickingInternal(level, posx, posy, posz, totemHorizontalDist, totemVerticalDist);
        return false;
    }

    private static boolean isNearPlayerInternal(Level level, double posx, double posy, double posz, int horizontalDist, int verticalDist) {
        List<? extends Player> players = level.players();
        for (Player player : players) {
            if (player == null) {
                continue;
            }

            if (Math.abs(player.getY() - posy) < verticalDist) {
                double x = player.getX() - posx;
                double z = player.getZ() - posz;

                if (x * x + z * z < horizontalDist * horizontalDist) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isNearTotemOfTickingInternal(Level level, double posx, double posy, double posz, int horizontalDist, int verticalDist) {
        Set<BlockPos> totemsForThisLevel = TickingTotemBlockEntity.TICKING_TOTEM_LOCATIONS.get(level.dimensionType());
        if(totemsForThisLevel == null)
            return false;
        for(BlockPos totemPos : totemsForThisLevel){
            if (Math.abs(totemPos.getY() - posy) < verticalDist) {
                double x = totemPos.getX() - posx;
                double z = totemPos.getZ() - posz;

                if (x * x + z * z < horizontalDist * horizontalDist) {
                    return true;
                }
            }
        }
        return false;
    }
}
