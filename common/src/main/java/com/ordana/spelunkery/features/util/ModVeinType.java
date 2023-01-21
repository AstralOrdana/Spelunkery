package com.ordana.spelunkery.features.util;

import com.ordana.spelunkery.reg.ModBlocks;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class ModVeinType {
    public static enum VeinType {
        GOLD(Blocks.GOLD_ORE.defaultBlockState(), Blocks.RAW_GOLD_BLOCK.defaultBlockState(), Blocks.BLACKSTONE.defaultBlockState(), 0, 50),
        LAPIS(ModBlocks.SANDSTONE_LAPIS_ORE.get().defaultBlockState(), ModBlocks.ROUGH_LAZURITE_BLOCK.get().defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), -60, -8);

        final BlockState ore;
        final BlockState rawOreBlock;
        final BlockState filler;
        protected final int minY;
        protected final int maxY;

        private VeinType(BlockState blockState, BlockState blockState2, BlockState blockState3, int j, int k) {
            this.ore = blockState;
            this.rawOreBlock = blockState2;
            this.filler = blockState3;
            this.minY = j;
            this.maxY = k;
        }
    }
}
