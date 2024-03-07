package net.jensenj.youshallnottick.forge;

import dev.architectury.platform.forge.EventBuses;
import net.jensenj.youshallnottick.config.ClientConfig;
import net.jensenj.youshallnottick.registry.TickingTotemBlockEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.jensenj.youshallnottick.config.ServerConfig;
import net.jensenj.youshallnottick.YouShallNotTick;

@Mod(YouShallNotTick.MOD_ID)
@SuppressWarnings("unused")
public class YouShallNotTickModForge {
    public YouShallNotTickModForge() {
        EventBuses.registerModEventBus(YouShallNotTick.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        YouShallNotTick.init();

        MinecraftForge.EVENT_BUS.register(YouShallNotTickModForge.class);

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.CLIENT_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ServerConfig.SERVER_CONFIG);
    }

    @SubscribeEvent
    public static void onLevelLoad(LevelEvent.Load e){
        ServerConfig.updateMobLists();
    }

    @SubscribeEvent
    public static void onLevelUnload(LevelEvent.Unload e){
        if(e.getLevel().isClientSide())
            return;
        TickingTotemBlockEntity.TICKING_TOTEM_LOCATIONS.remove(e.getLevel().dimensionType());
    }

    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load e){
        TickingTotemBlockEntity.handleChunkLoading(e.getLevel(), e.getChunk());
    }

    @SubscribeEvent
    public static void onChunkUnload(ChunkEvent.Unload e){
        TickingTotemBlockEntity.handleChunkUnloading(e.getLevel(), e.getChunk());
    }

}
