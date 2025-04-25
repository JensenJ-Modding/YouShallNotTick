package net.youshallnottick.registry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

import com.mojang.blaze3d.vertex.PoseStack;
import net.youshallnottick.render.TotemOutlineGenerator;

public class TickingTotemBlockEntityRenderer implements BlockEntityRenderer<TickingTotemBlockEntity> {

    public TickingTotemBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {}

    @Override
    public void render(
            TickingTotemBlockEntity totem,
            float tickDelta,
            PoseStack poseStack,
            MultiBufferSource vertexConsumers,
            int light,
            int overlay) {

        boolean outlined = totem.getBlockState().getValue(TickingTotemBlock.OUTLINED);
        boolean active = totem.getBlockState().getValue(TickingTotemBlock.POWERED);

        // If the outline status has changed
        if (outlined != totem.getLastOutlined()) {
            if (outlined) {
                totem.getOutlineRenderer().setGenerator(new TotemOutlineGenerator(active));
            } else {
                totem.getOutlineRenderer().cleanup();
            }
        }

        // If the active status has changed
        if (active != totem.getLastActive() && outlined) {
            totem.getOutlineRenderer().setGenerator(new TotemOutlineGenerator(active));
        }

        totem.setLastActive(active);
        totem.setLastOutlined(outlined);

        if (outlined) {
            totem.getOutlineRenderer()
                    .render(
                            poseStack,
                            totem.getBlockPos().getCenter(),
                            Minecraft.getInstance().gameRenderer.getMainCamera().getPosition());
        }
    }
}
