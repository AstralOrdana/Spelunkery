package com.ordana.spelunkery.blocks.dispenser_interactions;

import com.ordana.spelunkery.Spelunkery;
import com.ordana.spelunkery.configs.CommonConfigs;
import com.ordana.spelunkery.reg.ModBlockProperties;
import com.ordana.spelunkery.reg.ModBlocks;
import net.mehvahdjukaar.moonlight.api.util.DispenserHelper;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

public class GrindstoneBehavior extends DispenserHelper.AdditionalDispenserBehavior {

    protected GrindstoneBehavior(Item item) {
        super(item);
    }

    @Override
    protected InteractionResultHolder<ItemStack> customBehavior(BlockSource source, ItemStack itemStack) {
        ServerLevel level = source.level();
        BlockPos pos = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
        BlockState state = level.getBlockState(pos);
        if (!state.is(Blocks.GRINDSTONE) && !state.is(ModBlocks.DIAMOND_GRINDSTONE.get())) return InteractionResultHolder.pass(itemStack);


        var depleted = true;
        var diamondGrindstone = state.is(ModBlocks.DIAMOND_GRINDSTONE.get());
        if (diamondGrindstone) depleted = state.getValue(ModBlockProperties.DEPLETION) == 3;
        var itemName = Utils.getID(itemStack.getItem()).getPath();

        var tablePath = Spelunkery.res("gameplay/" + (diamondGrindstone && !depleted ? "diamond_" : "") + "grindstone_polishing/" + itemName);
        var lootTable = level.getServer().reloadableRegistries().getLootTable(ResourceKey.create(Registries.LOOT_TABLE, tablePath));

        LootParams.Builder builder = (new LootParams.Builder(level))
                .withParameter(LootContextParams.BLOCK_STATE, level.getBlockState(pos))
                .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
                .withParameter(LootContextParams.TOOL, ItemStack.EMPTY);

        var lootItem = lootTable.getRandomItems(builder.create(LootContextParamSets.BLOCK));

        if (lootItem.isEmpty()) {
            tablePath = Spelunkery.res("gameplay/grindstone_polishing/" + itemName);
            var lootTable2 = level.getServer().reloadableRegistries().getLootTable(ResourceKey.create(Registries.LOOT_TABLE, tablePath));

            lootItem = lootTable2.getRandomItems(builder.create(LootContextParamSets.BLOCK));
        }

        //fail if no loot table present
        if (lootItem.isEmpty()) {
            return InteractionResultHolder.fail(itemStack);
        }
        var deplChance = 0;
        itemStack.shrink(1);
        level.playSound(null, pos, SoundEvents.GRINDSTONE_USE, SoundSource.BLOCKS, 0.5F, 0.0F);

        //give loot items and xp
        for (ItemStack stack : lootItem) {
            if (stack.is(Items.EXPERIENCE_BOTTLE)) {
                ExperienceOrb.award(level, Vec3.atCenterOf(pos), 1);
                continue;
            }
            if (stack.is(Items.BEDROCK)) {
                deplChance += 1;
                continue;
            }
            var itemEntity = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, stack);
            itemEntity.setDeltaMovement(Vec3.ZERO);
            level.addFreshEntity(itemEntity);
        }

        //depletion
        if (tablePath.getPath().contains("diamond")) {
            var depl = CommonConfigs.DIAMOND_GRINDSTONE_DEPLETE_CHANCE.get();
            for (int i = 0; i < deplChance; ++i) {
                var chance = depl == 0 ? 0 : level.random.nextInt(CommonConfigs.DIAMOND_GRINDSTONE_DEPLETE_CHANCE.get());
                if (chance > 0 && diamondGrindstone) {
                    if (chance == 1 && !depleted)
                        level.setBlockAndUpdate(pos, state.setValue(ModBlockProperties.DEPLETION, state.getValue(ModBlockProperties.DEPLETION) + 1));
                }
            }
        }
        return InteractionResultHolder.success(itemStack);
    }
}
