package com.ordana.spelunkery.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import com.ordana.spelunkery.SpelunkeryClient;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;
import java.util.stream.Stream;

@Mixin(TextureAtlas.class)
@Debug(export = true)
public abstract class TextureAtlasMixin {
    @Inject(method = "prepareToStitch", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;popPush(Ljava/lang/String;)V", ordinal = 0, shift = At.Shift.AFTER))
    private void addTextures(final ResourceManager resourceManager, final Stream<ResourceLocation> spriteNames, final ProfilerFiller profiler, int mipLevel, final CallbackInfoReturnable<TextureAtlas.Preparations> callback, @Local final Set<ResourceLocation> sprites) {
        if (location() == TextureAtlas.LOCATION_BLOCKS) {
            SpelunkeryClient.registerTextures(sprites);
        }
    }

    @Shadow public abstract ResourceLocation location();
}
