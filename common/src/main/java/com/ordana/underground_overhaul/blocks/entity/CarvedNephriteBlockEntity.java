package com.ordana.underground_overhaul.blocks.entity;

import com.ordana.underground_overhaul.blocks.nephrite.CarvedNephriteBlock;
import com.ordana.underground_overhaul.reg.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CarvedNephriteBlockEntity extends BlockEntity {
    public CarvedNephriteBlockEntity(BlockPos pos, BlockState state) {
        super(ModEntities.NEPHRITE_TILE.get(), pos, state);
    }

    private int charge = 0;

    public int getCharge(BlockPos targetPos, Level level) {
        if (level.getBlockEntity(targetPos) instanceof CarvedNephriteBlockEntity targetTile) {
            return targetTile.charge;
        }
        return 0;
    }

    public int setCharge(int newCharge) {
        return charge = newCharge;
    }

    public static void runFlow(Level level, BlockPos pos, BlockState state, CarvedNephriteBlockEntity selfTile) {

        int selfCharge = selfTile.getCharge(pos, level);
        int numOfNeighbors = 1;

        //priority downward flow
        int i = 0;
        BlockPos bottomBlock = null;
        while (bottomBlock == null) {
            if (!(level.getBlockEntity(pos.below(i + 1)) instanceof CarvedNephriteBlockEntity)) {
                bottomBlock = pos.below(i);
            }
            i++;
        }
        if (level.getBlockEntity(bottomBlock) instanceof CarvedNephriteBlockEntity belowTile) {
            int belowCharge = belowTile.getCharge(bottomBlock, level);
            if (belowCharge == 100) return;
            if (selfCharge + belowCharge <= 100) {
                belowTile.setCharge(belowCharge + selfCharge);
                selfTile.setCharge(0);
            } else {
                selfTile.setCharge(selfCharge - (100 - belowCharge));
                belowTile.setCharge(100);
            }
        }


        /*
        selfCharge = selfTile.getCharge(pos, level);
        int sumOfCharge = selfCharge;

        //horizontal spread flow
        for (var direction : Direction.Plane.HORIZONTAL) {
            BlockPos neighborPos = pos.relative(direction);
            if (level.getBlockEntity(neighborPos) instanceof CarvedNephriteBlockEntity neighborTile) {
                numOfNeighbors++;
                sumOfCharge += selfTile.getCharge(neighborPos, level);
            }
        }

        selfTile.setCharge((sumOfCharge / numOfNeighbors) + (sumOfCharge % numOfNeighbors));

        for (var direction : Direction.Plane.HORIZONTAL) {
            BlockPos neighborPos = pos.relative(direction);
            if (level.getBlockEntity(neighborPos) instanceof CarvedNephriteBlockEntity neighborTile) {
                neighborTile.setCharge(sumOfCharge / numOfNeighbors);
            }
        }


        selfCharge = selfTile.getCharge(pos, level);

        //remainder downward flow
        if (level.getBlockEntity(bottomBlock) instanceof CarvedNephriteBlockEntity belowTile) {
            int belowCharge = selfTile.getCharge(bottomBlock, level);
            if (belowCharge == 100) return;
            if (selfCharge + belowCharge <= 100) {
                belowTile.setCharge(belowCharge + selfCharge);
                selfTile.setCharge(0);
            } else {
                selfTile.setCharge(selfCharge - (100 - belowCharge));
                belowTile.setCharge(100);
            }
        }

         */
    }


    public static void tickBlock(Level level, BlockPos pos, BlockState state, CarvedNephriteBlockEntity selfTile) {

        if (!level.isClientSide) {
            runFlow(level, pos, state, selfTile);
        }
    }
}
