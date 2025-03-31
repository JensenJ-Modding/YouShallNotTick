package net.youshallnottick.compat.ponder;

import net.minecraft.resources.ResourceLocation;

import net.createmod.ponder.api.registration.*;
import net.youshallnottick.YouShallNotTick;
import org.jetbrains.annotations.NotNull;

public class YouShallNotTickPonderPlugin implements PonderPlugin {
    @Override
    public @NotNull String getModId() {
        return YouShallNotTick.MOD_ID;
    }

    @Override
    public void registerScenes(@NotNull PonderSceneRegistrationHelper<ResourceLocation> helper) {
        YouShallNotTickPonderScenes.register(helper);
    }

    @Override
    public void registerTags(@NotNull PonderTagRegistrationHelper<ResourceLocation> helper) {
        YouShallNotTickPonderTags.register(helper);
    }
}
