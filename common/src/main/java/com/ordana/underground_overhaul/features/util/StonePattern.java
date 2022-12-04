package com.ordana.underground_overhaul.features.util;

import net.minecraft.core.BlockPos;
import org.lwjgl.system.CallbackI;

public class StonePattern {
    private FastNoiseLite noise;
    private final float primaryMin;
    private final float primaryMax;
    private final float secondaryMin;
    private final float secondaryMax;
    private final float verticalScale;

    private StonePattern(FastNoiseLite noise, float primaryMin, float primaryMax, float secondaryMin, float secondaryMax, float verticalScale){
        this.noise = noise;
        this.primaryMin = primaryMin;
        this.primaryMax = primaryMax;
        this.secondaryMin = secondaryMin;
        this.secondaryMax = secondaryMax;
        this.verticalScale = verticalScale;
    }

    public static StonePattern sedimentaryStone(){
        FastNoiseLite noise = new FastNoiseLite(88);
        noise.SetDomainWarpType(FastNoiseLite.DomainWarpType.OpenSimplex2);
        noise.SetRotationType3D(FastNoiseLite.RotationType3D.ImproveXZPlanes);
        noise.SetFrequency(0.03F);
        noise.SetDomainWarpAmp(1.5F);
        noise.SetFractalOctaves(1);
        return new StonePattern(noise, -0.4F, 0F, 0F, 0.4F, 0.4F);
    }
    public static StonePattern dottedClay(){
        FastNoiseLite noise = new FastNoiseLite(99);
        noise.SetDomainWarpType(FastNoiseLite.DomainWarpType.OpenSimplex2);
        noise.SetRotationType3D(FastNoiseLite.RotationType3D.ImproveXZPlanes);
        noise.SetFrequency(0.03F);
        noise.SetDomainWarpAmp(1.5F);
        noise.SetFractalOctaves(1);
        return new StonePattern(noise, -0.3F, 0.3F, -0.1F, 0.1F, 1F);
    }

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
