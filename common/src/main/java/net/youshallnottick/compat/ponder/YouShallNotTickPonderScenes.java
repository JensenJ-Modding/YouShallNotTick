package net.youshallnottick.compat.ponder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

import static net.minecraft.world.level.block.LeverBlock.POWERED;
import static net.minecraft.world.level.block.RedStoneWireBlock.POWER;

import dev.architectury.registry.registries.RegistrySupplier;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.youshallnottick.registry.YouShallNotTickRegistry;

public class YouShallNotTickPonderScenes {

    public static void register(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PonderSceneRegistrationHelper<RegistrySupplier<?>> HELPER = helper.withKeyFunction(RegistrySupplier::getId);

        HELPER.forComponents(YouShallNotTickRegistry.TICKING_TOTEM_BLOCK)
                .addStoryBoard(
                        "ticking_totem",
                        YouShallNotTickPonderScenes::tickingTotem,
                        YouShallNotTickPonderTags.TICKING_COMPONENTS);
    }

    // TODO: Update ponder to show outlining feature
    public static void tickingTotem(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("ticking_totem", "youshallnottick.ponder.ticking_totem.header");
        scene.configureBasePlate(0, 0, 5);
        scene.world().showSection(util.select().layer(0), Direction.UP);
        scene.addKeyframe();
        scene.idle(5);

        BlockPos tickingTotem = util.grid().at(2, 1, 2);
        Vec3 centerOf = util.vector().centerOf(tickingTotem);
        Vec3 sideOf = centerOf.add(-0.5, 0, 0);

        scene.overlay()
                .showText(100)
                .text("youshallnottick.ponder.ticking_totem.text_1")
                .placeNearTarget()
                .pointAt(centerOf);
        scene.idle(110);
        scene.overlay()
                .showText(100)
                .text("youshallnottick.ponder.ticking_totem.text_2")
                .placeNearTarget()
                .pointAt(centerOf);
        scene.idle(110);
        scene.overlay()
                .showText(100)
                .text("youshallnottick.ponder.ticking_totem.text_3")
                .placeNearTarget()
                .pointAt(centerOf);
        scene.idle(110);

        scene.addKeyframe();
        scene.world().showSection(util.select().position(tickingTotem), Direction.DOWN);
        scene.idle(20);
        scene.overlay()
                .showText(100)
                .text("youshallnottick.ponder.ticking_totem.text_4")
                .placeNearTarget()
                .pointAt(sideOf);
        scene.idle(110);
        scene.overlay()
                .showText(100)
                .text("youshallnottick.ponder.ticking_totem.text_5")
                .placeNearTarget()
                .pointAt(sideOf);
        scene.idle(110);

        scene.addKeyframe();
        scene.overlay()
                .showText(70)
                .text("youshallnottick.ponder.ticking_totem.text_6")
                .placeNearTarget()
                .pointAt(sideOf);
        scene.idle(80);

        BlockPos redstone = util.grid().at(1, 1, 2);
        BlockPos lever = util.grid().at(0, 1, 2);
        scene.world().showSection(util.select().position(redstone), Direction.DOWN);
        scene.idle(5);
        scene.world().showSection(util.select().position(lever), Direction.DOWN);
        scene.idle(20);

        scene.overlay()
                .showControls(util.vector().topOf(lever), Pointing.DOWN, 40)
                .rightClick();
        scene.idle(10);
        scene.world().modifyBlock(lever, blockState -> blockState.cycle(POWERED), false);
        scene.world().modifyBlock(redstone, blockState -> blockState.setValue(POWER, 15), false);
        scene.world().modifyBlock(tickingTotem, blockState -> blockState.cycle(POWERED), false);
        scene.idle(50);
        scene.overlay()
                .showText(100)
                .text("youshallnottick.ponder.ticking_totem.text_7")
                .placeNearTarget()
                .pointAt(sideOf);
        scene.idle(110);
        scene.markAsFinished();
    }
}
