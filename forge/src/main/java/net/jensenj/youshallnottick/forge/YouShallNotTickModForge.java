package net.jensenj.youshallnottick.forge;

import dev.architectury.platform.forge.EventBuses;
import net.jensenj.youshallnottick.registry.TickingTotemBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
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
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> YouShallNotTickModForgeClient::clientSetup);

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ServerConfig.SERVER_CONFIG);
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent e){
        TickingTotemBlockEntity.sendFullTotemMapToPlayer(e.getPlayer());
    }

    @SubscribeEvent
    public static void onLevelLoad(WorldEvent.Load e){
        ServerConfig.updateMobLists();
    }

    @SubscribeEvent
    public static void onLevelUnload(WorldEvent.Unload e){
        if(e.getWorld().isClientSide())
            return;
        TickingTotemBlockEntity.TICKING_TOTEM_LOCATIONS.remove(((Level) e.getWorld()).dimension().location());
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
