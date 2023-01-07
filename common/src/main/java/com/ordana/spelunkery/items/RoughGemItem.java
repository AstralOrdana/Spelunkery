package com.ordana.spelunkery.items;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;
import java.util.List;

public class RoughGemItem extends Item {
    public RoughGemItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag context) {
        tooltip.add(Component.translatable("tooltip.spelunkery.grindable").setStyle(Style.EMPTY.applyFormats(ChatFormatting.GRAY, ChatFormatting.ITALIC)));
    }
}
