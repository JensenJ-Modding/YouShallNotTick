package net.youshallnottick.render;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.level.block.entity.BlockEntity;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;

public class OutlineRenderer {
    private OutlineGenerator outlineGenerator;

    public void render(PoseStack poseStack, BlockEntity blockEntity) {
        if (outlineGenerator == null) {
            return;
        }
        RenderSystem.assertOnRenderThread();
        RenderSystem.enableDepthTest();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        poseStack.pushPose();
        outlineGenerator.transformOutline(poseStack, blockEntity);
        VertexBuffer vertexBuffer = outlineGenerator.getVertexBuffer();
        if (vertexBuffer != null && !vertexBuffer.isInvalid()) {
            vertexBuffer.bind();
            vertexBuffer.drawWithShader(
                    poseStack.last().pose(), RenderSystem.getProjectionMatrix(), RenderSystem.getShader());
            VertexBuffer.unbind();
        }
        RenderSystem.disableDepthTest();
        poseStack.popPose();
    }

    public void setGenerator(OutlineGenerator generator) {
        outlineGenerator = generator;
    }

    public OutlineGenerator getGenerator() {
        return outlineGenerator;
    }
}
