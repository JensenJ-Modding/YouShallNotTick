package net.jensenj.youshallnottick.neoforge;

import fuzs.forgeconfigapiport.neoforge.api.forge.v4.ForgeConfigRegistry;
import net.jensenj.youshallnottick.YouShallNotTick;
import net.jensenj.youshallnottick.config.ClientConfig;
import net.jensenj.youshallnottick.config.ServerConfig;
import net.jensenj.youshallnottick.registry.TickingTotemBlockEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.common.NeoForge;

@SuppressWarnings("unused")
public class YouShallNotTickModNeoForgeClient {

    public static void clientSetup(){
        NeoForge.EVENT_BUS.register(YouShallNotTickModNeoForgeClient.class);
        ForgeConfigRegistry.INSTANCE.register(YouShallNotTick.MOD_ID, ModConfig.Type.CLIENT, ClientConfig.CLIENT_CONFIG);
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
