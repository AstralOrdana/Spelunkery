package com.ordana.spelunkery.mixins;

import com.ordana.spelunkery.configs.CommonConfigs;
import com.ordana.spelunkery.reg.ModComponents;
import com.ordana.spelunkery.reg.ModItems;
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
        //if (!this.level.isClientSide) {

            ItemStack stack = this.getItem();
            boolean active = stack.getOrDefault(ModComponents.ACTIVE.get(), false);

            if (active && this.getItem().is(ModItems.ITEM_MAGNET.get())) {
                int r = CommonConfigs.MAGNET_RANGE.get();
                AABB area = new AABB(this.position().add(-r, -r, -r), this.position().add(r, r, r));


                List<ItemEntity> items = level.getEntities(EntityType.ITEM, area,
                        item -> item.isAlive() && (/*!level.isClientSide || */item.tickCount > 1) &&
                                (item.getOwner() == null || !item.hasPickUpDelay()) &&
                                !item.getItem().isEmpty() /*&& !item.getPersistentData().contains("PreventRemoteMovement") && this.canPickupStack(tag, item.getItem())*/
                );



                items.forEach(item -> {

                    Vec3 vec3 = new Vec3(this.getX() - item.getX(), this.getY() - item.getY(), this.getZ() - item.getZ());
                    double d = vec3.lengthSqr();
                    if (d < 64.0) {
                        double e = 1.0 - Math.sqrt(d) / 8.0;
                        item.setDeltaMovement(item.getDeltaMovement().add(vec3.normalize().scale(e * e * 0.1)));
                    }
                });
            }

        //}
        super.tick();
    }
}
