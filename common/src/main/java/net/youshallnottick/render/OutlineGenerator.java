package net.youshallnottick.render;

import java.util.function.BiConsumer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import org.joml.Vector3d;
import org.joml.Vector4f;

public abstract class OutlineGenerator {

    protected VertexBuffer vertexBuffer;

    abstract void generateOutline(BiConsumer<Vector3d, Vector4f> vertexConsumer);

    abstract void transformOutline(PoseStack poseStack);

    protected OutlineGenerator() {
        vertexBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
    }

    public void populateVertexBuffer() {
        RenderSystem.assertOnRenderThread();
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();

        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        generateOutline((position, colour) -> {
            buffer.vertex(position.x, position.y, position.z)
                    .color(colour.x, colour.y, colour.z, colour.w)
                    .endVertex();
        });

        vertexBuffer.bind();
        vertexBuffer.upload(buffer.end());
        VertexBuffer.unbind();
    }

    public void cleanup() {
        if (vertexBuffer != null) {
            vertexBuffer.close();
            vertexBuffer = null;
        }
    }

    public VertexBuffer getVertexBuffer() {
        return vertexBuffer;
    }
}
