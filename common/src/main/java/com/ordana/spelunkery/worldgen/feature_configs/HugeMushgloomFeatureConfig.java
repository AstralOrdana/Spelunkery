package com.ordana.spelunkery.worldgen.feature_configs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

import java.util.stream.Stream;

public class HugeMushgloomFeatureConfig implements FeatureConfiguration {

    public static final Codec<HugeMushgloomFeatureConfig> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            BlockStateProvider.CODEC.fieldOf("block_provider").forGetter((hugeMushgloomFeatureConfig) -> hugeMushgloomFeatureConfig.foliageProvider),
            BlockStateProvider.CODEC.fieldOf("stem_provider").forGetter((hugeMushgloomFeatureConfig) -> hugeMushgloomFeatureConfig.stemProvider),
            Codec.intRange(0, 16).fieldOf("height").orElse(2).forGetter((hugeMushgloomFeatureConfig) -> hugeMushgloomFeatureConfig.height))
            .apply(instance, HugeMushgloomFeatureConfig::new));

    public final BlockStateProvider foliageProvider;
    public final BlockStateProvider stemProvider;
    public int height;

    public HugeMushgloomFeatureConfig(BlockStateProvider foliageProvider, BlockStateProvider stemProvider, int height) {
        this.foliageProvider = foliageProvider;
        this.stemProvider = stemProvider;
        this.height = height;
    }

    @Override
    public Stream<ConfiguredFeature<?, ?>> getFeatures() {
        return FeatureConfiguration.super.getFeatures();
    }
}
