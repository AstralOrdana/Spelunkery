package com.ordana.spelunkery.items;

import com.mojang.blaze3d.platform.InputConstants;
import com.ordana.spelunkery.configs.ClientConfigs;
import com.ordana.spelunkery.utils.EnchantmentUtils;
import com.ordana.spelunkery.utils.TranslationUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class NephriteCharmItem extends Item {
    public NephriteCharmItem(Properties properties) {
        super(properties);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag context) {
        if (ClientConfigs.ENABLE_TOOLTIPS.get()) {
            tooltip.add(Component.translatable("tooltip.spelunkery.nephrite_charm_1", getStoredXP(stack), "1395").setStyle(Style.EMPTY.applyFormat(ChatFormatting.DARK_GREEN)));
            if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), Minecraft.getInstance().options.keyShift.key.getValue())) {
                tooltip.add(Component.translatable("tooltip.spelunkery.nephrite_charm_2").setStyle(Style.EMPTY.applyFormat(ChatFormatting.GRAY)));
                tooltip.add(TranslationUtils.NEPHRITE_CHARM_3.component());
            } else {
                tooltip.add(TranslationUtils.CROUCH.component());
            }
        }
    }


    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return getStoredXP(stack) == 1395;
    }

    @Override
    @NotNull
    public InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        int storedXP = getStoredXP(stack);
        if (level instanceof ServerLevel) {
            if (player.isCrouching()) {
                if (storedXP < 1395 && storedXP >= 0)
                    if (player.totalExperience > 0 || (player.totalExperience == 0 && player.experienceLevel > 0)) {

                        /*
                        if (Screen.hasControlDown()) {
                            player.giveExperiencePoints(-1);
                            setStoredXP(stack, storedXP + 1);
                        }
                         */

                        //else {
                            int totalXp = EnchantmentUtils.getPlayerXP(player);

                            if (totalXp == 0)
                                return new InteractionResultHolder<>(InteractionResult.PASS, stack);

                            int actuallyStored = addXP(stack, totalXp);

                            if (actuallyStored > 0) {
                                EnchantmentUtils.addPlayerXP(player, -actuallyStored);
                            }
                        //}

                        float f = level.random.nextFloat();
                        level.playSound(null, player.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.BLOCKS, f * 0.9F, ((f + 1F) / 2) - 0.3f);
                        return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
                    }
            }

            else if (!player.isCrouching()) {
                if (storedXP > 0 && storedXP <= 1395) {
                    /*if (Screen.hasControlDown()) {
                        ExperienceOrb.award((ServerLevel) level, player.position(), 1);
                        setStoredXP(stack, storedXP - 1);
                    }
                     */
                    //else {
                        ExperienceOrb.award((ServerLevel) level, player.position(), storedXP);
                        setStoredXP(stack, 0);
                    //}
                    return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
                }
            }
        }
        return new InteractionResultHolder<>(InteractionResult.PASS, stack);
    }

    /**
     * Tries to add the given amount of XP to the given stack. If that action would exceed the storage capacity, as much XP as
     * possible will be stored.
     *
     * @param stack The stack to add XP to
     * @param amount The amount of XP to add
     * @return The amount XP that was added
     */
    public int addXP(ItemStack stack, int amount) {
        if (amount <= 0) //can't add a negative amount of XP
            return 0;

        int stored = getStoredXP(stack);
        int maxStorage = 1395;

        if (stored >= maxStorage) //can't add XP to a full book
            return 0;

        if (stored + amount <= maxStorage) {
            setStoredXP(stack, stored + amount);
            return amount;
        }
        else {
            setStoredXP(stack, maxStorage);
            return maxStorage - stored;
        }
    }

    public void setStoredXP(ItemStack stack, int amount) {
        stack.getOrCreateTag().putInt("xp", amount);
    }

    public int getStoredXP(ItemStack stack) {
        return stack.getOrCreateTag().getInt("xp");
    }
}
