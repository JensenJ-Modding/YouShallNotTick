package net.jensenj.youshallnottick;

import net.jensenj.youshallnottick.registry.YouShallNotTickRegistry;

public class YouShallNotTick {
    public static final String MOD_ID = "youshallnottick";

    public static void init() {
        YouShallNotTickRegistry.BLOCKS.register();
        YouShallNotTickRegistry.ITEMS.register();
        YouShallNotTickRegistry.BLOCK_ENTITIES.register();
    }
}
