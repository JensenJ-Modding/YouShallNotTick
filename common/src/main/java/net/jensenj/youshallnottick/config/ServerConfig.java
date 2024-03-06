package net.jensenj.youshallnottick.config;

import net.jensenj.youshallnottick.Utils;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ServerConfig {
    public static final String CATEGORY_GENERAL = "general";
    public static final String CATEGORY_SPAWNING = "spawning";
    public static final String CATEGORY_TICKING = "ticking";

    public static ForgeConfigSpec SERVER_CONFIG;

    public static ForgeConfigSpec.ConfigValue<Boolean> shouldEnableSpawnMixin;
    public static ForgeConfigSpec.ConfigValue<Integer> maxEntitySpawnDistanceHorizontal;
    public static ForgeConfigSpec.ConfigValue<Integer> maxEntitySpawnDistanceVertical;

    public static ForgeConfigSpec.ConfigValue<Boolean> shouldEnableTickMixin;
    public static ForgeConfigSpec.ConfigValue<Integer> maxEntityTickDistanceHorizontal;
    public static ForgeConfigSpec.ConfigValue<Integer> maxEntityTickDistanceVertical;

    public static ForgeConfigSpec.ConfigValue<List<String>> entityIgnoreList;
    public static ForgeConfigSpec.ConfigValue<Integer> minPlayers;

    public static final Set<ResourceLocation> entityResources = new HashSet<>();
    public static final Set<TagKey<EntityType<?>>> entityTagKeys = new HashSet<>();
    public static final Set<String> entityWildcards = new HashSet<>();

    public static void updateMobLists() {
        Utils.isIgnored.clear();

        for (String key : entityIgnoreList.get()) {
            if (key.contains("#")) {
                entityTagKeys.add(TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(key.replace("#", ""))));
            } else if (key.contains("*")) {
                entityWildcards.add(key.split(":")[0]);
            } else {
                entityResources.add(new ResourceLocation(key));
            }
        }
    }

    static {
        ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

        BUILDER.comment("General settings").push(CATEGORY_GENERAL);
        minPlayers = BUILDER.comment("Minimum number of players before mod is enabled. [Default: 1]")
                .define("minPlayers", 1);
        BUILDER.pop();

        BUILDER.comment("Spawning settings").push(CATEGORY_SPAWNING);
        shouldEnableSpawnMixin = BUILDER.comment("Whether the living entity spawning check should be enabled [Default: true]")
                .define("enableEntitySpawnCheck", true);
        maxEntitySpawnDistanceHorizontal = BUILDER.comment("Maximum distance from player (horizontally) for living entity spawning check [Default: 48]")
                .define("maxEntitySpawnDistanceHorizontal", 48);
        maxEntitySpawnDistanceVertical = BUILDER.comment("Maximum distance from player (vertically) for living entity spawning check [Default: 32]")
                .define("maxEntitySpawnDistanceVertical", 32);
        BUILDER.pop();

        BUILDER.comment("Ticking settings").push(CATEGORY_TICKING);
        shouldEnableTickMixin = BUILDER.comment("Whether the living entity ticking check should be enabled [Default: true]")
                .define("enableEntityTickCheck", true);
        maxEntityTickDistanceHorizontal = BUILDER.comment("Maximum distance from player (horizontally) to allow living entity ticking [Default: 48]")
                .define("maxEntityTickDistanceHorizontal", 48);
        maxEntityTickDistanceVertical = BUILDER.comment("Maximum distance from player (vertically) to allow living entity ticking [Default: 32]")
                .define("maxEntityTickDistanceVertical", 32);

        List<String> defaultIgnoreList = new ArrayList<>();
        defaultIgnoreList.add("minecraft:wither");
        defaultIgnoreList.add("minecraft:phantom");
        defaultIgnoreList.add("minecraft:ender_dragon");
        defaultIgnoreList.add("minecraft:elder_guardian");
        entityIgnoreList = BUILDER.comment(
                        "List of LIVING entities to ignore when checking if they are allowed to tick",
                        "Only LIVING entities need to be added to this list, all other entities are ignored by the mod",
                        "Tags can be used by using #minecraft:<tag_name> or #modid:<tag_name>",
                        "You can also use a wildcard after modid (modid:*)",
                        "For example, alexsmobs:* would allow all mobs from alex's mobs to tick normally"
                )
                .define("entityIgnoreList", defaultIgnoreList);
        BUILDER.pop();

        SERVER_CONFIG = BUILDER.build();
    }
}