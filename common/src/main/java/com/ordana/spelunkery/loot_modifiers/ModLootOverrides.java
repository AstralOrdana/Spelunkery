package com.ordana.spelunkery.loot_modifiers;

import com.google.gson.JsonElement;
import com.ordana.spelunkery.Spelunkery;
import com.ordana.spelunkery.configs.CommonConfigs;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.resources.RPUtils;
import net.mehvahdjukaar.moonlight.api.resources.ResType;
import net.mehvahdjukaar.moonlight.api.resources.pack.DynServerResourcesGenerator;
import net.mehvahdjukaar.moonlight.api.resources.pack.DynamicDataPack;
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceGenTask;
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceSink;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.resources.ResourceManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.function.Consumer;

public class ModLootOverrides extends DynServerResourcesGenerator {

    public static final ModLootOverrides INSTANCE = new ModLootOverrides();

    public ModLootOverrides() {
        super(new DynamicDataPack(Spelunkery.res("generated_pack"), Pack.Position.TOP, true, true));
        this.dynamicPack.setGenerateDebugResources(false);
        this.dynamicPack.addNamespaces("spelunkery");
        this.dynamicPack.addNamespaces("minecraft");
        this.dynamicPack.addNamespaces("create");
        this.dynamicPack.addNamespaces("sullysmod");
        this.dynamicPack.addNamespaces("oreganized");
        this.dynamicPack.addNamespaces("ditr");
        this.dynamicPack.addNamespaces("create_dd");
        this.dynamicPack.addNamespaces("etcetera");
    }


    @Override
    public Logger getLogger() {
        return Spelunkery.LOGGER;
    }

    public void overrideDataFile(ResourceSink sink, ResourceManager manager, List<String> list, String targetNamespace, String targetPath, String sourcePath, ResType resType) {
        for (var recipe : list) {
            ResourceLocation target = new ResourceLocation(targetNamespace, targetPath + recipe);
            ResourceLocation source = new ResourceLocation("spelunkery", sourcePath + recipe + ".json");

            try (var bsStream = manager.getResource(source).orElseThrow().open()) {
                JsonElement bsElement = RPUtils.deserializeJson(bsStream);
                sink.addJson(target, bsElement, resType);

            } catch (Exception ignored) {
            }
        }
    }

    public void overrideDataFileGeneric(ResourceSink sink, ResourceManager manager, List<String> list, String targetNamespace, String targetPath, String sourcePath, ResType resType) {
        for (var recipe : list) {
            ResourceLocation target = new ResourceLocation(targetNamespace, targetPath + recipe + ".json");
            ResourceLocation source = new ResourceLocation("spelunkery", sourcePath + recipe + ".json");

            try (var bsStream = manager.getResource(source).orElseThrow().open()) {
                JsonElement bsElement = RPUtils.deserializeJson(bsStream);
                sink.addJson(target, bsElement, resType);

            } catch (Exception ignored) {
            }
        }
    }

    @Override
    public void regenerateDynamicAssets(Consumer<ResourceGenTask> executor) {
        super.regenerateDynamicAssets(executor);
        executor.accept((manager, sink) -> {


            //recipes
            var spelunkeryRecipeCrushingGems = List.of(
                    "smooth_basalt_diamond_ore",
                    "andesite_emerald_ore",
                    "sandstone_lapis_ore",
                    "calcite_redstone_ore",
                    "andesite_diamond_ore",
                    "andesite_lapis_ore",
                    "andesite_redstone_ore",
                    "granite_diamond_ore",
                    "granite_emerald_ore",
                    "granite_lapis_ore",
                    "granite_redstone_ore",
                    "diorite_diamond_ore",
                    "diorite_emerald_ore",
                    "diorite_lapis_ore",
                    "diorite_redstone_ore",
                    "tuff_diamond_ore",
                    "tuff_emerald_ore",
                    "tuff_lapis_ore",
                    "tuff_redstone_ore"
            );

            var createRecipeCrushingMetal = List.of(
                    "asurine",
                    "asurine_recycling",
                    "crimsite",
                    "crimsite_recycling",
                    "ochrum",
                    "ochrum_recycling",
                    "tuff",
                    "tuff_recycling",
                    "veridium",
                    "veridium_recycling",
                    "nether_gold_ore"
            );

            var createRecipeCrushingGems = List.of(
                    "deepslate_diamond_ore",
                    "deepslate_emerald_ore",
                    "deepslate_lapis_ore",
                    "deepslate_redstone_ore",
                    "diamond_ore",
                    "emerald_ore",
                    "lapis_ore",
                    "redstone_ore"
            );

            var createRecipeSplashingMetal = List.of(
                    "gravel",
                    "red_sand",
                    "soul_sand"
            );

            var oreganizedRecipeCrushingMetal = List.of(
                    "glance",
                    "glance_recycling"
            );

            var createRecipeCrushingLapis = List.of(
                    "lazurite"
            );


            //loot tables
            var vanillaLootGems = List.of(
                    "deepslate_diamond_ore",
                    "deepslate_emerald_ore",
                    "deepslate_lapis_ore",
                    "deepslate_redstone_ore",
                    "diamond_ore",
                    "emerald_ore",
                    "lapis_ore",
                    "redstone_ore"
            );

            var vanillaLootMetal = List.of(
                    "deepslate_iron_ore",
                    "deepslate_copper_ore",
                    "deepslate_gold_ore",
                    "nether_gold_ore",
                    "deepslate_coal_ore"
            );

            var spelunkeryLootGems = List.of(
                    "smooth_basalt_diamond_ore",
                    "sandstone_lapis_ore",
                    "calcite_redstone_ore",

                    "andesite_diamond_ore",
                    "andesite_emerald_ore",
                    "andesite_lapis_ore",
                    "andesite_redstone_ore",
                    "granite_diamond_ore",
                    "granite_emerald_ore",
                    "granite_lapis_ore",
                    "granite_redstone_ore",
                    "diorite_diamond_ore",
                    "diorite_emerald_ore",
                    "diorite_lapis_ore",
                    "diorite_redstone_ore",
                    "tuff_diamond_ore",
                    "tuff_emerald_ore",
                    "tuff_lapis_ore",
                    "tuff_redstone_ore"
            );

            var spelunkeryLootMetal = List.of(
                    "tuff_coal_ore",
                    "tuff_copper_ore",
                    "tuff_gold_ore",
                    "tuff_iron_ore"
            );

            var sullysLootGems = List.of(
                    "deepslate_jade_ore"
            );

            var sullysLootSpelunkeryGems = List.of(
                    "andesite_jade_ore",
                    "granite_jade_ore",
                    "diorite_jade_ore",
                    "tuff_jade_ore"
            );

            var ditrLootGems = List.of(
                    "obsidian_diamond_ore"
            );


            var oreganizedLootMetal = List.of(
                    "deepslate_lead_ore",
                    "deepslate_silver_ore"
            );

            var oreganizedLootSpelunkeryMetal = List.of(
                    "andesite_lead_ore",
                    "granite_lead_ore",
                    "diorite_lead_ore",
                    "tuff_lead_ore",
                    "andesite_silver_ore",
                    "granite_silver_ore",
                    "diorite_silver_ore",
                    "tuff_silver_ore"
            );

            var oreganizedLootSpelunkeryMetalNuggets = List.of(
                    "andesite_lead_ore",
                    "granite_lead_ore",
                    "diorite_lead_ore",
                    "tuff_lead_ore",
                    "andesite_silver_ore",
                    "granite_silver_ore",
                    "diorite_silver_ore",
                    "tuff_silver_ore"
            );

            var createLootMetal = List.of(
                    "deepslate_zinc_ore"
            );

            var createLootSpelunkeryMetal = List.of(
                    "andesite_zinc_ore",
                    "granite_zinc_ore",
                    "diorite_zinc_ore",
                    "tuff_zinc_ore"
            );

            var createLootSpelunkeryMetalNuggets = List.of(
                    "andesite_zinc_ore",
                    "granite_zinc_ore",
                    "diorite_zinc_ore",
                    "tuff_zinc_ore"
            );

            var etceteraLootMetalNuggets = List.of(
                    "bismuth_ore"
            );

            //misc
            var vanillaLootSculk = List.of(
                    "sculk",
                    "sculk_vein"
            );

            var vanillaLootPiglins = List.of(
                    "piglin_bartering"
            );


            var vanillaWorldgenStones = List.of(
                    "ore_andesite_upper",
                    "ore_andesite_lower",
                    "ore_granite_upper",
                    "ore_granite_lower",
                    "ore_diorite_upper",
                    "ore_diorite_lower",
                    "ore_tuff",
                    "ore_dirt",
                    "ore_gravel",
                    "ore_clay"
            );

            var vanillaWorldgenMoss = List.of(
                    "moss_patch",
                    "moss_patch_bonemeal",
                    "moss_vegetation"
            );

            var vanillaWorldgenPortabellas = List.of(
                    "dark_forest_vegetation"
            );

            //worldgen
            var vanillaWorldgenMores = List.of(
                    "ore_coal",
                    "ore_coal_buried",
                    "ore_copper_large",
                    "ore_copper_small",
                    "ore_diamond_buried",
                    "ore_diamond_large",
                    "ore_diamond_small",
                    "ore_emerald",
                    "ore_gold",
                    "ore_gold_buried",
                    "ore_iron",
                    "ore_iron_small",
                    "ore_lapis",
                    "ore_lapis_buried",
                    "ore_redstone"
            );

            var createWorldgenMores = List.of(
                    "zinc_ore"
            );

            var oreganizedWorldgenMores = List.of(
                    "lead_ore",
                    "lead_ore_extra",
                    "silver_ore",
                    "silver_ore_low"
            );

            var sullysWorldgenMores = List.of(
                    "jade_ore"
            );


            //gems
            if (PlatHelper.isModLoaded("sullysmod")) {

                //enable spelunkery m'ores loot tables when sullys mod is loaded
                overrideDataFile(sink,manager, sullysLootSpelunkeryGems,
                        "spelunkery", "",
                        "overrides/loot_tables/", ResType.BLOCK_LOOT_TABLES);
            }

            if (CommonConfigs.ENABLE_ROUGH_GEMS.get()) {

                //override vanilla loot tables to make gem ores drop rough gems
                overrideDataFile(sink,manager, vanillaLootGems,
                        "minecraft", "",
                        "overrides/loot_tables/", ResType.BLOCK_LOOT_TABLES);

                //override spelunkery loot tables to make m'ores gem ores drop rough gems
                overrideDataFile(sink,manager, spelunkeryLootGems,
                        "spelunkery", "",
                        "overrides/loot_tables/", ResType.BLOCK_LOOT_TABLES);

                if (PlatHelper.isModLoaded("sullysmod")) {

                    //override sullysmod loot tables to make gem ores drop rough gem shards
                    overrideDataFile(sink,manager, sullysLootGems,
                            "sullysmod", "",
                            "overrides/loot_tables/sullysmod/", ResType.BLOCK_LOOT_TABLES);
                }

                if (PlatHelper.isModLoaded("create")) {

                    //override create mod ore crushing recipes to create rough gems
                    overrideDataFile(sink,manager, createRecipeCrushingGems,
                            "create", "crushing/",
                            "overrides/recipes/crushing/", ResType.RECIPES);

                    //override spelunkery vanilla M'ores ore crushing recipes to create rough gems
                    overrideDataFile(sink,manager, spelunkeryRecipeCrushingGems,
                            "spelunkery", "crushing/",
                            "overrides/recipes/crushing/", ResType.RECIPES);
                }

                if (PlatHelper.isModLoaded("ditr")) {

                    //override ditr loot tables to mak gem ores drop rough gems
                    overrideDataFile(sink,manager, ditrLootGems,
                            "ditr", "",
                            "overrides/loot_tables/ditr/", ResType.BLOCK_LOOT_TABLES);
                }
            }


            //metals
            if (PlatHelper.isModLoaded("oreganized") && !CommonConfigs.ENABLE_RAW_NUGGETS.get()) {

                //enable oreganized m'ores loot tables when oreganized mod is loaded
                overrideDataFile(sink,manager, oreganizedLootSpelunkeryMetal,
                        "spelunkery", "",
                        "overrides/loot_tables/", ResType.BLOCK_LOOT_TABLES);
            }

            if (PlatHelper.isModLoaded("create") && !CommonConfigs.ENABLE_RAW_NUGGETS.get()) {

                //enable create m'ores loot tables when create mod is loaded
                overrideDataFile(sink,manager, createLootSpelunkeryMetal,
                        "spelunkery", "",
                        "overrides/loot_tables/", ResType.BLOCK_LOOT_TABLES);
            }

            if (PlatHelper.isModLoaded("etcetera") && !CommonConfigs.ENABLE_RAW_NUGGETS.get()) {

                overrideDataFile(sink,manager, etceteraLootMetalNuggets,
                        "etcetera", "blocks/",
                        "overrides/loot_tables/etcetera/", ResType.BLOCK_LOOT_TABLES);
            }

            if (CommonConfigs.ENABLE_RAW_NUGGETS.get()) {

                //override vanilla loot tables to make deepslate metal ores drop raw nuggets
                overrideDataFile(sink,manager, vanillaLootMetal,
                        "minecraft", "",
                        "overrides/loot_tables/", ResType.BLOCK_LOOT_TABLES);

                //override spelunkery loot tables to make m'ores metal ores drop raw nuggets
                overrideDataFile(sink,manager, spelunkeryLootMetal,
                        "spelunkery", "",
                        "overrides/loot_tables/", ResType.BLOCK_LOOT_TABLES);

                if (PlatHelper.isModLoaded("oreganized")) {

                    //override spelunkery loot tables to make oreganized mod compat m'ores metal ores drop raw nuggets
                    overrideDataFile(sink,manager, oreganizedLootSpelunkeryMetalNuggets,
                            "spelunkery", "",
                            "overrides/loot_tables/oreganized/", ResType.BLOCK_LOOT_TABLES);

                    //override oreganized loot tables to make deepslate metal ores drop raw nuggets
                    overrideDataFile(sink,manager, oreganizedLootMetal,
                            "oreganized", "",
                            "overrides/loot_tables/oreganized/", ResType.BLOCK_LOOT_TABLES);
                }

                if (PlatHelper.isModLoaded("create")) {

                    //override create loot tables to make deepslate metal ores drop raw nuggets
                    overrideDataFile(sink,manager, createLootMetal,
                            "create", "",
                            "overrides/loot_tables/create/", ResType.BLOCK_LOOT_TABLES);

                    //override spelunkery loot tables to make create mod compat m'ores metal ores drop raw nuggets
                    overrideDataFile(sink,manager, createLootSpelunkeryMetalNuggets,
                            "spelunkery", "",
                            "overrides/loot_tables/create/", ResType.BLOCK_LOOT_TABLES);

                    //override create mod stone types to create raw nuggets when crushed
                    overrideDataFile(sink,manager, createRecipeCrushingMetal,
                            "create", "crushing/",
                            "overrides/recipes/crushing/", ResType.RECIPES);

                    //override create mod sand and gravel splashing to create raw nuggets
                    overrideDataFile(sink,manager, createRecipeSplashingMetal,
                            "create", "splashing/",
                            "overrides/recipes/splashing/", ResType.RECIPES);

                    //override lazurite crushing to create raw gold nuggets
                    overrideDataFile(sink,manager, createRecipeCrushingLapis,
                            "spelunkery", "crushing/",
                            "overrides/recipes/crushing/", ResType.RECIPES);

                    if (PlatHelper.isModLoaded("oreganized")) {

                        //override glance crushing to create raw lead nuggets
                        overrideDataFile(sink,manager, oreganizedRecipeCrushingMetal,
                                "oreganized", "create/crushing/",
                                "overrides/recipes/crushing/oreganized/", ResType.RECIPES);
                    }
                }
            }


            //misc
            if (CommonConfigs.SCULK_SHEARING.get()) {

                //make sculk blocks drop sculk veins when mined with shears
                overrideDataFile(sink,manager, vanillaLootSculk,
                        "minecraft", "",
                        "overrides/loot_tables/", ResType.BLOCK_LOOT_TABLES);
            }

            if (CommonConfigs.PIGLINS_GIVE_CRYING_OBSIDIAN.get()) {

                //make piglins not give you crying obsidian as barter loot
                overrideDataFile(sink,manager, vanillaLootPiglins,
                        "minecraft", "gameplay/",
                        "overrides/loot_tables/", ResType.LOOT_TABLES);
            }


            //worldgen
            if (CommonConfigs.STONE_STRIPE_FEATURES.get()) {

                //disable vanilla stone patches
                overrideDataFileGeneric(sink,manager, vanillaWorldgenStones,
                        "minecraft", "worldgen/placed_feature/",
                        "overrides/worldgen/placed_feature/", ResType.GENERIC);
            }

            if (CommonConfigs.ENABLE_SPOROPHYTES.get()) {

                //make sporophytes generate instead of tall grass in lush caves
                overrideDataFileGeneric(sink,manager, vanillaWorldgenMoss,
                        "minecraft", "worldgen/configured_feature/",
                        "overrides/worldgen/configured_feature/", ResType.GENERIC);
            }

            if (CommonConfigs.DARK_FOREST_PORTABELLAS.get()) {

                //make large portabellas generate in dark forests
                overrideDataFileGeneric(sink,manager, vanillaWorldgenPortabellas,
                        "minecraft", "worldgen/configured_feature/",
                        "overrides/worldgen/configured_feature/", ResType.GENERIC);
            }

            if (CommonConfigs.ENABLE_MORES.get()) {

                //enable M'ores ores to generate
                overrideDataFileGeneric(sink,manager, vanillaWorldgenMores,
                        "minecraft", "worldgen/configured_feature/",
                        "overrides/worldgen/configured_feature/", ResType.GENERIC);

                if (PlatHelper.isModLoaded("create")) {
                    //enable create compat M'ores ores to generate
                    overrideDataFileGeneric(sink,manager, createWorldgenMores,
                            "create", "worldgen/configured_feature/",
                            "overrides/worldgen/configured_feature/create/", ResType.GENERIC);
                }

                if (PlatHelper.isModLoaded("sullysmod")) {
                    //enable sullysmod compat M'ores ores to generate
                    overrideDataFileGeneric(sink,manager, sullysWorldgenMores,
                            "sullysmod", "worldgen/configured_feature/",
                            "overrides/worldgen/configured_feature/sullysmod/", ResType.GENERIC);
                }

                if (PlatHelper.isModLoaded("oreganized")) {
                    //enable oreganized compat M'ores ores to generate
                    overrideDataFileGeneric(sink,manager, oreganizedWorldgenMores,
                            "oreganized", "worldgen/configured_feature/",
                            "overrides/worldgen/configured_feature/oreganized/", ResType.GENERIC);
                }
            }
        });
    }

}