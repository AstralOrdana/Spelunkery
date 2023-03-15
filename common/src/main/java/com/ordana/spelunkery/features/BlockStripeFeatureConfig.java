package com.ordana.spelunkery.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ordana.spelunkery.features.util.StoneEntry;
import com.ordana.spelunkery.features.util.StonePattern;
import com.ordana.spelunkery.reg.ModBlocks;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RootSystemConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;

import java.util.List;

public class BlockStripeFeatureConfig implements FeatureConfiguration {

    public static final Codec<BlockStripeFeatureConfig> CODEC = RecordCodecBuilder.create((instance)
            -> instance.group(Registry.BLOCK.byNameCodec().fieldOf("block"),

                    RegistryCodecs.homogeneousList(Registry.BLOCK_REGISTRY).fieldOf("first_target").forGetter((blockStripeFeatureConfig)
                            -> blockStripeFeatureConfig.firstTarget),

                    Codec.list(TargetPlacer.CODEC).fieldOf("first_target_placer").forGetter((blockStripeFeatureConfig)
                            -> blockStripeFeatureConfig.firstTargetPlacer),

                    Codec.BOOL.fieldOf("use_second_target").orElse(false).forGetter((blockStripeFeatureConfig)
                            -> blockStripeFeatureConfig.useSecondTarget),

                    RegistryCodecs.homogeneousList(Registry.BLOCK_REGISTRY).fieldOf("second_target").forGetter((blockStripeFeatureConfig)
                            -> blockStripeFeatureConfig.secondTarget),

                    Codec.list(TargetPlacer.CODEC).fieldOf("second_target_placer").forGetter((blockStripeFeatureConfig)
                            -> blockStripeFeatureConfig.secondTargetPlacer),



                    Codec.BOOL.fieldOf("use_biome_filter").orElse(false).forGetter((blockStripeFeatureConfig)
                            -> blockStripeFeatureConfig.useBiomeFilter),

                    RegistryCodecs.homogeneousList(Registry.BIOME_REGISTRY).fieldOf("biomes").forGetter((blockStripeFeatureConfig)
                            -> blockStripeFeatureConfig.biomes),



                    Codec.floatRange(0.0F, 1.0F).fieldOf("biomes").orElse(0.0f).forGetter((blockStripeFeatureConfig)
                            -> blockStripeFeatureConfig.blankPatchChance),



                    Codec.BOOL.fieldOf("use_height_filter").orElse(false).forGetter((blockStripeFeatureConfig)
                            -> blockStripeFeatureConfig.useHeightFilter),

                    Codec.intRange(0, 64).fieldOf("surface_offset").orElse(0).forGetter((blockStripeFeatureConfig)
                            -> blockStripeFeatureConfig.surfaceOffset),

                    Codec.intRange(0, 64).fieldOf("bottom_offset").orElse(0).forGetter((blockStripeFeatureConfig)
                            -> blockStripeFeatureConfig.bottomOffset))

            .apply(instance, BlockStripeFeatureConfig::new));


    private final HolderSet<Block> firstTarget;
    private final List<StoneEntry> firstTargetPlacer;
    public final boolean useSecondTarget;
    private final HolderSet<Block> secondTarget;
    private final List<StoneEntry> secondTargetPlacer;

    public final boolean useBiomeFilter;
    private final HolderSet<Biome> biomes;

    private final float blankPatchChance;

    private final boolean useHeightFilter;
    private final int surfaceOffset;
    private final int bottomOffset;


    public BlockStripeFeatureConfig(HolderSet<Block> firstTarget, List<StoneEntry> firstTargetPlacer, Boolean useSecondTarget, HolderSet<Block> secondTarget, List<StoneEntry> secondTargetPlacer, HolderSet<Biome> biomes, Boolean useBiomeFilter, float blankPatchChance, Boolean useHeightFilter, int surfaceOffset, int bottomOffset) {

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


    public static class TargetPlacer {
        public static final Codec<BlockStripeFeatureConfig.TargetPlacer> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
                BlockStateProvider.CODEC.fieldOf("target").forGetter((targetPlacer)
                        -> targetPlacer.primaryStoneType),
                BlockStateProvider.CODEC.fieldOf("target").forGetter((targetPlacer)
                        -> targetPlacer.primaryStoneType),
                StonePattern.CODEC.fieldOf("state").forGetter((targetPlacer)
                        -> targetPlacer.stonePattern))
                .apply(instance, TargetPlacer::new));

        private final BlockStateProvider primaryStoneType;
        private final BlockStateProvider secondaryStoneType;
        private StonePattern stonePattern;

        TargetPlacer(BlockStateProvider primaryStoneType, BlockStateProvider secondaryStoneType, StonePattern stonePattern) {
            this.primaryStoneType = primaryStoneType;
            this.secondaryStoneType = secondaryStoneType;
            this.stonePattern = stonePattern;
        }
    }
}
