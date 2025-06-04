package net.youshallnottick.neoforge;

import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.event.events.client.ClientPlayerEvent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.youshallnottick.YouShallNotTick;

@Mod(value = YouShallNotTick.MOD_ID, dist = Dist.CLIENT)
public class YouShallNotTickModNeoForgeClient {

    public YouShallNotTickModNeoForgeClient(IEventBus bus) {
        YouShallNotTick.initClient();

        ClientLifecycleEvent.CLIENT_STOPPING.register(
                (minecraft) -> NeoForgeTickingTotemBlockEntityRenderer.cleanupOutlines());
        ClientPlayerEvent.CLIENT_PLAYER_QUIT.register(
                (minecraft) -> NeoForgeTickingTotemBlockEntityRenderer.cleanupOutlines());
        ClientPlayerEvent.CLIENT_PLAYER_JOIN.register(
                (minecraft) -> NeoForgeTickingTotemBlockEntityRenderer.createOutlines());
    }
}
