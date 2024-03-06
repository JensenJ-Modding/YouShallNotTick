package net.jensenj.youshallnottick.registry;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TickingTotemBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private static final VoxelShape SHAPE = Block.box(4,0,4,12,16,12);

    protected TickingTotemBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext placeContext){
        return this.defaultBlockState().setValue(FACING, placeContext.getHorizontalDirection().getOpposite());
    }

    @Override
    public @NotNull BlockState rotate(BlockState state, Rotation rotation){
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState mirror(BlockState state, Mirror mirror){
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }


    //Block Entity Data

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public void onPlace(BlockState state, @NotNull Level level, @NotNull BlockPos pos, BlockState newState, boolean pIsMoving) {
        if(state.getBlock() != newState.getBlock()) {
            System.out.println("Placed totem");
            //TODO: Add this to ticking totem set for this level
        }
        super.onRemove(state, level, pos, newState, pIsMoving);
    }

    @Override
    public void onRemove(BlockState state, @NotNull Level level, @NotNull BlockPos pos, BlockState newState, boolean pIsMoving) {
        if(state.getBlock() != newState.getBlock()) {
            System.out.println("Removed totem");
            //TODO: Remove this from ticking totem set for this level
        }
        super.onRemove(state, level, pos, newState, pIsMoving);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TickingTotemBlockEntity(blockPos, blockState);
    }
}
