package com.ordana.spelunkery.events;

import com.google.gson.JsonElement;
import com.ordana.spelunkery.Spelunkery;
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

        var crushingRecipes = List.of(
                "asurine",
                "asurine_recycling",
                "crimsite",
                "crimsite_recycling",
                "deepslate_diamond_ore",
                "deepslate_emerald_ore",
                "deepslate_lapis_ore",
                "deepslate_redstone_ore",
                "diamond_ore",
                "emerald_ore",
                "lapis_ore",
                "nether_gold_ore",
                "ochrum",
                "ochrum_recycling",
                "redstone_ore",
                "tuff",
                "tuff_recycling",
                "veridium",
                "veridium_recycling"
        );

        var splashingRecipes = List.of(
                "crushed_copper_ore",
                "crushed_gold_ore",
                "crushed_iron_ore",
                "crushed_zinc_ore",
                "gravel",
                "red_sand",
                "soul_sand"
        );

        for (var recipe : crushingRecipes) {
            ResourceLocation target = new ResourceLocation("create", "crushing/" + recipe);
            ResourceLocation source = new ResourceLocation("spelunkery", "overrides/crushing/" + recipe + ".json");

            try (var bsStream = manager.getResource(source).orElseThrow().open()) {
                JsonElement bsElement = RPUtils.deserializeJson(bsStream);

                if (PlatformHelper.isModLoaded("create")) {
                    dynamicPack.addJson(target, bsElement, ResType.RECIPES);
                }

            } catch (Exception ignored) {
            }
        }

        for (var recipe : splashingRecipes) {
            ResourceLocation target = new ResourceLocation("create", "splashing/" + recipe);
            ResourceLocation source = new ResourceLocation("spelunkery", "overrides/splashing/" + recipe + ".json");

            try (var bsStream = manager.getResource(source).orElseThrow().open()) {
                JsonElement bsElement = RPUtils.deserializeJson(bsStream);

                if (PlatformHelper.isModLoaded("create")) {
                    dynamicPack.addJson(target, bsElement, ResType.RECIPES);
                }

            } catch (Exception ignored) {
            }
        }
    }

    @Override
    public void generateStaticAssetsOnStartup(ResourceManager manager) {
    }


}