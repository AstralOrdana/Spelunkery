package com.ordana.spelunkery.items;

import com.ordana.spelunkery.Spelunkery;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class DustBunItem extends Item {
    public DustBunItem(Properties properties) {
        super(properties);
    }

    @Override
    @NotNull
    public InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        BlockPos pos = player.blockPosition();
        if (level instanceof ServerLevel) {
            if (player.isSecondaryUseActive()) {
                int bunCount = stack.getCount();
                for (int i = 0; i < bunCount; i++) {
                    LootTable lootTable = level.getServer().getLootTables().get(new ResourceLocation("oreganized"));

                    LootContext.Builder builder = new LootContext.Builder((ServerLevel) level)
                            .withRandom(level.random)
                            .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
                            .withOptionalParameter(LootContextParams.THIS_ENTITY, player);

                    var l = lootTable.getRandomItems(builder.create(LootContextParamSets.GIFT));
                    ItemStack bunStack = l.iterator().next();

                    if (bunStack != null) {
                        if (!player.getInventory().add(new ItemStack(bunStack.getItem()))) {
                            player.drop(new ItemStack(bunStack.getItem()), false);
                        }
                        stack.shrink(1);
                    }
                }
                return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
            } else {
                LootTable lootTable = level.getServer().getLootTables().get(Spelunkery.res("gameplay/dust_bun"));
                LootContext.Builder builder = new LootContext.Builder((ServerLevel) level)
                        .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(Vec3i.ZERO))
                        .withOptionalParameter(LootContextParams.THIS_ENTITY, player);

                var l = lootTable.getRandomItems(builder.create(LootContextParamSets.GIFT));
                ItemStack bunStack = l.iterator().next();

                if (bunStack != null) {
                    if (!player.getInventory().add(new ItemStack(bunStack.getItem()))) {
                        player.drop(new ItemStack(bunStack.getItem()), false);
                    }
                    stack.shrink(1);
                    return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
                }
            }
        }
        return new InteractionResultHolder<>(InteractionResult.FAIL, stack);
    }
}
