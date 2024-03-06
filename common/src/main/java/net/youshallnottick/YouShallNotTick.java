package net.youshallnottick;

public class YouShallNotTick {
    public static final String MOD_ID = "youshallnottick";

    public static void init() {
        ServerConfig.updateMobLists();
        YouShallNotTickRegistry.BLOCKS.register();
        YouShallNotTickRegistry.ITEMS.register();
        YouShallNotTickRegistry.BLOCK_ENTITIES.register();
    }
}
