package com.ordana.spelunkery.neoforge;


import com.ordana.spelunkery.Spelunkery;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
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
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class SpelunkeryPlatformImpl {
    public static final DeferredRegister<GameEvent> GAME_EVENTS = DeferredRegister.create(Registries.GAME_EVENT, Spelunkery.MOD_ID);

    public static void addFeatureToBiome(GenerationStep.Decoration step, TagKey<Biome> tagKey, ResourceKey<PlacedFeature> feature) {
    }


    public static void addCarverToBiome(GenerationStep.Carving step, TagKey<Biome> tagKey, ResourceKey<ConfiguredWorldCarver<?>> carver) {
    }

    public static LiquidBlock doSpringWater(Supplier<FlowingFluid> flowingFluid, BlockBehaviour.Properties properties) {
        return new SpringWaterBlock(flowingFluid, properties);
    }

    public static Holder<GameEvent> registerForHolder(ResourceLocation id, Supplier<GameEvent> eventSupplier) {
        return GAME_EVENTS.register(id.getPath(), eventSupplier);
    }
}
