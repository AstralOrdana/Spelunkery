package com.ordana.spelunkery.blocks.dispenser_interactions;

import com.ordana.spelunkery.reg.ModBlockProperties;
import com.ordana.spelunkery.reg.ModBlocks;
import net.mehvahdjukaar.moonlight.api.util.DispenserHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;

public class GrindstoneRepairBehavior extends DispenserHelper.AdditionalDispenserBehavior {

    protected GrindstoneRepairBehavior(Item item) {
        super(item);
    }

    @Override
    protected InteractionResultHolder<ItemStack> customBehavior(BlockSource source, ItemStack stack) {
        ServerLevel level = source.level();
        BlockPos pos = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
        BlockState state = level.getBlockState(pos);
        if ((state.is(Blocks.GRINDSTONE) || (state.is(ModBlocks.DIAMOND_GRINDSTONE.get()) && state.getValue(ModBlockProperties.DEPLETION) > 0))) {
            level.setBlockAndUpdate(pos, state.setValue(ModBlockProperties.DEPLETION, 0));
            stack.shrink(1);
            return InteractionResultHolder.success(stack);
        }

        return InteractionResultHolder.pass(stack);
    }
}