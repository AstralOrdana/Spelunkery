package com.ordana.underhaul;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.PlacedFeature;

public class ModFeatures {
    public static void registerFeatures() {
        RegistryKey<PlacedFeature> large_coal_vein = RegistryKey.of(Registry.PLACED_FEATURE_KEY,
                new Identifier("underhaul", "large_coal_vein"));
        RegistryKey<PlacedFeature> large_emerald_vein = RegistryKey.of(Registry.PLACED_FEATURE_KEY,
                new Identifier("underhaul", "large_emerald_vein"));
        RegistryKey<PlacedFeature> large_lapis_vein = RegistryKey.of(Registry.PLACED_FEATURE_KEY,
                new Identifier("underhaul", "large_lapis_vein"));
        RegistryKey<PlacedFeature> large_redstone_vein = RegistryKey.of(Registry.PLACED_FEATURE_KEY,
                new Identifier("underhaul", "large_redstone_vein"));

        BiomeModifications.addFeature(BiomeSelectors.categories(Biome.Category.SWAMP), GenerationStep.Feature.UNDERGROUND_ORES, large_coal_vein);
        BiomeModifications.addFeature(BiomeSelectors.categories(Biome.Category.MOUNTAIN), GenerationStep.Feature.UNDERGROUND_ORES, large_emerald_vein);
        BiomeModifications.addFeature(BiomeSelectors.categories(Biome.Category.DESERT), GenerationStep.Feature.UNDERGROUND_ORES, large_lapis_vein);
        BiomeModifications.addFeature(BiomeSelectors.categories(Biome.Category.JUNGLE), GenerationStep.Feature.UNDERGROUND_ORES, large_redstone_vein);
    }
}