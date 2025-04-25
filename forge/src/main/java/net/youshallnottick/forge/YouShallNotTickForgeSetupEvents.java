package net.youshallnottick.forge;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import net.youshallnottick.registry.TickingTotemBlockEntityRenderer;
import net.youshallnottick.registry.YouShallNotTickRegistry;

public class YouShallNotTickForgeSetupEvents {

    @SubscribeEvent
    public static void onClientSetup(final FMLClientSetupEvent event) {
        BlockEntityRenderers.register(
                YouShallNotTickRegistry.TICKING_TOTEM_BLOCK_ENTITY.get(), TickingTotemBlockEntityRenderer::new);
    }
}
