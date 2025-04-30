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

    // TODO: Fix server config not syncing to client correctly, we use the value too early
    public static void createOutlines() {
        TotemOutlineGenerator.preGenerateOutlineResources();
        activeOutline = new TotemOutlineGenerator(true);
        inactiveOutline = new TotemOutlineGenerator(false);
        outlineGenerated = true;
    }

    // TODO: Fix offscreen culling on Forge, and optimise this so we don't always render
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

        if (!outlineGenerated) return;

        boolean outlined = totem.getBlockState().getValue(TickingTotemBlock.OUTLINED);
        boolean active = !totem.getBlockState().getValue(TickingTotemBlock.POWERED);

        TotemOutlineGenerator newGenerator = active ? activeOutline : inactiveOutline;

        // If the outline status has changed
        if (outlined != totem.getLastOutlined()) {
            if (outlined) {
                totem.getOutlineRenderer().setGenerator(newGenerator);
            } else {
                totem.getOutlineRenderer().setGenerator(null);
            }
        }

        // If the active status has changed
        if (active != totem.getLastActive() && outlined) {
            totem.getOutlineRenderer().setGenerator(newGenerator);
        }

        totem.setLastActive(active);
        totem.setLastOutlined(outlined);

        if (outlined && totem.getOutlineRenderer() != null) {
            totem.getOutlineRenderer().render(poseStack);
        }
    }
}
