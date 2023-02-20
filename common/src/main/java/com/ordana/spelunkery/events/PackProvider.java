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
        this.dynamicPack.addNamespaces("spelunkery");
        this.dynamicPack.addNamespaces("minecraft");
        this.dynamicPack.addNamespaces("create");
        this.dynamicPack.addNamespaces("sullysmod");
        this.dynamicPack.addNamespaces("oreganized");
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

        var createCrushingMetalRecipes = List.of(
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

        for (var recipe : createCrushingMetalRecipes) {
            ResourceLocation target = new ResourceLocation("create", "crushing/" + recipe);
            ResourceLocation source = new ResourceLocation("spelunkery", "overrides/recipes/crushing/" + recipe + ".json");

            try (var bsStream = manager.getResource(source).orElseThrow().open()) {
                JsonElement bsElement = RPUtils.deserializeJson(bsStream);

                if (PlatformHelper.isModLoaded("create") && CommonConfigs.ENABLE_RAW_NUGGETS.get()) {
                    dynamicPack.addJson(target, bsElement, ResType.RECIPES);
                }

            } catch (Exception ignored) {
            }
        }

        var oreganizedCrushingMetalRecipes = List.of(
                "glance",
                "glance_recycling"
        );

        for (var recipe : oreganizedCrushingMetalRecipes) {
            ResourceLocation target = new ResourceLocation("oreganized", "create/crushing" + recipe);
            ResourceLocation source = new ResourceLocation("spelunkery", "overrides/recipes/crushing/oreganized" + recipe + ".json");

            try (var bsStream = manager.getResource(source).orElseThrow().open()) {
                JsonElement bsElement = RPUtils.deserializeJson(bsStream);

                if (PlatformHelper.isModLoaded("oreganized") && CommonConfigs.ENABLE_RAW_NUGGETS.get()) {
                    dynamicPack.addJson(target, bsElement, ResType.RECIPES);
                }

            } catch (Exception ignored) {
            }
        }


        var crushingSpelunkeryMetalRecipes = List.of(
                "lazurite"
        );

        for (var recipe : crushingSpelunkeryMetalRecipes) {
            ResourceLocation target = new ResourceLocation("spelunkery", "crushing/" + recipe);
            ResourceLocation source = new ResourceLocation("spelunkery", "overrides/recipes/crushing/" + recipe + ".json");

            try (var bsStream = manager.getResource(source).orElseThrow().open()) {
                JsonElement bsElement = RPUtils.deserializeJson(bsStream);

                if (PlatformHelper.isModLoaded("create") && CommonConfigs.ENABLE_RAW_NUGGETS.get()) {
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
            ResourceLocation source = new ResourceLocation("spelunkery", "overrides/recipes/splashing/" + recipe + ".json");

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
            ResourceLocation source = new ResourceLocation("spelunkery", "overrides/recipes/crushing/" + recipe + ".json");

            try (var bsStream = manager.getResource(source).orElseThrow().open()) {
                JsonElement bsElement = RPUtils.deserializeJson(bsStream);

                if (PlatformHelper.isModLoaded("create") && CommonConfigs.ENABLE_ROUGH_GEMS.get()) {
                    dynamicPack.addJson(target, bsElement, ResType.RECIPES);
                }

            } catch (Exception ignored) {
            }
        }


        var crushingSpelunkeryGemRecipes = List.of(
                "smooth_basalt_diamond_ore",
                "andesite_emerald_ore",
                "sandstone_lapis_ore",
                "calcite_redstone_ore"
        );

        for (var recipe : crushingSpelunkeryGemRecipes) {
            ResourceLocation target = new ResourceLocation("spelunkery", "crushing/" + recipe);
            ResourceLocation source = new ResourceLocation("spelunkery", "overrides/recipes/crushing/" + recipe + ".json");

            try (var bsStream = manager.getResource(source).orElseThrow().open()) {
                JsonElement bsElement = RPUtils.deserializeJson(bsStream);

                if (PlatformHelper.isModLoaded("create") && CommonConfigs.ENABLE_ROUGH_GEMS.get()) {
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
            ResourceLocation source = new ResourceLocation("spelunkery", "overrides/loot_tables/" + loot + ".json");

            try (var bsStream = manager.getResource(source).orElseThrow().open()) {
                JsonElement bsElement = RPUtils.deserializeJson(bsStream);

                if (CommonConfigs.ENABLE_ROUGH_GEMS.get()) {
                    dynamicPack.addJson(target, bsElement, ResType.BLOCK_LOOT_TABLES);
                }

            } catch (Exception ignored) {
            }
        }

        var moresGemLoot = List.of(
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

        for (var loot : moresGemLoot) {
            ResourceLocation target = new ResourceLocation("spelunkery", loot);
            ResourceLocation source = new ResourceLocation("spelunkery", "overrides/loot_tables/mores/" + loot + ".json");

            try (var bsStream = manager.getResource(source).orElseThrow().open()) {
                JsonElement bsElement = RPUtils.deserializeJson(bsStream);

                if (CommonConfigs.ENABLE_ROUGH_GEMS.get()) {
                    dynamicPack.addJson(target, bsElement, ResType.BLOCK_LOOT_TABLES);
                }

            } catch (Exception ignored) {
            }
        }

        var spelunkeryGemLoot = List.of(
                "smooth_basalt_diamond_ore",
                "andesite_emerald_ore",
                "sandstone_lapis_ore",
                "calcite_redstone_ore"
        );

        for (var loot : spelunkeryGemLoot) {
            ResourceLocation target = new ResourceLocation("spelunkery", loot);
            ResourceLocation source = new ResourceLocation("spelunkery", "overrides/loot_tables/" + loot + ".json");

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
                "deepslate_copper_ore",
                "nether_gold_ore",
                "deepslate_coal_ore"
        );

        for (var loot : vanillaMetalLoot) {
            ResourceLocation target = new ResourceLocation("minecraft", loot);
            ResourceLocation source = new ResourceLocation("spelunkery", "overrides/loot_tables/" + loot + ".json");

            try (var bsStream = manager.getResource(source).orElseThrow().open()) {
                JsonElement bsElement = RPUtils.deserializeJson(bsStream);

                if (CommonConfigs.ENABLE_RAW_NUGGETS.get()) {
                    dynamicPack.addJson(target, bsElement, ResType.BLOCK_LOOT_TABLES);
                }

            } catch (Exception ignored) {
            }
        }

        var moresMetalLoot = List.of(
                "tuff_coal_ore",
                "tuff_copper_ore",
                "tuff_gold_ore",
                "tuff_iron_ore"
        );

        for (var loot : moresMetalLoot) {
            ResourceLocation target = new ResourceLocation("spelunkery", loot);
            ResourceLocation source = new ResourceLocation("spelunkery", "overrides/loot_tables/mores/" + loot + ".json");

            try (var bsStream = manager.getResource(source).orElseThrow().open()) {
                JsonElement bsElement = RPUtils.deserializeJson(bsStream);

                if (CommonConfigs.ENABLE_RAW_NUGGETS.get()) {
                    dynamicPack.addJson(target, bsElement, ResType.BLOCK_LOOT_TABLES);
                }

            } catch (Exception ignored) {
            }
        }



        var sullysGemLoot = List.of(
                "deepslate_jade_ore"
        );

        for (var loot : sullysGemLoot) {
            ResourceLocation target = new ResourceLocation("sullysmod", loot);
            ResourceLocation source = new ResourceLocation("spelunkery", "overrides/loot_tables/sullysmod/" + loot + ".json");

            try (var bsStream = manager.getResource(source).orElseThrow().open()) {
                JsonElement bsElement = RPUtils.deserializeJson(bsStream);

                if (PlatformHelper.isModLoaded("sullysmod")) {
                    dynamicPack.addJson(target, bsElement, ResType.BLOCK_LOOT_TABLES);
                }

            } catch (Exception ignored) {
            }
        }

        var moresSullysGemLoot = List.of(
                "tuff_jade_ore"
        );

        for (var loot : moresSullysGemLoot) {
            ResourceLocation target = new ResourceLocation("spelunkery", loot);
            ResourceLocation source = new ResourceLocation("spelunkery", "overrides/loot_tables/mores/" + loot + ".json");

            try (var bsStream = manager.getResource(source).orElseThrow().open()) {
                JsonElement bsElement = RPUtils.deserializeJson(bsStream);

                if (PlatformHelper.isModLoaded("sullysmod") && CommonConfigs.ENABLE_ROUGH_GEMS.get()) {
                    dynamicPack.addJson(target, bsElement, ResType.BLOCK_LOOT_TABLES);
                }

            } catch (Exception ignored) {
            }
        }


        var oreganizedMetalLoot = List.of(
                "deepslate_lead_ore",
                "deepslate_silver_ore"
        );

        for (var loot : oreganizedMetalLoot) {
            ResourceLocation target = new ResourceLocation("oreganized", loot);
            ResourceLocation source = new ResourceLocation("spelunkery", "overrides/loot_tables/oreganized/" + loot + ".json");

            try (var bsStream = manager.getResource(source).orElseThrow().open()) {
                JsonElement bsElement = RPUtils.deserializeJson(bsStream);

                if (PlatformHelper.isModLoaded("oreganized") && CommonConfigs.ENABLE_RAW_NUGGETS.get()) {
                    dynamicPack.addJson(target, bsElement, ResType.BLOCK_LOOT_TABLES);
                }

            } catch (Exception ignored) {
            }
        }


        var moresOreganizedMetalLoot = List.of(
                "tuff_lead_ore",
                "tuff_silver_ore"
        );

        for (var loot : moresOreganizedMetalLoot) {
            ResourceLocation target = new ResourceLocation("spelunkery", loot);
            ResourceLocation source = new ResourceLocation("spelunkery", "overrides/loot_tables/mores/" + loot + ".json");

            try (var bsStream = manager.getResource(source).orElseThrow().open()) {
                JsonElement bsElement = RPUtils.deserializeJson(bsStream);

                if (PlatformHelper.isModLoaded("oreganized") && CommonConfigs.ENABLE_RAW_NUGGETS.get()) {
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
            ResourceLocation source = new ResourceLocation("spelunkery", "overrides/loot_tables/create/" + loot + ".json");

            try (var bsStream = manager.getResource(source).orElseThrow().open()) {
                JsonElement bsElement = RPUtils.deserializeJson(bsStream);

                if (PlatformHelper.isModLoaded("create") && CommonConfigs.ENABLE_RAW_NUGGETS.get()) {
                    dynamicPack.addJson(target, bsElement, ResType.BLOCK_LOOT_TABLES);
                }

            } catch (Exception ignored) {
            }
        }


        var moresCreateMetalLoot = List.of(
                "tuff_zinc_ore"
        );

        for (var loot : moresCreateMetalLoot) {
            ResourceLocation target = new ResourceLocation("spelunkery", loot);
            ResourceLocation source = new ResourceLocation("spelunkery", "overrides/loot_tables/mores/" + loot + ".json");

            try (var bsStream = manager.getResource(source).orElseThrow().open()) {
                JsonElement bsElement = RPUtils.deserializeJson(bsStream);

                if (PlatformHelper.isModLoaded("create") && CommonConfigs.ENABLE_RAW_NUGGETS.get()) {
                    dynamicPack.addJson(target, bsElement, ResType.BLOCK_LOOT_TABLES);
                }

            } catch (Exception ignored) {
            }
        }


        var sculkShearsDrops = List.of(
                "sculk",
                "sculk_vein"
        );

        for (var loot : sculkShearsDrops) {
            ResourceLocation target = new ResourceLocation("minecraft", loot);
            ResourceLocation source = new ResourceLocation("spelunkery", "overrides/loot_tables/" + loot + ".json");

            try (var bsStream = manager.getResource(source).orElseThrow().open()) {
                JsonElement bsElement = RPUtils.deserializeJson(bsStream);

                if (CommonConfigs.SCULK_SHEARING.get()) {
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
            ResourceLocation source = new ResourceLocation("spelunkery", "overrides/loot_tables/" + loot + ".json");

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
            ResourceLocation source = new ResourceLocation("spelunkery", "overrides/worldgen/configured_feature/" + gen + ".json");

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
            ResourceLocation source = new ResourceLocation("spelunkery", "overrides/worldgen/configured_feature/" + gen + ".json");

            try (var bsStream = manager.getResource(source).orElseThrow().open()) {
                JsonElement bsElement = RPUtils.deserializeJson(bsStream);

                if (CommonConfigs.DARK_FOREST_PORTABELLAS.get()) {
                    dynamicPack.addJson(target, bsElement, ResType.GENERIC);
                }

            } catch (Exception ignored) {
            }
        }

        var moresWorldgen = List.of(
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

        for (var gen : moresWorldgen) {
            ResourceLocation target = new ResourceLocation("minecraft", "worldgen/configured_feature/" + gen + ".json");
            ResourceLocation source = new ResourceLocation("spelunkery", "overrides/worldgen/configured_feature/" + gen + ".json");

            try (var bsStream = manager.getResource(source).orElseThrow().open()) {
                JsonElement bsElement = RPUtils.deserializeJson(bsStream);

                if (CommonConfigs.ENABLE_MORES.get()) {
                    dynamicPack.addJson(target, bsElement, ResType.GENERIC);
                }

            } catch (Exception ignored) {
            }
        }

        var moresCreateWorldgen = List.of(
                "zinc_ore"
        );

        for (var gen : moresCreateWorldgen) {
            ResourceLocation target = new ResourceLocation("create", "worldgen/configured_feature/" + gen + ".json");
            ResourceLocation source = new ResourceLocation("spelunkery", "overrides/worldgen/configured_feature/create/" + gen + ".json");

            try (var bsStream = manager.getResource(source).orElseThrow().open()) {
                JsonElement bsElement = RPUtils.deserializeJson(bsStream);

                if (PlatformHelper.isModLoaded("create") && CommonConfigs.ENABLE_MORES.get()) {
                    dynamicPack.addJson(target, bsElement, ResType.GENERIC);
                }

            } catch (Exception ignored) {
            }
        }

        var moresOreganizedWorldgen = List.of(
                "lead_ore",
                "lead_ore_extra",
                "silver_ore",
                "silver_ore_low"
        );

        for (var gen : moresOreganizedWorldgen) {
            ResourceLocation target = new ResourceLocation("oreganized", "worldgen/configured_feature/" + gen + ".json");
            ResourceLocation source = new ResourceLocation("spelunkery", "overrides/worldgen/configured_feature/oreganized/" + gen + ".json");

            try (var bsStream = manager.getResource(source).orElseThrow().open()) {
                JsonElement bsElement = RPUtils.deserializeJson(bsStream);

                if (PlatformHelper.isModLoaded("oreganized") && CommonConfigs.ENABLE_MORES.get()) {
                    dynamicPack.addJson(target, bsElement, ResType.GENERIC);
                }

            } catch (Exception ignored) {
            }
        }

        var moresSullysmodWorldgen = List.of(
                "jade_ore"
        );

        for (var gen : moresSullysmodWorldgen) {
            ResourceLocation target = new ResourceLocation("sullysmod", "worldgen/configured_feature/" + gen + ".json");
            ResourceLocation source = new ResourceLocation("spelunkery", "overrides/worldgen/configured_feature/sullysmod/" + gen + ".json");

            try (var bsStream = manager.getResource(source).orElseThrow().open()) {
                JsonElement bsElement = RPUtils.deserializeJson(bsStream);

                if (PlatformHelper.isModLoaded("sullysmod") && CommonConfigs.ENABLE_MORES.get()) {
                    dynamicPack.addJson(target, bsElement, ResType.GENERIC);
                }

            } catch (Exception ignored) {
                throw new RuntimeException(ignored);
            }
        }
    }

    @Override
    public void generateStaticAssetsOnStartup(ResourceManager manager) {
    }


}