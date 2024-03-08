package net.jensenj.youshallnottick.forge;

import net.jensenj.youshallnottick.config.ClientConfig;
import net.jensenj.youshallnottick.config.ServerConfig;
import net.jensenj.youshallnottick.registry.TickingTotemBlockEntity;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

@SuppressWarnings("unused")
public class YouShallNotTickModForgeClient {

    public static void clientSetup(){
        MinecraftForge.EVENT_BUS.register(YouShallNotTickModForgeClient.class);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.CLIENT_CONFIG);
    }

    @SubscribeEvent
    public static void onPlayerDisconnect(ClientPlayerNetworkEvent.LoggingOut e){
        TickingTotemBlockEntity.TICKING_TOTEM_LOCATIONS.clear();
    }

    @SubscribeEvent
    public static void onPlayerConnect(ClientPlayerNetworkEvent.LoggingIn e){
        ServerConfig.updateMobLists();
    }
}
