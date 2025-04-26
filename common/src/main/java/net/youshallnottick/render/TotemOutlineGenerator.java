package net.youshallnottick.render;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

import com.mojang.blaze3d.vertex.PoseStack;
import org.joml.Vector3d;
import org.joml.Vector4f;

public class TotemOutlineGenerator extends OutlineGenerator {

    private final boolean isTotemActive;

    public TotemOutlineGenerator(boolean isTotemActive) {
        super();
        this.isTotemActive = isTotemActive;
        populateVertexBuffer();
    }

    @Override
    public void generateOutline(BiConsumer<Vector3d, Vector4f> vertexConsumer) {
        // List<BlockPos> blockPositions = getPositionsInEllipsoid(
        //        totemPosition,
        //        ServerConfig.totemMaxEntityTickHorizontalDist.get(),
        //        ServerConfig.totemMaxEntityTickVerticalDist.get());

        Vector4f colour = new Vector4f(1, 0, 0, 1);
        if (isTotemActive) {
            colour = new Vector4f(0, 1, 0, 1);
        }

        // TODO: replace with actual outline generator for the totem
        for (int x = 0; x < 100; x++) {
            for (int y = 0; y < 10; y++) {
                for (int z = 0; z < 100; z++) {
                    BlockPos renderPos = new BlockPos(x * 4, y * 4, -z * 4);
                    Vec3 min = renderPos.above(1).getCenter();
                    Vec3 max = renderPos.east(1).above(2).getCenter();

                    vertexConsumer.accept(new Vector3d(min.x, min.y, min.z), colour);
                    vertexConsumer.accept(new Vector3d(max.x, min.y, min.z), colour);
                    vertexConsumer.accept(new Vector3d(max.x, max.y, min.z), colour);
                    vertexConsumer.accept(new Vector3d(min.x, max.y, min.z), colour);
                }
            }
        }
    }

    @Override
    public void transformOutline(PoseStack pose) {
        double time = System.currentTimeMillis() / 1000.0;
        float scale = 1.0f + 0.25f * (float) Math.sin(time * Math.PI);
        // pose.scale(scale, scale, scale);
    }

    public static List<BlockPos> getPositionsInEllipsoid(BlockPos center, int horizontalDist, int verticalDist) {
        List<BlockPos> positions = new ArrayList<>();

        int xMin = center.getX() - horizontalDist;
        int xMax = center.getX() + horizontalDist;
        int yMin = center.getY() - verticalDist;
        int yMax = center.getY() + verticalDist;
        int zMin = center.getZ() - horizontalDist;
        int zMax = center.getZ() + horizontalDist;

        for (int x = xMin; x <= xMax; x++) {
            for (int y = yMin; y <= yMax; y++) {
                for (int z = zMin; z <= zMax; z++) {
                    double dx = x - center.getX();
                    double dy = (y - center.getY()) / (double) verticalDist;
                    double dz = z - center.getZ();

                    double distanceSq = dx * dx + dy * dy + dz * dz;
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
