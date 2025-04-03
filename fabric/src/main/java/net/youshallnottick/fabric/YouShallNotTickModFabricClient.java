package net.youshallnottick.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.youshallnottick.YouShallNotTickClient;

@SuppressWarnings("unused")
public class YouShallNotTickModFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        // TODO: Reenable when ponder is properly supported on Fabric
        // YouShallNotTickClient.initialiseClient();

        WorldRenderEvents.AFTER_TRANSLUCENT.register(renderContext -> {
            YouShallNotTickClient.renderTotemOutlines(renderContext.matrixStack());
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            YouShallNotTickClient.clearOutlines();
        });
    }
}
