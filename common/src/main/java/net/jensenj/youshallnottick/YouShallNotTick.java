package net.jensenj.youshallnottick;

import dev.architectury.platform.Platform;
import net.fabricmc.api.EnvType;
import net.jensenj.youshallnottick.network.YouShallNotTickMessages;
import net.jensenj.youshallnottick.registry.YouShallNotTickRegistry;

public class YouShallNotTick {
    public static final String MOD_ID = "youshallnottick";

    public static void init() {
        YouShallNotTickRegistry.BLOCKS.register();
        YouShallNotTickRegistry.ITEMS.register();
        YouShallNotTickRegistry.BLOCK_ENTITIES.register();

        if(Platform.getEnv() == EnvType.CLIENT)
            YouShallNotTickMessages.registerS2CMessages();
    }
}
