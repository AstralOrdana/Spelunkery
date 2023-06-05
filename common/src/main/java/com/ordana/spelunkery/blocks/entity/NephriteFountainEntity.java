package com.ordana.spelunkery.blocks.entity;

import com.ordana.spelunkery.blocks.nephrite.NephriteFountainBlock;
import com.ordana.spelunkery.reg.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class NephriteFountainEntity extends BlockEntity {
    public NephriteFountainEntity(BlockPos pos, BlockState state) {
        super(ModEntities.NEPHRITE_SPOUT.get(), pos, state);
    }

    private int tickCounter = 0;

    public int setTickCounter(int tick) {
        return tickCounter = tick;
    }

    public static void pourXp(Level level, BlockPos pos, BlockState state, NephriteFountainEntity selfTile) {
        Direction dir = state.getValue(NephriteFountainBlock.FACING);
        if (level.getBlockEntity(pos.relative(dir.getOpposite())) instanceof CarvedNephriteBlockEntity neighborTile && neighborTile.getCharge() > 0 && level instanceof ServerLevel) {

            var pourVector = new Vec3((double)pos.getX() + 0.5D, (double)pos.getY() + 0.125D, (double)pos.getZ() + 0.5D);

            neighborTile.setCharge(neighborTile.getCharge() - 1);
            ExperienceOrb.award((ServerLevel) level, pourVector, 1);
        }
    }

    public static void tickBlock(Level level, BlockPos pos, BlockState state, NephriteFountainEntity selfTile) {
        selfTile.tickCounter++;
        if (!level.isClientSide && selfTile.tickCounter >= 1 && state.getValue(NephriteFountainBlock.POWERED)) {
            pourXp(level, pos, state, selfTile);
            selfTile.setTickCounter(0);
        }
    }
}
