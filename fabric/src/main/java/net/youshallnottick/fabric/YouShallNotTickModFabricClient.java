package net.youshallnottick.fabric;

import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.youshallnottick.registry.TickingTotemBlockEntityRenderer;
import net.youshallnottick.registry.YouShallNotTickRegistry;

public class YouShallNotTickModFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // TODO: Reenable when ponder works on both server and client outside of dev environment
        // PonderIndex.addPlugin(new YouShallNotTickPonderPlugin());
        BlockEntityRendererRegistry.register(
                YouShallNotTickRegistry.TICKING_TOTEM_BLOCK_ENTITY.get(), TickingTotemBlockEntityRenderer::new);
    }
}
