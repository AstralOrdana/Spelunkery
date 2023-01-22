package com.ordana.spelunkery.items;

import com.google.common.base.Suppliers;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.ordana.spelunkery.reg.ModItems;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
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

import java.util.Optional;
import java.util.function.Supplier;

public class HammerAndChiselItem extends Item implements Vanishable {
    public HammerAndChiselItem(Properties properties) {
        super(properties);
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
        if (state.is(BlockTags.MINEABLE_WITH_PICKAXE)) {
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

    public static final Supplier<BiMap<Block, Block>> CHISELED_BLOCKS = Suppliers.memoize(() -> {
        var builder = ImmutableBiMap.<Block, Block>builder()
                .put(Blocks.STONE_BRICKS, Blocks.CHISELED_STONE_BRICKS)
                .put(Blocks.INFESTED_STONE_BRICKS, Blocks.INFESTED_CHISELED_STONE_BRICKS)
                .put(Blocks.POLISHED_DEEPSLATE, Blocks.CHISELED_DEEPSLATE)
                .put(Blocks.NETHER_BRICKS, Blocks.CHISELED_NETHER_BRICKS)
                .put(Blocks.QUARTZ_BLOCK, Blocks.CHISELED_QUARTZ_BLOCK)
                .put(Blocks.CUT_SANDSTONE, Blocks.CHISELED_SANDSTONE)
                .put(Blocks.CUT_RED_SANDSTONE, Blocks.CHISELED_RED_SANDSTONE)
                .put(Blocks.POLISHED_BLACKSTONE, Blocks.CHISELED_POLISHED_BLACKSTONE);
        return builder.build();
    });

    public static Optional<BlockState> getChiseled(BlockState state) {
        return Optional.ofNullable(CHISELED_BLOCKS.get().get(state.getBlock()))
                .map(block -> block.withPropertiesOf(state));
    }
}
