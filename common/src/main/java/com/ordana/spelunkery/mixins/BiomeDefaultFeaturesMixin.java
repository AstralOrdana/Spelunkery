package com.ordana.spelunkery.mixins;

import com.ordana.spelunkery.Spelunkery;
import com.ordana.spelunkery.reg.ModFeatures;
import net.mehvahdjukaar.moonlight.api.misc.RegSupplier;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.placement.CavePlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.CanyonCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BiomeDefaultFeatures.class)
public class BiomeDefaultFeaturesMixin {

    /*
    @Inject(method = "addDefaultCarversAndLakes", at = @At("HEAD"))
    private static void overwriteCarvers(BiomeGenerationSettings.Builder builder, CallbackInfo ci) {
        var crevice = BuiltinRegistries.CONFIGURED_CARVER.getHolderOrThrow(ResourceKey.create(Registry.CONFIGURED_CARVER_REGISTRY, Spelunkery.res("crevice")));
        builder.addCarver(GenerationStep.Carving.AIR, crevice);
    }
     */

    @Inject(method = "addDefaultUndergroundVariety", at = @At("HEAD"), cancellable = true)
    private static void overwriteUndergroundVariety(BiomeGenerationSettings.Builder builder, CallbackInfo ci) {
        builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, CavePlacements.GLOW_LICHEN);
        ci.cancel();
    }

    @Inject(method = "addLushCavesSpecialOres", at = @At("HEAD"), cancellable = true)
    private static void overwriteLushCavesClay(BiomeGenerationSettings.Builder builder, CallbackInfo ci) {
        ci.cancel();
    }
}
