package net.youshallnottick.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.youshallnottick.YouShallNotTickClient;

public class YouShallNotTickModFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        YouShallNotTickClient.initialiseClient();

        // The rendering of totems is handled within LeverRendererMixin, we can still use the event to clear them
        // though.
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            YouShallNotTickClient.clearOutlines();
        });
    }
}
