package net.youshallnottick.registry;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;

import net.youshallnottick.Utils;

public class TickingTotemBlockEntity extends BlockEntity {

    // These are updated first on the server then propagated to clients
    public static final Map<ResourceLocation, Set<BlockPos>> ACTIVE_TICKING_TOTEMS = new HashMap<>();
    public static final Map<ResourceLocation, Set<BlockPos>> OUTLINED_TICKING_TOTEMS = new HashMap<>();

    public TickingTotemBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(YouShallNotTickRegistry.TICKING_TOTEM_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    public static void addActiveTickingTotem(ResourceLocation dim, BlockPos pos) {
        Set<BlockPos> blockPosSet =
                TickingTotemBlockEntity.ACTIVE_TICKING_TOTEMS.computeIfAbsent(dim, k -> new HashSet<>());
        blockPosSet.add(pos);
    }

    public static void removeActiveTickingTotem(ResourceLocation dim, BlockPos pos) {
        TickingTotemBlockEntity.ACTIVE_TICKING_TOTEMS.computeIfPresent(dim, (k, blockPosSet) -> {
            blockPosSet.remove(pos);
            return blockPosSet.isEmpty() ? null : blockPosSet;
        });
    }

    public static void addOutlinedTickingTotem(ResourceLocation dim, BlockPos pos) {
        Set<BlockPos> blockPosSet =
                TickingTotemBlockEntity.OUTLINED_TICKING_TOTEMS.computeIfAbsent(dim, k -> new HashSet<>());
        blockPosSet.add(pos);
    }

    public static void removeOutlinedTickingTotem(ResourceLocation dim, BlockPos pos) {
        TickingTotemBlockEntity.OUTLINED_TICKING_TOTEMS.computeIfPresent(dim, (k, blockPosSet) -> {
            blockPosSet.remove(pos);
            return blockPosSet.isEmpty() ? null : blockPosSet;
        });
    }

    public static void handleChunkLoading(LevelAccessor level, ChunkAccess chunk) {
        if (level.isClientSide()) return;

        ResourceLocation dim = Utils.resourceLocationForLevel(level);
        if (dim == null) return;

        for (BlockPos pos : chunk.getBlockEntitiesPos()) {
            if (!(chunk.getBlockEntity(pos) instanceof TickingTotemBlockEntity)) continue;

            if (!chunk.getBlockState(pos).getValue(TickingTotemBlock.POWERED)) {
                addActiveTickingTotem(dim, pos);
            }

            if (chunk.getBlockState(pos).getValue(TickingTotemBlock.OUTLINED)) {
                addOutlinedTickingTotem(dim, pos);
            }

            TickingTotemBlock.sendDataToClient(level, pos, chunk.getBlockState(pos));
        }
    }

    public static void handleChunkUnloading(LevelAccessor level, ChunkAccess chunk) {
        if (level.isClientSide()) return;

        ResourceLocation dim = Utils.resourceLocationForLevel(level);
        if (dim == null) return;

        for (BlockPos pos : chunk.getBlockEntitiesPos()) {
            if (!(chunk.getBlockEntity(pos) instanceof TickingTotemBlockEntity)) continue;

            if (chunk.getBlockState(pos).getValue(TickingTotemBlock.POWERED)) {
                removeActiveTickingTotem(dim, pos);
            }

            if (!chunk.getBlockState(pos).getValue(TickingTotemBlock.OUTLINED)) {
                removeOutlinedTickingTotem(dim, pos);
            }

            TickingTotemBlock.sendDataToClient(level, pos, chunk.getBlockState(pos));
        }
    }
}
