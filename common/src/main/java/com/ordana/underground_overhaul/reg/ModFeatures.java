package com.ordana.underground_overhaul.reg;

import com.ordana.underground_overhaul.UAPlatform;
import com.ordana.underground_overhaul.UndergroundOverhaul;
import com.ordana.underground_overhaul.features.util.NoiseBasedStoneFeature;
import com.ordana.underground_overhaul.features.util.StoneEntry;
import com.ordana.underground_overhaul.features.util.StonePattern;
import net.mehvahdjukaar.moonlight.api.misc.RegSupplier;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;
import java.util.function.Supplier;

public class ModFeatures {

    //features
    public static final Supplier<Feature<NoneFeatureConfiguration>> NOISE_STONE_FEATURE = RegHelper.registerFeature(
            UndergroundOverhaul.res("noise_stone"), () -> NoiseBasedStoneFeature.featureWithDeepslateUnfiltered(NoneFeatureConfiguration.CODEC,
                    List.of(
                            new StoneEntry(Blocks.GRANITE, Blocks.STONE, StonePattern.sedimentaryStone()),
                            new StoneEntry(Blocks.STONE, Blocks.DIORITE, StonePattern.sedimentaryStone()),
                            new StoneEntry(Blocks.ANDESITE, Blocks.STONE, StonePattern.sedimentaryStone())
                    ),
                    List.of(
                            new StoneEntry(Blocks.TUFF, Blocks.DEEPSLATE, StonePattern.sedimentaryStone()),
                            new StoneEntry(Blocks.TUFF, Blocks.DEEPSLATE, StonePattern.sedimentaryStone()),
                            new StoneEntry(Blocks.TUFF, Blocks.DEEPSLATE, StonePattern.sedimentaryStone())
                    ),
                    0.25F,
                    (ModTags.STONE_TARGET),
                    (ModTags.DEEPSLATE_TARGET),
                    false, 10, 0
            )

    );

    public static final Supplier<Feature<NoneFeatureConfiguration>> NOISE_LUSH_FEATURE = RegHelper.registerFeature(
            UndergroundOverhaul.res("noise_lush"), () -> NoiseBasedStoneFeature.featureWithDeepslateFiltered(NoneFeatureConfiguration.CODEC,
                    List.of(
                            new StoneEntry(Blocks.CLAY, Blocks.CALCITE, StonePattern.dottedClay())
                    ),
                    List.of(
                            new StoneEntry(Blocks.TUFF, Blocks.CLAY, StonePattern.dottedClay())
                    ),
                    (ModTags.HAS_LUSH_NOISE),
                    0F,
                    (ModTags.STONE_TARGET),
                    (ModTags.DEEPSLATE_TARGET),
                    false, 10, 0
            )
    );

    public static final Supplier<Feature<NoneFeatureConfiguration>> NOISE_OCEAN_FEATURE = RegHelper.registerFeature(
            UndergroundOverhaul.res("noise_ocean"), () -> NoiseBasedStoneFeature.featureWithDeepslateFiltered(NoneFeatureConfiguration.CODEC,
                    List.of(
                            new StoneEntry(Blocks.SMOOTH_BASALT, Blocks.SMOOTH_BASALT, StonePattern.dottedClay())
                    ),
                    List.of(
                            new StoneEntry(Blocks.BASALT, Blocks.SMOOTH_BASALT, StonePattern.dottedClay())
                    ),
                    (ModTags.HAS_OCEAN_NOISE),
                    0F,
                    (ModTags.OCEAN_TARGET),
                    (ModTags.DEEPSLATE_TARGET),
                    true, 0, 128
            )
    );

    public static final Supplier<Feature<NoneFeatureConfiguration>> NOISE_DIRT_FEATURE = RegHelper.registerFeature(
            UndergroundOverhaul.res("noise_dirt"), () -> NoiseBasedStoneFeature.featureNoDeepslateFiltered(NoneFeatureConfiguration.CODEC,
                    List.of(
                            new StoneEntry(Blocks.DIRT, Blocks.COARSE_DIRT, StonePattern.dirtStripes()),
                            new StoneEntry(Blocks.COARSE_DIRT, Blocks.GRAVEL, StonePattern.dirtStripes())
                    ),
                    (ModTags.HAS_DIRT_NOISE),
                    0F,
                    (ModTags.STONE_TARGET),
                    true, 0, 25
            )
    );

    public static final Supplier<Feature<NoneFeatureConfiguration>> NOISE_RIVER_FEATURE = RegHelper.registerFeature(
            UndergroundOverhaul.res("noise_river"), () -> NoiseBasedStoneFeature.featureNoDeepslateFiltered(NoneFeatureConfiguration.CODEC,
                    List.of(
                            new StoneEntry(Blocks.SAND, Blocks.CLAY, StonePattern.river_stripes()),
                            new StoneEntry(Blocks.CLAY, Blocks.GRAVEL, StonePattern.river_stripes())
                    ),
                    (BiomeTags.IS_RIVER),
                    0F,
                    (ModTags.RIVER_TARGET),
                    true, 0, 2
            )
    );

    public static final Supplier<Feature<NoneFeatureConfiguration>> NOISE_DESERT_FEATURE = RegHelper.registerFeature(
            UndergroundOverhaul.res("noise_desert"), () -> NoiseBasedStoneFeature.featureWithDeepslateFiltered(NoneFeatureConfiguration.CODEC,
                    List.of(
                            new StoneEntry(Blocks.SANDSTONE, Blocks.SAND, StonePattern.desert_terrain()),
                            new StoneEntry(Blocks.SANDSTONE, Blocks.PACKED_MUD, StonePattern.desert_terrain()),
                            new StoneEntry(Blocks.SAND, Blocks.SANDSTONE, StonePattern.desert_terrain()),
                            new StoneEntry(Blocks.SMOOTH_SANDSTONE, Blocks.SAND, StonePattern.desert_terrain()),
                            new StoneEntry(Blocks.SMOOTH_SANDSTONE, Blocks.PACKED_MUD, StonePattern.desert_terrain())
                    ),
                    List.of(
                            new StoneEntry(Blocks.SANDSTONE, Blocks.TUFF, StonePattern.desert_terrain()),
                            new StoneEntry(Blocks.SANDSTONE, Blocks.MUD, StonePattern.desert_terrain()),
                            new StoneEntry(Blocks.TUFF, Blocks.SAND, StonePattern.desert_terrain()),
                            new StoneEntry(Blocks.SANDSTONE, Blocks.TUFF, StonePattern.desert_terrain()),
                            new StoneEntry(Blocks.SANDSTONE, Blocks.MUD, StonePattern.desert_terrain())
                    ),
                    (ModTags.HAS_DESERT_NOISE),
                    0F,
                    (ModTags.STONE_TARGET),
                    (ModTags.DEEPSLATE_TARGET),
                    true, 0, 128
            )
    );


    //configured features
    public static final RegSupplier<ConfiguredFeature<NoneFeatureConfiguration, Feature<NoneFeatureConfiguration>>> NOISE_STONE_CONFIGURED =
            RegHelper.registerConfiguredFeature(UndergroundOverhaul.res("noise_stone"),
                    NOISE_STONE_FEATURE, () -> FeatureConfiguration.NONE);

    public static final RegSupplier<ConfiguredFeature<NoneFeatureConfiguration, Feature<NoneFeatureConfiguration>>> NOISE_LUSH_CONFIGURED =
            RegHelper.registerConfiguredFeature(UndergroundOverhaul.res("noise_lush"),
                    NOISE_LUSH_FEATURE, () -> FeatureConfiguration.NONE);

    public static final RegSupplier<ConfiguredFeature<NoneFeatureConfiguration, Feature<NoneFeatureConfiguration>>> NOISE_DIRT_CONFIGURED =
            RegHelper.registerConfiguredFeature(UndergroundOverhaul.res("noise_dirt"),
                    NOISE_DIRT_FEATURE, () -> FeatureConfiguration.NONE);

    public static final RegSupplier<ConfiguredFeature<NoneFeatureConfiguration, Feature<NoneFeatureConfiguration>>> NOISE_RIVER_CONFIGURED =
            RegHelper.registerConfiguredFeature(UndergroundOverhaul.res("noise_river"),
                    NOISE_RIVER_FEATURE, () -> FeatureConfiguration.NONE);

    public static final RegSupplier<ConfiguredFeature<NoneFeatureConfiguration, Feature<NoneFeatureConfiguration>>> NOISE_OCEAN_CONFIGURED =
            RegHelper.registerConfiguredFeature(UndergroundOverhaul.res("noise_ocean"),
                    NOISE_OCEAN_FEATURE, () -> FeatureConfiguration.NONE);

    public static final RegSupplier<ConfiguredFeature<NoneFeatureConfiguration, Feature<NoneFeatureConfiguration>>> NOISE_DESERT_CONFIGURED =
            RegHelper.registerConfiguredFeature(UndergroundOverhaul.res("noise_desert"),
                    NOISE_DESERT_FEATURE, () -> FeatureConfiguration.NONE);

    //placed features
    public static final RegSupplier<PlacedFeature> PLACED_NOISE_STONE =
            RegHelper.registerPlacedFeature(UndergroundOverhaul.res("noise_stone"), NOISE_STONE_CONFIGURED, List::of);

    public static final RegSupplier<PlacedFeature> PLACED_NOISE_LUSH =
            RegHelper.registerPlacedFeature(UndergroundOverhaul.res("noise_lush"), NOISE_LUSH_CONFIGURED, List::of);

    public static final RegSupplier<PlacedFeature> PLACED_NOISE_OCEAN =
            RegHelper.registerPlacedFeature(UndergroundOverhaul.res("noise_ocean"), NOISE_OCEAN_CONFIGURED, List::of);

    public static final RegSupplier<PlacedFeature> PLACED_NOISE_DIRT =
            RegHelper.registerPlacedFeature(UndergroundOverhaul.res("noise_dirt"), NOISE_DIRT_CONFIGURED, List::of);

    public static final RegSupplier<PlacedFeature> PLACED_NOISE_RIVER =
            RegHelper.registerPlacedFeature(UndergroundOverhaul.res("noise_river"), NOISE_RIVER_CONFIGURED, List::of);

    public static final RegSupplier<PlacedFeature> PLACED_NOISE_DESERT =
            RegHelper.registerPlacedFeature(UndergroundOverhaul.res("noise_desert"), NOISE_DESERT_CONFIGURED, List::of);

    public static void init() {

        //stone generation
        ResourceKey<PlacedFeature> noise_stone = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, UndergroundOverhaul.res("noise_stone"));
        UAPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, BiomeTags.IS_OVERWORLD, noise_stone);

        ResourceKey<PlacedFeature> noise_lush = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, UndergroundOverhaul.res("noise_lush"));
        UAPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, ModTags.HAS_LUSH_NOISE, noise_lush);

        ResourceKey<PlacedFeature> noise_ocean = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, UndergroundOverhaul.res("noise_ocean"));
        UAPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, ModTags.HAS_OCEAN_NOISE, noise_ocean);

        ResourceKey<PlacedFeature> noise_dirt = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, UndergroundOverhaul.res("noise_dirt"));
        UAPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, BiomeTags.IS_OVERWORLD, noise_dirt);

        ResourceKey<PlacedFeature> noise_river = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, UndergroundOverhaul.res("noise_river"));
        UAPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, BiomeTags.IS_RIVER, noise_river);

        ResourceKey<PlacedFeature> noise_desert = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, UndergroundOverhaul.res("noise_desert"));
        UAPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, BiomeTags.IS_OVERWORLD, noise_desert);

        //coal vein
        ResourceKey<PlacedFeature> large_coal_vein = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, UndergroundOverhaul.res("large_coal_vein"));
        UAPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, BiomeTags.HAS_SWAMP_HUT, large_coal_vein);

        //emerald vein
        ResourceKey<PlacedFeature> large_emerald_vein = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, UndergroundOverhaul.res("large_emerald_vein"));
        UAPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, BiomeTags.IS_MOUNTAIN, large_emerald_vein);

        //lapis vein
        ResourceKey<PlacedFeature> large_lapis_vein = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, UndergroundOverhaul.res("large_lapis_vein"));
        UAPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, BiomeTags.HAS_DESERT_PYRAMID, large_lapis_vein);

        /*
        //redstone vein
        ResourceKey<PlacedFeature> large_redstone_vein = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, UndergroundOverhaul.res("large_redstone_vein"));
        UAPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, BiomeTags.IS_OVERWORLD, large_redstone_vein);
         */

    }
}

