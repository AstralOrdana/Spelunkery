package com.ordana.spelunkery.items;

import com.mojang.blaze3d.platform.InputConstants;
import com.ordana.spelunkery.Spelunkery;
import com.ordana.spelunkery.configs.ClientConfigs;
import com.ordana.spelunkery.reg.ModParticles;
import com.ordana.spelunkery.utils.TranslationUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class DustBunItem extends Item {
    public DustBunItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag context) {
        if (ClientConfigs.ENABLE_TOOLTIPS.get()) {
            if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), Minecraft.getInstance().options.keyShift.key.getValue())) {
                tooltip.add(Component.translatable("tooltip.spelunkery.dust_bun_1").setStyle(Style.EMPTY.applyFormat(ChatFormatting.GRAY)));
            } else {
                tooltip.add(TranslationUtils.CROUCH.component());
            }
        }
    }

    @Override
    @NotNull
    public InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {

        ItemStack stack = player.getItemInHand(hand);
        if (level instanceof ServerLevel serverLevel) {
            LootTable lootTable = Objects.requireNonNull(level.getServer()).getLootData().getLootTable(Spelunkery.res("gameplay/dust_bun"));
            LootParams.Builder builder = new LootParams.Builder((ServerLevel) level)
                    .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(Vec3i.ZERO))
                    .withOptionalParameter(LootContextParams.THIS_ENTITY, player);

            int bunCount = 1;
            if (player.isSecondaryUseActive()) bunCount = stack.getCount();

            for (int i = 0; i < bunCount; i++) {

                var l = lootTable.getRandomItems(builder.create(LootContextParamSets.GIFT));
                ItemStack bunStack = l.iterator().next();

                if (bunStack != null) {
                    if (!player.getInventory().add(new ItemStack(bunStack.getItem()))) {
                        player.drop(new ItemStack(bunStack.getItem()), false);
                    }
                    stack.shrink(1);
                }

                double d0 = level.random.nextGaussian() * 0.02D;
                double d1 = level.random.nextGaussian() * 0.02D;
                double d2 = level.random.nextGaussian() * 0.02D;
                serverLevel.sendParticles(ModParticles.DUST_POOF.get(), (player.getX()) - d0 * 10.0D, (player.getY() + player.getEyeHeight() - 0.2) - d1 * 10.0D, (player.getZ()) - d2 * 10.0D, 5, 0, 0, 0, d2);

            }
            level.playSound(null, player.blockPosition(), SoundEvents.LEASH_KNOT_BREAK, SoundSource.BLOCKS, 1.0f, 1.0f);
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
        }

        return new InteractionResultHolder<>(InteractionResult.FAIL, stack);
    }
}

