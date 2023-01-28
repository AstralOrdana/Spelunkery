package com.ordana.spelunkery.events;

import com.google.gson.JsonElement;
import com.ordana.spelunkery.Spelunkery;
import com.ordana.spelunkery.configs.CommonConfigs;
import net.mehvahdjukaar.moonlight.api.platform.PlatformHelper;
import net.mehvahdjukaar.moonlight.api.resources.RPUtils;
import net.mehvahdjukaar.moonlight.api.resources.ResType;
import net.mehvahdjukaar.moonlight.api.resources.pack.DynServerResourcesProvider;
import net.mehvahdjukaar.moonlight.api.resources.pack.DynamicDataPack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.resources.ResourceManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class PackProvider extends DynServerResourcesProvider {

    public static final PackProvider INSTANCE = new PackProvider();

    public PackProvider() {
        super(new DynamicDataPack(Spelunkery.res("generated_pack"), Pack.Position.TOP, true, true));
        this.dynamicPack.generateDebugResources = true;
        this.dynamicPack.addNamespaces("create");
        this.dynamicPack.addNamespaces("minecraft");
    }

    @Override
    public Logger getLogger() {
        return Spelunkery.LOGGER;
    }

    @Override
    public boolean dependsOnLoadedPacks() {
        return true;
    }

    @Override
    public void regenerateDynamicAssets(ResourceManager manager) {

        var crushingMetalRecipes = List.of(
                "asurine",
                "asurine_recycling",
                "crimsite",
                "crimsite_recycling",
                "ochrum",
                "ochrum_recycling",
                "tuff",
                "tuff_recycling",
                "veridium",
                "veridium_recycling"
        );

        for (var recipe : crushingMetalRecipes) {
            ResourceLocation target = new ResourceLocation("create", "crushing/" + recipe);
            ResourceLocation source = new ResourceLocation("spelunkery", "recipes/overrides/crushing/" + recipe + ".json");

            try (var bsStream = manager.getResource(source).orElseThrow().open()) {
                JsonElement bsElement = RPUtils.deserializeJson(bsStream);

                if (PlatformHelper.isModLoaded("create") && CommonConfigs.ENABLE_RAW_NUGGETS.get()) {
                    dynamicPack.addJson(target, bsElement, ResType.RECIPES);
                }

            } catch (Exception ignored) {
            }
        }


        var crushingGemRecipes = List.of(
                "deepslate_diamond_ore",
                "deepslate_emerald_ore",
                "deepslate_lapis_ore",
                "deepslate_redstone_ore",
                "diamond_ore",
                "emerald_ore",
                "lapis_ore",
                "redstone_ore"
        );

        for (var recipe : crushingGemRecipes) {
            ResourceLocation target = new ResourceLocation("create", "crushing/" + recipe);
            ResourceLocation source = new ResourceLocation("spelunkery", "recipes/overrides/crushing/" + recipe + ".json");

            try (var bsStream = manager.getResource(source).orElseThrow().open()) {
                JsonElement bsElement = RPUtils.deserializeJson(bsStream);

                if (PlatformHelper.isModLoaded("create") && CommonConfigs.ENABLE_ROUGH_GEMS.get()) {
                    dynamicPack.addJson(target, bsElement, ResType.RECIPES);
                }

            } catch (Exception ignored) {
            }
        }


        var splashingRecipes = List.of(
                "gravel",
                "red_sand",
                "soul_sand"
        );

        for (var recipe : splashingRecipes) {
            ResourceLocation target = new ResourceLocation("create", "splashing/" + recipe);
            ResourceLocation source = new ResourceLocation("spelunkery", "recipes/overrides/splashing/" + recipe + ".json");

            try (var bsStream = manager.getResource(source).orElseThrow().open()) {
                JsonElement bsElement = RPUtils.deserializeJson(bsStream);

                if (PlatformHelper.isModLoaded("create") && CommonConfigs.ENABLE_RAW_NUGGETS.get()) {
                    dynamicPack.addJson(target, bsElement, ResType.RECIPES);
                }

            } catch (Exception ignored) {
            }
        }


        var vanillaGemLoot = List.of(
                "deepslate_diamond_ore",
                "deepslate_emerald_ore",
                "deepslate_lapis_ore",
                "deepslate_redstone_ore",
                "diamond_ore",
                "emerald_ore",
                "lapis_ore",
                "redstone_ore"
        );

        for (var loot : vanillaGemLoot) {
            ResourceLocation target = new ResourceLocation("minecraft", loot);
            ResourceLocation source = new ResourceLocation("spelunkery", "loot_tables/overrides/" + loot + ".json");

            try (var bsStream = manager.getResource(source).orElseThrow().open()) {
                JsonElement bsElement = RPUtils.deserializeJson(bsStream);

                if (CommonConfigs.ENABLE_ROUGH_GEMS.get()) {
                    dynamicPack.addJson(target, bsElement, ResType.BLOCK_LOOT_TABLES);
                }

            } catch (Exception ignored) {
            }
        }


        var vanillaMetalLoot = List.of(
                "deepslate_gold_ore",
                "deepslate_iron_ore",
                "nether_gold_ore"
        );

        for (var loot : vanillaMetalLoot) {
            ResourceLocation target = new ResourceLocation("minecraft", loot);
            ResourceLocation source = new ResourceLocation("spelunkery", "loot_tables/overrides/" + loot + ".json");

            try (var bsStream = manager.getResource(source).orElseThrow().open()) {
                JsonElement bsElement = RPUtils.deserializeJson(bsStream);

                if (CommonConfigs.ENABLE_RAW_NUGGETS.get()) {
                    dynamicPack.addJson(target, bsElement, ResType.BLOCK_LOOT_TABLES);
                }

            } catch (Exception ignored) {
            }
        }

        var vanillaCreateMetalLoot = List.of(
                "deepslate_copper_ore"
        );

        for (var loot : vanillaCreateMetalLoot) {
            ResourceLocation target = new ResourceLocation("minecraft", loot);
            ResourceLocation source = new ResourceLocation("spelunkery", "loot_tables/overrides/create/" + loot + ".json");

            try (var bsStream = manager.getResource(source).orElseThrow().open()) {
                JsonElement bsElement = RPUtils.deserializeJson(bsStream);

                if (PlatformHelper.isModLoaded("create") && CommonConfigs.ENABLE_RAW_NUGGETS.get()) {
                    dynamicPack.addJson(target, bsElement, ResType.BLOCK_LOOT_TABLES);
                }

            } catch (Exception ignored) {
            }
        }

        var createMetalLoot = List.of(
                "deepslate_zinc_ore"
        );

        for (var loot : createMetalLoot) {
            ResourceLocation target = new ResourceLocation("create", loot);
            ResourceLocation source = new ResourceLocation("spelunkery", "loot_tables/overrides/create/" + loot + ".json");

            try (var bsStream = manager.getResource(source).orElseThrow().open()) {
                JsonElement bsElement = RPUtils.deserializeJson(bsStream);

                if (PlatformHelper.isModLoaded("create") && CommonConfigs.ENABLE_RAW_NUGGETS.get()) {
                    dynamicPack.addJson(target, bsElement, ResType.BLOCK_LOOT_TABLES);
                }

            } catch (Exception ignored) {
            }
        }


        var piglins = List.of(
                "piglin_bartering"
        );

        for (var loot : piglins) {
            ResourceLocation target = new ResourceLocation("minecraft", "gameplay/" + loot);
            ResourceLocation source = new ResourceLocation("spelunkery", "loot_tables/overrides/" + loot + ".json");

            try (var bsStream = manager.getResource(source).orElseThrow().open()) {
                JsonElement bsElement = RPUtils.deserializeJson(bsStream);

                if (!CommonConfigs.PIGLINS_GIVE_CRYING_OBSIDIAN.get()) {
                    dynamicPack.addJson(target, bsElement, ResType.LOOT_TABLES);
                }

            } catch (Exception ignored) {
            }
        }

        //worldgen overrides
        var vanillaMossWorldgen = List.of(
                "moss_patch",
                "moss_patch_bonemeal",
                "moss_vegetation"
        );

        for (var gen : vanillaMossWorldgen) {
            ResourceLocation target = new ResourceLocation("minecraft", "worldgen/configured_feature/" + gen + ".json");
            ResourceLocation source = new ResourceLocation("spelunkery", "worldgen/overrides/configured_feature/" + gen + ".json");

            try (var bsStream = manager.getResource(source).orElseThrow().open()) {
                JsonElement bsElement = RPUtils.deserializeJson(bsStream);

                if (CommonConfigs.ENABLE_SPOROPHYTES.get()) {
                    dynamicPack.addJson(target, bsElement, ResType.GENERIC);
                }

            } catch (Exception ignored) {
            }
        }

        var hugePortabellas = List.of(
                "dark_forest_vegetation"
        );

        for (var gen : hugePortabellas) {
            ResourceLocation target = new ResourceLocation("minecraft", "worldgen/configured_feature/" + gen + ".json");
            ResourceLocation source = new ResourceLocation("spelunkery", "worldgen/overrides/configured_feature/" + gen + ".json");

            try (var bsStream = manager.getResource(source).orElseThrow().open()) {
                JsonElement bsElement = RPUtils.deserializeJson(bsStream);

                if (CommonConfigs.DARK_FOREST_PORTABELLAS.get()) {
                    dynamicPack.addJson(target, bsElement, ResType.GENERIC);
                }

            } catch (Exception ignored) {
            }
        }
    }

    @Override
    public void generateStaticAssetsOnStartup(ResourceManager manager) {
    }


}