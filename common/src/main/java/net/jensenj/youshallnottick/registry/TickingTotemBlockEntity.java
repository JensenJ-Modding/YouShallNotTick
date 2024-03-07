package net.jensenj.youshallnottick.registry;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.dimension.DimensionType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TickingTotemBlockEntity extends BlockEntity {

    public static final Map<DimensionType, Set<BlockPos>> TICKING_TOTEM_LOCATIONS = new HashMap<>();

    public TickingTotemBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(YouShallNotTickRegistry.TICKING_TOTEM_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    public static void addTickingTotemPosition(LevelAccessor level, BlockPos pos){
        Set<BlockPos> blockPosSet = TickingTotemBlockEntity.TICKING_TOTEM_LOCATIONS.computeIfAbsent(level.dimensionType(), k -> new HashSet<>());
        blockPosSet.add(pos);
    }

    public static void removeTickingTotemPosition(LevelAccessor level, BlockPos pos){
        TickingTotemBlockEntity.TICKING_TOTEM_LOCATIONS.computeIfPresent(level.dimensionType(), (k, blockPosSet) -> {
            blockPosSet.remove(pos);
            return blockPosSet.isEmpty() ? null : blockPosSet;
        });
    }


    public static void handleChunkLoading(LevelAccessor level, ChunkAccess chunk){
        if(level.isClientSide())
            return;
        for(BlockPos pos : chunk.getBlockEntitiesPos()){
            if(!(chunk.getBlockEntity(pos) instanceof TickingTotemBlockEntity))
                continue;
            if(chunk.getBlockState(pos).getValue(TickingTotemBlock.POWERED)){ //Skip powered totems
                continue;
            }
            addTickingTotemPosition(level, pos);
            return;
        }
    }

    public static void handleChunkUnloading(LevelAccessor level, ChunkAccess chunk){
        if(level.isClientSide())
            return;
        for(BlockPos pos : chunk.getBlockEntitiesPos()){
            if(!(chunk.getBlockEntity(pos) instanceof TickingTotemBlockEntity))
                continue;
            if(!chunk.getBlockState(pos).getValue(TickingTotemBlock.POWERED)){ //Skip unpowered totems
                continue;
            }
            removeTickingTotemPosition(level, pos);
            return;
        }
    }
}
