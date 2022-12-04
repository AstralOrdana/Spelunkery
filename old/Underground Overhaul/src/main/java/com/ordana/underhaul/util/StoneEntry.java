package com.ordana.underhaul.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;

public class StoneEntry {
    private final Block primaryStoneType;
    private final Block secondaryStoneType;
    private StonePattern stonePattern;

    public StoneEntry(Block primaryStoneType, Block secondaryStoneType, StonePattern stonePattern){
        this.primaryStoneType = primaryStoneType;
        this.secondaryStoneType = secondaryStoneType;
        this.stonePattern = stonePattern;
    }

    public StonePattern getStonePattern(){
        return this.stonePattern;
    }

    public BlockState getPrimaryStoneState(){
        return primaryStoneType.getDefaultState();
    }

    public BlockState getSecondaryStoneState(){
        return secondaryStoneType.getDefaultState();
    }
}
