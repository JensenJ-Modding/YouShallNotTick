package net.jensenj.youshallnottick.neoforge;

import fuzs.forgeconfigapiport.neoforge.api.forge.v4.ForgeConfigRegistry;
import net.jensenj.youshallnottick.registry.TickingTotemBlockEntity;
import net.minecraft.world.level.Level;
import net.jensenj.youshallnottick.config.ServerConfig;
import net.jensenj.youshallnottick.YouShallNotTick;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.level.LevelEvent;

@Mod(YouShallNotTick.MOD_ID)
@SuppressWarnings("unused")
public class YouShallNotTickModNeoForge {
    public YouShallNotTickModNeoForge(IEventBus modEventBus) {
        YouShallNotTick.init();

        NeoForge.EVENT_BUS.register(YouShallNotTickModNeoForge.class);
        if(FMLEnvironment.dist == Dist.CLIENT)
            YouShallNotTickModNeoForgeClient.clientSetup();
        ForgeConfigRegistry.INSTANCE.register(YouShallNotTick.MOD_ID, ModConfig.Type.SERVER, ServerConfig.SERVER_CONFIG);
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent e){
        TickingTotemBlockEntity.sendFullTotemMapToPlayer(e.getEntity());
    }

    @SubscribeEvent
    public static void onLevelLoad(LevelEvent.Load e){
        ServerConfig.updateMobLists();
    }

    @SubscribeEvent
    public static void onLevelUnload(LevelEvent.Unload e){
        if(e.getLevel().isClientSide())
            return;
        TickingTotemBlockEntity.TICKING_TOTEM_LOCATIONS.remove(((Level) e.getLevel()).dimension().location());
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
