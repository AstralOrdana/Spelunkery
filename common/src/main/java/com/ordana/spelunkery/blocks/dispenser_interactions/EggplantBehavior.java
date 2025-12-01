package com.ordana.spelunkery.blocks.dispenser_interactions;

import com.ordana.spelunkery.entities.ThrownEggplantEntity;
import com.ordana.spelunkery.reg.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

public class EggplantBehavior extends ProjectileBehavior {

    protected EggplantBehavior() {
        super(ModItems.EGGPLANT.get());
    }

    @Override
    protected Projectile getProjectileEntity(BlockSource source, Position position, ItemStack stackIn) {
        var entity = new ThrownEggplantEntity(source.level(), position.x(), position.y(), position.z());
        entity.setItem(stackIn.copyWithCount(1));
        return entity;
    }

    @Override
    protected InteractionResultHolder<ItemStack> customBehavior(BlockSource source, ItemStack stack) {
        Level world = source.level();
        Position dispensePosition = DispenserBlock.getDispensePosition(source);
        Direction direction = source.state().getValue(DispenserBlock.FACING);
        BlockPos frontPos = source.pos().relative(direction);
        if (!world.getBlockState(frontPos).getCollisionShape(world, frontPos).isEmpty()) {
            return InteractionResultHolder.fail(stack);
        }
        Projectile projectileEntity = this.getProjectileEntity(source, dispensePosition,  stack);
        projectileEntity.shoot(direction.getStepX(), direction.getStepY() + 0.1F, direction.getStepZ(), this.getProjectileVelocity(), this.getProjectileInaccuracy());
        world.addFreshEntity(projectileEntity);
        return InteractionResultHolder.success(stack);
    }
}