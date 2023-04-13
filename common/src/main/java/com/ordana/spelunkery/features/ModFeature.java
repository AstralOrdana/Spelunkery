package com.ordana.spelunkery.features;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class ModFeature extends Feature {
    public ModFeature(Codec codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext context) {
        return false;
    }

    private static <C extends FeatureConfiguration, F extends Feature<C>> F register(String key, F value) {
        return Registry.register(Registry.FEATURE, key, value);
    }

    public static final HugeConkFungusFeature HUGE_CONK;

    static {
        HUGE_CONK = register("huge_conk", new HugeConkFungusFeature(HugeConkFungusFeatureConfig.CODEC));
    }
}
