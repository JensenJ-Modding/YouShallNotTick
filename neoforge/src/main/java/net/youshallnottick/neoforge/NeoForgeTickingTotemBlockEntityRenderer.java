package net.youshallnottick.neoforge;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import com.mojang.blaze3d.vertex.PoseStack;
import net.youshallnottick.registry.TickingTotemBlockEntity;
import net.youshallnottick.registry.TickingTotemBlockEntityRenderer;
import org.jetbrains.annotations.NotNull;

// This class purely exists to fix the outline culling issue on 1.21.1, as we can't override getRenderBoundingBox as
// it's added by NeoForge. Every other function call passes up to the main renderer.
public class NeoForgeTickingTotemBlockEntityRenderer implements BlockEntityRenderer<TickingTotemBlockEntity> {

    public NeoForgeTickingTotemBlockEntityRenderer(BlockEntityRendererProvider.Context ignoredCtx) {}

    public static void cleanupOutlines() {
        TickingTotemBlockEntityRenderer.cleanupOutlines();
    }

    public static void createOutlines() {
        TickingTotemBlockEntityRenderer.createOutlines();
    }

    @Override
    public boolean shouldRender(@NotNull TickingTotemBlockEntity blockEntity, @NotNull Vec3 vec3) {
        return true;
    }

    @Override
    public boolean shouldRenderOffScreen(@NotNull TickingTotemBlockEntity blockEntity) {
        return true;
    }

    @Override
    public @NotNull AABB getRenderBoundingBox(@NotNull TickingTotemBlockEntity blockEntity) {
        return AABB.INFINITE;
    }

    @Override
    public void render(
            @NotNull TickingTotemBlockEntity totem,
            float tickDelta,
            @NotNull PoseStack poseStack,
            @NotNull MultiBufferSource vertexConsumers,
            int light,
            int overlay) {

        TickingTotemBlockEntityRenderer.renderOutline(totem, poseStack);
    }
}
