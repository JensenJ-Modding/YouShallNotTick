package net.jensenj.youshallnottick.forge;

import dev.architectury.platform.forge.EventBuses;
import net.jensenj.youshallnottick.registry.TickingTotemBlockEntity;
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
@SuppressWarnings("unused")
public class YouShallNotTickModForge {
    public YouShallNotTickModForge() {
        EventBuses.registerModEventBus(YouShallNotTick.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        YouShallNotTick.init();
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ServerConfig.SERVER_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.CLIENT_CONFIG);
    }

    @SubscribeEvent
    public static void onLevelUnload(WorldEvent.Unload e){
        if(e.getWorld().isClientSide())
            return;
        TickingTotemBlockEntity.TICKING_TOTEM_LOCATIONS.remove(e.getWorld().dimensionType());
    }

    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load e){
        TickingTotemBlockEntity.handleChunkLoading(e.getWorld(), e.getChunk());
    }

    @SubscribeEvent
    public static void onChunkUnload(ChunkEvent.Unload e){
        TickingTotemBlockEntity.handleChunkUnloading(e.getWorld(), e.getChunk());
    }

}
