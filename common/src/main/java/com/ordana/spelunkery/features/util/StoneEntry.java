package com.ordana.spelunkery.features.util;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class StoneEntry {
    private final Block primaryStoneType;
    private final Block secondaryStoneType;
    private StonePattern stonePattern;

    public StoneEntry(Block primaryStoneType, Block secondaryStoneType, StonePattern stonePattern) {
        this.primaryStoneType = primaryStoneType;
        this.secondaryStoneType = secondaryStoneType;
        this.stonePattern = stonePattern;
    }

    public StonePattern getStonePattern(){
        return this.stonePattern;
    }

    public BlockState getPrimaryStoneState(){
        return primaryStoneType.defaultBlockState();
    }

    public BlockState getSecondaryStoneState(){
        return secondaryStoneType.defaultBlockState();
    }
}
