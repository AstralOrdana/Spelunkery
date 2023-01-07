package com.ordana.spelunkery.mixins;


import com.ordana.spelunkery.reg.ModTags;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(Item.class)
public class TooltipMixin {

    @Inject(method = "appendHoverText", at = @At("HEAD"))
    private void recoveryCompassTooltip(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag isAdvanced, CallbackInfo ci) {
        if (stack.is(ModTags.KEEP_ON_DEATH)) {
            tooltip.add(Component.translatable("tooltip.spelunkery.keep_on_death").setStyle(Style.EMPTY.applyFormat(ChatFormatting.DARK_AQUA)));
        }
        if (stack.is(Items.CRYING_OBSIDIAN)) {
            tooltip.add(Component.translatable("tooltip.spelunkery.crying_obsidian").setStyle(Style.EMPTY.applyFormat(ChatFormatting.DARK_PURPLE)));
        }
        if (stack.is(Items.GRINDSTONE)) {
            if (!Screen.hasShiftDown()) {
                tooltip.add(Component.translatable("tooltip.spelunkery.hold_crouch").setStyle(Style.EMPTY.applyFormat(ChatFormatting.GOLD)));
            }
            if (Screen.hasShiftDown()) {
                tooltip.add(Component.translatable("tooltip.spelunkery.grindstone_1").setStyle(Style.EMPTY.applyFormat(ChatFormatting.GRAY)));
                tooltip.add(Component.translatable("tooltip.spelunkery.grindstone_2").setStyle(Style.EMPTY.applyFormat(ChatFormatting.GRAY)));
            }
        }
    }
}
