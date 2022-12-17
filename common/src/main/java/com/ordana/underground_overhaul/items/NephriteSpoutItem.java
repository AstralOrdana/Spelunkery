package com.ordana.underground_overhaul.items;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;
import java.util.List;

public class NephriteSpoutItem extends BlockItem {
    public NephriteSpoutItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag context) {
        if (!Screen.hasShiftDown()) {
            tooltip.add(Component.translatable("tooltip.underground_overhaul.hold_crouch").setStyle(Style.EMPTY.applyFormat(ChatFormatting.GOLD)));
        }
        if (Screen.hasShiftDown()) {
            tooltip.add(Component.translatable("tooltip.underground_overhaul.nephrite_spout_1").setStyle(Style.EMPTY.applyFormat(ChatFormatting.GRAY)));
            tooltip.add(Component.translatable("tooltip.underground_overhaul.nephrite_spout_2").setStyle(Style.EMPTY.applyFormat(ChatFormatting.GRAY)));
            tooltip.add(Component.translatable("tooltip.underground_overhaul.nephrite_spout_3").setStyle(Style.EMPTY.applyFormat(ChatFormatting.GRAY)));
        }
    }
}