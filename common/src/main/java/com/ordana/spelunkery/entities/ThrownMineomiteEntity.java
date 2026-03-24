package com.ordana.spelunkery.entities;

import com.ordana.spelunkery.blocks.GlowstickBlock;
import com.ordana.spelunkery.blocks.MineomiteBlock;
import com.ordana.spelunkery.reg.ModBlocks;
import com.ordana.spelunkery.reg.ModEntities;
import com.ordana.spelunkery.reg.ModItems;
import net.mehvahdjukaar.moonlight.api.entity.ImprovedProjectileEntity;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RodBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class ThrownMineomiteEntity extends ImprovedProjectileEntity {

    public ThrownMineomiteEntity(EntityType<? extends ThrownMineomiteEntity> type, Level world) {
        super(type, world);
    }

    public ThrownMineomiteEntity(Level level, LivingEntity thrower) {
        super(ModEntities.MINEOMITE.get(), thrower, level);
    }

    public ThrownMineomiteEntity(Level worldIn, double x, double y, double z) {
        super(ModEntities.MINEOMITE.get(), x, y, z, worldIn);
    }


    @Override
    protected Item getDefaultItem() {
        return ModItems.MINEOMITE.get();
    }

    public static boolean canPlace (BlockState state) {
        return state.isAir() || state.canBeReplaced() || state.is(ModBlocks.MINEOMITE.get());
    }

    public void spawnTrailParticles() {
        if (this.isOnFire()) {
            for (int i = 0; i < 4; ++i) {
                this.level.addParticle(random.nextBoolean() ? ParticleTypes.FLAME : ParticleTypes.SMOKE, this.getX(), this.getY() + 0.5, this.getZ(), 0.0D, 0.0D, 0.0D);
            }
        }
        super.spawnTrailParticles();
    }

    protected void onHit(HitResult result) {
        if (this.isOnFire()) {
            if (!this.level.isClientSide) {
                if (!this.isRemoved()) {
                    this.reachedEndOfLife();
                }
            }
        }
        else if (result instanceof BlockHitResult bResult) placeGlowstick(level, bResult);
        super.onHit(result);
    }

    public void placeGlowstick(Level level, BlockHitResult hitResult) {

        Direction dir = hitResult.getDirection();
        BlockPos pos = hitResult.getBlockPos();
        BlockPos relativePos = pos.relative(dir);
        BlockState replaceState = level.getBlockState(relativePos);
        BlockState placeState = ModBlocks.MINEOMITE.get().defaultBlockState().setValue(RodBlock.FACING, dir).setValue(GlowstickBlock.WATERLOGGED, replaceState.getFluidState().is(Fluids.WATER));
        var sticks = 1;

        if (replaceState.is(ModBlocks.MINEOMITE.get()) && replaceState.getValue(MineomiteBlock.STICKS) < 9) sticks = replaceState.getValue(MineomiteBlock.STICKS) + 1;
        if (canPlace(replaceState)) level.setBlockAndUpdate(relativePos, placeState.setValue(MineomiteBlock.STICKS, sticks));
        else level.addFreshEntity(new ItemEntity(level, relativePos.getX(), relativePos.getY(), relativePos.getZ(), new ItemStack(getDefaultItem())));

        this.remove(RemovalReason.DISCARDED);
    }

    //createMiniExplosion
    @Override
    public void reachedEndOfLife() {
        this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.GENERIC_EXPLODE, SoundSource.NEUTRAL, 1.5F, 1f);
        if (!this.level.isClientSide()) {
            this.createExplosion();
            this.level.broadcastEntityEvent(this, (byte) 10);
        }
        this.discard();

    }

    private void createExplosion() {

        boolean breaks = (this.getOwner() instanceof Player ||
                PlatHelper.isMobGriefingOn(this.level, this.getOwner()));

        this.level.explode(null, this.getX(), this.getY(), this.getZ(),
                2F, breaks ? Level.ExplosionInteraction.TNT : Level.ExplosionInteraction.NONE);

    }
}
