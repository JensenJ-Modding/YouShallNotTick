package net.youshallnottick.fabric;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.fml.config.ModConfig;

import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.youshallnottick.YouShallNotTick;
import net.youshallnottick.config.ServerConfig;
import net.youshallnottick.registry.TickingTotemBlockEntity;

public class YouShallNotTickModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        YouShallNotTick.init();
        ForgeConfigRegistry.INSTANCE.register(
                YouShallNotTick.MOD_ID, ModConfig.Type.SERVER, ServerConfig.SERVER_CONFIG);

        ServerWorldEvents.LOAD.register((MinecraftServer server, ServerLevel level) -> ServerConfig.updateMobLists());
        ServerWorldEvents.UNLOAD.register(
                (MinecraftServer server, ServerLevel level) -> TickingTotemBlockEntity.ACTIVE_TICKING_TOTEMS.clear());
        ServerChunkEvents.CHUNK_LOAD.register(TickingTotemBlockEntity::handleChunkLoading);
        ServerChunkEvents.CHUNK_UNLOAD.register(TickingTotemBlockEntity::handleChunkUnloading);
    }
}
