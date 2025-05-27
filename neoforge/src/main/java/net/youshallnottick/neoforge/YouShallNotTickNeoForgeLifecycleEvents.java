package net.youshallnottick.neoforge;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.youshallnottick.registry.TickingTotemBlockEntityRenderer;
import net.youshallnottick.registry.YouShallNotTickRegistry;

public class YouShallNotTickNeoForgeLifecycleEvents {

    @SubscribeEvent
    public static void onClientSetup(final FMLClientSetupEvent event) {
        BlockEntityRenderers.register(
                YouShallNotTickRegistry.TICKING_TOTEM_BLOCK_ENTITY.get(), TickingTotemBlockEntityRenderer::new);
    }
}
