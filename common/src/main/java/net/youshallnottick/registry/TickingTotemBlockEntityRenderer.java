package net.youshallnottick.registry;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

import com.mojang.blaze3d.vertex.PoseStack;
import net.youshallnottick.render.TotemOutlineGenerator;

public class TickingTotemBlockEntityRenderer implements BlockEntityRenderer<TickingTotemBlockEntity> {

    private static final TotemOutlineGenerator activeOutline = new TotemOutlineGenerator(true);
    private static final TotemOutlineGenerator inactiveOutline = new TotemOutlineGenerator(false);

    public TickingTotemBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {}

    public static void cleanupOutlines() {
        activeOutline.cleanup();
        inactiveOutline.cleanup();
    }

    @Override
    public void render(
            TickingTotemBlockEntity totem,
            float tickDelta,
            PoseStack poseStack,
            MultiBufferSource vertexConsumers,
            int light,
            int overlay) {

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
