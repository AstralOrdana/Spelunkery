package com.ordana.spelunkery.blocks.dispenser_interactions;

import com.ordana.spelunkery.entities.GlowstickEntity;
import com.ordana.spelunkery.entities.PebbleEntity;
import com.ordana.spelunkery.items.GlowstickItem;
import net.mehvahdjukaar.moonlight.api.util.DispenserHelper;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

public class GlowstickBehavior extends DispenserHelper.AdditionalDispenserBehavior {

    public GlowstickBehavior(Item item) {
        super(item);
    }

    @Override
    protected InteractionResultHolder<ItemStack> customBehavior(BlockSource source, ItemStack stack) {
        Level world = source.getLevel();
        Position dispensePosition = DispenserBlock.getDispensePosition(source);
        Direction direction = source.getBlockState().getValue(DispenserBlock.FACING);
        Projectile projectileEntity = this.getProjectileEntity(world, dispensePosition, stack);
        projectileEntity.shoot(direction.getStepX(), direction.getStepY() + 0.1F, direction.getStepZ(), this.getProjectileVelocity(), this.getProjectileInaccuracy());
        world.addFreshEntity(projectileEntity);
        stack.shrink(1);
        return InteractionResultHolder.success(stack);
    }

    @Override
    protected void playSound(BlockSource source, boolean success) {
        source.getLevel().playSound(null, source.x() + 0.5, source.y() + 0.5, source.z() + 0.5, SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (source.getLevel().getRandom().nextFloat() * 0.4F + 0.8F));
    }

    protected Projectile getProjectileEntity(Level worldIn, Position position, ItemStack stackIn) {
        var glowstick = new GlowstickEntity(worldIn, null);
        glowstick.setItem(stackIn);
        if (stackIn.getItem() instanceof GlowstickItem glick) glick.getColor(glick);
        return glowstick;
    }

    protected float getProjectileInaccuracy() {
        return 7.0F;
    }

    protected float getProjectileVelocity() {
        return 0.9F;
    }

}