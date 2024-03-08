package net.jensenj.youshallnottick;

import dev.architectury.injectables.annotations.ExpectPlatform;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.jensenj.youshallnottick.registry.TickingTotemBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.jensenj.youshallnottick.config.ServerConfig;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.dimension.DimensionType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Utils {

    public static Object2BooleanMap<EntityType<?>> isIgnored = new Object2BooleanOpenHashMap<>();
    public static Map<DimensionType, ResourceLocation> dimensionMap = new HashMap<>();

    public static boolean enoughPlayers(Level level) {
        return level.players().size() >= ServerConfig.minPlayers.get();
    }

    @ExpectPlatform
    @SuppressWarnings("unused")
    public static ResourceLocation getEntityRegistrationLocation(Entity entity){
        throw new AssertionError("Override not found for getEntityRegistrationLocation in mod loader.");
    }

    public static ResourceLocation getDimensionLocation(LevelAccessor level){
        Registry<DimensionType> dimensionRegistry = level.registryAccess().registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY);
        return dimensionMap.computeIfAbsent(level.dimensionType(), (dimensionType) -> {
            for (Map.Entry<ResourceKey<DimensionType>, DimensionType> dimensionEntry : dimensionRegistry.entrySet()) {
                ResourceKey<DimensionType> dimensionKey = dimensionEntry.getKey();
                if(dimensionType.effectsLocation().equals(dimensionEntry.getValue().effectsLocation())) {
                    return dimensionKey.location();
                }
            }
            return null;
        });
    }

    public static boolean isIgnoredEntity(Entity entity) {
        if (ServerConfig.entityIgnoreList.get().isEmpty())
            return false;

        //Ignore tamed animals
        if(ServerConfig.shouldTamedMobsBeExempt.get()) {
            if (entity instanceof TamableAnimal tamedEntity) {
                if (tamedEntity.getOwner() != null)
                    return true;
            }
        }

        EntityType<?> entityType = entity.getType();
        return isIgnored.computeIfAbsent(entityType, (et) -> {
            ResourceLocation entityRegLoc = getEntityRegistrationLocation(entity);
            if (entityRegLoc == null)
                return false;

            var ignored = false;
            if (!ServerConfig.entityResources.isEmpty())
                ignored = ServerConfig.entityResources.contains(entityRegLoc);

            if (!ServerConfig.entityWildcards.isEmpty() && !ignored)
                ignored = ServerConfig.entityWildcards.stream().anyMatch(e -> entityRegLoc.toString().startsWith(e));

            if (!ServerConfig.entityTagKeys.isEmpty() && !ignored)
                ignored = ServerConfig.entityTagKeys.stream().anyMatch(entityType::is);

            return ignored;
        });
    }

    public static boolean isNearPlayer(Level level, double posX, double posY, double posZ, int playerHorizontalDist, int playerVerticalDist, int totemHorizontalDist, int totemVerticalDist) {
        boolean isNearPlayer = isNearPlayerInternal(level, posX, posY, posZ, playerHorizontalDist, playerVerticalDist);
        if(isNearPlayer)
            return true;
        if(ServerConfig.shouldEnableTotemOfTicking.get())
            return isNearTotemOfTickingInternal(level, posX, posY, posZ, totemHorizontalDist, totemVerticalDist);
        return false;
    }

    private static boolean isNearPlayerInternal(Level level, double posX, double posY, double posZ, int horizontalDist, int verticalDist) {
        List<? extends Player> players = level.players();
        for (Player player : players) {
            if (player == null)
                continue;

            if (Math.abs(player.getY() - posY) < verticalDist) {
                double x = player.getX() - posX;
                double z = player.getZ() - posZ;

                if (x * x + z * z < horizontalDist * horizontalDist)
                    return true;
            }
        }
        return false;
    }

    private static boolean isNearTotemOfTickingInternal(Level level, double posX, double posY, double posZ, int horizontalDist, int verticalDist) {
        ResourceLocation dim = Utils.getDimensionLocation(level);
        Set<BlockPos> totemsForThisLevel = TickingTotemBlockEntity.TICKING_TOTEM_LOCATIONS.get(dim);
        //if(level.isClientSide()) {
        //    System.out.println("dim resource location" + dim);
        //    System.out.println("totems for level" + totemsForThisLevel);
        //}
        if(totemsForThisLevel == null)
            return false;
        for(BlockPos totemPos : totemsForThisLevel){
            if (Math.abs(totemPos.getY() - posY) < verticalDist) {
                double x = totemPos.getX() - posX;
                double z = totemPos.getZ() - posZ;

                if (x * x + z * z < horizontalDist * horizontalDist)
                    return true;
            }
        }
        return false;
    }
}
