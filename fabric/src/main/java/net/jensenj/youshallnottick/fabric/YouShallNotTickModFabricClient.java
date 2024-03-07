package net.jensenj.youshallnottick.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.jensenj.youshallnottick.config.ServerConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;

@SuppressWarnings("unused")
public class YouShallNotTickModFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientPlayConnectionEvents.JOIN.register((ClientPacketListener handler, PacketSender sender, Minecraft client) -> ServerConfig.updateMobLists());
    }
}
