package com.ordana.spelunkery.blocks.dispenser_interactions;

import com.ordana.spelunkery.blocks.CompressionBlastMiner;
import net.mehvahdjukaar.moonlight.api.util.DispenserHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;

public class CBMBehavior extends DispenserHelper.AdditionalDispenserBehavior {

    protected CBMBehavior() {
        super(Items.TNT);
    }

    @Override
    protected InteractionResultHolder<ItemStack> customBehavior(BlockSource source, ItemStack stack) {
        //this.setSuccessful(false);
        ServerLevel level = source.level();
        BlockPos pos = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
        BlockState state = level.getBlockState(pos);
        if (state.getBlock() instanceof CompressionBlastMiner) {
            if (!state.getValue(CompressionBlastMiner.PRIMED)) {
                level.setBlockAndUpdate(pos, state.setValue(CompressionBlastMiner.PRIMED, true));
                stack.shrink(1);
                return InteractionResultHolder.success(stack);
            }
            return InteractionResultHolder.fail(stack);
        }

        return InteractionResultHolder.pass(stack);
    }
}