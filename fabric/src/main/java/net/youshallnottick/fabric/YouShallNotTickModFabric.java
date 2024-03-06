package net.youshallnottick.fabric;

import net.fabricmc.api.ModInitializer;
import net.minecraftforge.api.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.youshallnottick.ClientConfig;
import net.youshallnottick.ServerConfig;
import net.youshallnottick.YouShallNotTick;

public class YouShallNotTickModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        YouShallNotTick.init();
        ModLoadingContext.registerConfig(YouShallNotTick.MOD_ID, ModConfig.Type.SERVER, ServerConfig.SERVER_CONFIG);
        ModLoadingContext.registerConfig(YouShallNotTick.MOD_ID, ModConfig.Type.CLIENT, ClientConfig.CLIENT_CONFIG);
    }
}
