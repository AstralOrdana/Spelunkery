package com.ordana.underhaul.mixins;

import com.ordana.underhaul.Underhaul;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.feature.UndergroundPlacedFeatures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DefaultBiomeFeatures.class)

public class MixinBiomeDefaultFeatures {
    @Inject(method = "addMineables", at = @At("HEAD"), cancellable = true)
    private static void overwriteUndergroundVariety(GenerationSettings.Builder builder, CallbackInfo ci){
        builder.feature(GenerationStep.Feature.VEGETAL_DECORATION, UndergroundPlacedFeatures.GLOW_LICHEN);
        builder.feature(GenerationStep.Feature.RAW_GENERATION, Underhaul.PLACED_TEST_NOISE_STONE);
        builder.feature(GenerationStep.Feature.RAW_GENERATION, Underhaul.PLACED_TEST_LUSH_NOISE_STONE);
        ci.cancel();
    }

    @Inject(method = "addLushCavesDecoration", at = @At("HEAD"), cancellable = true)
    private static void overwriteLushCavesClay(GenerationSettings.Builder builder, CallbackInfo ci){
        ci.cancel();
    }
}
