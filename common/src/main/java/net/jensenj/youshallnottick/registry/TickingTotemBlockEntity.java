package net.jensenj.youshallnottick.registry;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TickingTotemBlockEntity extends BlockEntity {

    public TickingTotemBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(YouShallNotTickRegistry.TICKING_TOTEM_BLOCK_ENTITY.get(), blockPos, blockState);
    }
}
