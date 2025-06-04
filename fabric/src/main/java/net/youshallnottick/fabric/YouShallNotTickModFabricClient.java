package net.youshallnottick.fabric;

import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.event.events.client.ClientPlayerEvent;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.youshallnottick.YouShallNotTick;
import net.youshallnottick.registry.TickingTotemBlockEntityRenderer;
import net.youshallnottick.registry.YouShallNotTickRegistry;

public class YouShallNotTickModFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockEntityRendererRegistry.register(
                YouShallNotTickRegistry.TICKING_TOTEM_BLOCK_ENTITY.get(), TickingTotemBlockEntityRenderer::new);

        YouShallNotTick.initClient();

        ClientLifecycleEvent.CLIENT_STOPPING.register((minecraft) -> TickingTotemBlockEntityRenderer.cleanupOutlines());
        ClientPlayerEvent.CLIENT_PLAYER_QUIT.register((minecraft) -> TickingTotemBlockEntityRenderer.cleanupOutlines());
        ClientPlayerEvent.CLIENT_PLAYER_JOIN.register((minecraft) -> TickingTotemBlockEntityRenderer.createOutlines());
    }
}
