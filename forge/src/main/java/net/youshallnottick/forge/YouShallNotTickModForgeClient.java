package net.youshallnottick.forge;

import net.minecraftforge.common.MinecraftForge;

import net.createmod.ponder.foundation.PonderIndex;
import net.youshallnottick.compat.ponder.YouShallNotTickPonderPlugin;

public class YouShallNotTickModForgeClient {

    public static void clientSetup() {
        MinecraftForge.EVENT_BUS.register(YouShallNotTickModForgeClient.class);
        PonderIndex.addPlugin(new YouShallNotTickPonderPlugin());
    }
}
