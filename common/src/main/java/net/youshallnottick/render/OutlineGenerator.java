package net.youshallnottick.render;

import java.util.function.BiConsumer;

import net.minecraft.world.phys.Vec3;

import com.mojang.blaze3d.vertex.PoseStack;
import org.joml.Vector3d;
import org.joml.Vector4f;

public interface OutlineGenerator {
    void generateOutline(BiConsumer<Vector3d, Vector4f> vertexConsumer);

    void transformOutline(PoseStack poseStack, Vec3 renderPos, Vec3 camera);
}
