package com.ordana.spelunkery.features.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ordana.spelunkery.features.BlockStripeFeatureConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class StoneEntry {
    private final BlockStateProvider primaryStoneType;
    private final BlockStateProvider secondaryStoneType;
    private StonePattern stonePattern;

    public StoneEntry(BlockStateProvider primaryStoneType, BlockStateProvider secondaryStoneType, StonePattern stonePattern) {
        this.primaryStoneType = primaryStoneType;
        this.secondaryStoneType = secondaryStoneType;
        this.stonePattern = stonePattern;
    }

    public static final Codec<StoneEntry> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
                    BlockStateProvider.CODEC.fieldOf("primary_stone_type").forGetter((stoneEntry)
                            -> stoneEntry.primaryStoneType),
                    BlockStateProvider.CODEC.fieldOf("secondary_stone_type").forGetter((stoneEntry)
                            -> stoneEntry.secondaryStoneType),
                    StonePattern.CODEC.fieldOf("stone_pattern").forGetter((stoneEntry)
                            -> stoneEntry.stonePattern))
            .apply(instance, StoneEntry::new));

    public StonePattern getStonePattern(){
        return this.stonePattern;
    }

    public BlockState getPrimaryStoneState(WorldGenLevel level, BlockPos pos) {
        return primaryStoneType.getState(level.getRandom(), pos);
    }

    public BlockState getSecondaryStoneState(WorldGenLevel level, BlockPos pos) {
        return secondaryStoneType.getState(level.getRandom(), pos);
    }
}
