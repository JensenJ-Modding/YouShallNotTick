package net.youshallnottick.render;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.phys.Vec3;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;

public class OutlineRenderer {
    private VertexBuffer vertexBuffer;
    private OutlineGenerator outlineGenerator;

    private void refreshRenderer() {
        cleanup();
        vertexBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
        updateBuffer();
    }

    private void updateBuffer() {
        RenderSystem.assertOnRenderThread();
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();

        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        outlineGenerator.generateOutline((position, colour) -> {
            buffer.vertex(position.x, position.y, position.z)
                    .color(colour.x, colour.y, colour.z, colour.w)
                    .endVertex();
        });

        vertexBuffer.bind();
        vertexBuffer.upload(buffer.end());
        VertexBuffer.unbind();
    }

    public void render(PoseStack poseStack, Vec3 renderPos, Vec3 camera) {
        RenderSystem.assertOnRenderThread();
        RenderSystem.enableDepthTest();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        poseStack.pushPose();
        outlineGenerator.transformOutline(poseStack, renderPos, camera);
        if (vertexBuffer != null && !vertexBuffer.isInvalid()) {
            vertexBuffer.bind();
            vertexBuffer.drawWithShader(
                    poseStack.last().pose(), RenderSystem.getProjectionMatrix(), RenderSystem.getShader());
            VertexBuffer.unbind();
        }
        RenderSystem.disableDepthTest();
        poseStack.popPose();
    }

    public void cleanup() {
        if (vertexBuffer != null) {
            vertexBuffer.close();
            vertexBuffer = null;
        }
    }

    public void setGenerator(OutlineGenerator generator) {
        outlineGenerator = generator;
        refreshRenderer();
    }
}
