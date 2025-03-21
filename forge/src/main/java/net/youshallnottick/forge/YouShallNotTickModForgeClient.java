package net.youshallnottick.forge;

import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.youshallnottick.config.ServerConfig;

@SuppressWarnings("unused")
public class YouShallNotTickModForgeClient {

    public static void clientSetup() {
        MinecraftForge.EVENT_BUS.register(YouShallNotTickModForgeClient.class);
    }

    @SubscribeEvent
    public static void onPlayerConnect(ClientPlayerNetworkEvent.LoggingIn e) {
        ServerConfig.updateMobLists();
    }
}
