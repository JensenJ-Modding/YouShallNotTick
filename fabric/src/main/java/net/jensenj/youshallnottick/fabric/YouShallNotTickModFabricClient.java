package net.jensenj.youshallnottick.fabric;

import fuzs.forgeconfigapiport.fabric.api.forge.v4.ForgeConfigRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.jensenj.youshallnottick.YouShallNotTick;
import net.jensenj.youshallnottick.config.ClientConfig;
import net.jensenj.youshallnottick.config.ServerConfig;
import net.jensenj.youshallnottick.registry.TickingTotemBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraftforge.fml.config.ModConfig;

@SuppressWarnings("unused")
public class YouShallNotTickModFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientPlayConnectionEvents.JOIN.register((ClientPacketListener handler, PacketSender sender, Minecraft client) -> ServerConfig.updateMobLists());
        ClientPlayConnectionEvents.DISCONNECT.register((ClientPacketListener handler, Minecraft client) -> TickingTotemBlockEntity.TICKING_TOTEM_LOCATIONS.clear());
        ForgeConfigRegistry.INSTANCE.register(YouShallNotTick.MOD_ID, ModConfig.Type.CLIENT, ClientConfig.CLIENT_CONFIG);
    }
}
