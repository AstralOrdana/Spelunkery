package com.ordana.underground_overhaul.mixins;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CryingObsidianBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CryingObsidianBlock.class)
public class CryingObsidianMixin extends Block {
    public CryingObsidianMixin(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        ItemStack stack = player.getUseItem();
        Item item = stack.getItem();

        if (item == Items.GLASS_BOTTLE) {
            level.playSound(player, pos, SoundEvents.RESPAWN_ANCHOR_DEPLETE, SoundSource.BLOCKS, 1.0f, 1.0f);
            ParticleUtils.spawnParticlesOnBlockFaces(level, pos, ParticleTypes.DRIPPING_OBSIDIAN_TEAR, UniformInt.of(3, 5));
            if (player instanceof ServerPlayer serverPlayer) {
                if (!player.getAbilities().instabuild) stack.shrink(1);
                level.setBlockAndUpdate(pos, Blocks.OBSIDIAN.defaultBlockState());
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger( serverPlayer, pos, stack);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }
}
