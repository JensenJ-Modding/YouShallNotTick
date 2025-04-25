package net.youshallnottick;

import net.youshallnottick.registry.YouShallNotTickRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class YouShallNotTick {
    public static final String MOD_ID = "youshallnottick";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static void init() {
        YouShallNotTickRegistry.BLOCKS.register();
        YouShallNotTickRegistry.ITEMS.register();
        YouShallNotTickRegistry.BLOCK_ENTITIES.register();
    }
}
