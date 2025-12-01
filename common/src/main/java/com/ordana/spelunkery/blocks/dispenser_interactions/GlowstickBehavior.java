package com.ordana.spelunkery.blocks.dispenser_interactions;

import com.ordana.spelunkery.entities.ThrownGlowstickEntity;
import com.ordana.spelunkery.items.GlowstickItem;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class GlowstickBehavior extends ProjectileBehavior {

    protected GlowstickBehavior(Item item) {
        super(item);
    }

    @Override
    protected Projectile getProjectileEntity(BlockSource source, Position position, ItemStack stackIn) {
        var entity = new ThrownGlowstickEntity(source.level(), position.x(), position.y(), position.z());
        entity.setItem(stackIn.copyWithCount(1));
        if (stackIn.getItem() instanceof GlowstickItem stick) entity.setColor(stick.getColor());
        return entity;
    }
}