package com.ordana.spelunkery.reg;

import com.ordana.spelunkery.SpelunkeryPlatform;
import com.ordana.spelunkery.Spelunkery;
import com.ordana.spelunkery.features.CrystalFeature;
import com.ordana.spelunkery.features.CrystalFeatureConfig;
import com.ordana.spelunkery.features.WallMushroomFeature;
import com.ordana.spelunkery.features.WallMushroomFeatureConfig;
import com.ordana.spelunkery.features.util.NoiseBasedStoneFeature;
import com.ordana.spelunkery.features.util.StoneEntry;
import com.ordana.spelunkery.features.util.StonePattern;
import net.mehvahdjukaar.moonlight.api.misc.RegSupplier;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.MushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.HugeRedMushroomFeature;
import net.minecraft.world.level.levelgen.feature.VegetationPatchFeature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.AcaciaFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.ForkingTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.CaveSurface;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

import java.util.List;
import java.util.function.Supplier;

public class ModFeatures {

    public static final RegSupplier<ConfiguredFeature<VegetationPatchConfiguration, Feature<VegetationPatchConfiguration>>> SPORE_MOSS_PATCH_BONEMEAL =
            RegHelper.registerConfiguredFeature(Spelunkery.res("spore_moss_patch_bonemeal"), () -> Feature.VEGETATION_PATCH,
                    () -> new VegetationPatchConfiguration(
                            (BlockTags.MOSS_REPLACEABLE),
                            BlockStateProvider.simple(Blocks.MOSS_BLOCK),
                            PlacementUtils.inlinePlaced(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(
                                    new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                                            .add(Blocks.FLOWERING_AZALEA.defaultBlockState(), 4)
                                            .add(Blocks.AZALEA.defaultBlockState(), 7)
                                            .add(Blocks.MOSS_CARPET.defaultBlockState(), 25)
                                            .add(ModBlocks.SPOROPHYTE.get().defaultBlockState(), 50)
                                            .add(ModBlocks.TALL_SPOROPHYTE.get().defaultBlockState(), 10)))),
                            CaveSurface.FLOOR, ConstantInt.of(1), 0.0f, 5, 0.6f,
                            UniformInt.of(1, 2), 0.75f));

    public static final RegSupplier<ConfiguredFeature<HugeMushroomFeatureConfiguration, Feature<HugeMushroomFeatureConfiguration>>> HUGE_INKCAP_MUSHROOM =
            RegHelper.registerConfiguredFeature(Spelunkery.res("huge_inkcap_mushroom_bonemeal"), () -> Feature.HUGE_RED_MUSHROOM,
                    () -> new HugeMushroomFeatureConfiguration(
                            BlockStateProvider.simple(ModBlocks.INKCAP_MUSHROOM_BLOCK.get().defaultBlockState().setValue(HugeMushroomBlock.DOWN, false)),
                            BlockStateProvider.simple(Blocks.MUSHROOM_STEM.defaultBlockState().setValue(HugeMushroomBlock.UP, false).setValue(HugeMushroomBlock.DOWN, false)), 1));

    public static final RegSupplier<ConfiguredFeature<HugeMushroomFeatureConfiguration, Feature<HugeMushroomFeatureConfiguration>>> HUGE_WHITE_INKCAP_MUSHROOM =
            RegHelper.registerConfiguredFeature(Spelunkery.res("huge_white_inkcap_mushroom_bonemeal"), () -> Feature.HUGE_RED_MUSHROOM,
                    () -> new HugeMushroomFeatureConfiguration(
                            BlockStateProvider.simple(ModBlocks.WHITE_INKCAP_MUSHROOM_BLOCK.get().defaultBlockState().setValue(HugeMushroomBlock.DOWN, false)),
                            BlockStateProvider.simple(Blocks.MUSHROOM_STEM.defaultBlockState().setValue(HugeMushroomBlock.UP, false).setValue(HugeMushroomBlock.DOWN, false)), 1));


    public static final RegSupplier<ConfiguredFeature<TreeConfiguration, Feature<TreeConfiguration>>> HUGE_PORTABELLA =
            RegHelper.registerConfiguredFeature(Spelunkery.res("huge_portabella_bonemeal"), () -> Feature.TREE,
                    () -> (new TreeConfiguration.TreeConfigurationBuilder(
                            BlockStateProvider.simple(Blocks.MUSHROOM_STEM.defaultBlockState().setValue(HugeMushroomBlock.DOWN, false)),
                            new ForkingTrunkPlacer(5, 2, 2),
                            BlockStateProvider.simple(ModBlocks.PORTABELLA_BLOCK.get().defaultBlockState().setValue(HugeMushroomBlock.DOWN, false)),
                            new AcaciaFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0)),
                            new TwoLayersFeatureSize(1, 0, 2))).ignoreVines().build());

    //features
    public static final Supplier<Feature<WallMushroomFeatureConfig>> WALL_MUSHROOM_FEATURE = RegHelper.registerFeature(
            Spelunkery.res("wall_mushroom"), () ->
                    new WallMushroomFeature(WallMushroomFeatureConfig.CODEC));

    public static final Supplier<Feature<CrystalFeatureConfig>> CRYSTAL_FEATURE = RegHelper.registerFeature(
            Spelunkery.res("crystal"), () ->
                    new CrystalFeature(CrystalFeatureConfig.CODEC));

    public static final Supplier<Feature<NoneFeatureConfiguration>> NOISE_STONE_FEATURE = RegHelper.registerFeature(
            Spelunkery.res("noise_stone"), () -> NoiseBasedStoneFeature.featureWithDeepslateUnfiltered(NoneFeatureConfiguration.CODEC,
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
            Spelunkery.res("noise_lush"), () -> NoiseBasedStoneFeature.featureWithDeepslateFiltered(NoneFeatureConfiguration.CODEC,
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
            Spelunkery.res("noise_ocean"), () -> NoiseBasedStoneFeature.featureWithDeepslateFiltered(NoneFeatureConfiguration.CODEC,
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
            Spelunkery.res("noise_dirt"), () -> NoiseBasedStoneFeature.featureNoDeepslateFiltered(NoneFeatureConfiguration.CODEC,
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

    public static final Supplier<Feature<NoneFeatureConfiguration>> NOISE_DESERT_FEATURE = RegHelper.registerFeature(
            Spelunkery.res("noise_desert"), () -> NoiseBasedStoneFeature.featureWithDeepslateFiltered(NoneFeatureConfiguration.CODEC,
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

    public static final Supplier<Feature<NoneFeatureConfiguration>> NOISE_SALT_FEATURE = RegHelper.registerFeature(
            Spelunkery.res("noise_salt"), () -> NoiseBasedStoneFeature.featureNoDeepslateFiltered(NoneFeatureConfiguration.CODEC,
                    List.of(
                            new StoneEntry(Blocks.STONE, ModBlocks.ROCK_SALT_BLOCK.get(), StonePattern.rock_salt())
                    ),
                    (BiomeTags.IS_MOUNTAIN),
                    0.5F,
                    (ModTags.STONE_TARGET),
                    true, 0, 32
            )
    );


    //configured features
    public static final RegSupplier<ConfiguredFeature<NoneFeatureConfiguration, Feature<NoneFeatureConfiguration>>> NOISE_STONE_CONFIGURED =
            RegHelper.registerConfiguredFeature(Spelunkery.res("noise_stone"),
                    NOISE_STONE_FEATURE, () -> FeatureConfiguration.NONE);

    public static final RegSupplier<ConfiguredFeature<NoneFeatureConfiguration, Feature<NoneFeatureConfiguration>>> NOISE_LUSH_CONFIGURED =
            RegHelper.registerConfiguredFeature(Spelunkery.res("noise_lush"),
                    NOISE_LUSH_FEATURE, () -> FeatureConfiguration.NONE);

    public static final RegSupplier<ConfiguredFeature<NoneFeatureConfiguration, Feature<NoneFeatureConfiguration>>> NOISE_DIRT_CONFIGURED =
            RegHelper.registerConfiguredFeature(Spelunkery.res("noise_dirt"),
                    NOISE_DIRT_FEATURE, () -> FeatureConfiguration.NONE);

    public static final RegSupplier<ConfiguredFeature<NoneFeatureConfiguration, Feature<NoneFeatureConfiguration>>> NOISE_OCEAN_CONFIGURED =
            RegHelper.registerConfiguredFeature(Spelunkery.res("noise_ocean"),
                    NOISE_OCEAN_FEATURE, () -> FeatureConfiguration.NONE);

    public static final RegSupplier<ConfiguredFeature<NoneFeatureConfiguration, Feature<NoneFeatureConfiguration>>> NOISE_DESERT_CONFIGURED =
            RegHelper.registerConfiguredFeature(Spelunkery.res("noise_desert"),
                    NOISE_DESERT_FEATURE, () -> FeatureConfiguration.NONE);

    public static final RegSupplier<ConfiguredFeature<NoneFeatureConfiguration, Feature<NoneFeatureConfiguration>>> NOISE_SALT_CONFIGURED =
            RegHelper.registerConfiguredFeature(Spelunkery.res("noise_salt"),
                    NOISE_SALT_FEATURE, () -> FeatureConfiguration.NONE);


    //placed features
    public static final RegSupplier<PlacedFeature> PLACED_NOISE_STONE =
            RegHelper.registerPlacedFeature(Spelunkery.res("noise_stone"), NOISE_STONE_CONFIGURED, List::of);

    public static final RegSupplier<PlacedFeature> PLACED_NOISE_LUSH =
            RegHelper.registerPlacedFeature(Spelunkery.res("noise_lush"), NOISE_LUSH_CONFIGURED, List::of);

    public static final RegSupplier<PlacedFeature> PLACED_NOISE_OCEAN =
            RegHelper.registerPlacedFeature(Spelunkery.res("noise_ocean"), NOISE_OCEAN_CONFIGURED, List::of);

    public static final RegSupplier<PlacedFeature> PLACED_NOISE_DIRT =
            RegHelper.registerPlacedFeature(Spelunkery.res("noise_dirt"), NOISE_DIRT_CONFIGURED, List::of);

    public static final RegSupplier<PlacedFeature> PLACED_NOISE_DESERT =
            RegHelper.registerPlacedFeature(Spelunkery.res("noise_desert"), NOISE_DESERT_CONFIGURED, List::of);

    public static final RegSupplier<PlacedFeature> PLACED_NOISE_SALT =
            RegHelper.registerPlacedFeature(Spelunkery.res("noise_salt"), NOISE_SALT_CONFIGURED, List::of);


    public static void init() {

        //stone generation
        ResourceKey<PlacedFeature> noise_stone = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, Spelunkery.res("noise_stone"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, ModTags.HAS_STONE_NOISE, noise_stone);

        ResourceKey<PlacedFeature> noise_lush = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, Spelunkery.res("noise_lush"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, ModTags.HAS_LUSH_NOISE, noise_lush);

        ResourceKey<PlacedFeature> noise_ocean = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, Spelunkery.res("noise_ocean"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, ModTags.HAS_OCEAN_NOISE, noise_ocean);

        ResourceKey<PlacedFeature> noise_dirt = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, Spelunkery.res("noise_dirt"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, ModTags.HAS_DIRT_NOISE, noise_dirt);

        ResourceKey<PlacedFeature> noise_desert = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, Spelunkery.res("noise_desert"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, ModTags.HAS_DESERT_NOISE, noise_desert);

        ResourceKey<PlacedFeature> noise_salt = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, Spelunkery.res("noise_salt"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, BiomeTags.IS_MOUNTAIN, noise_salt);

        ResourceKey<PlacedFeature> rock_salt = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, Spelunkery.res("rock_salt"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, BiomeTags.IS_MOUNTAIN, rock_salt);

        //ores
        ResourceKey<PlacedFeature> ore_aquifer = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, Spelunkery.res("ore_aquifer"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.UNDERGROUND_ORES, BiomeTags.IS_OVERWORLD, ore_aquifer);

        ResourceKey<PlacedFeature> diamond_ore_lava = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, Spelunkery.res("diamond_ore_lava"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.UNDERGROUND_ORES, BiomeTags.IS_OVERWORLD, diamond_ore_lava);

        ResourceKey<PlacedFeature> nephrite_geode = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, Spelunkery.res("nephrite_geode"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.UNDERGROUND_ORES, BiomeTags.IS_OVERWORLD, nephrite_geode);

        //coal vein
        ResourceKey<PlacedFeature> large_coal_vein = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, Spelunkery.res("large_coal_vein"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, BiomeTags.HAS_SWAMP_HUT, large_coal_vein);

        //emerald vein
        ResourceKey<PlacedFeature> large_emerald_vein = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, Spelunkery.res("large_emerald_vein"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, BiomeTags.IS_MOUNTAIN, large_emerald_vein);

        //lapis vein
        ResourceKey<PlacedFeature> large_lapis_vein = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, Spelunkery.res("large_lapis_vein"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, ModTags.HAS_DESERT_NOISE, large_lapis_vein);

        //vegetation
        ResourceKey<PlacedFeature> tangle_roots_ceiling = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, Spelunkery.res("tangle_roots_ceiling"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.VEGETAL_DECORATION, BiomeTags.IS_OVERWORLD, tangle_roots_ceiling);

        ResourceKey<PlacedFeature> conk_fungus = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, Spelunkery.res("conk_fungus"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.VEGETAL_DECORATION, BiomeTags.IS_OVERWORLD, conk_fungus);

        ResourceKey<PlacedFeature> conk_fungus_surface = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, Spelunkery.res("conk_fungus_surface"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.VEGETAL_DECORATION, BiomeTags.IS_OVERWORLD, conk_fungus_surface);

        ResourceKey<PlacedFeature> inkcap = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, Spelunkery.res("inkcap"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.VEGETAL_DECORATION, BiomeTags.IS_OVERWORLD, inkcap);

        ResourceKey<PlacedFeature> inkcap_deepslate = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, Spelunkery.res("inkcap_deepslate"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.VEGETAL_DECORATION, BiomeTags.IS_OVERWORLD, inkcap_deepslate);

        ResourceKey<PlacedFeature> portabella = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, Spelunkery.res("portabella"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.VEGETAL_DECORATION, BiomeTags.IS_OVERWORLD, portabella);

        ResourceKey<PlacedFeature> phosphor_fungus = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, Spelunkery.res("phosphor_fungus"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.VEGETAL_DECORATION, BiomeTags.IS_OVERWORLD, phosphor_fungus);

        ResourceKey<PlacedFeature> rare_huge_mushroom = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, Spelunkery.res("rare_huge_mushroom"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.VEGETAL_DECORATION, BiomeTags.IS_OVERWORLD, rare_huge_mushroom);

        ResourceKey<PlacedFeature> deep_dark_fossil = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, Spelunkery.res("deep_dark_fossil"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.VEGETAL_DECORATION, BiomeTags.HAS_ANCIENT_CITY, deep_dark_fossil);

    }

}
