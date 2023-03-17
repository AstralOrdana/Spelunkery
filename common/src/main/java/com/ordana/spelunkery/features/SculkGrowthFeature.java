package com.ordana.spelunkery.features;

import com.mojang.serialization.Codec;
import com.ordana.spelunkery.features.util.FastNoiseLite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.SculkShriekerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class SculkGrowthFeature extends Feature<NoneFeatureConfiguration> {
    public SculkGrowthFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    FastNoiseLite sculkShapeNoise;
    private WorldGenLevel worldGenLevel;
    private RandomSource random;

    private static final float DECORATION_THRESHOLD = 0.03F;
    private static final float SENSOR_THRESHOLD = 0.7F;
    private static final float CATALYST_THRESHOLD = 0.8F;
    private static final float SHRIEKER_THRESHOLD = 1.0F;
    private static final int MAX_CATALYSTS = 1;

    private int catalystCount;

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContext) {
        this.sculkShapeNoise = new FastNoiseLite(0);
        this.sculkShapeNoise.SetFrequency(1/12F);
        this.sculkShapeNoise.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2S);
        this.worldGenLevel = featurePlaceContext.level();
        BlockPos blockPos = featurePlaceContext.origin();
        this.random = featurePlaceContext.random();
        this.catalystCount = 0;

        for(int x = -16; x < 16; x++){
            for(int y = -16; y < 16; y++){
                for(int z = -16; z < 16; z++){
                    float dist = Mth.sqrt((float) blockPos.offset(x, y, z).distSqr(blockPos)) / 16F;
                    BlockPos blockPos1 = blockPos.offset(x, y, z);
                    int worldX = blockPos1.getX();
                    int worldY = blockPos1.getY();
                    int worldZ = blockPos1.getZ();

                    dist = 1 - dist;
                    dist = Mth.lerp(0.5F, dist, dist * dist);
                    dist = 1 - dist;

                    float noise = sculkShapeNoise.GetNoise(worldX, worldZ);
                    noise = Math.abs(noise);
                    noise = 1 - noise;
                    noise = noise * noise;
                    noise = 1 - noise;

                    dist += noise * 0.4F;
                    dist -= 0.1F;

                    if(isBlockExposedToAir(blockPos.offset(x, y, z))){
                        if(dist < 0.825){
                            setSculkBlock(blockPos.offset(x, y, z));
                        } else if(dist < 1){
                            coverWithSculkVeins(blockPos.offset(x, y, z));
                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean isBlockExposedToAir(BlockPos blockPos){
        if(worldGenLevel.getBlockState(blockPos).isCollisionShapeFullBlock(worldGenLevel, blockPos)){
            if(exposedToAir(blockPos)){
                return true;
            }
        }
        return false;

    }

    private boolean exposedToAir(BlockPos blockPos){
        for(Direction direction : Direction.values()){
            BlockState blockState = this.worldGenLevel.getBlockState(blockPos.relative(direction));
            if(blockState.is(Blocks.AIR) || blockState.is(Blocks.SCULK_VEIN)){
                return true;
            }
        }
        return false;
    }

    private boolean exposedToAirNotSculk(BlockPos blockPos){
        for(Direction direction : Direction.values()){
            BlockState blockState = this.worldGenLevel.getBlockState(blockPos.relative(direction));
            if(blockState.is(Blocks.AIR)){
                return true;
            }
        }
        return false;
    }

    private void setSculkBlock(BlockPos blockPos){
        BlockState blockState = worldGenLevel.getBlockState(blockPos);
        if(blockState.is(BlockTags.SCULK_REPLACEABLE_WORLD_GEN)) {
            if(exposedToAirNotSculk(blockPos)){
                worldGenLevel.setBlock(blockPos, Blocks.SCULK.defaultBlockState(), 2);
                placeDecoration(blockPos.offset(0, 1, 0));
            }
        } else {
            if(!blockIsSculkAlready(blockPos)){
                coverWithSculkVeins(blockPos);
            }
        }
    }

    private boolean belowAirOrVeins(BlockPos blockPos){
        BlockState blockState = this.worldGenLevel.getBlockState(blockPos.relative(Direction.UP));
        if(!blockState.is(Blocks.AIR) && !blockState.is(Blocks.SCULK_VEIN)){
            return false;
        }
        return true;
    }

    private void placeDecoration(BlockPos blockPos){
        if(belowAirOrVeins(blockPos)){
            if(random.nextFloat() < DECORATION_THRESHOLD){
                BlockState blockState;
                if(random.nextFloat() < SENSOR_THRESHOLD){
                    blockState = Blocks.SCULK_SENSOR.defaultBlockState();
                } else if(random.nextFloat() < CATALYST_THRESHOLD && this.catalystCount < MAX_CATALYSTS){
                    this.catalystCount++;
                    blockState = Blocks.SCULK_CATALYST.defaultBlockState();
                } else {
                    blockState = Blocks.SCULK_SHRIEKER.defaultBlockState().setValue(SculkShriekerBlock.CAN_SUMMON, true);
                }
                this.worldGenLevel.setBlock(blockPos, blockState, 2);
            }
        }
    }

    private boolean blockIsSculkAlready(BlockPos blockPos){
        BlockState blockState = worldGenLevel.getBlockState(blockPos);
        return blockState.is(Blocks.SCULK_VEIN) || blockState.is(Blocks.SCULK);
    }

    private void coverWithSculkVeins(BlockPos blockPos){
        BlockState blockToCoverState = this.worldGenLevel.getBlockState(blockPos);
        if(!blockToCoverState.is(Blocks.SCULK) && !blockToCoverState.is(Blocks.SCULK_CATALYST)){
            for(Direction direction : Direction.values()){
                BlockState blockState = worldGenLevel.getBlockState(blockPos.relative(direction));
                if(blockState.is(Blocks.AIR) || blockState.is(Blocks.SCULK_VEIN)){
                    MultifaceBlock multifaceBlock = (MultifaceBlock) Blocks.SCULK_VEIN;
                    BlockState veinBlockState = multifaceBlock.getStateForPlacement(worldGenLevel.getBlockState(blockPos.relative(direction)), worldGenLevel, blockPos.relative(direction), direction.getOpposite());
                    if(veinBlockState != null){
                        worldGenLevel.setBlock(blockPos.relative(direction), veinBlockState, 2);
                    }
                }
            }
        }
    }
}
