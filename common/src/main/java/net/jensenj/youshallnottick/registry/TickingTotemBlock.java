package net.jensenj.youshallnottick.registry;

import net.jensenj.youshallnottick.YouShallNotTick;
import net.jensenj.youshallnottick.config.ServerConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

@SuppressWarnings("deprecation")
public class TickingTotemBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    private static final VoxelShape SHAPE = Block.box(4,0,4,12,16,12);

    protected TickingTotemBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(POWERED, false).setValue(FACING, Direction.NORTH));
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext placeContext){
        return this.defaultBlockState()
                .setValue(FACING, placeContext.getHorizontalDirection().getOpposite())
                .setValue(POWERED, placeContext.getLevel().hasNeighborSignal(placeContext.getClickedPos()));
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
        builder.add(POWERED);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable BlockGetter blockGetter, List<Component> list, TooltipFlag flag) {
        if(ServerConfig.shouldEnableTotemOfTicking.get()) {
            list.add(new TranslatableComponent("block." + YouShallNotTick.MOD_ID + ".ticking_totem.info.tooltip").withStyle(ChatFormatting.GRAY));
            list.add(new TranslatableComponent("block." + YouShallNotTick.MOD_ID + ".ticking_totem.range_h.tooltip")
                    .append(String.valueOf(ServerConfig.totemMaxEntitySpawnHorizontalDist.get())).withStyle(ChatFormatting.YELLOW));
            list.add(new TranslatableComponent("block." + YouShallNotTick.MOD_ID + ".ticking_totem.range_v.tooltip")
                    .append(String.valueOf(ServerConfig.totemMaxEntitySpawnVerticalDist.get())).withStyle(ChatFormatting.YELLOW));
            list.add(new TranslatableComponent("block." + YouShallNotTick.MOD_ID + ".ticking_totem.redstone.tooltip").withStyle(ChatFormatting.GRAY));
        }else{
            list.add(new TranslatableComponent("block." + YouShallNotTick.MOD_ID + ".ticking_totem.disabled.tooltip").withStyle(ChatFormatting.DARK_RED));
        }
    }

    //Block Entity Data

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public void onPlace(BlockState state, @NotNull Level level, @NotNull BlockPos pos, BlockState state2, boolean pIsMoving) {
        if(state.getBlock() != state2.getBlock()) {
            if(!state.getValue(POWERED)) //If unpowered, add to positions
                TickingTotemBlockEntity.addTickingTotemPosition(level, pos);
        }
        super.onRemove(state, level, pos, state2, pIsMoving);
    }

    @Override
    public void onRemove(BlockState state, @NotNull Level level, @NotNull BlockPos pos, BlockState state2, boolean pIsMoving) {
        if(state.getBlock() != state2.getBlock()) {
            if(!state.getValue(POWERED)) //If unpowered, remove from positions
                TickingTotemBlockEntity.removeTickingTotemPosition(level, pos);
        }
        super.onRemove(state, level, pos, state2, pIsMoving);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TickingTotemBlockEntity(blockPos, blockState);
    }

    @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl) {
        if (!level.isClientSide) {
            boolean powered = blockState.getValue(POWERED);
            if (powered != level.hasNeighborSignal(blockPos)) {
                level.scheduleTick(blockPos, this, 4);
            }
        }
    }

    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, Random random) {
        boolean previousPowered = blockState.getValue(POWERED);
        if (previousPowered && !serverLevel.hasNeighborSignal(blockPos)) { //if was previously powered and there is no signal
            //Enable totem
            serverLevel.setBlock(blockPos, blockState.setValue(POWERED, false), 2);
            TickingTotemBlockEntity.addTickingTotemPosition(serverLevel, blockPos);
        }else if(!previousPowered && serverLevel.hasNeighborSignal(blockPos)){ //If was previously unpowered and there is a signal
            //Disable totem
            serverLevel.setBlock(blockPos, blockState.setValue(POWERED, true), 2);
            TickingTotemBlockEntity.removeTickingTotemPosition(serverLevel, blockPos);
        }
    }
}
