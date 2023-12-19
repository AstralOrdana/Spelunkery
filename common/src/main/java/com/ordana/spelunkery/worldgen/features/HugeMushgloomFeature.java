package com.ordana.spelunkery.worldgen.features;

import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.ordana.spelunkery.reg.ModBlocks;
import com.ordana.spelunkery.worldgen.feature_configs.HugeConkFungusFeatureConfig;
import com.ordana.spelunkery.worldgen.feature_configs.HugeMushgloomFeatureConfig;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.material.Fluids;

import java.util.Set;
import java.util.function.BiConsumer;

public class HugeMushgloomFeature extends Feature<HugeMushgloomFeatureConfig> {
    public HugeMushgloomFeature(Codec<HugeMushgloomFeatureConfig> codec) {
        super(codec);
    }

    public boolean place(FeaturePlaceContext<HugeMushgloomFeatureConfig> context) {

        WorldGenLevel level = context.level();
        RandomSource random = level.getRandom();
        BlockPos blockPos = context.origin();
        HugeMushgloomFeatureConfig config = context.config();
        Set<BlockPos> set = Sets.newHashSet();
        BiConsumer<BlockPos, BlockState> blockSetter = (blockPosx, blockState) -> {
            set.add(blockPosx.immutable());
            level.setBlock(blockPosx, blockState, 19);
        };

        BiConsumer<BlockPos, BlockState> blockSetter2 = (blockPosx, blockState) -> {
            set.add(blockPosx.immutable());
            level.setBlock(blockPosx, blockState.setValue(PipeBlock.NORTH, false).setValue(PipeBlock.SOUTH, false).setValue(PipeBlock.EAST, false).setValue(PipeBlock.WEST, false), 19);
        };

        int radius = random.nextInt(2) + (config.height);


        this.placeBranches(level, blockSetter, random, config, blockPos);

        //this.placeLeavesRow(level, blockSetter, random, config, blockPos, radius, 0, large);

        return true;
    }

    protected void placeCap(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> blockSetter, RandomSource random, HugeMushgloomFeatureConfig config, BlockPos pos) {

        for(Direction dir : Direction.values()){
            if (dir != Direction.DOWN) blockSetter.accept(pos.relative(dir), config.foliageProvider.getState(random, pos));
        }
    }


    protected void placeBranches(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> blockSetter, RandomSource random, HugeMushgloomFeatureConfig config, BlockPos pos) {
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos().set(pos);
        var state = config.stemProvider.getState(random, pos);

        Direction dirf = Direction.Plane.HORIZONTAL.getRandomDirection(random);
        Direction dir2 = dirf.getClockWise();
        Direction dir3 = dir2.getClockWise();

        for(Direction dir : Direction.Plane.HORIZONTAL.shuffledCopy(random)) {
            blockSetter.accept(mutableBlockPos.relative(dir).relative(Direction.UP), state);
            if (random.nextBoolean()) {
                blockSetter.accept(mutableBlockPos, state);
                mutableBlockPos.move(Direction.UP);
                blockSetter.accept(mutableBlockPos, state);
            }
            for (int i = 0; i < config.height; ++i) {
                blockSetter.accept(mutableBlockPos, state);
                mutableBlockPos.move(Direction.UP).move(dir);
                blockSetter.accept(mutableBlockPos, state);
                mutableBlockPos.move(Direction.UP);
                blockSetter.accept(mutableBlockPos, state);

                //mutableBlockPos.move(Direction.UP).move(dir);
                //blockSetter.accept(mutableBlockPos, state);
            }
            if (random.nextBoolean()) {
                mutableBlockPos.move(Direction.UP);
                blockSetter.accept(mutableBlockPos, state);
            }
            this.placeCap(level, blockSetter, random, config, mutableBlockPos);
            mutableBlockPos.set(pos);
            if (random.nextFloat() >= 0.5) break;
        }

        /*
        blockSetter.accept(mutableBlockPos.relative(dir), state);
        mutableBlockPos.move(Direction.UP);
        blockSetter.accept(mutableBlockPos, state);
        mutableBlockPos.move(Direction.UP).move(dir);
        blockSetter.accept(mutableBlockPos, state);
        mutableBlockPos.move(Direction.UP);
        blockSetter.accept(mutableBlockPos, state);

        for(Direction dfir : Direction.allShuffled(random)) {
            if (dir != Direction.DOWN && dir != Direction.UP) {
                blockSetter.accept(mutableBlockPos.relative(dir), config.foliageProvider.getState(random, pos));
            }
        }
         */
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