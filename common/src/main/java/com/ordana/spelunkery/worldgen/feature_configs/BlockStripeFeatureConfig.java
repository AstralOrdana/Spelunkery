package com.ordana.spelunkery.worldgen.feature_configs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ordana.spelunkery.worldgen.feature_configs.util.StoneEntry;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import java.util.List;

public class BlockStripeFeatureConfig implements FeatureConfiguration {

    public static final Codec<BlockStripeFeatureConfig> CODEC = RecordCodecBuilder.create((instance)
            -> instance.group(

                    RegistryCodecs.homogeneousList(Registry.BLOCK_REGISTRY).fieldOf("first_target").forGetter((blockStripeFeatureConfig)
                            -> blockStripeFeatureConfig.firstTarget),
                    Codec.list(StoneEntry.CODEC).fieldOf("first_target_placer").forGetter((blockStripeFeatureConfig)
                            -> blockStripeFeatureConfig.firstTargetPlacer),
                    Codec.BOOL.fieldOf("use_second_target").orElse(false).forGetter((blockStripeFeatureConfig)
                            -> blockStripeFeatureConfig.useSecondTarget),
                    RegistryCodecs.homogeneousList(Registry.BLOCK_REGISTRY).fieldOf("second_target").orElse(null).forGetter((blockStripeFeatureConfig)
                            -> blockStripeFeatureConfig.secondTarget),
                    Codec.list(StoneEntry.CODEC).fieldOf("second_target_placer").orElse(null).forGetter((blockStripeFeatureConfig)
                            -> blockStripeFeatureConfig.secondTargetPlacer),

                    Codec.BOOL.fieldOf("use_biome_filter").orElse(false).forGetter((blockStripeFeatureConfig)
                            -> blockStripeFeatureConfig.useBiomeFilter),
                    RegistryCodecs.homogeneousList(Registry.BIOME_REGISTRY).fieldOf("biomes").orElse(null).forGetter((blockStripeFeatureConfig)
                            -> blockStripeFeatureConfig.biomes),
                    Codec.floatRange(0.0F, 1.0F).fieldOf("blank_patch_chance").orElse(0.0f).forGetter((blockStripeFeatureConfig)
                            -> blockStripeFeatureConfig.blankPatchChance),

                    Codec.BOOL.fieldOf("use_height_filter").orElse(false).forGetter((blockStripeFeatureConfig)
                            -> blockStripeFeatureConfig.useHeightFilter),
                    Codec.intRange(0, 64).fieldOf("surface_offset").orElse(0).forGetter((blockStripeFeatureConfig)
                            -> blockStripeFeatureConfig.surfaceOffset),
                    Codec.intRange(0, 64).fieldOf("bottom_offset").orElse(0).forGetter((blockStripeFeatureConfig)
                            -> blockStripeFeatureConfig.bottomOffset))

            .apply(instance, BlockStripeFeatureConfig::new));


    public final HolderSet<Block> firstTarget;
    public final List<StoneEntry> firstTargetPlacer;
    public final boolean useSecondTarget;
    public final HolderSet<Block> secondTarget;
    public final List<StoneEntry> secondTargetPlacer;

    public final boolean useBiomeFilter;
    public final HolderSet<Biome> biomes;

    public final float blankPatchChance;

    public final boolean useHeightFilter;
    public final int surfaceOffset;
    public final int bottomOffset;

    public BlockStripeFeatureConfig(
            HolderSet<Block> firstTarget,
            List<StoneEntry> firstTargetPlacer,
            Boolean useSecondTarget,
            HolderSet<Block> secondTarget,
            List<StoneEntry> secondTargetPlacer,
            Boolean useBiomeFilter,
            HolderSet<Biome> biomes,
            float blankPatchChance,
            Boolean useHeightFilter,
            int surfaceOffset,
            int bottomOffset) {

        this.firstTarget = firstTarget;
        this.firstTargetPlacer = firstTargetPlacer;
        this.useSecondTarget = useSecondTarget;
        this.secondTarget = secondTarget;
        this.secondTargetPlacer = secondTargetPlacer;

        this.useBiomeFilter = useBiomeFilter;
        this.biomes = biomes;

        this.blankPatchChance = blankPatchChance;

        this.useHeightFilter = useHeightFilter;
        this.surfaceOffset = surfaceOffset;
        this.bottomOffset = bottomOffset;

    }

}
