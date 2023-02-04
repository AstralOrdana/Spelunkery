package com.ordana.spelunkery.items;

import com.ordana.spelunkery.configs.ClientConfigs;
import com.ordana.spelunkery.utils.TranslationUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class NephriteDiodeItem extends BlockItem {
    public NephriteDiodeItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag context) {
        if (ClientConfigs.ENABLE_TOOLTIPS.get()) {
            if (Minecraft.getInstance().options.keyShift.isDown()) {
                tooltip.add(Component.translatable("tooltip.spelunkery.nephrite_diode_1").setStyle(Style.EMPTY.applyFormat(ChatFormatting.GRAY)));
                tooltip.add(Component.translatable("tooltip.spelunkery.nephrite_diode_2").setStyle(Style.EMPTY.applyFormat(ChatFormatting.GRAY)));
                tooltip.add(Component.translatable("tooltip.spelunkery.nephrite_diode_3").setStyle(Style.EMPTY.applyFormat(ChatFormatting.GRAY)));
            } else {
                tooltip.add(TranslationUtils.CROUCH.component());
            }
        }
    }
}
