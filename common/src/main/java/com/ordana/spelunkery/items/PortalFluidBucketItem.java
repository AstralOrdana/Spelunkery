package com.ordana.spelunkery.items;

import com.ordana.spelunkery.blocks.PortalFluidCauldronBlock;
import com.ordana.spelunkery.reg.ModBlocks;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CauldronBlock;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;

public class PortalFluidBucketItem extends BucketItem {
    public PortalFluidBucketItem(Fluid fluid, Properties properties) {
        super(fluid, properties);
    }
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();
        if (state.getBlock() instanceof CauldronBlock || (state.getBlock() instanceof PortalFluidCauldronBlock && state.getValue(LayeredCauldronBlock.LEVEL) < 3)) {
            level.playSound(player, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0f, 1.0f);
            if (player instanceof ServerPlayer serverPlayer) {
                ItemStack itemStack2 = ItemUtils.createFilledResult(stack, player, Items.BUCKET.getDefaultInstance());
                player.setItemInHand(context.getHand(), itemStack2);
                level.setBlockAndUpdate(pos, ModBlocks.PORTAL_CAULDRON.get().defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3));

                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, stack);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }
}
