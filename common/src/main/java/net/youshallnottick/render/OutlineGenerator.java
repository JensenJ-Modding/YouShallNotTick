package net.youshallnottick.render;

import java.util.function.BiConsumer;

import org.joml.Vector3d;
import org.joml.Vector4f;

@FunctionalInterface
public interface OutlineGenerator {
    void generateOutline(BiConsumer<Vector3d, Vector4f> vertexConsumer);
}
