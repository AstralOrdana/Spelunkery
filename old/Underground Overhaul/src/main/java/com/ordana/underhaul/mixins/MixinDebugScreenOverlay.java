package com.ordana.underhaul.mixins;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.source.BiomeCoords;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.gen.chunk.ChunkGenerator;

import static net.minecraft.client.gui.DrawableHelper.fill;

@Mixin(DebugHud.class)
public abstract class MixinDebugScreenOverlay {
    @Final private MinecraftClient minecraft;

    @Final private TextRenderer font;

    @Nullable protected abstract ServerWorld getServerLevel();

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void customDebug(MatrixStack poseStack, CallbackInfo ci){
        if (this.minecraft.options.debugTpsEnabled) {
            List<String> list = getAlternateDebugInformation();
            boolean bl = this.minecraft.getServer() != null;

            for(int i = 0; i < list.size(); ++i) {
                String string = (String)list.get(i);
                if (!Strings.isNullOrEmpty(string)) {
                    Objects.requireNonNull(this.font);
                    int j = 9;
                    int k = this.font.getWidth(string);
                    int m = 2 + j * i;
                    fill(poseStack, 1, m - 1, 2 + k + 1, m + j - 1, -1873784752);
                    this.font.draw(poseStack, string, 2.0F, (float)m, 14737632);
                }
            }
            ci.cancel();
        }
    }

    private List<String> getAlternateDebugInformation(){
        List<String> list = Lists.newArrayList();
        ServerWorld serverLevel = this.getServerLevel();
        if (serverLevel != null) {
            ServerChunkManager serverChunkCache = serverLevel.getChunkManager();
            ChunkGenerator chunkGenerator = serverChunkCache.getChunkGenerator();
            MultiNoiseUtil.MultiNoiseSampler sampler = chunkGenerator.getMultiNoiseSampler();
            BiomeSource biomeSource = chunkGenerator.getBiomeSource();
            BlockPos blockPos = Objects.requireNonNull(this.minecraft.getCameraEntity()).getBlockPos();


            int x = BiomeCoords.fromBlock(blockPos.getX());
            int y = BiomeCoords.fromBlock(blockPos.getY());
            int z = BiomeCoords.fromBlock(blockPos.getZ());
            MultiNoiseUtil.NoiseValuePoint targetPoint = sampler.sample(x, y, z);

            DecimalFormat decimalFormat = new DecimalFormat("0.000");
            DecimalFormat decimalFormat2 = new DecimalFormat("0.00");
            list.add("XYZ: " + decimalFormat2.format(this.minecraft.getCameraEntity().getX()) + " " + decimalFormat2.format(this.minecraft.getCameraEntity().getY()) + " " + decimalFormat2.format(this.minecraft.getCameraEntity().getZ()));
            list.add("");
            list.add("Continentalness: " + decimalFormat.format((double) MultiNoiseUtil.method_38665(targetPoint.continentalnessNoise())));
            list.add("Erosion: " + decimalFormat.format((double) MultiNoiseUtil.method_38665(targetPoint.erosionNoise())));
            list.add("Weirdness: " + decimalFormat.format((double) MultiNoiseUtil.method_38665(targetPoint.weirdnessNoise())));
        }
        return list;
    }
}
