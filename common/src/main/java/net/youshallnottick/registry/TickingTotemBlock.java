package net.youshallnottick.registry;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import net.youshallnottick.Utils;
import net.youshallnottick.config.ServerConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class TickingTotemBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final BooleanProperty OUTLINED = BooleanProperty.create("outlined");
    private static final VoxelShape SHAPE = Block.box(4, 0, 4, 12, 16, 12);

    protected TickingTotemBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(POWERED, false)
                .setValue(FACING, Direction.NORTH)
                .setValue(OUTLINED, false));
    }

    @Override
    public @NotNull InteractionResult use(
            BlockState blockState,
            Level level,
            BlockPos blockPos,
            Player player,
            InteractionHand interactionHand,
            BlockHitResult blockHitResult) {
        if (level.isClientSide()) return InteractionResult.PASS;

        if (!player.isShiftKeyDown()) return InteractionResult.PASS;

        if (interactionHand == InteractionHand.OFF_HAND) return InteractionResult.PASS;

        BlockState newState = blockState.cycle(OUTLINED);
        level.setBlock(blockPos, newState, 2);

        ResourceLocation dim = Utils.resourceLocationForLevel(level);
        if (dim == null) return InteractionResult.PASS;

        return InteractionResult.SUCCESS;
    }

    @Override
    public @NotNull VoxelShape getShape(
            @NotNull BlockState state,
            @NotNull BlockGetter level,
            @NotNull BlockPos pos,
            @NotNull CollisionContext context) {
        return SHAPE;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext placeContext) {
        return this.defaultBlockState()
                .setValue(FACING, placeContext.getHorizontalDirection().getOpposite())
                .setValue(POWERED, placeContext.getLevel().hasNeighborSignal(placeContext.getClickedPos()))
                .setValue(OUTLINED, false);
    }

    @Override
    public @NotNull BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(POWERED);
        builder.add(OUTLINED);
    }

    @Override
    public void appendHoverText(
            ItemStack itemStack, @Nullable BlockGetter blockGetter, List<Component> list, TooltipFlag flag) {
        if (ServerConfig.shouldEnableTotemOfTicking.get()) {
            list.add(Component.translatable("tooltip.youshallnottick.ticking_totem.info")
                    .withStyle(ChatFormatting.GRAY));
            list.add(Component.translatable("tooltip.youshallnottick.ticking_totem.tick_range_h")
                    .append(String.valueOf(ServerConfig.tickingTotemMaxEntityTickHorizontalDist.get()))
                    .withStyle(ChatFormatting.YELLOW));
            list.add(Component.translatable("tooltip.youshallnottick.ticking_totem.tick_range_v")
                    .append(String.valueOf(ServerConfig.tickingTotemMaxEntityTickVerticalDist.get()))
                    .withStyle(ChatFormatting.YELLOW));

            list.add(Component.translatable("tooltip.youshallnottick.ticking_totem.redstone")
                    .withStyle(ChatFormatting.GRAY));
        } else {
            list.add(Component.translatable("tooltip.youshallnottick.ticking_totem.disabled")
                    .withStyle(ChatFormatting.DARK_RED));
        }
    }

    // Block Entity Data

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public void onPlace(
            BlockState state, @NotNull Level level, @NotNull BlockPos pos, BlockState state2, boolean pIsMoving) {
        if (level.isClientSide()) return;
        if (state.getBlock() != state2.getBlock()) {
            if (!state.getValue(POWERED)) {
                ResourceLocation dim = Utils.resourceLocationForLevel(level);
                if (dim == null) return;
                TickingTotemBlockEntity.addActiveTickingTotem(dim, pos);
            }
        }
        super.onPlace(state, level, pos, state2, pIsMoving);
    }

    @Override
    public void onRemove(
            BlockState state, @NotNull Level level, @NotNull BlockPos pos, BlockState state2, boolean pIsMoving) {
        if (level.isClientSide()) return;
        if (state.getBlock() != state2.getBlock()) {
            if (!state.getValue(POWERED)) {
                ResourceLocation dim = Utils.resourceLocationForLevel(level);
                if (dim == null) return;
                TickingTotemBlockEntity.removeActiveTickingTotem(dim, pos);
            }
        }
        super.onRemove(state, level, pos, state2, pIsMoving);
    }

    @Nullable @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TickingTotemBlockEntity(blockPos, blockState);
    }

    @Override
    public void neighborChanged(
            BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl) {
        if (level.isClientSide) return;
        boolean powered = blockState.getValue(POWERED);
        if (powered != level.hasNeighborSignal(blockPos)) {
            level.scheduleTick(blockPos, this, 4);
        }
    }

    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource random) {
        if (serverLevel.isClientSide()) return;
        boolean previousPowered = blockState.getValue(POWERED);
        ResourceLocation dim = Utils.resourceLocationForLevel(serverLevel);
        if (dim == null) return;
        if (previousPowered && !serverLevel.hasNeighborSignal(blockPos)) { // if was previously powered and there is no
            // signal
            // Enable totem
            serverLevel.setBlock(blockPos, blockState.setValue(POWERED, false), 2);
            TickingTotemBlockEntity.addActiveTickingTotem(dim, blockPos);
        } else if (!previousPowered && serverLevel.hasNeighborSignal(blockPos)) { // If was previously unpowered and
            // there is a signal
            // Disable totem
            serverLevel.setBlock(blockPos, blockState.setValue(POWERED, true), 2);
            TickingTotemBlockEntity.removeActiveTickingTotem(dim, blockPos);
        }
    }
}
