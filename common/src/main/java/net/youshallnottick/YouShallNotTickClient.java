package net.youshallnottick;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.player.Player;

import com.mojang.blaze3d.vertex.PoseStack;
import net.createmod.ponder.foundation.PonderIndex;
import net.youshallnottick.compat.ponder.YouShallNotTickPonderPlugin;
import net.youshallnottick.registry.YouShallNotTickRegistry;
import net.youshallnottick.render.OutlineRenderer;
import net.youshallnottick.render.TotemOutlineGenerator;

public class YouShallNotTickClient {

    private static OutlineRenderer outliner;
    // TODO: This is only a temporary variable until we have a better way to add/remove outline generators
    public static boolean generated = false;

    public static void renderTotemOutlines(PoseStack poseStack) {
        Player player = Minecraft.getInstance().player;
        if (player == null
                || !(player.getMainHandItem().getItem()
                        == YouShallNotTickRegistry.TICKING_TOTEM_BLOCK.get().asItem())) {
            clearOutlines();
            return;
        }

        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            return;
        }

        if (outliner == null) {
            outliner = new OutlineRenderer();
        }

        if (!generated) {
            generated = true;
            outliner.setGenerator(new TotemOutlineGenerator(player.blockPosition()));
        }

        outliner.render(
                poseStack, Minecraft.getInstance().gameRenderer.getMainCamera().getPosition());
    }

    public static void clearOutlines() {
        generated = false;
        if (outliner != null) {
            outliner.cleanup();
            outliner = null;
        }
    }

    public static void initialiseClient() {
        PonderIndex.addPlugin(new YouShallNotTickPonderPlugin());
    }
}
