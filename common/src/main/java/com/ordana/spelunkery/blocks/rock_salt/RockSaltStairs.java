package com.ordana.spelunkery.blocks.rock_salt;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.Nullable;

public class RockSaltStairs extends StairBlock implements RockSalt {

    public RockSaltStairs(BlockState blockState, Properties properties) {
        super(blockState, properties);
        this.registerDefaultState(this.defaultBlockState().setValue(ILLUMINATED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
        super.createBlockStateDefinition(stateManager);
        stateManager.add(ILLUMINATED);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos neighborPos, boolean isMoving) {
        RockSalt.super.neighborChanged(state, level, pos, block, neighborPos, isMoving);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        RockSalt.super.tick(state, level, pos, random);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context).setValue(ILLUMINATED, this.neighborIsBright(context.getClickedPos(), context.getLevel()));
    }

    @Override
    public void stepOn(Level world, BlockPos pos, BlockState state, Entity entity) {
        onEntityStepOn(state, entity);
        super.stepOn(world, pos, state, entity);
    }

}
