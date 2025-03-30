package net.youshallnottick.compat.ponder;

import dev.architectury.registry.registries.RegistrySupplier;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.youshallnottick.registry.YouShallNotTickRegistry;

public class YouShallNotTickPonderScenes {

    public static void register(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PonderSceneRegistrationHelper<RegistrySupplier<?>> HELPER = helper.withKeyFunction(RegistrySupplier::getId);

        HELPER.forComponents(YouShallNotTickRegistry.TICKING_TOTEM_BLOCK).addStoryBoard("ticking_totem", YouShallNotTickPonderScenes::tickingTotem, YouShallNotTickPonderTags.TICKING_COMPONENTS);
    }

    public static void tickingTotem(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("ticking_totem", "youshallnottick.ponder.ticking_totem.header");
        scene.configureBasePlate(0, 0, 5);
        scene.world().showSection(util.select().layer(0), Direction.UP);

        scene.markAsFinished();
    }
}
