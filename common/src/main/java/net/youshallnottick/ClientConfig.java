package net.youshallnottick;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {

    public static final String CATEGORY_CLIENT = "client";

    public static ForgeConfigSpec CLIENT_CONFIG;

    public static ForgeConfigSpec.ConfigValue<Boolean> shouldRenderFrozenEntities;

    static {
        ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

        BUILDER.comment("Client Settings").push(CATEGORY_CLIENT);
        shouldRenderFrozenEntities = BUILDER.comment("Whether entities \"frozen\" by the mod should be rendered. [Default: true]")
                .define("frozenEntityRendering", true);
        BUILDER.pop();

        CLIENT_CONFIG = BUILDER.build();
    }
}
