package com.ordana.underground_overhaul.mixins;


import com.ordana.underground_overhaul.reg.ModTags;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Item.class)
public class TooltipMixin {

    @Inject(method = "appendHoverText", at = @At("HEAD"))
    private void recoveryCompassTooltip(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag isAdvanced, CallbackInfo ci) {
        if (stack.is(ModTags.KEEP_ON_DEATH)) {
            tooltip.add(Component.translatable("tooltip.underground_overhaul.keep_on_death").setStyle(Style.EMPTY.applyFormat(ChatFormatting.DARK_AQUA)));
        }
        if (stack.is(Items.CRYING_OBSIDIAN)) {
            tooltip.add(Component.translatable("tooltip.underground_overhaul.crying_obsidian").setStyle(Style.EMPTY.applyFormat(ChatFormatting.DARK_PURPLE)));
        }
    }
}
