package com.ordana.spelunkery.features.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;

public class StonePattern {
    private FastNoiseLite noise;
    private final int noiseSeed;
    private final float primaryMin;
    private final float primaryMax;
    private final float secondaryMin;
    private final float secondaryMax;
    private final float verticalScale;


    public static final Codec<StonePattern> CODEC = RecordCodecBuilder.create(instance -> instance.group(

            Codec.INT.fieldOf("noise_seed").forGetter((stonePattern)
                    -> stonePattern.noiseSeed),
            Codec.floatRange(0.0F, 1.0F).fieldOf("primary_min").orElse(0.0f).forGetter((stonePattern)
                    -> stonePattern.primaryMin),
            Codec.floatRange(0.0F, 1.0F).fieldOf("primary_max").orElse(0.0f).forGetter((stonePattern)
                    -> stonePattern.primaryMax),
            Codec.floatRange(0.0F, 1.0F).fieldOf("secondary_min").orElse(0.0f).forGetter((stonePattern)
                    -> stonePattern.secondaryMin),
            Codec.floatRange(0.0F, 1.0F).fieldOf("secondary_max").orElse(0.0f).forGetter((stonePattern)
                    -> stonePattern.secondaryMax),
            Codec.floatRange(0.0F, 1.0F).fieldOf("vertical_scale").orElse(0.0f).forGetter((stonePattern)
                    -> stonePattern.verticalScale)
    ).apply(instance, StonePattern::new));

    private StonePattern(int noiseSeed, float primaryMin, float primaryMax, float secondaryMin, float secondaryMax, float verticalScale){
        FastNoiseLite noise = new FastNoiseLite(noiseSeed);
        noise.SetDomainWarpType(FastNoiseLite.DomainWarpType.OpenSimplex2);
        noise.SetRotationType3D(FastNoiseLite.RotationType3D.ImproveXZPlanes);
        noise.SetFrequency(0.03F);
        noise.SetDomainWarpAmp(1.5F);
        noise.SetFractalOctaves(1);
        this.noise = noise;
        this.noiseSeed = noiseSeed;
        this.primaryMin = primaryMin;
        this.primaryMax = primaryMax;
        this.secondaryMin = secondaryMin;
        this.secondaryMax = secondaryMax;
        this.verticalScale = verticalScale;
    }

/*
    public static StonePattern oceanBasalt() {
        FastNoiseLite noise = new FastNoiseLite(76);
        noise.SetDomainWarpType(FastNoiseLite.DomainWarpType.OpenSimplex2);
        noise.SetRotationType3D(FastNoiseLite.RotationType3D.ImproveXZPlanes);
        noise.SetFrequency(0.03F);
        noise.SetDomainWarpAmp(1.5F);
        noise.SetFractalOctaves(1);
        return new StonePattern(noise, -5.0F, 0.5F, -0.2F, 0.2F, 0.4F);
    }

    public static StonePattern dirtStripes() {
        FastNoiseLite noise = new FastNoiseLite(77);
        noise.SetDomainWarpType(FastNoiseLite.DomainWarpType.OpenSimplex2);
        noise.SetRotationType3D(FastNoiseLite.RotationType3D.ImproveXZPlanes);
        noise.SetFrequency(0.03F);
        noise.SetDomainWarpAmp(1.5F);
        noise.SetFractalOctaves(1);
        return new StonePattern(noise, -0.3F, 0F, 0F, 0.3F, 0.3F);
    }

    public static StonePattern sedimentaryStone() {
        FastNoiseLite noise = new FastNoiseLite(88);
        noise.SetDomainWarpType(FastNoiseLite.DomainWarpType.OpenSimplex2);
        noise.SetRotationType3D(FastNoiseLite.RotationType3D.ImproveXZPlanes);
        noise.SetFrequency(0.03F);
        noise.SetDomainWarpAmp(1.5F);
        noise.SetFractalOctaves(1);
        return new StonePattern(noise, -0.4F, 0F, 0F, 0.4F, 0.4F);
    }

    public static StonePattern dottedClay() {
        FastNoiseLite noise = new FastNoiseLite(99);
        noise.SetDomainWarpType(FastNoiseLite.DomainWarpType.OpenSimplex2);
        noise.SetRotationType3D(FastNoiseLite.RotationType3D.ImproveXZPlanes);
        noise.SetFrequency(0.03F);
        noise.SetDomainWarpAmp(1.5F);
        noise.SetFractalOctaves(1);
        return new StonePattern(noise, -0.3F, 0.3F, -0.1F, 0.1F, 1F);
    }

    public static StonePattern desert_terrain() {
        FastNoiseLite noise = new FastNoiseLite(69);
        noise.SetDomainWarpType(FastNoiseLite.DomainWarpType.OpenSimplex2);
        noise.SetRotationType3D(FastNoiseLite.RotationType3D.ImproveXZPlanes);
        noise.SetFrequency(0.05F);
        noise.SetDomainWarpAmp(1.5F);
        noise.SetFractalOctaves(1);
        return new StonePattern(noise, -0.85F, 0.85F, -0.2F, 0.2F, 0.7F);
    }

    public static StonePattern rock_salt() {
        FastNoiseLite noise = new FastNoiseLite(80);
        noise.SetDomainWarpType(FastNoiseLite.DomainWarpType.OpenSimplex2);
        noise.SetRotationType3D(FastNoiseLite.RotationType3D.ImproveXZPlanes);
        noise.SetFrequency(0.03F);
        noise.SetDomainWarpAmp(1.5F);
        noise.SetFractalOctaves(1);
        return new StonePattern(noise, -0.3F, 0.3F, -0.5F, -0.4F, 0.7F);
    }

     */

    public void setSeed(int seed){
        this.noise.SetSeed(seed);
    }

    public boolean shouldPlacePrimaryStone(BlockPos blockPos){
        float x = blockPos.getX();
        float y = blockPos.getY();
        float z = blockPos.getZ();
        y /= verticalScale;
        float value = this.noise.GetNoise(x, y, z);
        return value < primaryMax && value > primaryMin;
    }

    public boolean shouldPlaceSecondaryStone(BlockPos blockPos){
        float x = blockPos.getX();
        float y = blockPos.getY();
        float z = blockPos.getZ();
        y /= verticalScale;
        float value = this.noise.GetNoise(x, y, z);
        return value < secondaryMax && value > secondaryMin;
    }
}
