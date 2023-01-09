package com.ordana.spelunkery.items;

import com.ordana.spelunkery.configs.ClientConfigs;
import com.ordana.spelunkery.openmods.utils.EnchantmentUtils;
import net.minecraft.ChatFormatting;
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

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag context) {
        if (ClientConfigs.ENABLE_TOOLTIPS.get()) {
            tooltip.add(Component.translatable("tooltip.spelunkery.nephrite_charm_1", getStoredXP(stack), "1395").setStyle(Style.EMPTY.applyFormat(ChatFormatting.DARK_GREEN)));
            if (!Screen.hasShiftDown()) {
                tooltip.add(Component.translatable("tooltip.spelunkery.hold_crouch").setStyle(Style.EMPTY.applyFormat(ChatFormatting.GOLD)));
            }
            if (Screen.hasShiftDown()) {
                tooltip.add(Component.translatable("tooltip.spelunkery.nephrite_charm_2").setStyle(Style.EMPTY.applyFormat(ChatFormatting.GRAY)));
                tooltip.add(Component.translatable("tooltip.spelunkery.nephrite_charm_3").setStyle(Style.EMPTY.applyFormat(ChatFormatting.GRAY)));
                tooltip.add(Component.translatable("tooltip.spelunkery.nephrite_charm_4").setStyle(Style.EMPTY.applyFormat(ChatFormatting.GRAY)));
            }
        }
    }


    @Override
    public boolean isFoil(ItemStack stack) {
        return getStoredXP(stack) == 1395;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, @NotNull Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        int storedXP = getStoredXP(stack);
        if (level instanceof ServerLevel) {
            if (player.isCrouching()) { //input vs output mode, crouching is input
                if (storedXP < 1395 && storedXP >= 0)
                    if (player.totalExperience > 0 || (player.totalExperience == 0 && player.experienceLevel > 0)) {


                        if (Screen.hasControlDown()) { //incremental xp mode
                            player.giveExperiencePoints(-1);
                            setStoredXP(stack, storedXP + 1);
                        }
                        else { //input all mode


                            int totalXp = EnchantmentUtils.getPlayerXP(player);

                            /*
                            if (Configuration.CONFIG.storeUntilPreviousLevel.get()) {
                                int xpForCurrentLevel = EnchantmentUtils.getExperienceForLevel(player.experienceLevel);

                                xpToStore = EnchantmentUtils.getPlayerXP(player) - xpForCurrentLevel;

                                if (xpToStore == 0 && player.experienceLevel > 0) //player has exactly x > 0 levels (xp bar looks empty)
                                    xpToStore = xpForCurrentLevel - EnchantmentUtils.getExperienceForLevel(player.experienceLevel - 1);
                            }
                             */

                            if (totalXp == 0)
                                return new InteractionResultHolder<>(InteractionResult.PASS, stack);

                            int actuallyStored = addXP(stack, totalXp); //store as much XP as possible

                            if (actuallyStored > 0) {
                                int previousLevel = player.experienceLevel;

                                //MinecraftForge.EVENT_BUS.post(new PlayerXpEvent.XpChange(player, -actuallyStored));
                                EnchantmentUtils.addPlayerXP(player, -actuallyStored); //negative value removes xp
                                /*
                                if (previousLevel != player.experienceLevel)
                                    MinecraftForge.EVENT_BUS.post(new PlayerXpEvent.LevelChange(player, player.experienceLevel));
                                 */
                            }
                        }

                        float f = level.random.nextFloat();
                        level.playSound(null, player.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.BLOCKS, f * 0.9F, ((f + 1F) / 2) - 0.3f);
                        return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
                    }
            }
            else if (!player.isCrouching()) { //input vs output mode, not crouching is output
                if (storedXP > 0 && storedXP <= 1395) {
                    if (Screen.hasControlDown()) {
                        ExperienceOrb.award((ServerLevel) level, player.position(), 1);
                        setStoredXP(stack, storedXP - 1);
                    }
                    else {
                        ExperienceOrb.award((ServerLevel) level, player.position(), storedXP);
                        setStoredXP(stack, 0);
                    }
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
