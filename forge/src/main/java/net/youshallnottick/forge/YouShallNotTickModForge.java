package net.youshallnottick.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.youshallnottick.YouShallNotTick;

@Mod(YouShallNotTick.MOD_ID)
public class YouShallNotTickModForge {
    public YouShallNotTickModForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(YouShallNotTick.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        YouShallNotTick.init();
    }
}
