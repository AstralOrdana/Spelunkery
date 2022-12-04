package com.ordana.underground_overhaul.reg;

import com.ordana.underground_overhaul.UAPlatform;
import com.ordana.underground_overhaul.UndergroundOverhaul;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.function.Supplier;

public class ModFeatures {

    public static void init() {

        //coal vein
        ResourceKey<PlacedFeature> large_coal_vein = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, UndergroundOverhaul.res("large_coal_vein"));
        UAPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, BiomeTags.HAS_SWAMP_HUT, large_coal_vein);

        //emerald vein
        ResourceKey<PlacedFeature> large_emerald_vein = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, UndergroundOverhaul.res("large_emerald_vein"));
        UAPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, BiomeTags.IS_MOUNTAIN, large_emerald_vein);

        //lapis vein
        ResourceKey<PlacedFeature> large_lapis_vein = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, UndergroundOverhaul.res("large_lapis_vein"));
        UAPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, BiomeTags.HAS_DESERT_PYRAMID, large_lapis_vein);

        //redstone vein
        ResourceKey<PlacedFeature> large_redstone_vein = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, UndergroundOverhaul.res("large_redstone_vein"));
        UAPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, BiomeTags.IS_OVERWORLD, large_redstone_vein);

    }
}

