package net.youshallnottick.registry;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;

public class TickingTotemBlockEntity extends BlockEntity {

    public static final Map<ResourceLocation, Set<BlockPos>> TICKING_TOTEM_LOCATIONS = new HashMap<>();

    public TickingTotemBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(YouShallNotTickRegistry.TICKING_TOTEM_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    public static void updateTickingTotemState(LevelAccessor levelAccessor, BlockPos pos, boolean shouldAdd) {
        if (levelAccessor.isClientSide()) return;
        Level level = (Level) levelAccessor;
        ResourceLocation dimension = level.dimensionTypeId().location();

        if (shouldAdd) // Server side updating totems
        addTickingTotemPosition(dimension, pos);
        else removeTickingTotemPosition(dimension, pos);
    }

    public static void addTickingTotemPosition(ResourceLocation dimension, BlockPos pos) {
        if (dimension == null) return;
        Set<BlockPos> blockPosSet =
                TickingTotemBlockEntity.TICKING_TOTEM_LOCATIONS.computeIfAbsent(dimension, k -> new HashSet<>());
        blockPosSet.add(pos);
    }

    public static void removeTickingTotemPosition(ResourceLocation dimension, BlockPos pos) {
        if (dimension == null) return;
        TickingTotemBlockEntity.TICKING_TOTEM_LOCATIONS.computeIfPresent(dimension, (k, blockPosSet) -> {
            blockPosSet.remove(pos);
            return blockPosSet.isEmpty() ? null : blockPosSet;
        });
    }

    public static void handleChunkLoading(LevelAccessor level, ChunkAccess chunk) {
        if (level.isClientSide()) return;
        for (BlockPos pos : chunk.getBlockEntitiesPos()) {
            if (!(chunk.getBlockEntity(pos) instanceof TickingTotemBlockEntity)) continue;
            if (chunk.getBlockState(pos).getValue(TickingTotemBlock.POWERED)) // Skip powered totems
            continue;
            updateTickingTotemState(level, pos, true);
            return;
        }
    }

    public static void handleChunkUnloading(LevelAccessor level, ChunkAccess chunk) {
        if (level.isClientSide()) return;
        for (BlockPos pos : chunk.getBlockEntitiesPos()) {
            if (!(chunk.getBlockEntity(pos) instanceof TickingTotemBlockEntity)) continue;
            if (!chunk.getBlockState(pos).getValue(TickingTotemBlock.POWERED)) // Skip unpowered totems
            continue;
            updateTickingTotemState(level, pos, false);
            return;
        }
    }
}
