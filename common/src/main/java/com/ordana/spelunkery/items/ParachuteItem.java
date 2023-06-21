package com.ordana.spelunkery.items;

import com.mojang.blaze3d.platform.InputConstants;
import com.ordana.spelunkery.configs.ClientConfigs;
import com.ordana.spelunkery.configs.CommonConfigs;
import com.ordana.spelunkery.reg.ModItems;
import com.ordana.spelunkery.reg.ModTags;
import com.ordana.spelunkery.utils.TranslationUtils;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ParachuteItem extends Item {
    public ParachuteItem(Properties properties) {
        super(properties);
    }

    public void setUsed(ItemStack stack, boolean used) {
        stack.getOrCreateTag().putBoolean("used", used);
    }

    public boolean getUsed(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean("used");
    }

    public void setActive(ItemStack stack, boolean active) {
        stack.getOrCreateTag().putBoolean("active", active);
    }

    public boolean getActive(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean("active");
    }

    private int tickCounter = 0;

    public void setTickCounter(int tick) {
        tickCounter = tick;
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, @NotNull ItemStack repairCandidate) {
        return repairCandidate.is(ModTags.PARACHUE_REPAIR);
    }

    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @javax.annotation.Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag context) {
        if (ClientConfigs.ENABLE_TOOLTIPS.get()) {
            tooltip.add(Component.translatable("tooltip.spelunkery.wip_items").setStyle(Style.EMPTY.applyFormat(ChatFormatting.RED)));
            if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), Minecraft.getInstance().options.keyShift.key.getValue())) {
                tooltip.add(Component.translatable("tooltip.spelunkery.parachute_1").setStyle(Style.EMPTY.applyFormat(ChatFormatting.GRAY)));
                tooltip.add(Component.translatable("tooltip.spelunkery.parachute_2").setStyle(Style.EMPTY.applyFormat(ChatFormatting.GRAY)));
                tooltip.add(Component.translatable("tooltip.spelunkery.parachute_3").setStyle(Style.EMPTY.applyFormat(ChatFormatting.GRAY)));
            } else {
                tooltip.add(TranslationUtils.CROUCH.component());
            }
        }
    }

    @Override
    @NotNull
    public InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {

        ItemStack stack = player.getItemInHand(hand);
        if (level instanceof ServerLevel) {

            if (player.isShiftKeyDown() && getUsed(stack)) {
                ItemStack itemStack = player.getItemInHand(hand);
                player.startUsingItem(hand);
                return InteractionResultHolder.consume(itemStack);
            }

            if (!player.isShiftKeyDown() && !getUsed(stack) && !getActive(stack)) {
                if (player.getBlockStateOn().isAir()) {
                    setActive(stack, true);
                    level.playSound(null, player.blockPosition(), SoundEvents.ARMOR_EQUIP_LEATHER, SoundSource.BLOCKS, 1.0f, 1.0f);
                    return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
                }
            }

        }

        return new InteractionResultHolder<>(InteractionResult.FAIL, stack);
    }

    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged) {
        if (livingEntity instanceof Player) {
            int i = this.getUseDuration(stack) - timeCharged;
            if (i >= 20) {
                setUsed(stack, false);
            }
        }
    }


    @Override
    public void inventoryTick(ItemStack stack, Level levelIn, Entity entityIn, int itemSlot, boolean isSelected) {
        super.inventoryTick(stack, levelIn, entityIn, itemSlot, isSelected);

        if (!levelIn.isClientSide() && !getUsed(stack)) {
            if (entityIn instanceof ServerPlayer player && !player.hasEffect(MobEffects.SLOW_FALLING)) {

                if (player.getDeltaMovement().y < -1.0D && !getActive(stack)) {
                    this.tickCounter++;
                    if (this.tickCounter >= CommonConfigs.PARACHUTE_DELAY.get()) {
                        setActive(stack, true);
                        levelIn.playSound(null, player.blockPosition(), SoundEvents.ARMOR_EQUIP_LEATHER, SoundSource.BLOCKS, 1.0f, 1.0f);
                        setTickCounter(0);
                    }
                }

                if (getActive(stack)) {
                    player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 5, 0, true, false, true));
                    if (!player.getBlockStateOn().isAir()) {
                        setActive(stack, false);
                        setUsed(stack, true);
                        if (!player.isCreative()) {
                            if (stack.getDamageValue() < stack.getMaxDamage()) stack.hurt(1, RandomSource.create(), player);
                            else {
                                stack.shrink(1);
                                player.awardStat(Stats.ITEM_BROKEN.get(stack.getItem()));
                                levelIn.playSound(null, player.blockPosition(), SoundEvents.LEASH_KNOT_BREAK, SoundSource.BLOCKS, 1.0f, 1.0f);
                            }
                        }
                    }

                }
            }
        }
    }
}

