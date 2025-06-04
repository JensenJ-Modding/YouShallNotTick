package net.youshallnottick.registry;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.phys.Vec3;

import com.mojang.blaze3d.vertex.PoseStack;
import net.youshallnottick.render.TotemOutlineGenerator;

public class TickingTotemBlockEntityRenderer implements BlockEntityRenderer<TickingTotemBlockEntity> {

    private static TotemOutlineGenerator activeOutline;
    private static TotemOutlineGenerator inactiveOutline;
    private static boolean outlineGenerated = false;

    public TickingTotemBlockEntityRenderer(BlockEntityRendererProvider.Context ignoredCtx) {}

    public static void cleanupOutlines() {
        if (activeOutline != null) {
            activeOutline.cleanup();
            activeOutline = null;
        }

        if (inactiveOutline != null) {
            inactiveOutline.cleanup();
            inactiveOutline = null;
        }
        outlineGenerated = false;
    }

    public static void createOutlines() {
        TotemOutlineGenerator.preGenerateOutlineResources();
        activeOutline = new TotemOutlineGenerator(true);
        inactiveOutline = new TotemOutlineGenerator(false);
        outlineGenerated = true;
    }

    @Override
    public boolean shouldRender(TickingTotemBlockEntity blockEntity, Vec3 vec3) {
        return true;
    }

    @Override
    public boolean shouldRenderOffScreen(TickingTotemBlockEntity blockEntity) {
        return true;
    }

    @Override
    public void render(
            TickingTotemBlockEntity totem,
            float tickDelta,
            PoseStack poseStack,
            MultiBufferSource vertexConsumers,
            int light,
            int overlay) {
        renderOutline(totem, poseStack);
    }

    public static void renderOutline(TickingTotemBlockEntity totem, PoseStack poseStack) {
        if (!outlineGenerated) return;

        boolean active = !totem.getBlockState().getValue(TickingTotemBlock.POWERED);

        TotemOutlineGenerator newGenerator = active ? activeOutline : inactiveOutline;

        if (totem.getOutlineRenderer().getGenerator() == null) {
            totem.getOutlineRenderer().setGenerator(newGenerator);
        }

        totem.setRenderScale(outlineAnimation(totem));

        if (active != totem.getLastActive()) {
            totem.getOutlineRenderer().setGenerator(newGenerator);
        }
        totem.setLastActive(active);

        if (totem.getRenderScale() >= 0.0f) {
            totem.getOutlineRenderer().render(poseStack, totem);
        }
    }

    // This is currently frame rate bound, it's not a big issue though
    public static float outlineAnimation(TickingTotemBlockEntity totem) {
        boolean outlined = totem.getBlockState().getValue(TickingTotemBlock.OUTLINED);
        float renderScale = totem.getRenderScale();
        float animationSpeed = 0.015f;
        float targetScale = outlined ? 1.0f : 0.0f;

        float progress = renderScale;
        float eased;
        if (targetScale == 1.0f) {
            eased = progress * progress * progress;
        } else {
            float inv = 1.0f - progress;
            eased = 1.0f - (inv * inv * inv);
        }

        renderScale += (targetScale - eased) * animationSpeed;
        return Math.max(0.0f, Math.min(1.0f, renderScale));
    }
}
