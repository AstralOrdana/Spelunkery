package com.ordana.spelunkery.events;

import com.ordana.spelunkery.Spelunkery;
import com.ordana.spelunkery.configs.CommonConfigs;
import com.ordana.spelunkery.reg.ModBlockProperties;
import com.ordana.spelunkery.reg.ModBlocks;
import com.ordana.spelunkery.reg.ModItems;
import com.ordana.spelunkery.reg.ModTags;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.mehvahdjukaar.moonlight.api.client.util.ParticleUtil;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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
        EVENTS.add(ModEvents::saltBoiling);
        EVENTS.add(ModEvents::anvilRepairing);
        EVENTS.add(ModEvents::disenchant);
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

    private static InteractionResult saltBoiling(Item item, ItemStack stack, BlockPos pos, BlockState state,
                                                 Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        if (item == ModItems.SALT.get()) {
            if (state.is(Blocks.WATER_CAULDRON) && level.getBlockState(pos.below()).is(ModTags.CAN_BOIL_WATER)) {
                level.playSound(player, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0f, 1.0f);
                level.playSound(player, pos, SoundEvents.GENERIC_SPLASH, SoundSource.BLOCKS, 1.0f, 1.0f);
                if (player instanceof ServerPlayer serverPlayer) {
                    ItemStack itemStack2 = ItemUtils.createFilledResult(stack, player, ModItems.ROCK_SALT.get().getDefaultInstance());
                    player.setItemInHand(hand, itemStack2);
                    if (state.getValue(LayeredCauldronBlock.LEVEL) > 1) level.setBlockAndUpdate(pos, Blocks.WATER_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, state.getValue(LayeredCauldronBlock.LEVEL) - 1));
                    else level.setBlockAndUpdate(pos, Blocks.CAULDRON.defaultBlockState());

                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, stack);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);

            }
        }
        return InteractionResult.PASS;
    }

    private static InteractionResult anvilRepairing(Item item, ItemStack stack, BlockPos pos, BlockState state,
                                                    Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        if (stack.is(ModTags.ANVIL_REPAIR_ITEM)) {
            if (state.is(BlockTags.ANVIL) && !state.is(Blocks.ANVIL)) {
                level.playSound(player, pos, SoundEvents.ANVIL_HIT, SoundSource.BLOCKS, 1.0f, 1.0f);
                level.playSound(player, pos, SoundEvents.ANVIL_PLACE, SoundSource.BLOCKS, 1.0f, 1.0f);
                ParticleUtils.spawnParticlesOnBlockFaces(level, pos, new BlockParticleOption(ParticleTypes.BLOCK, state), UniformInt.of(3, 5));
                if (player instanceof ServerPlayer serverPlayer) {
                    if (!player.getAbilities().instabuild) stack.shrink(1);
                    if (state.is(Blocks.CHIPPED_ANVIL)) level.setBlockAndUpdate(pos, Blocks.ANVIL.defaultBlockState().getBlock().withPropertiesOf(state));
                    else if (state.is(Blocks.DAMAGED_ANVIL)) level.setBlockAndUpdate(pos, Blocks.CHIPPED_ANVIL.defaultBlockState().getBlock().withPropertiesOf(state));
                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, stack);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);

            }
        }
        return InteractionResult.PASS;
    }

    private static InteractionResult disenchant(Item item, ItemStack stack, BlockPos pos, BlockState state,
                                                     Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        var depleted = (state.is(ModBlocks.DIAMOND_GRINDSTONE.get()) && state.getValue(ModBlockProperties.DEPLETION) == 3) || state.is(Blocks.GRINDSTONE);
        if (!player.isCrouching()) return InteractionResult.PASS;
        if (!state.is(Blocks.GRINDSTONE) && !state.is(ModBlocks.DIAMOND_GRINDSTONE.get())) return InteractionResult.PASS;

        //handle enchants
        if (stack.isEnchanted() && CommonConfigs.GRINDSTONE_DISENCHANTS_TOOLS.get()) {
            if (level instanceof ServerLevel serverLevel) ExperienceOrb.award(serverLevel, Vec3.atCenterOf(pos), getExperienceFromItem(stack, depleted));
            player.setItemInHand(hand, removeEnchants(stack, stack.getDamageValue(), depleted));
            return InteractionResult.SUCCESS;
        }
        else if (stack.is(ModTags.GRINDSTONE_REPAIR_ITEM) && state.is(ModBlocks.DIAMOND_GRINDSTONE.get()) && state.getValue(ModBlockProperties.DEPLETION) > 0) {
            if (player instanceof ServerPlayer serverPlayer) CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, stack);
            level.setBlockAndUpdate(pos, state.setValue(ModBlockProperties.DEPLETION, 0));
            if (!player.getAbilities().instabuild) stack.shrink(1);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    public static ItemInteractionResult useGrindstone(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, boolean diamond) {
        var itemStack = player.getItemInHand(hand);

        if (diamond && state.getValue(ModBlockProperties.DEPLETION) >= 3)
            diamond = false;

        if (!itemStack.is(ModTags.GRINDABLE) && (!diamond || !itemStack.is(ModTags.DIAMOND_GRINDABLE)))
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        // effects
        if (level.isClientSide()) {
            ParticleUtil.spawnParticlesOnBlockFaces(level, pos, new ItemParticleOption(ParticleTypes.ITEM, itemStack), UniformInt.of(3, 5), -0.05f, 0.05f, false);
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        var lootParams = (new LootParams.Builder((ServerLevel) level))
                .withParameter(LootContextParams.BLOCK_STATE, level.getBlockState(pos))
                .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
                .withParameter(LootContextParams.TOOL, ItemStack.EMPTY)
                .create(LootContextParamSets.BLOCK);

        // find loot table for held item
        var itemName = Utils.getID(itemStack.getItem()).getPath();
        ResourceLocation tablePath;
        LootTable lootTable;
        ObjectArrayList<ItemStack> lootItem = ObjectArrayList.of();

        if (diamond) {
            tablePath = Spelunkery.res("gameplay/diamond_grindstone_polishing/" + itemName);
            lootTable = level.getServer().reloadableRegistries().getLootTable(ResourceKey.create(Registries.LOOT_TABLE, tablePath));
            lootItem = lootTable.getRandomItems(lootParams);
        }

        if (lootItem.isEmpty()) {
            diamond = false;
            tablePath = Spelunkery.res("gameplay/grindstone_polishing/" + itemName);
            lootTable = level.getServer().reloadableRegistries().getLootTable(ResourceKey.create(Registries.LOOT_TABLE, tablePath));
            lootItem = lootTable.getRandomItems(lootParams);
        }

        // fail if no loot table present
        if (lootItem.isEmpty()) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        var deplChance = 0;

        // give loot items and xp
        for (ItemStack stack : lootItem) {
            if (stack.is(Items.EXPERIENCE_BOTTLE)) {
                ExperienceOrb.award((ServerLevel) level, Vec3.atCenterOf(pos), 1);
                continue;
            }
            if (stack.is(Items.BEDROCK)) {
                deplChance += 1;
                continue;
            }
            if (!player.getInventory().add(stack)) {
                player.drop(stack, false);
            }
        }

        // depletion
        if (diamond) {
            var depl = CommonConfigs.DIAMOND_GRINDSTONE_DEPLETE_CHANCE.get();
            for (int i = 0; i < deplChance; ++i) {
                var chance = depl == 0 ? 0 : level.random.nextInt(CommonConfigs.DIAMOND_GRINDSTONE_DEPLETE_CHANCE.get());
                if (chance == 1)
                    level.setBlockAndUpdate(pos, state.setValue(ModBlockProperties.DEPLETION, state.getValue(ModBlockProperties.DEPLETION) + 1));
            }
        }

        // subtract
        if (!player.getAbilities().instabuild && !level.isClientSide()) {
            itemStack.shrink(1);
        }

        level.playSound(null, pos, SoundEvents.GRINDSTONE_USE, SoundSource.BLOCKS, 0.5F, 0.0F);

        player.startUsingItem(hand);
        player.releaseUsingItem();
        return ItemInteractionResult.SUCCESS;
    }


    private static int getExperienceFromItem(ItemStack stack, boolean depleted) {
        int i = 0;
        ItemEnchantments map = EnchantmentHelper.getEnchantmentsForCrafting(stack);

        for (Object2IntMap.Entry<Holder<Enchantment>> enchantmentIntegerEntry : map.entrySet()) {
            Holder<Enchantment> enchantment = enchantmentIntegerEntry.getKey();
            int integer = enchantmentIntegerEntry.getIntValue();
            if (!enchantment.is(EnchantmentTags.CURSE) || !depleted) {
                i += enchantment.value().getMinCost(integer);
            }
        }

        return i;
    }

    private static ItemStack removeEnchants(ItemStack stack, int damage, boolean depleted) {
        ItemStack itemStack = stack.copy();
        itemStack.remove(DataComponents.ENCHANTMENTS);
        itemStack.remove(DataComponents.STORED_ENCHANTMENTS);
        if (damage > 0) {
            itemStack.setDamageValue(damage);
        } else {
            itemStack.remove(DataComponents.DAMAGE);
        }

        itemStack.setCount(1);
        ItemEnchantments.Mutable mutable = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
        Map<Holder<Enchantment>, Integer> map = EnchantmentHelper.getEnchantmentsForCrafting(stack).entrySet().stream().filter((entry) -> (depleted || !entry.getKey().is(EnchantmentTags.CURSE)) && entry.getKey().is(EnchantmentTags.CURSE)).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        map.forEach(mutable::set);
        EnchantmentHelper.setEnchantments(itemStack, mutable.toImmutable());
        itemStack.set(DataComponents.REPAIR_COST, 0);
        if (itemStack.is(Items.ENCHANTED_BOOK) && map.isEmpty()) {
            itemStack = new ItemStack(Items.BOOK);
            if (stack.has(DataComponents.CUSTOM_DATA)) {
                itemStack.set(DataComponents.CUSTOM_NAME, stack.getHoverName());
            }
        }

        return itemStack;
    }
}