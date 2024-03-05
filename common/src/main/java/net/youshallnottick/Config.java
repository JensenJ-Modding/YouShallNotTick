package net.youshallnottick;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Config {
    public static final String CATEGORY_GENERAL = "general";

    public static ForgeConfigSpec CONFIG;

    public static ForgeConfigSpec.ConfigValue<Integer> maxEntitySpawnDistanceHorizontal;
    public static ForgeConfigSpec.ConfigValue<Integer> maxEntitySpawnDistanceVertical;

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

        List<String> defaultIgnoreList = new ArrayList<>();
        defaultIgnoreList.add("minecraft:wither");
        defaultIgnoreList.add("minecraft:phantom");
        defaultIgnoreList.add("minecraft:ender_dragon");
        defaultIgnoreList.add("minecraft:elder_guardian");

        BUILDER.comment("General settings").push(CATEGORY_GENERAL);
        maxEntitySpawnDistanceHorizontal = BUILDER.comment("Maximum distance from player (horizontally) for entity spawning check [Default: 48]")
                .define("maxEntitySpawnDistanceHorizontal", 48);

        maxEntitySpawnDistanceVertical = BUILDER.comment("Maximum distance from player (vertically) for entity spawning check [Default: 32]")
                .define("maxEntitySpawnDistanceVertical", 32);

        maxEntityTickDistanceHorizontal = BUILDER.comment("Maximum distance from player (horizontally) to allow entity ticking [Default: 48]")
                .define("maxEntityTickDistanceHorizontal", 48);

        maxEntityTickDistanceVertical = BUILDER.comment("Maximum distance from player (vertically) to allow entity ticking [Default: 32]")
                .define("maxEntityTickDistanceVertical", 32);

        entityIgnoreList = BUILDER.comment(
                        "List of entities to ignore when checking if they are allowed to tick",
                        "Tags can be used by using #minecraft:<tag_name> or #modid:<tag_name>",
                        "You can also use a wildcard after modid (modid:*)"
                )
                .define("entityIgnoreList", defaultIgnoreList);

        minPlayers = BUILDER.comment("Minimum number of players before mod is enabled. [Default: 1]")
                .define("minPlayers", 1);


        BUILDER.pop();

        CONFIG = BUILDER.build();
    }
}