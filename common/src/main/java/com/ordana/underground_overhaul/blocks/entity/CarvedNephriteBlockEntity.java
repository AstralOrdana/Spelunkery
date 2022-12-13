package com.ordana.underground_overhaul.blocks.entity;

import com.ordana.underground_overhaul.blocks.nephrite.CarvedNephriteBlock;
import com.ordana.underground_overhaul.reg.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CarvedNephriteBlockEntity extends BlockEntity {
    public CarvedNephriteBlockEntity(BlockPos pos, BlockState state) {
        super(ModEntities.NEPHRITE_TILE.get(), pos, state);
    }

    private int charge = 0;

    private int tickCounter = 0;

    public int setTickCounter(int tick) {
        return tickCounter = tick;
    }

    private int maxCharge = 100;

    public int getCharge() {
        return charge;
    }

    public int setCharge(int newCharge) {
        return charge = newCharge;
    }

    public static void runFlow(Level level, BlockPos pos, BlockState state, CarvedNephriteBlockEntity selfTile) {

        int selfCharge = selfTile.getCharge();
        int numOfNeighbors = 1;

        //priority downward flow
        int i = 0;
        BlockPos bottomBlock = null;
        while (bottomBlock == null) {
            if (!(level.getBlockEntity(pos.below(i + 1)) instanceof CarvedNephriteBlockEntity) || (level.getBlockEntity(pos.below(i+1)) instanceof CarvedNephriteBlockEntity nephrite && nephrite.getCharge() >= 100)) {
                bottomBlock = pos.below(i);
            }
            i++;
        }
        if (!(bottomBlock.equals(pos)) && level.getBlockEntity(bottomBlock) instanceof CarvedNephriteBlockEntity belowTile) {
            int belowCharge = belowTile.getCharge();
            //if (belowCharge == 100) return;
            if (selfCharge + belowCharge <= 100) {
                belowTile.setCharge(belowCharge + selfCharge);
                selfTile.setCharge(0);
            } else {
                selfTile.setCharge(selfCharge - (100 - belowCharge));
                belowTile.setCharge(100);
            }
        }

        selfCharge = selfTile.getCharge();
        int sumOfCharge = selfCharge;
        List<CarvedNephriteBlockEntity> neighbors = new ArrayList<>();

        //horizontal spread flow
        for (var direction : Direction.Plane.HORIZONTAL) {
            BlockPos neighborPos = pos.relative(direction);
            if (level.getBlockEntity(neighborPos) instanceof CarvedNephriteBlockEntity neighborTile) {
                neighbors.add(neighborTile);
                sumOfCharge += neighborTile.getCharge();
            }
        }

        boolean remainderGiven = false;
        Collections.shuffle(neighbors);
        for (CarvedNephriteBlockEntity block : neighbors) {
            if (!remainderGiven && level.random.nextInt(neighbors.size() + 1) == 0) {
                block.setCharge((sumOfCharge / (neighbors.size() + 1)) + (sumOfCharge % (neighbors.size() + 1)));
                remainderGiven = true;
            } else {
                block.setCharge(sumOfCharge / (neighbors.size() + 1));
            }
        }

        if (remainderGiven) {
            selfTile.setCharge(sumOfCharge / (neighbors.size() + 1));
        } else {
            selfTile.setCharge((sumOfCharge / (neighbors.size() + 1)) + (sumOfCharge % (neighbors.size() + 1)));
        }


        selfCharge = selfTile.getCharge();

        //remainder downward flow
        if (!(bottomBlock.equals(pos)) && level.getBlockEntity(bottomBlock) instanceof CarvedNephriteBlockEntity bottomTile) {
            int belowCharge = bottomTile.getCharge();
            //if (belowCharge == 100) return;
            if (selfCharge + belowCharge <= 100) {
                bottomTile.setCharge(belowCharge + selfCharge);
                selfTile.setCharge(0);
            } else {
                selfTile.setCharge(selfCharge - (100 - belowCharge));
                bottomTile.setCharge(100);
            }
        }
    }

    public void load(CompoundTag tag) {
        if (tag.contains("Charge")) {
            this.setCharge(tag.getInt("Charge"));
        }
    }

    protected void saveAdditional(CompoundTag tag) {
        tag.putInt("Charge", this.getCharge());
        super.saveAdditional(tag);
    }


    public static void tickBlock(Level level, BlockPos pos, BlockState state, CarvedNephriteBlockEntity selfTile) {
        selfTile.tickCounter++;

        if (!level.isClientSide && selfTile.tickCounter == 5) {
            runFlow(level, pos, state, selfTile);
            selfTile.setTickCounter(0);
        }

        if (selfTile.getCharge() == 0) level.setBlockAndUpdate(pos, state.setValue(CarvedNephriteBlock.CHARGE, CarvedNephriteBlock.ChargeState.EMPTY));
        else if (selfTile.getCharge() > 0 && selfTile.getCharge() <= 20) level.setBlockAndUpdate(pos, state.setValue(CarvedNephriteBlock.CHARGE, CarvedNephriteBlock.ChargeState.ONE_TO_TWENTY));
        else if (selfTile.getCharge() > 20 && selfTile.getCharge() <= 40) level.setBlockAndUpdate(pos, state.setValue(CarvedNephriteBlock.CHARGE, CarvedNephriteBlock.ChargeState.TWENTYONE_TO_FOURTY));
        else if (selfTile.getCharge() > 40 && selfTile.getCharge() <= 60) level.setBlockAndUpdate(pos, state.setValue(CarvedNephriteBlock.CHARGE, CarvedNephriteBlock.ChargeState.FOURTYONE_TO_SIXTY));
        else if (selfTile.getCharge() > 60 && selfTile.getCharge() <= 80) level.setBlockAndUpdate(pos, state.setValue(CarvedNephriteBlock.CHARGE, CarvedNephriteBlock.ChargeState.SIXTYONE_TO_EIGHTY));
        else if (selfTile.getCharge() > 80 && selfTile.getCharge() <= 99) level.setBlockAndUpdate(pos, state.setValue(CarvedNephriteBlock.CHARGE, CarvedNephriteBlock.ChargeState.EIGHTYONE_TO_NINETYNINE));
        else if (selfTile.getCharge() == 100) level.setBlockAndUpdate(pos, state.setValue(CarvedNephriteBlock.CHARGE, CarvedNephriteBlock.ChargeState.FULL));

    }
}
