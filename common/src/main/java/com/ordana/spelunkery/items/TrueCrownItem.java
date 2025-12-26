package com.ordana.spelunkery.items;

import com.ordana.spelunkery.configs.ClientConfigs;
import com.ordana.spelunkery.reg.ModSoundEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TrueCrownItem extends Item implements Equipable {

    public TrueCrownItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable TooltipContext level, List<Component> tooltip, TooltipFlag context) {
        if (ClientConfigs.ENABLE_TOOLTIPS.get()) {
            tooltip.add(Component.translatable("tooltip.spelunkery.crown_1").setStyle(Style.EMPTY.applyFormats(ChatFormatting.RED, ChatFormatting.ITALIC)));
        }
    }

    @Override
    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.HEAD;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, @NotNull Player player, InteractionHand hand) {
        return swapWithEquipmentSlot(this, level, player, hand);
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, Entity entity, int slotId, boolean isSelected) {
        if (entity instanceof Player lEntity) {
            if (lEntity.getItemBySlot(EquipmentSlot.HEAD).getItem() == this) {
                lEntity.addEffect(new MobEffectInstance(MobEffects.UNLUCK, 2, 1, true, false, true));
                lEntity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 2, 20, true, false, true));
                if (level.random.nextFloat() < 0.1 && (lEntity.getDeltaMovement().x > 0 || lEntity.getDeltaMovement().z > 0)) level.playLocalSound(entity.blockPosition(), ModSoundEvents.JANGLE.get(), SoundSource.PLAYERS, 1.0f, 1.0f + (level.random.nextFloat() / 2), false);
            }
        }

    }
}