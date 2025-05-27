package net.youshallnottick.compat.ponder;

import net.minecraft.resources.ResourceLocation;

import dev.architectury.registry.registries.RegistrySupplier;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.youshallnottick.YouShallNotTick;
import net.youshallnottick.registry.YouShallNotTickRegistry;

public class YouShallNotTickPonderTags {

    public static final ResourceLocation TICKING_COMPONENTS =
            ResourceLocation.fromNamespaceAndPath(YouShallNotTick.MOD_ID, "ticking_components");

    public static void register(PonderTagRegistrationHelper<ResourceLocation> helper) {
        PonderTagRegistrationHelper<RegistrySupplier<?>> HELPER = helper.withKeyFunction(RegistrySupplier::getId);

        helper.registerTag(TICKING_COMPONENTS)
                .addToIndex()
                .item(YouShallNotTickRegistry.TICKING_TOTEM_BLOCK.get(), true, false)
                .title("youshallnottick.ponder.tag.ticking_components")
                .description("youshallnottick.ponder.tag.ticking_components.description")
                .register();

        HELPER.addToTag(TICKING_COMPONENTS).add(YouShallNotTickRegistry.TICKING_TOTEM_BLOCK);
    }
}
