package com.ordana.spelunkery.blocks.rock_salt;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SandBlock;
import net.minecraft.world.level.block.state.BlockState;

public class SaltBlockBlock extends SandBlock implements RockSalt {
    public SaltBlockBlock(int i, Properties properties) {
        super(i, properties);
    }

    @Override
    public void stepOn(Level world, BlockPos pos, BlockState state, Entity entity) {
        onEntityStepOn(state, entity);
        super.stepOn(world, pos, state, entity);
    }
}
