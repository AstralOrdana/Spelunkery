package com.ordana.spelunkery.mixins;

import com.ordana.spelunkery.configs.CommonConfigs;
import com.ordana.spelunkery.reg.ModItems;
import com.ordana.spelunkery.reg.ModTags;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.Objects;

@Mixin(LayeredCauldronBlock.class)
public class WaterCauldronBlockMixin extends AbstractCauldronBlock {

    @Shadow @Final public static IntegerProperty LEVEL;

    public WaterCauldronBlockMixin(Properties properties, Map<Item, CauldronInteraction> map) {
        super(properties, map);
    }

    @Inject(method = "entityInside", at = @At("HEAD"))
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, CallbackInfo ci) {
        if (entity instanceof ItemEntity item && state.is(Blocks.WATER_CAULDRON) && this.isEntityInsideContent(state, pos, entity) && level instanceof ServerLevel serverLevel) {
            ItemStack itemStack2 = item.getItem();
            if (itemStack2.is(ModItems.SALT.get()) && serverLevel.getBlockState(pos.below()).is(ModTags.CAN_BOIL_WATER)) {
                int count = itemStack2.getCount();
                ItemStack itemStack = new ItemStack(ModItems.ROCK_SALT.get());
                itemStack.setCount(count);
                item.setItem(itemStack);
                LayeredCauldronBlock.lowerFillLevel(state, serverLevel, pos);
            }
            if (itemStack2.is(Items.SLIME_BLOCK) && CommonConfigs.SLIME_CAULDRONS.get() && state.getValue(LEVEL) == 3) {
                Slime slime = EntityType.SLIME.create(serverLevel);
                if (slime != null) {
                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer) Objects.requireNonNull(item.getOwner()), pos, itemStack2);
                    item.remove(Entity.RemovalReason.DISCARDED);
                    slime.moveTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                    serverLevel.addFreshEntity(slime);
                    serverLevel.setBlockAndUpdate(pos, Blocks.CAULDRON.defaultBlockState());
                }
            }
        }
    }

    @Shadow
    public boolean isFull(BlockState state) {
        return false;
    }
}
