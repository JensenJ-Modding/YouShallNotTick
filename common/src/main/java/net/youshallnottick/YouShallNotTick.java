package net.youshallnottick;

import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.event.events.client.ClientPlayerEvent;
import dev.architectury.platform.Platform;
import net.youshallnottick.compat.ponder.YouShallNotTickPonderPlugin;
import net.youshallnottick.registry.TickingTotemBlockEntityRenderer;
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

    public static void initClient() {
        if (Platform.isModLoaded("ponder")) {
            YouShallNotTickPonderPlugin.registerPlugin();
        }

        ClientLifecycleEvent.CLIENT_STOPPING.register((minecraft) -> TickingTotemBlockEntityRenderer.cleanupOutlines());
        ClientPlayerEvent.CLIENT_PLAYER_QUIT.register((minecraft) -> TickingTotemBlockEntityRenderer.cleanupOutlines());

        ClientPlayerEvent.CLIENT_PLAYER_JOIN.register((minecraft) -> TickingTotemBlockEntityRenderer.createOutlines());
    }
}
