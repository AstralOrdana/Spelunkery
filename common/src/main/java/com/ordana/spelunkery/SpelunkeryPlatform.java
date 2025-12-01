package com.ordana.spelunkery;

import dev.architectury.injectables.annotations.ExpectPlatform;
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
import org.jetbrains.annotations.Contract;

import java.util.function.Supplier;

public class SpelunkeryPlatform {

    @Contract
    @ExpectPlatform
    public static void addFeatureToBiome(GenerationStep.Decoration step, TagKey<Biome> tagKey, ResourceKey<PlacedFeature> feature) {
        throw new AssertionError();
    }


    @Contract
    @ExpectPlatform
    public static void addCarverToBiome(GenerationStep.Carving step, TagKey<Biome> tagKey, ResourceKey<ConfiguredWorldCarver<?>> feature) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static LiquidBlock doPortalFluid(Supplier<FlowingFluid> flowingFluid, BlockBehaviour.Properties properties) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static LiquidBlock doSpringWater(Supplier<FlowingFluid> flowingFluid, BlockBehaviour.Properties properties) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static Holder<GameEvent> registerForHolder(ResourceLocation id, Supplier<GameEvent> eventSupplier) {
        return Registry.registerForHolder(BuiltInRegistries.GAME_EVENT, id, eventSupplier.get());
    }
}
