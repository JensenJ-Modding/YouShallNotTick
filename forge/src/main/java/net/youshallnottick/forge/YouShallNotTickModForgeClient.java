package net.youshallnottick.forge;

import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.youshallnottick.YouShallNotTickClient;
import net.youshallnottick.registry.TickingTotemBlockEntity;

public class YouShallNotTickModForgeClient {

    public static void clientSetup() {
        MinecraftForge.EVENT_BUS.register(YouShallNotTickModForgeClient.class);
        YouShallNotTickClient.initialiseClient();
    }

    @SubscribeEvent
    public static void renderLevel(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_SOLID_BLOCKS) {
            return;
        }
        YouShallNotTickClient.renderTotemOutlines(event.getPoseStack());
    }

    @SubscribeEvent
    public static void onExitWorld(ClientPlayerNetworkEvent.LoggingOut event) {
        YouShallNotTickClient.clearOutlines();
        TickingTotemBlockEntity.ACTIVE_TICKING_TOTEMS.clear();
        TickingTotemBlockEntity.OUTLINED_TICKING_TOTEMS.clear();
    }
}
