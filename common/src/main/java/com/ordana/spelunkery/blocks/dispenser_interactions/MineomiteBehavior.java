package com.ordana.spelunkery.blocks.dispenser_interactions;

import com.ordana.spelunkery.blocks.CompressionBlastMiner;
import net.mehvahdjukaar.moonlight.api.util.DispenserHelper;
import net.mehvahdjukaar.moonlight.api.util.FakePlayerManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class MineomiteBehavior extends DispenserHelper.AdditionalDispenserBehavior {
    public MineomiteBehavior(Item item) {
        super(item);
    }

    @Override
    protected InteractionResultHolder<ItemStack> customBehavior(BlockSource source, ItemStack stack) {
        ServerLevel level = source.getLevel();
        Direction dir = source.getBlockState().getValue(DispenserBlock.FACING);
        BlockPos blockPos = source.getPos().relative(dir);

        var blockState = level.getBlockState(blockPos);
        if (blockState.getBlock() instanceof CompressionBlastMiner && stack.is(Items.TNT)) {
            level.setBlockAndUpdate(blockPos, blockState.setValue(CompressionBlastMiner.PRIMED, true));

            return InteractionResultHolder.consume(stack);
        }
        return InteractionResultHolder.pass(stack);
    }
}