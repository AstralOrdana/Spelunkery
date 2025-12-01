package com.ordana.spelunkery.fabric;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.material.FlowingFluid;

import java.util.function.Supplier;

public class SpelunkeryPlatformImpl {
    public static void addFeatureToBiome(GenerationStep.Decoration step, TagKey<Biome> tagKey, ResourceKey<PlacedFeature> feature) {
        BiomeModifications.addFeature(BiomeSelectors.tag(tagKey), step, feature);
    }

    public static void addCarverToBiome(GenerationStep.Carving step, TagKey<Biome> tagKey, ResourceKey<ConfiguredWorldCarver<?>> carver) {
        BiomeModifications.addCarver(BiomeSelectors.tag(tagKey), step, carver);
    }

    public static LiquidBlock doSpringWater(Supplier<FlowingFluid> flowingFluid, BlockBehaviour.Properties properties) {
        return new SpringWaterBlock(flowingFluid.get(), properties);
    }

    public static Holder<GameEvent> registerForHolder(ResourceLocation id, Supplier<GameEvent> eventSupplier) {
        return Registry.registerForHolder(BuiltInRegistries.GAME_EVENT, id, eventSupplier.get());
    }
}
