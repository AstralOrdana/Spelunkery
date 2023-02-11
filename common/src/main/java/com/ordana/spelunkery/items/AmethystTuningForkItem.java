package com.ordana.spelunkery.items;

import com.mojang.blaze3d.platform.InputConstants;
import com.ordana.spelunkery.configs.ClientConfigs;
import com.ordana.spelunkery.configs.CommonConfigs;
import com.ordana.spelunkery.utils.TranslationUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AmethystTuningForkItem extends Item {

    public AmethystTuningForkItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @javax.annotation.Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag context) {
        if (ClientConfigs.ENABLE_TOOLTIPS.get()) {
            if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), Minecraft.getInstance().options.keyShift.key.getValue())) {
                tooltip.add(Component.translatable("tooltip.spelunkery.tuning_fork_1", getTollRange()).setStyle(Style.EMPTY.applyFormat(ChatFormatting.GRAY)));
                tooltip.add(Component.translatable("tooltip.spelunkery.tuning_fork_2").setStyle(Style.EMPTY.applyFormat(ChatFormatting.GRAY)));
            } else {
                tooltip.add(TranslationUtils.CROUCH.component());
            }
        }
    }

    @Override
    @NotNull
    public InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        tollFork(player, stack, level);
        player.getCooldowns().addCooldown(this, 600);
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
    }

    public void tollFork(Player player, ItemStack stack, Level level) {
        if(!player.level.isClientSide && stack.getItem() instanceof EchoForkItem){
            level.playSound(null, player.blockPosition(), SoundEvents.SCULK_CLICKING, SoundSource.BLOCKS, 1.0f, 2.0f);

            int r = getTollRange();
            AABB area = new AABB(player.position().add(-r, -r, -r), player.position().add(r, r, r));

            List<LivingEntity> entities = level.getEntities(EntityTypeTest.forClass(LivingEntity.class), area,
                    LivingEntity::isAlive
            );

            entities.forEach(item -> item.addEffect(new MobEffectInstance(MobEffects.GLOWING, 60)));
        }
    }


    public int getTollRange() {
        return CommonConfigs.ECHO_FORK_RANGE.get();
    }

}
