package net.youshallnottick.render;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import com.mojang.blaze3d.vertex.PoseStack;
import net.youshallnottick.config.ServerConfig;
import org.joml.Vector3d;
import org.joml.Vector4f;

public class TotemOutlineGenerator implements OutlineGenerator {

    private final BlockPos totemPosition;

    public TotemOutlineGenerator(BlockPos totemPosition) {
        this.totemPosition = totemPosition;
    }

    @Override
    public void generateOutline(BiConsumer<Vector3d, Vector4f> vertexConsumer) {
        Player player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }

        List<BlockPos> blockPositions = getPositionsInEllipsoid(
                totemPosition,
                ServerConfig.totemMaxEntityTickHorizontalDist.get(),
                ServerConfig.totemMaxEntityTickVerticalDist.get());

        // TODO: replace with actual outline generator for the totem
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                for (int z = 0; z < 10; z++) {
                    BlockPos renderPos = totemPosition.offset(x * 4, y * 4, z * -4);
                    Vec3 min = renderPos.getCenter();
                    Vec3 max = renderPos.east(2).above(3).getCenter();
                    Vector4f colour = new Vector4f(1, 0, 0, 1);

                    vertexConsumer.accept(new Vector3d(min.x, min.y, min.z), colour);
                    vertexConsumer.accept(new Vector3d(max.x, min.y, min.z), colour);
                    vertexConsumer.accept(new Vector3d(max.x, max.y, min.z), colour);
                    vertexConsumer.accept(new Vector3d(min.x, max.y, min.z), colour);
                }
            }
        }
    }

    @Override
    public void transformOutline(PoseStack pose, Vec3 camera) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            return;
        }
        double time = System.currentTimeMillis() / 1000.0;
        float scale = 1.0f + 0.25f * (float) Math.sin(time * Math.PI);
        pose.translate(
                totemPosition.getX() - camera.x, totemPosition.getY() - camera.y, totemPosition.getZ() - camera.z);
        pose.scale(scale, scale, scale);
        pose.translate(-totemPosition.getX(), -totemPosition.getY(), -totemPosition.getZ());
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
