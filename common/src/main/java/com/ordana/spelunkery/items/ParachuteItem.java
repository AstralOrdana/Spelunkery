package com.ordana.spelunkery.items;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import com.ordana.spelunkery.configs.ClientConfigs;
import com.ordana.spelunkery.configs.CommonConfigs;
import com.ordana.spelunkery.reg.ModTags;
import com.ordana.spelunkery.utils.TranslationUtils;
import net.mehvahdjukaar.moonlight.api.item.IFirstPersonAnimationProvider;
import net.mehvahdjukaar.moonlight.api.item.IThirdPersonAnimationProvider;
import net.mehvahdjukaar.moonlight.api.misc.DualWeildState;
import net.mehvahdjukaar.moonlight.api.util.math.MthUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ParachuteItem extends Item implements IFirstPersonAnimationProvider, IThirdPersonAnimationProvider {
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

    private boolean sound1 = false;
    private boolean sound2 = false;
    private boolean sound3 = false;
    private boolean sound4 = false;

    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        if (!level.isClientSide) {
            SoundEvent soundEvent = SoundEvents.LEASH_KNOT_PLACE;
            SoundEvent soundEvent2 = SoundEvents.CROSSBOW_LOADING_MIDDLE;
            float f = (float)(stack.getUseDuration() - remainingUseDuration) / 20;
            if (f < 0.2F) {
                this.sound1 = false;
                this.sound2 = false;
                this.sound3 = false;
                this.sound4 = false;
            }

            if (f >= 0.5F && !this.sound1) {
                this.sound1 = true;
                level.playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), soundEvent, SoundSource.PLAYERS, 0.5F, 0.2F);
            }

            if (f >= 1.5F && soundEvent2 != null && !this.sound2) {
                this.sound2 = true;
                level.playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), soundEvent, SoundSource.PLAYERS, 0.5F, 1.0F);
            }

            if (f >= 2.5F && soundEvent2 != null && !this.sound3) {
                this.sound3 = true;
                level.playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), soundEvent, SoundSource.PLAYERS, 0.5F, 1.5F);
            }

            if (f >= 3.5F && soundEvent2 != null && !this.sound4) {
                this.sound4 = true;
                level.playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), soundEvent, SoundSource.PLAYERS, 0.5F, 2.0F);
            }
        }

    }

    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged) {
        if (livingEntity instanceof Player) {
            int i = this.getUseDuration(stack) - timeCharged;
            if (i >= 70) {
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



    //stolen from Supplementaries slingshot

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        //need to use NONE for custom one
        return UseAnim.NONE;
    }

    @Override
    public <T extends LivingEntity> boolean poseLeftArm(ItemStack stack, HumanoidModel<T> model, T entity, HumanoidArm mainHand, DualWeildState twoHanded) {
        if (entity.getUseItemRemainingTicks() > 0 &&
                entity.getUseItem().getItem() == this &&
                entity.getTicksUsingItem() < 70) {
            //twoHanded.setTwoHanded(true);
            model.leftArm.yRot = MthUtils.wrapRad(0.1F + model.head.yRot);
            model.leftArm.xRot = MthUtils.wrapRad((-(float) Math.PI / 2F) + model.head.xRot);
            return true;
        }
        return false;
    }

    //TODO: finish this
    @Override
    public <T extends LivingEntity> boolean poseRightArm(ItemStack stack, HumanoidModel<T> model, T entity, HumanoidArm mainHand, DualWeildState twoHanded) {
        if (entity.getUseItemRemainingTicks() > 0 &&
                entity.getUseItem().getItem() == this &&
                entity.getTicksUsingItem() < 70) {
            //twoHanded.setTwoHanded(true);
            model.rightArm.yRot = MthUtils.wrapRad(-0.1F + model.head.yRot);
            model.rightArm.xRot = MthUtils.wrapRad((-(float) Math.PI / 2F) + model.head.xRot);
            return true;
        }
        return false;
    }

    @Override
    public void animateItemFirstPerson(LivingEntity entity, ItemStack stack, InteractionHand hand, PoseStack matrixStack, float partialTicks, float pitch, float attackAnim, float handHeight) {
        //is using item
        if (entity.isUsingItem() && entity.getUseItemRemainingTicks() > 0 && entity.getUsedItemHand() == hand &&
                entity.getTicksUsingItem() < 75) {
            //bow anim

            float timeLeft = (float) stack.getUseDuration() - ((float) entity.getUseItemRemainingTicks() - partialTicks + 1.0F);
            float f12 = 1f;

            float f15 = Mth.sin((timeLeft - 0.1F) * 1.3F);
            float f18 = f12 - 0.1F;
            float f20 = f15 * f18;
            matrixStack.translate(0, f20 * 0.007F, 0);

            matrixStack.translate(0, 0, f12 * 0.04F);
            matrixStack.scale(1.0F, 1.0F, 1.0F + f12 * 0.2F);
            //matrixStack.mulPose(Vector3f.YN.rotationDegrees((float)k * 45.0F));
        }
    }
}


