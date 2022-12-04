package com.ordana.underhaul;

import com.ordana.underhaul.feature.NoiseBasedStoneFeature;
import com.ordana.underhaul.util.StoneEntry;
import com.ordana.underhaul.util.StonePattern;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;
import java.util.List;

public class Underhaul implements ModInitializer {

    public static final String MOD_ID = "underhaul";
    public static final Feature<DefaultFeatureConfig> TEST_NOISE_STONE = NoiseBasedStoneFeature.featureWithDeepslateUnfiltered(DefaultFeatureConfig.CODEC,
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
            0.25F
    );
    public static final Feature<DefaultFeatureConfig> TEST_LUSH_NOISE_STONE = NoiseBasedStoneFeature.featureWithDeepslateFiltered(DefaultFeatureConfig.CODEC,
            List.of(
                    new StoneEntry(Blocks.CLAY, Blocks.CALCITE, StonePattern.dottedClay())
            ),
            List.of(
                    new StoneEntry(Blocks.TUFF, Blocks.CLAY, StonePattern.dottedClay())
            ),
            List.of(BiomeKeys.LUSH_CAVES),
            0F
    );
    public static final PlacedFeature PLACED_TEST_NOISE_STONE = TEST_NOISE_STONE.withPlacement(countModifiers(10));
    public static final ConfiguredFeature PLACED_TEST_LUSH_NOISE_STONE = TEST_LUSH_NOISE_STONE.configured(FeatureConfig.DEFAULT).placed(SquarePlacementModifier.of());

    @Override
    public void onInitialize() {
        ModItems.registerItems();
        ModBlocks.registerBlocks();
        ModFeatures.registerFeatures();
        registerFeatures();
    }

    public void registerFeatures(){
        Registry.register(Registry.FEATURE, new Identifier(MOD_ID, "noise_based_stone_test"), TEST_NOISE_STONE);
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new Identifier(MOD_ID, "noise_based_stone_test"), TEST_NOISE_STONE.configured(FeatureConfig.DEFAULT));
        Registry.register(BuiltinRegistries.PLACED_FEATURE, new Identifier(MOD_ID, "noise_based_stone_test"), PLACED_TEST_NOISE_STONE);

        Registry.register(Registry.FEATURE, new Identifier(MOD_ID, "test_lush_noise_stone"), TEST_LUSH_NOISE_STONE);
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new Identifier(MOD_ID, "test_lush_noise_stone"), TEST_LUSH_NOISE_STONE.configured(FeatureConfig.DEFAULT));
        Registry.register(BuiltinRegistries.PLACED_FEATURE, new Identifier(MOD_ID, "test_lush_noise_stone"), PLACED_TEST_LUSH_NOISE_STONE);
    }
}
