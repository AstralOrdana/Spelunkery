package com.ordana.spelunkery.reg;

import com.ordana.spelunkery.Spelunkery;
import com.ordana.spelunkery.SpelunkeryPlatform;
import com.ordana.spelunkery.worldgen.feature_configs.BlockStripeFeatureConfig;
import com.ordana.spelunkery.worldgen.feature_configs.CrystalFeatureConfig;
import com.ordana.spelunkery.worldgen.feature_configs.HugeConkFungusFeatureConfig;
import com.ordana.spelunkery.worldgen.feature_configs.WallMushroomFeatureConfig;
import com.ordana.spelunkery.worldgen.features.*;
import com.ordana.spelunkery.worldgen.structures.MineshaftDustCorridor;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

import java.util.function.Supplier;

public class ModWorldgenFeatures {

    //structure pieces

    public static final Supplier<StructurePieceType> MINESHAFT_DUST_CORRIDOR = RegHelper.register(
            Spelunkery.res("mineshaft_dust_corridor"), () -> MineshaftDustCorridor::new, Registries.STRUCTURE_PIECE);


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

    public static void init() {

        //carver generation
        ResourceKey<ConfiguredWorldCarver<?>> end_cave = ResourceKey.create(Registries.CONFIGURED_CARVER, Spelunkery.res("end_cave"));
        SpelunkeryPlatform.addCarverToBiome(GenerationStep.Carving.AIR, ModTags.HAS_END_NOISE, end_cave);

        ResourceKey<ConfiguredWorldCarver<?>> end_cave_extra = ResourceKey.create(Registries.CONFIGURED_CARVER, Spelunkery.res("end_cave_extra"));
        SpelunkeryPlatform.addCarverToBiome(GenerationStep.Carving.AIR, ModTags.HAS_END_NOISE, end_cave_extra);

        ResourceKey<ConfiguredWorldCarver<?>> end_canyon = ResourceKey.create(Registries.CONFIGURED_CARVER, Spelunkery.res("end_canyon"));
        SpelunkeryPlatform.addCarverToBiome(GenerationStep.Carving.AIR, ModTags.HAS_END_NOISE, end_canyon);

        ResourceKey<ConfiguredWorldCarver<?>> crevice = ResourceKey.create(Registries.CONFIGURED_CARVER, Spelunkery.res("crevice"));
        SpelunkeryPlatform.addCarverToBiome(GenerationStep.Carving.AIR, ModTags.HAS_STONE_NOISE, crevice);

        //stone generation
        ResourceKey<PlacedFeature> noise_stone = ResourceKey.create(Registries.PLACED_FEATURE, Spelunkery.res("noise_stone"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, ModTags.HAS_STONE_NOISE, noise_stone);

        ResourceKey<PlacedFeature> noise_lush = ResourceKey.create(Registries.PLACED_FEATURE, Spelunkery.res("noise_lush"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, ModTags.HAS_LUSH_NOISE, noise_lush);

        ResourceKey<PlacedFeature> noise_dirt = ResourceKey.create(Registries.PLACED_FEATURE, Spelunkery.res("noise_dirt"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, ModTags.HAS_DIRT_NOISE, noise_dirt);

        ResourceKey<PlacedFeature> noise_ocean = ResourceKey.create(Registries.PLACED_FEATURE, Spelunkery.res("noise_ocean"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, ModTags.HAS_OCEAN_NOISE, noise_ocean);

        ResourceKey<PlacedFeature> noise_desert = ResourceKey.create(Registries.PLACED_FEATURE, Spelunkery.res("noise_desert"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, ModTags.HAS_DESERT_NOISE, noise_desert);

        ResourceKey<PlacedFeature> noise_ice = ResourceKey.create(Registries.PLACED_FEATURE, Spelunkery.res("noise_ice"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, ModTags.HAS_ICE_NOISE, noise_ice);

        ResourceKey<PlacedFeature> noise_sculk = ResourceKey.create(Registries.PLACED_FEATURE, Spelunkery.res("noise_sculk"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, ModTags.HAS_SCULK_NOISE, noise_sculk);

        ResourceKey<PlacedFeature> noise_salt = ResourceKey.create(Registries.PLACED_FEATURE, Spelunkery.res("noise_salt"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, ModTags.HAS_SALT_NOISE, noise_salt);

        ResourceKey<PlacedFeature> rock_salt = ResourceKey.create(Registries.PLACED_FEATURE, Spelunkery.res("rock_salt"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, ModTags.HAS_SALT_NOISE, rock_salt);


        ResourceKey<PlacedFeature> noise_iron = ResourceKey.create(Registries.PLACED_FEATURE, Spelunkery.res("noise_iron"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, ModTags.HAS_STONE_NOISE, noise_iron);


        //ores
        ResourceKey<PlacedFeature> spring_water_pool = ResourceKey.create(Registries.PLACED_FEATURE, Spelunkery.res("spring_water_pool"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.FLUID_SPRINGS, ModTags.HAS_SALT_NOISE, spring_water_pool);

        ResourceKey<PlacedFeature> spring_water_spring = ResourceKey.create(Registries.PLACED_FEATURE, Spelunkery.res("spring_water_spring"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.FLUID_SPRINGS, ModTags.HAS_SALT_NOISE, spring_water_spring);

        ResourceKey<PlacedFeature> ore_aquifer = ResourceKey.create(Registries.PLACED_FEATURE, Spelunkery.res("ore_aquifer"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.UNDERGROUND_ORES, BiomeTags.IS_OVERWORLD, ore_aquifer);

        ResourceKey<PlacedFeature> diamond_ore_lava = ResourceKey.create(Registries.PLACED_FEATURE, Spelunkery.res("diamond_ore_lava"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.UNDERGROUND_ORES, BiomeTags.IS_OVERWORLD, diamond_ore_lava);

        ResourceKey<PlacedFeature> nephrite_geode = ResourceKey.create(Registries.PLACED_FEATURE, Spelunkery.res("nephrite_geode"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.UNDERGROUND_ORES, BiomeTags.IS_OVERWORLD, nephrite_geode);

        ResourceKey<PlacedFeature> magnetite_geode = ResourceKey.create(Registries.PLACED_FEATURE, Spelunkery.res("magnetite_geode"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.UNDERGROUND_ORES, BiomeTags.IS_NETHER, magnetite_geode);

        ResourceKey<PlacedFeature> quartz_geode = ResourceKey.create(Registries.PLACED_FEATURE, Spelunkery.res("quartz_geode"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.UNDERGROUND_ORES, BiomeTags.IS_NETHER, quartz_geode);

        //veins
        ResourceKey<PlacedFeature> large_gold_vein = ResourceKey.create(Registries.PLACED_FEATURE, Spelunkery.res("large_gold_vein"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, ModTags.HAS_NETHER_NOISE, large_gold_vein);

        ResourceKey<PlacedFeature> large_coal_vein = ResourceKey.create(Registries.PLACED_FEATURE, Spelunkery.res("large_coal_vein"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, ModTags.HAS_SWAMP_NOISE, large_coal_vein);

        ResourceKey<PlacedFeature> large_emerald_vein = ResourceKey.create(Registries.PLACED_FEATURE, Spelunkery.res("large_emerald_vein"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, ModTags.HAS_SALT_NOISE, large_emerald_vein);

        ResourceKey<PlacedFeature> large_lapis_vein = ResourceKey.create(Registries.PLACED_FEATURE, Spelunkery.res("large_lapis_vein"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, ModTags.HAS_DESERT_NOISE, large_lapis_vein);

        ResourceKey<PlacedFeature> tangle_roots_ceiling = ResourceKey.create(Registries.PLACED_FEATURE, Spelunkery.res("tangle_roots_ceiling"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.VEGETAL_DECORATION, BiomeTags.IS_OVERWORLD, tangle_roots_ceiling);


        //vegetation
        ResourceKey<PlacedFeature> conk_fungus = ResourceKey.create(Registries.PLACED_FEATURE, Spelunkery.res("conk_fungus"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.VEGETAL_DECORATION, BiomeTags.IS_OVERWORLD, conk_fungus);

        ResourceKey<PlacedFeature> conk_fungus_surface = ResourceKey.create(Registries.PLACED_FEATURE, Spelunkery.res("conk_fungus_surface"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.VEGETAL_DECORATION, BiomeTags.IS_OVERWORLD, conk_fungus_surface);

        ResourceKey<PlacedFeature> inkcap = ResourceKey.create(Registries.PLACED_FEATURE, Spelunkery.res("inkcap"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.VEGETAL_DECORATION, BiomeTags.IS_OVERWORLD, inkcap);

        ResourceKey<PlacedFeature> inkcap_deepslate = ResourceKey.create(Registries.PLACED_FEATURE, Spelunkery.res("inkcap_deepslate"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.VEGETAL_DECORATION, BiomeTags.IS_OVERWORLD, inkcap_deepslate);

        ResourceKey<PlacedFeature> portabella = ResourceKey.create(Registries.PLACED_FEATURE, Spelunkery.res("portabella"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.VEGETAL_DECORATION, BiomeTags.IS_OVERWORLD, portabella);

        ResourceKey<PlacedFeature> phosphor_fungus = ResourceKey.create(Registries.PLACED_FEATURE, Spelunkery.res("phosphor_fungus"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.VEGETAL_DECORATION, BiomeTags.IS_OVERWORLD, phosphor_fungus);

        ResourceKey<PlacedFeature> mushgloom = ResourceKey.create(Registries.PLACED_FEATURE, Spelunkery.res("mushgloom"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.VEGETAL_DECORATION, BiomeTags.IS_OVERWORLD, mushgloom);

        ResourceKey<PlacedFeature> rare_huge_mushroom = ResourceKey.create(Registries.PLACED_FEATURE, Spelunkery.res("rare_huge_mushroom"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.VEGETAL_DECORATION, BiomeTags.IS_OVERWORLD, rare_huge_mushroom);

        ResourceKey<PlacedFeature> deep_dark_fossil = ResourceKey.create(Registries.PLACED_FEATURE, Spelunkery.res("deep_dark_fossil"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.VEGETAL_DECORATION, ModTags.HAS_SCULK_NOISE, deep_dark_fossil);

        ResourceKey<PlacedFeature> portal_fluid_pool = ResourceKey.create(Registries.PLACED_FEATURE, Spelunkery.res("portal_fluid_pool"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.SURFACE_STRUCTURES, ModTags.HAS_END_NOISE, portal_fluid_pool);

        ResourceKey<PlacedFeature> portal_fluid_spring = ResourceKey.create(Registries.PLACED_FEATURE, Spelunkery.res("portal_fluid_spring"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.SURFACE_STRUCTURES, ModTags.HAS_END_NOISE, portal_fluid_spring);

        ResourceKey<PlacedFeature> obsidian_patch = ResourceKey.create(Registries.PLACED_FEATURE, Spelunkery.res("obsidian_patch"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, ModTags.HAS_END_NOISE, obsidian_patch);

        ResourceKey<PlacedFeature> noise_end = ResourceKey.create(Registries.PLACED_FEATURE, Spelunkery.res("noise_end"));
        SpelunkeryPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, ModTags.HAS_END_NOISE, noise_end);

    }

}
