package net.youshallnottick.neoforge;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.youshallnottick.YouShallNotTick;

@Mod(value = YouShallNotTick.MOD_ID, dist = Dist.CLIENT)
public class YouShallNotTickModNeoForgeClient {

    public YouShallNotTickModNeoForgeClient(IEventBus bus) {
        YouShallNotTick.initClient();
    }
}
