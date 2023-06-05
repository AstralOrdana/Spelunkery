package com.ordana.spelunkery.mixins;

import com.ordana.spelunkery.configs.CommonConfigs;
import com.ordana.spelunkery.reg.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(ItemFrame.class)
public abstract class ItemFrameMixin extends HangingEntity {

    @Shadow
    public abstract ItemStack getItem();

    protected ItemFrameMixin(EntityType<? extends HangingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void tick() {
        if (!this.level.isClientSide) {

            ItemStack stack = this.getItem();
            CompoundTag tag = stack.getOrCreateTag();

            if (tag.contains("active") && tag.getBoolean("active") && this.getItem().is(ModItems.ITEM_MAGNET.get())) {
                int r = CommonConfigs.MAGNET_RANGE.get();
                AABB area = new AABB(this.position().add(-r, -r, -r), this.position().add(r, r, r));


                List<ItemEntity> items = level.getEntities(EntityType.ITEM, area,
                        item -> item.isAlive() && (!level.isClientSide || item.tickCount > 1) &&
                                (item.getThrower() == null || !item.getThrower().equals(this.getUUID()) || !item.hasPickUpDelay()) &&
                                !item.getItem().isEmpty() /*&& !item.getPersistentData().contains("PreventRemoteMovement") && this.canPickupStack(tag, item.getItem())*/
                );

                items.forEach(item -> item.setDeltaMovement(item.getDeltaMovement().add(
                        new Vec3((this.position().x) - item.getX(), (this.position().y) - item.getY(), (this.position().z) - item.getZ()).normalize().scale(((1.4D - Math.sqrt(
                                new Vec3((this.position().x) - item.getX(), (this.position().y) - item.getY(), (this.position().z) - item.getZ()).lengthSqr()) / 8.0D) * (1.4D - Math.sqrt(
                                new Vec3((this.position().x) - item.getX(), (this.position().y) - item.getY(), (this.position().z) - item.getZ()).lengthSqr()) / 8.0D)) * 0.1D))));
            }

        }
        super.tick();
    }
}
