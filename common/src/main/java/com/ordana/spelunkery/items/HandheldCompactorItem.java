package com.ordana.spelunkery.items;

import com.google.common.base.Suppliers;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.mojang.blaze3d.platform.InputConstants;
import com.ordana.spelunkery.configs.ClientConfigs;
import com.ordana.spelunkery.reg.ModBlocks;
import com.ordana.spelunkery.reg.ModItems;
import com.ordana.spelunkery.reg.ModTags;
import com.ordana.spelunkery.utils.TranslationUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraft.world.level.block.Block;
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

                    var compressed = getCompressedNugget(foundItem);
                    if (compressed.isPresent()) {
                        ItemStack newStack = compressed.get();

                        if (!player.getInventory().add(newStack)) player.drop(newStack, false);
                        foundItem.shrink(9);
                    }
                }
            }

        }
    }

    public static void addOptional(ImmutableBiMap.Builder<Item, Item> map,
                                   String moddedId, String moddedId2) {
        var o1 = Registry.ITEM.getOptional(new ResourceLocation(moddedId));
        var o2 = Registry.ITEM.getOptional(new ResourceLocation(moddedId2));
        if (o1.isPresent() && o2.isPresent()) {
            map.put(o1.get(), o2.get());
        }
    }

    public static final Supplier<BiMap<Item, Item>> NUGGET_COMPACTING = Suppliers.memoize(() -> {

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

        addOptional(builder, "spelunkery:rough_jade_shard", "sullysmod:rough_jade");
        addOptional(builder, "spelunkery:raw_silver_nugget", "oreganized:raw_silver");
        addOptional(builder, "spelunkery:raw_lead_nugget", "oreganized:raw_lead");
        addOptional(builder, "spelunkery:raw_zinc_nugget", "create:raw_zinc");

        addOptional(builder, "spelunkery:jade_shard", "sullysmod:polished_jade");
        addOptional(builder, "oreganized:silver_nugget", "oreganized:silver_ingot");
        addOptional(builder, "oreganized:lead_nugget", "oreganized:lead_ingot");
        addOptional(builder, "create:zinc_nugget", "create:zinc_ingot");
        addOptional(builder, "create:brass_nugget", "create:brass_ingot");

        return builder.build();
    });

    public static Optional<ItemStack> getCompressedNugget(ItemStack stack) {
        return Optional.ofNullable(NUGGET_COMPACTING.get().get(stack.getItem()))
                .map(item -> item.asItem().getDefaultInstance());
    }

    public static final Supplier<BiMap<Item, Item>> INGOT_COMPACTING = Suppliers.memoize(() -> {

        var builder = ImmutableBiMap.<Item, Item>builder()
                .put(Items.IRON_INGOT, Items.IRON_BLOCK)
                .put(Items.GOLD_INGOT, Items.GOLD_BLOCK)
                .put(Items.COPPER_INGOT, Items.COPPER_BLOCK)

                .put(Items.RAW_IRON, Items.RAW_IRON_BLOCK)
                .put(Items.RAW_GOLD, Items.RAW_GOLD_BLOCK)
                .put(Items.RAW_COPPER, Items.RAW_COPPER_BLOCK)
                .put(ModItems.MAGNETITE_CHUNK.get(), ModBlocks.MAGNETITE.get().asItem())

                .put(ModItems.CINNABAR.get(), ModBlocks.CINNABAR_BLOCK.get().asItem())
                .put(Items.REDSTONE, Items.REDSTONE_BLOCK)
                .put(Items.LAPIS_LAZULI, Items.LAPIS_BLOCK)
                .put(Items.EMERALD, Items.EMERALD_BLOCK)
                .put(Items.DIAMOND, Items.DIAMOND_BLOCK)

                .put(ModItems.ROUGH_CINNABAR.get(), ModBlocks.ROUGH_CINNABAR_BLOCK.get().asItem())
                .put(ModItems.ROUGH_LAZURITE.get(), ModBlocks.ROUGH_LAZURITE_BLOCK.get().asItem())
                .put(ModItems.ROUGH_EMERALD.get(), ModBlocks.ROUGH_EMERALD_BLOCK.get().asItem())
                .put(ModItems.ROUGH_DIAMOND.get(), ModBlocks.ROUGH_DIAMOND_BLOCK.get().asItem());

        addOptional(builder, "sullysmod:rough_jade", "sullysmod:rough_jade_block");
        addOptional(builder, "oreganized:raw_silver", "oreganized:raw_silver_block");
        addOptional(builder, "oreganized:raw_lead", "oreganized:raw_lead_block");
        addOptional(builder, "create:raw_zinc", "create:raw_zinc_block");

        addOptional(builder, "sullysmod:polished_jade", "sullysmod:polished_jade_block");
        addOptional(builder, "oreganized:silver_ingot", "oreganized:silver_block");
        addOptional(builder, "oreganized:lead_ingot", "oreganized:lead_block");
        addOptional(builder, "create:zinc_ingot", "create:zinc_block");
        addOptional(builder, "create:brass_ingot", "create:brass_block");

        return builder.build();
    });

    public static Optional<ItemStack> getCompressedIngot(ItemStack stack) {
        return Optional.ofNullable(INGOT_COMPACTING.get().get(stack.getItem()))
                .map(item -> item.asItem().getDefaultInstance());
    }
}
