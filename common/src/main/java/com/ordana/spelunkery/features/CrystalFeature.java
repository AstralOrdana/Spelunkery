package com.ordana.spelunkery.features;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class CrystalFeature extends Feature<CrystalFeatureConfig> {
    public CrystalFeature(Codec<CrystalFeatureConfig> codec) {
        super(codec);
    }

    public boolean place(FeaturePlaceContext<CrystalFeatureConfig> context) {

        WorldGenLevel worldGenLevel = context.level();
        BlockPos blockPos = context.origin();
        CrystalFeatureConfig crystalFeatureConfig = context.config();
        if (worldGenLevel.isEmptyBlock(blockPos)) {
            Direction[] var4 = Direction.values();

            for (Direction direction : var4) {
                if (worldGenLevel.getBlockState(blockPos.relative(direction)).is(crystalFeatureConfig.canBePlacedOn)) {
                    worldGenLevel.setBlock(blockPos, crystalFeatureConfig.placeBlock.defaultBlockState().setValue(BlockStateProperties.FACING, direction.getOpposite()), 2);
                    return true;
                }

            }

        }
        return false;
    }
}
