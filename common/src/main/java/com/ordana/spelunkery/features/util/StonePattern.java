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
            Codec.floatRange(-1.0F, 1.0F).fieldOf("primary_min").orElse(0.0f).forGetter((stonePattern)
                    -> stonePattern.primaryMin),
            Codec.floatRange(-1.0F, 1.0F).fieldOf("primary_max").orElse(0.0f).forGetter((stonePattern)
                    -> stonePattern.primaryMax),
            Codec.floatRange(-1.0F, 1.0F).fieldOf("secondary_min").orElse(0.0f).forGetter((stonePattern)
                    -> stonePattern.secondaryMin),
            Codec.floatRange(-1.0F, 1.0F).fieldOf("secondary_max").orElse(0.0f).forGetter((stonePattern)
                    -> stonePattern.secondaryMax),
            Codec.floatRange(0.0F, 1.0F).fieldOf("vertical_scale").orElse(0.0f).forGetter((stonePattern)
                    -> stonePattern.verticalScale)
    ).apply(instance, StonePattern::new));

    private StonePattern(int noiseSeed, float primaryMin, float primaryMax, float secondaryMin, float secondaryMax, float verticalScale) {
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
