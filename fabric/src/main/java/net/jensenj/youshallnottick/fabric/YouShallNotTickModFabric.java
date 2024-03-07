package net.jensenj.youshallnottick.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.jensenj.youshallnottick.config.ClientConfig;
import net.jensenj.youshallnottick.registry.TickingTotemBlockEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.api.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.jensenj.youshallnottick.config.ServerConfig;
import net.jensenj.youshallnottick.YouShallNotTick;

@SuppressWarnings("unused")
public class YouShallNotTickModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        YouShallNotTick.init();
        ModLoadingContext.registerConfig(YouShallNotTick.MOD_ID, ModConfig.Type.SERVER, ServerConfig.SERVER_CONFIG);
        ModLoadingContext.registerConfig(YouShallNotTick.MOD_ID, ModConfig.Type.CLIENT, ClientConfig.CLIENT_CONFIG);

        ServerWorldEvents.LOAD.register((MinecraftServer server, ServerLevel level) -> ServerConfig.updateMobLists());
        ServerWorldEvents.UNLOAD.register((MinecraftServer server, ServerLevel level) -> TickingTotemBlockEntity.TICKING_TOTEM_LOCATIONS.clear());
        ServerChunkEvents.CHUNK_LOAD.register(TickingTotemBlockEntity::handleChunkLoading);
        ServerChunkEvents.CHUNK_UNLOAD.register(TickingTotemBlockEntity::handleChunkUnloading);
    }
}
