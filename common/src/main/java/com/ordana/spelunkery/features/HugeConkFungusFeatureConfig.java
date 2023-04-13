package com.ordana.spelunkery.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

import java.util.stream.Stream;

public class HugeConkFungusFeatureConfig implements FeatureConfiguration {

    public static final Codec<HugeConkFungusFeatureConfig> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            BlockStateProvider.CODEC.fieldOf("block_provider").forGetter((treeConfiguration) -> treeConfiguration.foliageProvider),
            Codec.intRange(0, 16).fieldOf("radius").orElse(2).forGetter((hugeConkFungusFeatureConfig) -> hugeConkFungusFeatureConfig.radius),
            Codec.intRange(0, 16).fieldOf("large_chance").orElse(0).forGetter((hugeConkFungusFeatureConfig) -> hugeConkFungusFeatureConfig.largeChance))
            .apply(instance, HugeConkFungusFeatureConfig::new));

    public final BlockStateProvider foliageProvider;
    public int radius;
    public int largeChance;

    public HugeConkFungusFeatureConfig(BlockStateProvider foliageProvider, int radius, int largeChance) {
        this.foliageProvider = foliageProvider;
        this.radius = radius;
        this.largeChance = largeChance;
    }

    @Override
    public Stream<ConfiguredFeature<?, ?>> getFeatures() {
        return FeatureConfiguration.super.getFeatures();
    }
}
