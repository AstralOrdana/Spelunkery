package com.ordana.spelunkery.mixins;


import com.mojang.blaze3d.platform.InputConstants;
import com.ordana.spelunkery.configs.ClientConfigs;
import com.ordana.spelunkery.configs.CommonConfigs;
import com.ordana.spelunkery.reg.ModBlocks;
import com.ordana.spelunkery.reg.ModTags;
import com.ordana.spelunkery.utils.TranslationUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
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

import java.util.List;

@Mixin(Item.class)
public class TooltipMixin {

    @Environment(EnvType.CLIENT)
    @Inject(method = "appendHoverText", at = @At("HEAD"))
    private void vanillaItemTooltips(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag isAdvanced, CallbackInfo ci) {
        if (ClientConfigs.ENABLE_TOOLTIPS.get()) {
            if (stack.is(ModTags.SLIME_FOOD)) {
                tooltip.add(Component.translatable("tooltip.spelunkery.slime_food").setStyle(Style.EMPTY.applyFormat(ChatFormatting.GREEN)));
            }
            if (stack.is(Items.FLINT_AND_STEEL) && !CommonConfigs.FlINT_AND_STEEL_PORTAL_LIGHTING.get()) {
                tooltip.add(Component.translatable("tooltip.spelunkery.flint_and_steel").setStyle(Style.EMPTY.applyFormat(ChatFormatting.DARK_PURPLE)));
            }
            if (CommonConfigs.SCULK_SHEARING.get()) {
                if (stack.is(Items.SCULK)) tooltip.add(Component.translatable("tooltip.spelunkery.sculk").setStyle(Style.EMPTY.applyFormat(ChatFormatting.DARK_AQUA)));
                if (stack.is(Items.SCULK_VEIN)) tooltip.add(Component.translatable("tooltip.spelunkery.sculk_vein").setStyle(Style.EMPTY.applyFormat(ChatFormatting.DARK_AQUA)));
            }
            if (stack.is(Items.REDSTONE)) {
                tooltip.add(Component.translatable("tooltip.spelunkery.redstone").setStyle(Style.EMPTY.applyFormat(ChatFormatting.DARK_RED)));
            }
            if (stack.is(ModTags.KEEP_ON_DEATH)) {
                tooltip.add(Component.translatable("tooltip.spelunkery.keep_on_death").setStyle(Style.EMPTY.applyFormat(ChatFormatting.DARK_AQUA)));
            }
            if (CommonConfigs.CRYING_OBSIDIAN_PORTAL_FLUID.get() && stack.is(Items.CRYING_OBSIDIAN)) {
                tooltip.add(Component.translatable("tooltip.spelunkery.crying_obsidian").setStyle(Style.EMPTY.applyFormat(ChatFormatting.DARK_PURPLE)));
            }
            if (CommonConfigs.RESPAWN_ANCHOR_PORTAL_FLUID.get() && stack.is(Items.RESPAWN_ANCHOR)) {
                tooltip.add(Component.translatable("tooltip.spelunkery.crying_obsidian").setStyle(Style.EMPTY.applyFormat(ChatFormatting.DARK_PURPLE)));
            }
            if (stack.is(Items.GRINDSTONE) || stack.is(ModBlocks.DIAMOND_GRINDSTONE.get().asItem())) {
                if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), Minecraft.getInstance().options.keyShift.key.getValue())) {
                    tooltip.add(TranslationUtils.GRINDSTONE_1.component());
                    tooltip.add(TranslationUtils.GRINDSTONE_2.component());
                } else {
                    tooltip.add(TranslationUtils.CROUCH.component());
                }
            }
            if (stack.is(ModTags.GRINDABLE)) {
                tooltip.add(Component.translatable("tooltip.spelunkery.grindable").setStyle(Style.EMPTY.applyFormats(ChatFormatting.GRAY, ChatFormatting.ITALIC)));
            }
            if (stack.is(ModTags.DIAMOND_GRINDABLE)) {
                tooltip.add(Component.translatable("tooltip.spelunkery.diamond_grindable").setStyle(Style.EMPTY.applyFormats(ChatFormatting.GRAY, ChatFormatting.ITALIC)));
            }
        }
    }
}
