package net.youshallnottick.fabric;

import net.fabricmc.api.ModInitializer;
import net.youshallnottick.YouShallNotTick;

public class YouShallNotTickModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        YouShallNotTick.init();
    }
}
