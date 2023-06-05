package com.ordana.spelunkery.items;

import com.google.common.base.Suppliers;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.mojang.blaze3d.platform.InputConstants;
import com.ordana.spelunkery.configs.ClientConfigs;
import com.ordana.spelunkery.reg.ModItems;
import com.ordana.spelunkery.reg.ModTags;
import com.ordana.spelunkery.utils.TranslationUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class HandheldCompactorItem extends Item {
    public HandheldCompactorItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @javax.annotation.Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag context) {
        if (ClientConfigs.ENABLE_TOOLTIPS.get()) {
            CompoundTag compoundTag = stack.getOrCreateTag();
            if (compoundTag.getBoolean("active")) tooltip.add(Component.translatable("tooltip.spelunkery.active").setStyle(Style.EMPTY.applyFormats(ChatFormatting.DARK_GREEN, ChatFormatting.ITALIC)));
            if (!compoundTag.getBoolean("active")) tooltip.add(Component.translatable("tooltip.spelunkery.inactive").setStyle(Style.EMPTY.applyFormats(ChatFormatting.DARK_RED, ChatFormatting.ITALIC)));
            if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), Minecraft.getInstance().options.keyShift.key.getValue())) {
                tooltip.add(Component.translatable("tooltip.spelunkery.handheld_compactor_1").setStyle(Style.EMPTY.applyFormat(ChatFormatting.GRAY)));
                tooltip.add(Component.translatable("tooltip.spelunkery.handheld_compactor_2").setStyle(Style.EMPTY.applyFormat(ChatFormatting.GRAY)));
            } else {
                tooltip.add(TranslationUtils.CROUCH.component());
            }
        }
    }

    @Override
    @NotNull
    public InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        toggleCompactor(player, stack, level);
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
    }

    public static void toggleCompactor(Player player, ItemStack stack, Level level) {
        if(!player.level.isClientSide && stack.getItem() instanceof HandheldCompactorItem){
            boolean active = stack.getOrCreateTag().contains("active") && stack.getOrCreateTag().getBoolean("active");
            var beaconSound = active ? SoundEvents.BEACON_DEACTIVATE : SoundEvents.BEACON_ACTIVATE;
            level.playSound(null, player.blockPosition(), beaconSound, SoundSource.BLOCKS, 1.0f, 2.0f);
            stack.getOrCreateTag().putBoolean("active", !active);
        }
    }

    @Override
    public boolean isFoil(ItemStack stack){
        return stack.getOrCreateTag().contains("active") && stack.getOrCreateTag().getBoolean("active");
    }


    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, Entity entity, int slotId, boolean isSelected) {
        if(entity.isSpectator()) {
            return;
        }

        CompoundTag tag = stack.getOrCreateTag();
        if (tag.contains("active") && tag.getBoolean("active") && entity instanceof Player player) {

            var inventory = player.getInventory();

            for (int i = 0; i < inventory.getContainerSize(); i++) {
                ItemStack foundItem = inventory.getItem(i);

                if (foundItem.getCount() >= 9) {

                    var compressed = getCompressed(foundItem);
                    if (compressed.isPresent()) {
                        ItemStack newStack = compressed.get();

                        if (!player.getInventory().add(newStack)) player.drop(newStack, false);
                        foundItem.shrink(9);
                    }
                }
            }

        }
    }



    public static final Supplier<BiMap<Item, Item>> COMPACTED_ITEMS = Suppliers.memoize(() -> {

        var builder = ImmutableBiMap.<Item, Item>builder()
                .put(Items.IRON_NUGGET, Items.IRON_INGOT)
                .put(Items.GOLD_NUGGET, Items.GOLD_INGOT)
                .put(ModItems.COPPER_NUGGET.get(), Items.COPPER_INGOT)

                .put(ModItems.RAW_IRON_NUGGET.get(), Items.RAW_IRON)
                .put(ModItems.RAW_GOLD_NUGGET.get(), Items.RAW_GOLD)
                .put(ModItems.RAW_COPPER_NUGGET.get(), Items.RAW_COPPER)
                .put(ModItems.RAW_MAGNETITE_NUGGET.get(), ModItems.MAGNETITE_CHUNK.get())

                .put(ModItems.CINNABAR_SHARD.get(), ModItems.CINNABAR.get())
                .put(ModItems.LAPIS_LAZULI_SHARD.get(), Items.LAPIS_LAZULI)
                .put(ModItems.EMERALD_SHARD.get(), Items.EMERALD)
                .put(ModItems.DIAMOND_SHARD.get(), Items.DIAMOND)

                .put(ModItems.ROUGH_CINNABAR_SHARD.get(), ModItems.ROUGH_CINNABAR.get())
                .put(ModItems.ROUGH_LAZURITE_SHARD.get(), ModItems.ROUGH_LAZURITE.get())
                .put(ModItems.ROUGH_EMERALD_SHARD.get(), ModItems.ROUGH_EMERALD.get())
                .put(ModItems.ROUGH_DIAMOND_SHARD.get(), ModItems.ROUGH_DIAMOND.get());

        return builder.build();
    });

    public static Optional<ItemStack> getCompressed(ItemStack stack) {
        return Optional.ofNullable(COMPACTED_ITEMS.get().get(stack.getItem()))
                .map(item -> item.asItem().getDefaultInstance());
    }
}
