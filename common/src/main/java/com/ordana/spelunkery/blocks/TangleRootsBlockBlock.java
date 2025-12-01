package com.ordana.spelunkery.blocks;

import com.mojang.serialization.MapCodec;
import com.ordana.spelunkery.reg.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.NetherVines;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluids;

public class TangleRootsBlockBlock extends DirectionalBlock implements BonemealableBlock {

    public TangleRootsBlockBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends DirectionalBlock> codec() {
        return null;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    protected boolean canGrowInto(BlockState state) {
        return NetherVines.isValidGrowthState(state) || state.getFluidState().is(Fluids.WATER);
    }

    public static int getBlocksToGrowWhenBonemealed(RandomSource random) {
        double d = 1.0;

        int i;
        for(i = 0; random.nextDouble() < d; ++i) {
            d *= 0.826;
        }

        return i;
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return this.canGrowInto(level.getBlockState(pos.below()));
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        BlockPos blockPos = pos.below();
        int i = 1;
        int j = getBlocksToGrowWhenBonemealed(random);

        for(int k = 0; k < j && this.canGrowInto(level.getBlockState(blockPos)); ++k) {
            level.setBlockAndUpdate(blockPos, ModBlocks.TANGLE_ROOTS.get().defaultBlockState().setValue(TangleRootsHeadBlock.AGE, i));
            blockPos = blockPos.below();
            i = Math.min(i + 1, 25);
        }

    }
}