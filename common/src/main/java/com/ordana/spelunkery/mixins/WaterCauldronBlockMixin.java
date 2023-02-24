package com.ordana.spelunkery.mixins;

import com.ordana.spelunkery.configs.CommonConfigs;
import com.ordana.spelunkery.reg.ModItems;
import com.ordana.spelunkery.reg.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.function.Predicate;

@Mixin(LayeredCauldronBlock.class)
public class WaterCauldronBlockMixin extends AbstractCauldronBlock {

    @Shadow @Final public static IntegerProperty LEVEL;

    public WaterCauldronBlockMixin(Properties properties, Map<Item, CauldronInteraction> map) {
        super(properties, map);
    }

    @Inject(method = "entityInside", at = @At("HEAD"))
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, CallbackInfo ci) {
        if (entity instanceof ItemEntity item && state.is(Blocks.WATER_CAULDRON) && this.isEntityInsideContent(state, pos, entity)) {
            ItemStack itemStack2 = item.getItem();
            if (itemStack2.is(ModItems.SALT.get()) && level.getBlockState(pos.below()).is(ModTags.CAN_BOIL_WATER)) {
                int count = itemStack2.getCount();
                ItemStack itemStack = new ItemStack(ModItems.ROCK_SALT.get());
                itemStack.setCount(count);
                item.setItem(itemStack);
                LayeredCauldronBlock.lowerFillLevel(state, level, pos);
            }
            if (itemStack2.is(Items.SLIME_BALL) && CommonConfigs.SLIME_CAULDRONS.get() && state.getValue(LEVEL) == 3) {
                item.remove(Entity.RemovalReason.DISCARDED);
                Slime slime = EntityType.SLIME.create(level);
                if (slime != null) {
                    slime.moveTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                    level.addFreshEntity(slime);
                    level.setBlockAndUpdate(pos, Blocks.CAULDRON.defaultBlockState());
                }
            }
        }
    }

    @Shadow
    public boolean isFull(BlockState state) {
        return false;
    }
}
