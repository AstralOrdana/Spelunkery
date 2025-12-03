package com.ordana.spelunkery.mixins;

import com.ordana.spelunkery.configs.CommonConfigs;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LavaCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(LavaCauldronBlock.class)
public abstract class LavaCauldronBlockMixin extends AbstractCauldronBlock {

    public LavaCauldronBlockMixin(Properties properties, CauldronInteraction.InteractionMap map) {
        super(properties, map);
    }

    @Override
    public boolean isFull(BlockState state) {
        return false;
    }

    @Inject(method = "entityInside", at = @At("HEAD"))
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, CallbackInfo ci) {
        if (entity instanceof ItemEntity item && this.isEntityInsideContent(state, pos, entity) && level instanceof ServerLevel serverLevel) {
            ItemStack itemStack = item.getItem();
            if (itemStack.is(Items.SLIME_BLOCK) && CommonConfigs.SLIME_CAULDRONS.get()) {
                Slime slime = EntityType.MAGMA_CUBE.create(serverLevel);
                if (slime != null) {
                    try {
                        ServerPlayer player = (ServerPlayer) item.getOwner();
                        CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(Objects.requireNonNull(player), pos, itemStack);
                    } catch (NullPointerException|ClassCastException ex) {
                        // ignored -- not thrown by a player
                    }
                    item.remove(Entity.RemovalReason.DISCARDED);
                    slime.moveTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                    serverLevel.addFreshEntity(slime);
                    serverLevel.setBlockAndUpdate(pos, Blocks.CAULDRON.defaultBlockState());
                }
            }
        }
    }

}
