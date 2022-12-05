package com.ordana.underground_overhaul;

import com.ordana.underground_overhaul.features.util.NoiseBasedStoneFeature;
import com.ordana.underground_overhaul.features.util.StoneEntry;
import com.ordana.underground_overhaul.features.util.StonePattern;
import com.ordana.underground_overhaul.reg.ModFeatures;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class UndergroundOverhaul {

    public static final String MOD_ID = "underground_overhaul";
    public static final Logger LOGGER = LogManager.getLogger();

    private static final String NAME = "underground_overhaul";

    public static ResourceLocation res(String name) {
        return new ResourceLocation(MOD_ID, name);
    }

    public static void commonInit() {
        ModFeatures.init();
    }

}