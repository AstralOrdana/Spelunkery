package com.ordana.spelunkery.events;

import com.ordana.spelunkery.recipes.GrindstonePolishingRecipe;
import com.ordana.spelunkery.reg.ModBlocks;
import com.ordana.spelunkery.reg.ModItems;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.ArrayList;
import java.util.List;


public class ModEvents {

    @FunctionalInterface
    public interface InteractionEvent {
        InteractionResult run(Item i, ItemStack stack,
                              BlockPos pos,
                              BlockState state,
                              Player player, Level level,
                              InteractionHand hand,
                              BlockHitResult hit);
    }

    private static final List<InteractionEvent> EVENTS = new ArrayList<>();

    static {
        EVENTS.add(ModEvents::obsidianDraining);
        EVENTS.add(ModEvents::polishingRecipe);
    }


    public static InteractionResult onBlockCLicked(ItemStack stack, Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        if (stack.isEmpty()) return InteractionResult.PASS;
        Item i = stack.getItem();
        BlockPos pos = hitResult.getBlockPos();
        BlockState state = level.getBlockState(pos);
        for (var event : EVENTS) {
            var result = event.run(i, stack, pos, state, player, level, hand, hitResult);
            if (result != InteractionResult.PASS) return result;
        }
        return InteractionResult.PASS;
    }

    private static InteractionResult obsidianDraining(Item item, ItemStack stack, BlockPos pos, BlockState state,
                                                  Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {

        if (item == Items.GLASS_BOTTLE) {
            if (state.getBlock() instanceof CryingObsidianBlock) {
                level.playSound(player, pos, SoundEvents.RESPAWN_ANCHOR_DEPLETE, SoundSource.BLOCKS, 1.0f, 1.0f);
                ParticleUtils.spawnParticlesOnBlockFaces(level, pos, ParticleTypes.FALLING_OBSIDIAN_TEAR, UniformInt.of(3, 5));
                    if (player instanceof ServerPlayer serverPlayer) {
                        ItemStack itemStack2 = ItemUtils.createFilledResult(stack, player, ModItems.PORTAL_FLUID_BOTTLE.get().getDefaultInstance());
                        player.setItemInHand(hand, itemStack2);
                        //if (!player.getAbilities().instabuild) stack.shrink(1);
                        level.setBlockAndUpdate(pos, Blocks.OBSIDIAN.defaultBlockState());
                        CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger( serverPlayer, pos, stack);
                    }
                    return InteractionResult.sidedSuccess(level.isClientSide);

            }
        }
        return InteractionResult.PASS;
    }


    private static InteractionResult polishingRecipe(Item item, ItemStack stack, BlockPos pos, BlockState state,
                                                      Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {

        if (state.getBlock() instanceof GrindstoneBlock) {
            /* Code below modified and adapted from Sully's Mod: https://github.com/Uraneptus/Sullys-Mod/
            Specific section modified:https://github.com/Uraneptus/Sullys-Mod/blob/1.19.x/src/main/java/com/uraneptus/sullysmod/core/events/SMPlayerEvents.java#L33-L96
            Significant changes include: addition of byproducts, location of code within mod, particle creation based on ground item.
            Used under GNU LESSER GENERAL PUBLIC LICENSE, full text can be found in root/LICENSE
             */

            ArrayList<GrindstonePolishingRecipe> recipes = new ArrayList<>(GrindstonePolishingRecipe.getRecipes(level));
            for (GrindstonePolishingRecipe polishingRecipe : recipes) {
                if (!recipes.isEmpty()) {
                    RandomSource random = level.getRandom();
                    ItemStack ingredient = polishingRecipe.ingredient;
                    ItemStack result = polishingRecipe.getResultItem();
                    int resultCount = polishingRecipe.getResultCount();
                    ItemStack byproduct = polishingRecipe.getByproduct();
                    int byproductCount = random.nextIntBetweenInclusive(polishingRecipe.getByproductMin(), polishingRecipe.getByproductMax());
                    int xpAmount = polishingRecipe.getExperience();
                    if (stack.is(ingredient.getItem())) {
                        if (polishingRecipe.isRequiresDiamondGrindstone() && !state.is(ModBlocks.DIAMOND_GRINDSTONE.get())) {
                            ParticleUtils.spawnParticlesOnBlockFaces(level, pos, ParticleTypes.SMOKE, UniformInt.of(3, 5));
                            player.swing(hand);
                            level.playSound(player, pos, SoundEvents.SHIELD_BREAK, SoundSource.BLOCKS, 0.5F, 0.0F);
                            return InteractionResult.sidedSuccess(level.isClientSide);
                        }
                        ItemStack resultItem = result.copy();
                        ItemStack byproductItem = byproduct.copy();
                        if (player.isShiftKeyDown()) {
                            int ingredientCount = stack.getCount();

                            if (!player.getAbilities().instabuild) {
                                stack.shrink(ingredientCount);
                            }
                            if (!player.getInventory().add(new ItemStack(resultItem.getItem(), resultCount * ingredientCount))) {
                                player.drop(new ItemStack(resultItem.getItem(), resultCount * ingredientCount), false);
                            }
                            if (!player.getInventory().add(new ItemStack(byproductItem.getItem(), byproductCount * ingredientCount))) {
                                player.drop(new ItemStack(byproductItem.getItem(), byproductCount * ingredientCount), false);
                            }
                            if (!(xpAmount == 0)) {
                                for (int i = 0; i <= ingredientCount; i++) {
                                    int dropXp = random.nextInt(2);
                                    if (dropXp < 1) {
                                        xpAmount = xpAmount + polishingRecipe.getExperience();
                                    }
                                }
                                level.addFreshEntity(new ExperienceOrb(level, pos.getX(), pos.getY() + 1, pos.getZ(), xpAmount));
                            }
                        } else {
                            resultItem.setCount(resultCount);
                            byproductItem.setCount(byproductCount);
                            if (!player.getAbilities().instabuild) {
                                stack.shrink(1);
                            }
                            if (!player.getInventory().add(new ItemStack(resultItem.getItem(), resultCount))) {
                                player.drop(new ItemStack(resultItem.getItem(), resultCount), false);
                            }
                            if (!player.getInventory().add(new ItemStack(byproduct.getItem(), byproductCount))) {
                                player.drop(new ItemStack(byproductItem.getItem(), byproductCount), false);
                            }
                            if (!(xpAmount == 0)) {
                                int canDropXp = random.nextInt(2);

                                if (canDropXp < 1) {
                                    level.addFreshEntity(new ExperienceOrb(level, pos.getX(), pos.getY() + 1, pos.getZ(), xpAmount));
                                }
                            }
                        }
                        if (!resultItem.is(Items.AIR)) ParticleUtils.spawnParticlesOnBlockFaces(level, pos, new ItemParticleOption(ParticleTypes.ITEM, resultItem), UniformInt.of(3, 5));
                        else ParticleUtils.spawnParticlesOnBlockFaces(level, pos, new ItemParticleOption(ParticleTypes.ITEM, byproductItem), UniformInt.of(3, 5));
                        player.swing(hand);
                        level.playSound(player, pos, SoundEvents.GRINDSTONE_USE, SoundSource.BLOCKS, 0.5F, 0.0F);
                        return InteractionResult.sidedSuccess(level.isClientSide);
                    }
                }
            }
        }
        return InteractionResult.PASS;
    }

}