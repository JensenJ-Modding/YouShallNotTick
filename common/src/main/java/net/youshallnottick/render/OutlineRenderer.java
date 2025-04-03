package net.youshallnottick.render;

import java.util.HashSet;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.phys.Vec3;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;

public class OutlineRenderer {
    private VertexBuffer vertexBuffer;
    private final HashSet<OutlineGenerator> outlineGenerators = new HashSet<>();

    private void refreshRenderer() {
        if (vertexBuffer != null) {
            vertexBuffer.close();
            vertexBuffer = null;
        }
        vertexBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
        updateBuffer();
    }

    public void addGenerator(OutlineGenerator generator) {
        outlineGenerators.add(generator);
        refreshRenderer();
    }

    public void removeGenerator(OutlineGenerator generator) {
        outlineGenerators.remove(generator);
        refreshRenderer();
    }

    private void updateBuffer() {
        RenderSystem.assertOnRenderThread();
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();

        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        for (OutlineGenerator generator : outlineGenerators) {
            generator.generateOutline((position, colour) -> {
                buffer.vertex(position.x, position.y, position.z)
                        .color(colour.x, colour.y, colour.z, colour.w)
                        .endVertex();
            });
        }

        vertexBuffer.bind();
        vertexBuffer.upload(buffer.end());
        VertexBuffer.unbind();
    }

    public void render(PoseStack poseStack, Vec3 camera) {
        RenderSystem.assertOnRenderThread();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        poseStack.pushPose();
        PoseStack.Pose pose = poseStack.last();
        poseStack.translate(-camera.x, -camera.y, -camera.z);
        if (vertexBuffer != null && !vertexBuffer.isInvalid()) {
            vertexBuffer.bind();
            vertexBuffer.drawWithShader(pose.pose(), RenderSystem.getProjectionMatrix(), RenderSystem.getShader());
            VertexBuffer.unbind();
        }
        poseStack.popPose();
    }

    public void cleanup() {
        if (vertexBuffer != null) {
            vertexBuffer.close();
            vertexBuffer = null;
        }
        outlineGenerators.clear();
    }
}
