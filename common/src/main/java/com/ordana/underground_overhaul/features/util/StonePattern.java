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

    public static StonePattern oceanBasalt() {
        FastNoiseLite noise = new FastNoiseLite(76);
        noise.SetDomainWarpType(FastNoiseLite.DomainWarpType.OpenSimplex2);
        noise.SetRotationType3D(FastNoiseLite.RotationType3D.ImproveXZPlanes);
        noise.SetFrequency(0.03F);
        noise.SetDomainWarpAmp(1.5F);
        noise.SetFractalOctaves(1);
        return new StonePattern(noise, -5.F, 0.5F, -0.2F, 0.2F, 0.4F);
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
