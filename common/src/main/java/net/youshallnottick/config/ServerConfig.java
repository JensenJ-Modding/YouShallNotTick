package net.youshallnottick.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.youshallnottick.Utils;

public class ServerConfig {
    public static final String CATEGORY_GENERAL = "general";
    public static final String CATEGORY_TICKING = "ticking";
    public static final String CATEGORY_TOTEM = "totem";
    public static ModConfigSpec SERVER_CONFIG;
    public static ModConfigSpec.ConfigValue<Integer> minPlayers;
    public static ModConfigSpec.ConfigValue<Boolean> spectatorsAllowTicking;

    public static ModConfigSpec.ConfigValue<Boolean> shouldRaidParticipantsTick;
    public static ModConfigSpec.ConfigValue<Integer> playerMaxEntityTickHorizontalDist;
    public static ModConfigSpec.ConfigValue<Integer> playerMaxEntityTickVerticalDist;
    public static ModConfigSpec.ConfigValue<List<String>> entityIgnoreList;
    public static final Set<ResourceLocation> entityResources = new HashSet<>();
    public static final Set<TagKey<EntityType<?>>> entityTagKeys = new HashSet<>();
    public static final Set<String> entityWildcards = new HashSet<>();

    public static ModConfigSpec.ConfigValue<Boolean> shouldEnableTotemOfTicking;
    public static ModConfigSpec.ConfigValue<Integer> tickingTotemMaxEntityTickHorizontalDist;
    public static ModConfigSpec.ConfigValue<Integer> tickingTotemMaxEntityTickVerticalDist;

    public static void updateMobLists() {
        Utils.isIgnored.clear();

        for (String key : entityIgnoreList.get()) {
            if (key.contains("#")) {
                entityTagKeys.add(TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.parse(key.replace("#", ""))));
            } else if (key.contains("*")) {
                entityWildcards.add(key.split(":")[0]);
            } else {
                entityResources.add(ResourceLocation.parse(key));
            }
        }
    }

    static {
        ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

        BUILDER.comment("General settings").push(CATEGORY_GENERAL);
        minPlayers = BUILDER.comment("Minimum number of players before mod is enabled. [Default: 1]")
                .define("minPlayers", 1);
        spectatorsAllowTicking = BUILDER.comment("Should spectators allow nearby entities to tick. [Default: false]")
                .define("spectatorsAllowTicking", false);
        BUILDER.pop();

        BUILDER.comment("Ticking settings").push(CATEGORY_TICKING);
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
        defaultIgnoreList.add("create_factory_logistics:composite_package");
        defaultIgnoreList.add("create_factory_logistics:jar");
        defaultIgnoreList.add("minecolonies:*");
        entityIgnoreList = BUILDER.comment(
                        "List of living entities to ignore when checking if they are allowed to tick",
                        "Only living entities need to be added to this list, all other entities are ignored by the mod",
                        "Living entities which have an owner, e.g. wolves are also ignored by the mod.",
                        "Tags can be used by using #minecraft:<tag_name> or #modid:<tag_name>",
                        "You can also use a wildcard after modid (modid:*)",
                        "For example, alexsmobs:* would allow all mobs from alex's mobs to tick normally",
                        "[Default: [\"minecraft:wither\", \"minecraft:phantom\","
                                + " \"minecraft:blaze\", \"minecraft:ghast\","
                                + " \"minecraft:enderman\", \"minecraft:ender_dragon\","
                                + " \"minecraft:elder_guardian\", \"minecraft:warden\","
                                + " \"create:package\", \"railways:conductor\","
                                + " \"create_factory_logistics:composite_package\", \"create_factory_logistics:jar\","
                                + " \"minecolonies:*\"]]")
                .define("entityIgnoreList", defaultIgnoreList);
        BUILDER.pop();

        BUILDER.comment("Totem Settings").push(CATEGORY_TOTEM);
        shouldEnableTotemOfTicking = BUILDER.comment("Whether the totem of ticking should be enabled [Default: true]")
                .define("enableTotemOfTicking", true);
        tickingTotemMaxEntityTickHorizontalDist = BUILDER.comment(
                        "Maximum distance from a totem of ticking (horizontally) to allow"
                                + " living entity ticking [Default: 8]")
                .define("tickingTotemMaxEntityTickDistanceHorizontal", 8);
        tickingTotemMaxEntityTickVerticalDist = BUILDER.comment(
                        "Maximum distance from a totem of ticking (vertically) to allow"
                                + " living entity ticking [Default: 4]")
                .define("tickingTotemMaxEntityTickDistanceVertical", 4);
        BUILDER.pop();

        SERVER_CONFIG = BUILDER.build();
    }
}
