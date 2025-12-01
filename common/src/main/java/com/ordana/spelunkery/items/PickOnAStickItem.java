package com.ordana.spelunkery.items;

import com.ordana.spelunkery.entities.PickOnAStickEntity;
import com.ordana.spelunkery.reg.ModBlocks;
import com.ordana.spelunkery.utils.IPickHelper;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;


public class PickOnAStickItem extends Item {
    public PickOnAStickItem(Item.Properties properties) {
        super(properties);
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {


        ItemStack itemStack = player.getItemInHand(usedHand);
        var pickEntity = ((IPickHelper)player).spelunkery$getEntity();


        if (pickEntity != null) {
            var id = pickEntity.getId();
            if (level.getEntity(id) != null) {

                if (player.isSecondaryUseActive()) {
                    pickEntity.remove(Entity.RemovalReason.DISCARDED);
                    return InteractionResultHolder.success(itemStack);
                }
                var pos = pickEntity.getPos();
                level.setBlockAndUpdate(pos, ModBlocks.ROUGH_DIAMOND_BLOCK.get().defaultBlockState());
            }
        }
        else {
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FISHING_BOBBER_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
            if (!level.isClientSide) {
                PickOnAStickEntity pick = new PickOnAStickEntity(level, player);
                pick.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
                level.addFreshEntity(pick);
                ((IPickHelper)player).spelunkery$setEntity(pick);
            }

            player.awardStat(Stats.ITEM_USED.get(this));
            player.gameEvent(GameEvent.ITEM_INTERACT_START);
        }

        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
    }
}
