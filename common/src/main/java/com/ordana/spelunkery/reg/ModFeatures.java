package com.ordana.spelunkery.reg;

import com.ordana.spelunkery.SpelunkeryPlatform;
import com.ordana.spelunkery.Spelunkery;
import com.ordana.spelunkery.features.*;
import com.ordana.spelunkery.features.util.StoneEntry;
import com.ordana.spelunkery.features.util.StonePattern;
import net.mehvahdjukaar.moonlight.api.misc.RegSupplier;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.AcaciaFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.ForkingTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.CaveSurface;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

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

    public static final Supplier<Feature<BlockStripeFeatureConfig>> BLOCK_STRIPE_FEATURE = RegHelper.registerFeature(
            Spelunkery.res("block_stripe"), () ->
                    new BlockStripeFeature(BlockStripeFeatureConfig.CODEC));

    public static final Supplier<Feature<NoneFeatureConfiguration>> SCULK_PATCH_FEATURE = RegHelper.registerFeature(
            Spelunkery.res("sculk_patch"), () ->
                    new SculkGrowthFeature(NoneFeatureConfiguration.CODEC));


    public static void init() {

        //stone generation
        ResourceKey<PlacedFeature> noise_stone = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, Spelunkery.res("noise_stone"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, ModTags.HAS_STONE_NOISE, noise_stone);

        ResourceKey<PlacedFeature> noise_lush = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, Spelunkery.res("noise_lush"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, ModTags.HAS_LUSH_NOISE, noise_lush);

        ResourceKey<PlacedFeature> noise_dirt = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, Spelunkery.res("noise_dirt"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, ModTags.HAS_DIRT_NOISE, noise_dirt);

        ResourceKey<PlacedFeature> noise_ocean = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, Spelunkery.res("noise_ocean"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, ModTags.HAS_OCEAN_NOISE, noise_ocean);

        ResourceKey<PlacedFeature> noise_desert = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, Spelunkery.res("noise_desert"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, ModTags.HAS_DESERT_NOISE, noise_desert);

        ResourceKey<PlacedFeature> noise_ice = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, Spelunkery.res("noise_ice"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, ModTags.HAS_ICE_NOISE, noise_ice);

        ResourceKey<PlacedFeature> noise_sculk = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, Spelunkery.res("noise_sculk"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, ModTags.HAS_SCULK_NOISE, noise_sculk);

        ResourceKey<PlacedFeature> noise_salt = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, Spelunkery.res("noise_salt"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, ModTags.HAS_SALT_NOISE, noise_salt);

        ResourceKey<PlacedFeature> rock_salt = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, Spelunkery.res("rock_salt"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, ModTags.HAS_SALT_NOISE, rock_salt);


        ResourceKey<PlacedFeature> noise_iron = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, Spelunkery.res("noise_iron"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, ModTags.HAS_STONE_NOISE, noise_iron);


        //ores
        ResourceKey<PlacedFeature> ore_aquifer = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, Spelunkery.res("ore_aquifer"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.UNDERGROUND_ORES, BiomeTags.IS_OVERWORLD, ore_aquifer);

        ResourceKey<PlacedFeature> diamond_ore_lava = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, Spelunkery.res("diamond_ore_lava"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.UNDERGROUND_ORES, BiomeTags.IS_OVERWORLD, diamond_ore_lava);

        ResourceKey<PlacedFeature> nephrite_geode = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, Spelunkery.res("nephrite_geode"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.UNDERGROUND_ORES, BiomeTags.IS_OVERWORLD, nephrite_geode);

        ResourceKey<PlacedFeature> magnetite_geode = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, Spelunkery.res("magnetite_geode"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.UNDERGROUND_ORES, BiomeTags.IS_OVERWORLD, magnetite_geode);

        ResourceKey<PlacedFeature> quartz_geode = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, Spelunkery.res("quartz_geode"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.UNDERGROUND_ORES, BiomeTags.IS_NETHER, quartz_geode);

        //veins
        ResourceKey<PlacedFeature> large_gold_vein = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, Spelunkery.res("large_gold_vein"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, ModTags.HAS_NETHER_NOISE, large_gold_vein);

        ResourceKey<PlacedFeature> large_coal_vein = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, Spelunkery.res("large_coal_vein"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, ModTags.HAS_SWAMP_NOISE, large_coal_vein);

        ResourceKey<PlacedFeature> large_emerald_vein = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, Spelunkery.res("large_emerald_vein"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, ModTags.HAS_SALT_NOISE, large_emerald_vein);

        ResourceKey<PlacedFeature> large_lapis_vein = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, Spelunkery.res("large_lapis_vein"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, ModTags.HAS_DESERT_NOISE, large_lapis_vein);

        ResourceKey<PlacedFeature> tangle_roots_ceiling = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, Spelunkery.res("tangle_roots_ceiling"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.VEGETAL_DECORATION, BiomeTags.IS_OVERWORLD, tangle_roots_ceiling);


        //vegetation
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

        ResourceKey<PlacedFeature> mushgloom = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, Spelunkery.res("mushgloom"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.VEGETAL_DECORATION, BiomeTags.IS_OVERWORLD, mushgloom);

        ResourceKey<PlacedFeature> rare_huge_mushroom = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, Spelunkery.res("rare_huge_mushroom"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.VEGETAL_DECORATION, BiomeTags.IS_OVERWORLD, rare_huge_mushroom);

        ResourceKey<PlacedFeature> deep_dark_fossil = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, Spelunkery.res("deep_dark_fossil"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.VEGETAL_DECORATION, ModTags.HAS_SCULK_NOISE, deep_dark_fossil);

        ResourceKey<PlacedFeature> portal_fluid_pool = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, Spelunkery.res("portal_fluid_pool"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.SURFACE_STRUCTURES, ModTags.HAS_END_NOISE, portal_fluid_pool);

        ResourceKey<PlacedFeature> portal_fluid_spring = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, Spelunkery.res("portal_fluid_spring"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.SURFACE_STRUCTURES, ModTags.HAS_END_NOISE, portal_fluid_spring);

        ResourceKey<PlacedFeature> obsidian_patch = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, Spelunkery.res("obsidian_patch"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, ModTags.HAS_END_NOISE, obsidian_patch);

        ResourceKey<PlacedFeature> noise_end = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, Spelunkery.res("noise_end"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, ModTags.HAS_END_NOISE, noise_end);

    }

}
