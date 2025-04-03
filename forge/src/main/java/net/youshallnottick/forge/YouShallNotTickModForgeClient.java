package net.youshallnottick.forge;

import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.youshallnottick.YouShallNotTickClient;

@SuppressWarnings("unused")
public class YouShallNotTickModForgeClient {

    public static void clientSetup() {
        MinecraftForge.EVENT_BUS.register(YouShallNotTickModForgeClient.class);
        YouShallNotTickClient.initialiseClient();
    }

    @SubscribeEvent
    public static void renderLevel(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
            return;
        }
        YouShallNotTickClient.renderTotemOutlines(event.getPoseStack());
    }

    @SubscribeEvent
    public static void onExitWorld(ClientPlayerNetworkEvent.LoggingOut event) {
        YouShallNotTickClient.clearOutlines();
    }
}
