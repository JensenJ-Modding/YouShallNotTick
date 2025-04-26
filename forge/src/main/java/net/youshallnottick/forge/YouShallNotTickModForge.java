package net.youshallnottick.forge;

import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import dev.architectury.platform.forge.EventBuses;
import net.youshallnottick.YouShallNotTick;
import net.youshallnottick.config.ServerConfig;
import net.youshallnottick.registry.TickingTotemBlockEntity;

@Mod(YouShallNotTick.MOD_ID)
public class YouShallNotTickModForge {
    public YouShallNotTickModForge(FMLJavaModLoadingContext context) {
        EventBuses.registerModEventBus(YouShallNotTick.MOD_ID, context.getModEventBus());
        YouShallNotTick.init();

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> YouShallNotTickModForgeClient::clientSetup);

        IEventBus bus = context.getModEventBus();
        bus.register(YouShallNotTickForgeLifecycleEvents.class);

        MinecraftForge.EVENT_BUS.register(YouShallNotTickModForge.class);
        context.registerConfig(ModConfig.Type.SERVER, ServerConfig.SERVER_CONFIG);
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
