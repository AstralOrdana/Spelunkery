package com.ordana.underground_overhaul.blocks.fungi;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ModMushroomBlock extends BushBlock {
    protected static final VoxelShape SHAPE = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 6.0D, 11.0D);

    public ModMushroomBlock(Properties properties) {
        super(properties);
    }

    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (random.nextInt(25) == 0) {
            int i = 5;
            boolean j = true;

            for (BlockPos blockPos : BlockPos.betweenClosed(pos.offset(-4, -1, -4), pos.offset(4, 1, 4))) {
                if (level.getBlockState(blockPos).is(this)) {
                    --i;
                    if (i <= 0) {
                        return;
                    }
                }
            }

            BlockPos blockPos2 = pos.offset(random.nextInt(3) - 1, random.nextInt(2) - random.nextInt(2), random.nextInt(3) - 1);

            for(int k = 0; k < 4; ++k) {
                if (level.isEmptyBlock(blockPos2) && state.canSurvive(level, blockPos2)) {
                    pos = blockPos2;
                }

                blockPos2 = pos.offset(random.nextInt(3) - 1, random.nextInt(2) - random.nextInt(2), random.nextInt(3) - 1);
            }

            if (level.isEmptyBlock(blockPos2) && state.canSurvive(level, blockPos2)) {
                level.setBlock(blockPos2, state, 2);
            }
        }

    }

    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.isSolidRender(level, pos);
    }

    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos blockPos = pos.below();
        BlockState blockState = level.getBlockState(blockPos);
        if (blockState.is(BlockTags.MUSHROOM_GROW_BLOCK)) {
            return true;
        } else {
            return level.getRawBrightness(pos, 0) < 13 && this.mayPlaceOn(blockState, level, blockPos);
        }
    }
}
