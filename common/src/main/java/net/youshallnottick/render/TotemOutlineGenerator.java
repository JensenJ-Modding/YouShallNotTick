package net.youshallnottick.render;

import java.util.ArrayList;
import java.util.List;
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

    // TODO: Fix minor discrepancy in outline vs real ticking distance
    // This is caused by measuring in full blocks for outline and raw distance for ticking
    // Could be maybe fixed by flooring/rounding the result in the raw distance check
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
        // Transform the outline to the correct location around the totem
        float offsetX = (float) ServerConfig.totemMaxEntityTickHorizontalDist.get();
        float offsetY = (float) ServerConfig.totemMaxEntityTickVerticalDist.get();
        float offsetZ = (float) ServerConfig.totemMaxEntityTickHorizontalDist.get() / 4;

        TickingTotemBlockEntity totem = (TickingTotemBlockEntity) entity;

        float scale = totem.getRenderScale();
        pose.translate(0.5f, 0.5f, 0.5f);
        pose.scale(scale, scale, scale);

        pose.translate(-offsetX, -offsetY, -offsetZ);
    }

    public static List<BlockPos> getPositionsInEllipsoid(int horizontalDist, int verticalDist) {
        List<BlockPos> positions = new ArrayList<>();

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

        return positions;
    }
}
