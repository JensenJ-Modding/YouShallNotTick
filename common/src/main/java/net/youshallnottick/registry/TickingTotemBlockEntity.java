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
import net.minecraft.world.phys.AABB;

import net.youshallnottick.Utils;
import net.youshallnottick.config.ServerConfig;
import net.youshallnottick.render.OutlineRenderer;

public class TickingTotemBlockEntity extends BlockEntity {

    // Client fields for rendering
    private final OutlineRenderer outlineRenderer;
    private float renderScale;
    private boolean lastActive = false;

    public static final Map<ResourceLocation, Set<BlockPos>> ACTIVE_TICKING_TOTEMS = new HashMap<>();

    public TickingTotemBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(YouShallNotTickRegistry.TICKING_TOTEM_BLOCK_ENTITY.get(), blockPos, blockState);
        outlineRenderer = new OutlineRenderer();
        renderScale = blockState.getValue(TickingTotemBlock.OUTLINED) ? 1 : 0;
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

    public static void handleChunkLoading(LevelAccessor level, ChunkAccess chunk) {
        if (level.isClientSide()) return;

        ResourceLocation dim = Utils.resourceLocationForLevel(level);
        if (dim == null) return;

        for (BlockPos pos : chunk.getBlockEntitiesPos()) {
            if (!(chunk.getBlockEntity(pos) instanceof TickingTotemBlockEntity)) continue;

            if (!chunk.getBlockState(pos).getValue(TickingTotemBlock.POWERED)) {
                addActiveTickingTotem(dim, pos);
            }
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
        }
    }

    public OutlineRenderer getOutlineRenderer() {
        return outlineRenderer;
    }

    public boolean getLastActive() {
        return lastActive;
    }

    public void setLastActive(boolean active) {
        lastActive = active;
    }

    public float getRenderScale() {
        return renderScale;
    }

    public void setRenderScale(float renderScale) {
        this.renderScale = renderScale;
    }

    // Forge calls this, cant @Override due to a shared class file with fabric. This function gets moved in 1.21 Neo so
    // we will come back to this
    @Deprecated
    @SuppressWarnings("unused")
    public AABB getRenderBoundingBox() {
        int inflation = Math.max(
                ServerConfig.tickingTotemMaxEntityTickHorizontalDist.get(),
                ServerConfig.tickingTotemMaxEntityTickVerticalDist.get());
        return new AABB(getBlockPos()).inflate(inflation);
    }
}
