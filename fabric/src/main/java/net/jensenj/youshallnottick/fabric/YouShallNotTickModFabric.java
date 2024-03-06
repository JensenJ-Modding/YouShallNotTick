package net.jensenj.youshallnottick.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerBlockEntityEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.jensenj.youshallnottick.config.ClientConfig;
import net.jensenj.youshallnottick.config.ServerConfig;
import net.jensenj.youshallnottick.YouShallNotTick;

public class YouShallNotTickModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        YouShallNotTick.init();
        ModLoadingContext.registerConfig(YouShallNotTick.MOD_ID, ModConfig.Type.SERVER, ServerConfig.SERVER_CONFIG);
        ModLoadingContext.registerConfig(YouShallNotTick.MOD_ID, ModConfig.Type.CLIENT, ClientConfig.CLIENT_CONFIG);

        ServerBlockEntityEvents.BLOCK_ENTITY_LOAD.register((BlockEntity blockEntity, ServerLevel level) -> {

        });

        ServerBlockEntityEvents.BLOCK_ENTITY_UNLOAD.register((BlockEntity blockEntity, ServerLevel level) -> {

        });
    }
}
