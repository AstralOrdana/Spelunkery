package com.ordana.spelunkery.mixins;

import com.ordana.spelunkery.reg.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.function.Predicate;

@Mixin(LayeredCauldronBlock.class)
public class WaterCauldronBlockMixin extends AbstractCauldronBlock {

    public WaterCauldronBlockMixin(Properties properties, Map<Item, CauldronInteraction> map) {
        super(properties, map);
    }

    @Inject(method = "entityInside", at = @At("HEAD"))
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, CallbackInfo ci) {
        if (entity instanceof ItemEntity item && state.is(Blocks.WATER_CAULDRON) && this.isEntityInsideContent(state, pos, entity)) {
            ItemStack itemStack2 = item.getItem();
            if (itemStack2.is(ModItems.SALT.get()) && (level.getBlockState(pos.below()).is(BlockTags.FIRE) || level.getBlockState(pos.below()).is(BlockTags.CAMPFIRES))) {
                int count = itemStack2.getCount();
                ItemStack itemStack = new ItemStack(ModItems.ROCK_SALT.get());
                itemStack.setCount(count);
                item.setItem(itemStack);
                LayeredCauldronBlock.lowerFillLevel(state, level, pos);
            }
        }
    }

    @Shadow
    public boolean isFull(BlockState state) {
        return false;
    }
}
