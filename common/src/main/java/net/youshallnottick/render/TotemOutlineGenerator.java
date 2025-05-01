package net.youshallnottick.render;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.youshallnottick.config.ServerConfig;
import net.youshallnottick.registry.TickingTotemBlockEntity;
import org.joml.Vector3d;
import org.joml.Vector4f;

public class TotemOutlineGenerator extends OutlineGenerator {

    private final boolean isTotemActive;
    private static List<BlockPos> blockPositions;

    public TotemOutlineGenerator(boolean isTotemActive) {
        super();
        this.isTotemActive = isTotemActive;
        populateVertexBuffer();
    }

    public static void preGenerateOutlineResources() {
        blockPositions = getPositionsInEllipsoid(
                ServerConfig.totemMaxEntityTickHorizontalDist.get(), ServerConfig.totemMaxEntityTickVerticalDist.get());
    }

    @Override
    public void generateOutline(BiConsumer<Vector3d, Vector4f> vertexConsumer) {
        Vector4f colour = new Vector4f(0.75f, 0, 0, 1);
        if (isTotemActive) {
            colour = new Vector4f(0.76f, 0.68f, 0.14f, 1);
        }

        OutlineMeshBuilder.buildMesh(blockPositions, colour, (float) 1 / 16, vertexConsumer);
    }

    @Override
    public void transformOutline(PoseStack pose, BlockEntity entity) {
        TickingTotemBlockEntity totem = (TickingTotemBlockEntity) entity;

        float scale = totem.getRenderScale();
        pose.translate(0.5f, 0.5f, 0.5f);
        pose.scale(scale, scale, scale);
        pose.translate(-0.5f, -0.5f, -0.5f);
    }

    public static List<BlockPos> getPositionsInEllipsoid(int horizontalDist, int verticalDist) {
        Set<BlockPos> positions = new HashSet<>();
        // We need to add 0, 0, 0 first to make the mesh centered on the origin of the world
        positions.add(new BlockPos(0, 0, 0));

        for (int x = -horizontalDist; x <= horizontalDist; x++) {
            for (int y = -verticalDist; y <= verticalDist; y++) {
                for (int z = -horizontalDist; z <= horizontalDist; z++) {
                    double dy = y / (double) verticalDist;
                    double distanceSq = x * x + dy * dy + z * z;
                    double maxDistSq = horizontalDist * horizontalDist;

                    if (distanceSq < maxDistSq) {
                        positions.add(new BlockPos(x, y, z));
                    }
                }
            }
        }

        return positions.stream().toList();
    }
}
