package net.jensenj.youshallnottick.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.jensenj.youshallnottick.config.ClientConfig;
import net.jensenj.youshallnottick.config.ServerConfig;
import net.jensenj.youshallnottick.YouShallNotTick;

@Mod(YouShallNotTick.MOD_ID)
public class YouShallNotTickModForge {
    public YouShallNotTickModForge() {
        EventBuses.registerModEventBus(YouShallNotTick.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        YouShallNotTick.init();
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ServerConfig.SERVER_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.CLIENT_CONFIG);
    }

    @SubscribeEvent
    public static void onLevelUnload(WorldEvent.Unload e){
        //Clear the ticking totem map for this level
    }

    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load e){
        //Find any ticking totem block entities in this level and add their locations to map
    }

    @SubscribeEvent
    public static void onChunkUnload(ChunkEvent.Unload e){
        //Find any ticking totem block entities in this level and remove their locations from map
    }

}
