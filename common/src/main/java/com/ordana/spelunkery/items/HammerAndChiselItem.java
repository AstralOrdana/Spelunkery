package com.ordana.spelunkery.items;

import com.google.common.base.Suppliers;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.ordana.spelunkery.configs.ClientConfigs;
import com.ordana.spelunkery.reg.ModItems;
import com.ordana.spelunkery.reg.ModTags;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.PortalShape;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class HammerAndChiselItem extends Item implements Vanishable {
    public HammerAndChiselItem(Properties properties) {
        super(properties);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag context) {
        if (ClientConfigs.ENABLE_TOOLTIPS.get()) {
            if (stack.is(ModItems.FLINT_HAMMER_AND_CHISEL.get())) tooltip.add(Component.translatable("tooltip.spelunkery.flint_hammer_and_chisel").setStyle(Style.EMPTY.applyFormat(ChatFormatting.DARK_PURPLE)));
            if (stack.is(ModItems.OBSIDIAN_HAMMER_AND_CHISEL.get())) tooltip.add(Component.translatable("tooltip.spelunkery.obsidian_hammer_and_chisel").setStyle(Style.EMPTY.applyFormat(ChatFormatting.DARK_PURPLE)));
            if (!Screen.hasShiftDown()) {
                tooltip.add(Component.translatable("tooltip.spelunkery.hold_crouch").setStyle(Style.EMPTY.applyFormat(ChatFormatting.GOLD)));
            }
            if (Screen.hasShiftDown()) {
                tooltip.add(Component.translatable("tooltip.spelunkery.hammer_and_chisel_1").setStyle(Style.EMPTY.applyFormat(ChatFormatting.GRAY)));
                tooltip.add(Component.translatable("tooltip.spelunkery.hammer_and_chisel_2").setStyle(Style.EMPTY.applyFormat(ChatFormatting.GRAY)));
                tooltip.add(Component.translatable("tooltip.spelunkery.hammer_and_chisel_3").setStyle(Style.EMPTY.applyFormat(ChatFormatting.GRAY)));
                tooltip.add(Component.translatable("tooltip.spelunkery.hammer_and_chisel_4").setStyle(Style.EMPTY.applyFormat(ChatFormatting.GRAY)));
            }
        }
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack repairCandidate) {
        return (stack.is(ModItems.OBSIDIAN_HAMMER_AND_CHISEL.get()) && repairCandidate.is(Items.OBSIDIAN)) || (stack.is(ModItems.FLINT_HAMMER_AND_CHISEL.get()) && repairCandidate.is(Items.FLINT));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();
        BlockState state = level.getBlockState(pos);
        InteractionHand hand = context.getHand();
        if (player.isSecondaryUseActive()) {
            var chiseled = getChiseled(state);

            if (chiseled.isPresent()) {
                level.playSound(player, pos, SoundEvents.ANVIL_HIT, SoundSource.BLOCKS, 1.0f, 1.0f);
                level.playSound(player, pos, state.getBlock().getSoundType(state).getBreakSound(), SoundSource.BLOCKS, 1.0f, 1.0f);
                ParticleUtils.spawnParticlesOnBlockFaces(level, pos, new BlockParticleOption(ParticleTypes.BLOCK, state), UniformInt.of(3, 5));
                if (player instanceof ServerPlayer serverPlayer) {
                    if (!player.isCreative()) stack.hurtAndBreak(1, player, (l) -> l.broadcastBreakEvent(hand));
                    level.setBlockAndUpdate(pos, chiseled.get());
                    player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, stack);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        if (state.is(ModTags.CHISELABLE)) {
            if (stack.is(ModItems.OBSIDIAN_HAMMER_AND_CHISEL.get())
                    || (stack.is(ModItems.FLINT_HAMMER_AND_CHISEL.get()) && !state.is(BlockTags.NEEDS_IRON_TOOL) && !state.is(BlockTags.NEEDS_DIAMOND_TOOL))) {
                if (player instanceof ServerPlayer serverPlayer) {
                    if (!player.isCreative()) stack.hurtAndBreak(1, player, (l) -> l.broadcastBreakEvent(hand));
                    level.destroyBlock(pos, false, player);
                    level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), state.getBlock().getCloneItemStack(level, pos, state)));

                    player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, stack);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }

        return InteractionResult.PASS;
    }

    public static void addOptional(ImmutableBiMap.Builder<Block, Block> map,
                                   String moddedId, String moddedId2) {
        var o1 = Registry.BLOCK.getOptional(new ResourceLocation(moddedId));
        var o2 = Registry.BLOCK.getOptional(new ResourceLocation(moddedId2));
        if (o1.isPresent() && o2.isPresent()) {
            map.put(o1.get(), o2.get());
        }
    }



    public static final Supplier<BiMap<Block, Block>> CHISELED_BLOCKS = Suppliers.memoize(() -> {
        var botaniaChiselables = List.of(
                "livingrock_bricks",
                "dark_quartz",
                "mana_quartz",
                "blaze_quartz",
                "lavender_quartz",
                "red_quartz",
                "elf_quartz",
                "sunny_quartz",
                "metamorphic_forest_bricks",
                "metamorphic_plains_bricks",
                "metamorphic_mountain_bricks",
                "metamorphic_fungal_bricks",
                "metamorphic_swamp_bricks",
                "metamorphic_desert_bricks",
                "metamorphic_taiga_bricks",
                "metamorphic_mesa_bricks"
        );

        var malumChiselables = List.of(
                "tainted_rock",
                "twisted_rock"
        );

        var quarkChiselables = List.of(
                "granite_bricks",
                "diorite_bricks",
                "andesite_bricks",
                "calcite_bricks",
                "dripstone_bricks",
                "tuff_bricks",
                "limestone_bricks",
                "jasper_bricks",
                "shale_bricks",
                "myalite_bricks",
                "soul_sandstone_bricks"
        );


        var builder = ImmutableBiMap.<Block, Block>builder()
                .put(Blocks.STONE_BRICKS, Blocks.CHISELED_STONE_BRICKS)
                .put(Blocks.INFESTED_STONE_BRICKS, Blocks.INFESTED_CHISELED_STONE_BRICKS)
                .put(Blocks.POLISHED_DEEPSLATE, Blocks.CHISELED_DEEPSLATE)
                .put(Blocks.NETHER_BRICKS, Blocks.CHISELED_NETHER_BRICKS)
                .put(Blocks.QUARTZ_BLOCK, Blocks.CHISELED_QUARTZ_BLOCK)
                .put(Blocks.CUT_SANDSTONE, Blocks.CHISELED_SANDSTONE)
                .put(Blocks.CUT_RED_SANDSTONE, Blocks.CHISELED_RED_SANDSTONE)
                .put(Blocks.POLISHED_BLACKSTONE, Blocks.CHISELED_POLISHED_BLACKSTONE);
        addOptional(builder, "minecraft:prismarine_bricks", "immersive_weathering:chiseled_prismarine_bricks");
        for (var chiselable : botaniaChiselables) addOptional(builder, "botania:" + chiselable, "botania:chiseled_" + chiselable);
        for (var chiselable : malumChiselables) addOptional(builder, "malum:" + chiselable, "malum:chiseled_" + chiselable);
        for (var chiselable : quarkChiselables) addOptional(builder, "quark:" + chiselable, "quark:chiseled_" + chiselable);
        return builder.build();
    });

    public static Optional<BlockState> getChiseled(BlockState state) {
        return Optional.ofNullable(CHISELED_BLOCKS.get().get(state.getBlock()))
                .map(block -> block.withPropertiesOf(state));
    }
}
