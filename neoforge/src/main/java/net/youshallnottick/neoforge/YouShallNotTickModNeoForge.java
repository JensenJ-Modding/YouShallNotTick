package net.youshallnottick.neoforge;

import net.minecraft.world.level.Level;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.youshallnottick.YouShallNotTick;
import net.youshallnottick.config.ServerConfig;
import net.youshallnottick.registry.TickingTotemBlockEntity;

@Mod(YouShallNotTick.MOD_ID)
@EventBusSubscriber(modid = YouShallNotTick.MOD_ID)
public class YouShallNotTickModNeoForge {
    public YouShallNotTickModNeoForge(ModContainer container, IEventBus bus) {
        bus.register(YouShallNotTickNeoForgeLifecycleEvents.class);
        container.registerConfig(ModConfig.Type.SERVER, ServerConfig.SERVER_CONFIG);
        YouShallNotTick.init();
    }

    @SubscribeEvent
    public static void onLevelLoad(LevelEvent.Load e) {
        ServerConfig.updateMobLists();
    }

    @SubscribeEvent
    public static void onLevelUnload(LevelEvent.Unload e) {
        if (e.getLevel().isClientSide()) return;
        TickingTotemBlockEntity.ACTIVE_TICKING_TOTEMS.remove(
                ((Level) e.getLevel()).dimension().location());
    }

    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load e) {
        TickingTotemBlockEntity.handleChunkLoading(e.getLevel(), e.getChunk());
    }

    @SubscribeEvent
    public static void onChunkUnload(ChunkEvent.Unload e) {
        TickingTotemBlockEntity.handleChunkUnloading(e.getLevel(), e.getChunk());
    }
}
