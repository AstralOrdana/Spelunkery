package com.ordana.spelunkery.reg;

import com.ordana.spelunkery.Spelunkery;
import com.ordana.spelunkery.SpelunkeryPlatform;
import com.ordana.spelunkery.features.*;
import net.mehvahdjukaar.moonlight.api.misc.RegSupplier;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.VegetationPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.placement.CaveSurface;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.function.Supplier;

public class ModFeatures {

    //custom feature types
    public static final Supplier<Feature<HugeConkFungusFeatureConfig>> HUGE_CONK_FEATURE = RegHelper.registerFeature(
            Spelunkery.res("huge_conk"), () ->
                    new HugeConkFungusFeature(HugeConkFungusFeatureConfig.CODEC));

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

    // TODO 1.19.2 :: removed?
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

    public static void init() {

        //carver generation
        ResourceKey<ConfiguredWorldCarver<?>> end_cave = ResourceKey.create(Registry.CONFIGURED_CARVER_REGISTRY, Spelunkery.res("end_cave"));
        SpelunkeryPlatform.addCarverToBiome(GenerationStep.Carving.AIR, ModTags.HAS_END_NOISE, end_cave);

        ResourceKey<ConfiguredWorldCarver<?>> end_cave_extra = ResourceKey.create(Registry.CONFIGURED_CARVER_REGISTRY, Spelunkery.res("end_cave_extra"));
        SpelunkeryPlatform.addCarverToBiome(GenerationStep.Carving.AIR, ModTags.HAS_END_NOISE, end_cave_extra);

        ResourceKey<ConfiguredWorldCarver<?>> end_canyon = ResourceKey.create(Registry.CONFIGURED_CARVER_REGISTRY, Spelunkery.res("end_canyon"));
        SpelunkeryPlatform.addCarverToBiome(GenerationStep.Carving.AIR, ModTags.HAS_END_NOISE, end_canyon);

        ResourceKey<ConfiguredWorldCarver<?>> crevice = ResourceKey.create(Registry.CONFIGURED_CARVER_REGISTRY, Spelunkery.res("crevice"));
        SpelunkeryPlatform.addCarverToBiome(GenerationStep.Carving.AIR, ModTags.HAS_STONE_NOISE, crevice);

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
        ResourceKey<PlacedFeature> spring_water_pool = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, Spelunkery.res("spring_water_pool"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.FLUID_SPRINGS, ModTags.HAS_SALT_NOISE, spring_water_pool);

        ResourceKey<PlacedFeature> spring_water_spring = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, Spelunkery.res("spring_water_spring"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.FLUID_SPRINGS, ModTags.HAS_SALT_NOISE, spring_water_spring);

        ResourceKey<PlacedFeature> ore_aquifer = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, Spelunkery.res("ore_aquifer"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.UNDERGROUND_ORES, BiomeTags.IS_OVERWORLD, ore_aquifer);

        ResourceKey<PlacedFeature> diamond_ore_lava = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, Spelunkery.res("diamond_ore_lava"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.UNDERGROUND_ORES, BiomeTags.IS_OVERWORLD, diamond_ore_lava);

        ResourceKey<PlacedFeature> nephrite_geode = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, Spelunkery.res("nephrite_geode"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.UNDERGROUND_ORES, BiomeTags.IS_OVERWORLD, nephrite_geode);

        ResourceKey<PlacedFeature> magnetite_geode = ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, Spelunkery.res("magnetite_geode"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.UNDERGROUND_ORES, BiomeTags.IS_NETHER, magnetite_geode);

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
