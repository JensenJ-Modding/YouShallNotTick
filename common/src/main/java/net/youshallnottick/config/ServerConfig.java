package net.youshallnottick.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.ForgeConfigSpec;

import net.youshallnottick.Utils;

public class ServerConfig {
    public static final String CATEGORY_GENERAL = "general";
    public static final String CATEGORY_SPAWNING = "spawning";
    public static final String CATEGORY_TICKING = "ticking";
    public static final String CATEGORY_TOTEM = "totem";
    public static ForgeConfigSpec SERVER_CONFIG;
    public static ForgeConfigSpec.ConfigValue<Integer> minPlayers;
    public static ForgeConfigSpec.ConfigValue<Boolean> spectatorsAllowTicking;
    public static ForgeConfigSpec.ConfigValue<Boolean> shouldEnableSpawnMixin;
    public static ForgeConfigSpec.ConfigValue<Integer> playerMaxEntitySpawnHorizontalDist;
    public static ForgeConfigSpec.ConfigValue<Integer> playerMaxEntitySpawnVerticalDist;

    public static ForgeConfigSpec.ConfigValue<Boolean> shouldEnableAITickMixin;
    public static ForgeConfigSpec.ConfigValue<Boolean> shouldRaidParticipantsTick;
    public static ForgeConfigSpec.ConfigValue<Integer> playerMaxEntityTickHorizontalDist;
    public static ForgeConfigSpec.ConfigValue<Integer> playerMaxEntityTickVerticalDist;
    public static ForgeConfigSpec.ConfigValue<List<String>> entityIgnoreList;
    public static final Set<ResourceLocation> entityResources = new HashSet<>();
    public static final Set<TagKey<EntityType<?>>> entityTagKeys = new HashSet<>();
    public static final Set<String> entityWildcards = new HashSet<>();

    public static ForgeConfigSpec.ConfigValue<Boolean> shouldEnableTotemOfTicking;
    public static ForgeConfigSpec.ConfigValue<Integer> totemMaxEntitySpawnHorizontalDist;
    public static ForgeConfigSpec.ConfigValue<Integer> totemMaxEntitySpawnVerticalDist;
    public static ForgeConfigSpec.ConfigValue<Integer> totemMaxEntityTickHorizontalDist;
    public static ForgeConfigSpec.ConfigValue<Integer> totemMaxEntityTickVerticalDist;

    public static void updateMobLists() {
        Utils.isIgnored.clear();

        for (String key : entityIgnoreList.get()) {
            if (key.contains("#")) {
                entityTagKeys.add(TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(key.replace("#", ""))));
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
        spectatorsAllowTicking = BUILDER.comment("Should spectators allow nearby entities to tick. [Default: false]")
                .define("spectatorsAllowTicking", false);
        BUILDER.pop();

        BUILDER.comment("Spawning settings").push(CATEGORY_SPAWNING);
        shouldEnableSpawnMixin = BUILDER.comment(
                        "Whether the living entity spawning check should be enabled" + " [Default: false]",
                        "Warning: having this turned on can cause higher mob spawn rates"
                                + " near players, as there are less places for mobs to spawn")
                .define("enableEntitySpawnCheck", false);
        playerMaxEntitySpawnHorizontalDist = BUILDER.comment(
                        "Maximum distance from player (horizontally) for living entity"
                                + " spawning check [Default: 48]")
                .define("playerMaxEntitySpawnDistanceHorizontal", 48);
        playerMaxEntitySpawnVerticalDist = BUILDER.comment(
                        "Maximum distance from player (vertically) for living entity" + " spawning check [Default: 32]")
                .define("playerMaxEntitySpawnDistanceVertical", 32);
        BUILDER.pop();

        BUILDER.comment("Ticking settings").push(CATEGORY_TICKING);
        shouldEnableAITickMixin = BUILDER.comment("Whether the living entity AI ticking check should be enabled. This"
                        + " disables AI for entities outside of tick distance."
                        + " [Default: true]")
                .define("enableAIEntityTickCheck", true);
        shouldRaidParticipantsTick = BUILDER.comment(
                        "Should raid participants tick regardless of the range from a player or totem?"
                                + " [Default: true]")
                .define("shouldRaidParticipantsTick", true);
        playerMaxEntityTickHorizontalDist = BUILDER.comment(
                        "Maximum distance from player (horizontally) to allow living entity" + " ticking [Default: 48]")
                .define("playerMaxEntityTickDistanceHorizontal", 48);
        playerMaxEntityTickVerticalDist = BUILDER.comment(
                        "Maximum distance from player (vertically) to allow living entity" + " ticking [Default: 32]")
                .define("playerMaxEntityTickDistanceVertical", 32);

        List<String> defaultIgnoreList = new ArrayList<>();
        defaultIgnoreList.add("minecraft:wither");
        defaultIgnoreList.add("minecraft:phantom");
        defaultIgnoreList.add("minecraft:blaze");
        defaultIgnoreList.add("minecraft:ghast");
        defaultIgnoreList.add("minecraft:enderman");
        defaultIgnoreList.add("minecraft:ender_dragon");
        defaultIgnoreList.add("minecraft:elder_guardian");
        defaultIgnoreList.add("minecraft:warden");
        defaultIgnoreList.add("create:package");
        defaultIgnoreList.add("railways:conductor");
        defaultIgnoreList.add("minecolonies:*");
        entityIgnoreList = BUILDER.comment(
                        "List of living entities to ignore when checking if they are" + " allowed to tick",
                        "Only living entities need to be added to this list, all other"
                                + " entities are ignored by the mod",
                        "Living entities which have an owner, e.g. wolves are also ignored" + " by the mod.",
                        "This list is not taken into account with the spawning check.",
                        "Tags can be used by using #minecraft:<tag_name> or" + " #modid:<tag_name>",
                        "You can also use a wildcard after modid (modid:*)",
                        "For example, alexsmobs:* would allow all mobs from alex's mobs to" + " tick normally",
                        "[Default: [\"minecraft:wither\", \"minecraft:phantom\","
                                + " \"minecraft:blaze\", \"minecraft:ghast\","
                                + " \"minecraft:enderman\", \"minecraft:ender_dragon\","
                                + " \"minecraft:elder_guardian\", \"minecraft:warden\","
                                + " \"create:package\", \"railways:conductor\","
                                + " \"minecolonies:*\"]]")
                .define("entityIgnoreList", defaultIgnoreList);
        BUILDER.pop();

        BUILDER.comment("Totem of Ticking Settings").push(CATEGORY_TOTEM);
        shouldEnableTotemOfTicking = BUILDER.comment("Whether the totem of ticking should be enabled [Default: true]")
                .define("enableTotemOfTicking", true);
        totemMaxEntitySpawnHorizontalDist = BUILDER.comment(
                        "Maximum distance from a totem of ticking (horizontally) for living"
                                + " entity spawning check [Default: 24]")
                .define("totemMaxEntitySpawnDistanceHorizontal", 24);
        totemMaxEntitySpawnVerticalDist = BUILDER.comment(
                        "Maximum distance from a totem of ticking (vertically) for living"
                                + " entity spawning check [Default: 16]")
                .define("totemMaxEntitySpawnDistanceVertical", 16);
        totemMaxEntityTickHorizontalDist = BUILDER.comment(
                        "Maximum distance from a totem of ticking (horizontally) to allow"
                                + " living entity ticking [Default: 24]")
                .define("totemMaxEntityTickDistanceHorizontal", 24);
        totemMaxEntityTickVerticalDist = BUILDER.comment(
                        "Maximum distance from a totem of ticking (vertically) to allow"
                                + " living entity ticking [Default: 16]")
                .define("totemMaxEntityTickDistanceVertical", 16);
        BUILDER.pop();

        SERVER_CONFIG = BUILDER.build();
    }
}
