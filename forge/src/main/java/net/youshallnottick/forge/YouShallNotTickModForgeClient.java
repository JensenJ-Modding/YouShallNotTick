package net.youshallnottick.forge;

import net.minecraftforge.common.MinecraftForge;

import net.youshallnottick.YouShallNotTick;

public class YouShallNotTickModForgeClient {

    public static void clientSetup() {
        MinecraftForge.EVENT_BUS.register(YouShallNotTickModForgeClient.class);
        YouShallNotTick.initClient();
    }
}
