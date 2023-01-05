package com.ordana.spelunkery.features;

import com.mojang.serialization.Codec;
import com.ordana.spelunkery.blocks.fungi.FloorAndSidesMushroomBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.levelgen.feature.*;

public class WallMushroomFeature extends Feature<WallMushroomFeatureConfig> {
    public WallMushroomFeature(Codec<WallMushroomFeatureConfig> codec) {
        super(codec);
    }

    public boolean place(FeaturePlaceContext<WallMushroomFeatureConfig> context) {

        WorldGenLevel worldGenLevel = context.level();
        BlockPos blockPos = context.origin();
        WallMushroomFeatureConfig wallMushroomFeatureConfig = context.config();
        if (worldGenLevel.isEmptyBlock(blockPos)) {
            Direction[] var4 = Direction.values();

            for (Direction direction : var4) {
                if (direction != Direction.UP) {
                    if (direction == Direction.DOWN && wallMushroomFeatureConfig.canPlaceOnFloor && worldGenLevel.getBlockState(blockPos.below()).isFaceSturdy(worldGenLevel, blockPos.below(), Direction.UP)) {
                        worldGenLevel.setBlock(blockPos, wallMushroomFeatureConfig.placeBlock.defaultBlockState().setValue(FloorAndSidesMushroomBlock.FLOOR, true), 2);
                        return true;
                    } else if (direction != Direction.DOWN && VineBlock.isAcceptableNeighbour(worldGenLevel, blockPos.relative(direction), direction) && worldGenLevel.getBlockState(blockPos.relative(direction)).is(wallMushroomFeatureConfig.canBePlacedOn)) {
                        worldGenLevel.setBlock(blockPos, wallMushroomFeatureConfig.placeBlock.defaultBlockState().setValue(FloorAndSidesMushroomBlock.FACING, direction.getOpposite()).setValue(FloorAndSidesMushroomBlock.FLOOR, false), 2);
                        return true;
                    }
                }
            }

        }
        return false;
    }
}
