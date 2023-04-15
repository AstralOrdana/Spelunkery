package com.ordana.spelunkery.features;

import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.ordana.spelunkery.reg.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;

import java.util.Set;
import java.util.function.BiConsumer;

public class HugeConkFungusFeature extends Feature<HugeConkFungusFeatureConfig> {
    public HugeConkFungusFeature(Codec<HugeConkFungusFeatureConfig> codec) {
        super(codec);
    }

    public boolean place(FeaturePlaceContext<HugeConkFungusFeatureConfig> context) {

        WorldGenLevel level = context.level();
        RandomSource random = level.getRandom();
        BlockPos blockPos = context.origin();
        HugeConkFungusFeatureConfig config = context.config();
        Set<BlockPos> set = Sets.newHashSet();
        BiConsumer<BlockPos, BlockState> blockSetter = (blockPosx, blockState) -> {
            set.add(blockPosx.immutable());
            level.setBlock(blockPosx, blockState, 19);
        };

        BiConsumer<BlockPos, BlockState> blockSetter2 = (blockPosx, blockState) -> {
            set.add(blockPosx.immutable());
            level.setBlock(blockPosx, blockState.setValue(PipeBlock.NORTH, false).setValue(PipeBlock.SOUTH, false).setValue(PipeBlock.EAST, false).setValue(PipeBlock.WEST, false), 19);
        };

        int radius = random.nextInt(2) + (config.radius);

        int i = 0;
        if (config.largeChance > 0) i = random.nextInt(0, config.largeChance);
        boolean large = i == 1;

        this.placeLeavesRow(level, blockSetter2, random, config, blockPos, radius - 1, 0, large);
        this.placeLeavesRow(level, blockSetter, random, config, blockPos, radius, -1, large);

        //this.placeLeavesRow(level, blockSetter, random, config, blockPos, radius, 0, large);

        return false;
    }

    protected void placeLeavesRow(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> blockSetter, RandomSource random, HugeConkFungusFeatureConfig config, BlockPos pos, int range, int yOffset, boolean large) {
        int i = large ? 1 : 0;
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();

        for(int j = -range; j <= range + i; ++j) {
            for(int k = -range; k <= range + i; ++k) {
                if (!this.shouldSkipLocationSignedOg(random, j, yOffset, k, range, large)) {
                    mutableBlockPos.setWithOffset(pos, j, yOffset, k);
                    tryPlaceLeaf(level, blockSetter, random, config, mutableBlockPos);
                }
            }
        }

    }

    protected static void tryPlaceLeaf(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> blockSetter, RandomSource random, HugeConkFungusFeatureConfig config, BlockPos pos) {
        if (validTreePos(level, pos)) {
            BlockState blockState = config.foliageProvider.getState(random, pos);
            if (blockState.hasProperty(BlockStateProperties.WATERLOGGED)) {
                blockState = blockState.setValue(BlockStateProperties.WATERLOGGED, level.isFluidAtPosition(pos, (fluidState) -> fluidState.isSourceOfType(Fluids.WATER)));
            }

            blockSetter.accept(pos, blockState);
        }

    }

    public static boolean isBlockWater(LevelSimulatedReader level, BlockPos pos) {
        return level.isStateAtPosition(pos, (blockState) -> blockState.is(Blocks.WATER));
    }

    public static boolean isAirOrLeaves(LevelSimulatedReader level, BlockPos pos) {
        return level.isStateAtPosition(pos, (blockState) -> blockState.isAir() || blockState.is(BlockTags.LEAVES) || blockState.is(ModBlocks.CONK_FUNGUS.get()));
    }

    private static boolean isReplaceablePlant(LevelSimulatedReader level, BlockPos pos) {
        return level.isStateAtPosition(pos, (blockState) -> {
            Material material = blockState.getMaterial();
            return material == Material.REPLACEABLE_PLANT || material == Material.REPLACEABLE_WATER_PLANT || material == Material.REPLACEABLE_FIREPROOF_PLANT;
        });
    }

    public static boolean validTreePos(LevelSimulatedReader level, BlockPos pos) {
        return isAirOrLeaves(level, pos) || isReplaceablePlant(level, pos) || isBlockWater(level, pos);
    }

    protected boolean shouldSkipLocation(RandomSource random, int localX, int localY, int localZ, int range, boolean large) {
        {
            return localX == range && localZ == range && range > 0;
        }
    }

    protected boolean shouldSkipLocationSignedOg(RandomSource random, int localX, int localY, int localZ, int range, boolean large) {
        int i;
        int j;
        if (large) {
            i = Math.min(Math.abs(localX), Math.abs(localX - 1));
            j = Math.min(Math.abs(localZ), Math.abs(localZ - 1));
        } else {
            i = Math.abs(localX);
            j = Math.abs(localZ);
        }

        return this.shouldSkipLocation(random, i, localY, j, range, large);
    }
}