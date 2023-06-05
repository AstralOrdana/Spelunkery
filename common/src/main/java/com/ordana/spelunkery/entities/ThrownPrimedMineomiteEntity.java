package com.ordana.spelunkery.entities;

import com.ordana.spelunkery.reg.ModEntities;
import com.ordana.spelunkery.reg.ModItems;
import net.mehvahdjukaar.moonlight.api.entity.ImprovedProjectileEntity;
import net.mehvahdjukaar.moonlight.api.platform.PlatformHelper;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public class ThrownPrimedMineomiteEntity extends ImprovedProjectileEntity {

    public ThrownPrimedMineomiteEntity(EntityType<? extends ThrownPrimedMineomiteEntity> type, Level world) {
        super(type, world);
    }

    public ThrownPrimedMineomiteEntity(Level level, LivingEntity thrower) {
        super(ModEntities.THROWN_PRIMED_MINEOMITE.get(), thrower, level);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.MINEOMITE.get();
    }

    @Override
    public void tick() {
        level.addParticle(ParticleTypes.FLAME, this.position().x, this.position().y + 0.5, this.position().z, 0.0D, 0.0D, 0.0D);
        level.addParticle(ParticleTypes.SMOKE, this.position().x, this.position().y + 0.5, this.position().z, 0.0D, 0.0D, 0.0D);
        super.tick();
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level.isClientSide) {
            if (!this.isRemoved()) {
                this.reachedEndOfLife();
            }
        }
    }

    @Override
    protected void updateRotation() {
    }

    //createMiniExplosion
    @Override
    public void reachedEndOfLife() {
        this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.GENERIC_EXPLODE, SoundSource.NEUTRAL, 1.5F, 1f);
        if (!this.level.isClientSide && !this.isInWater()) {
            this.createExplosion();
            this.level.broadcastEntityEvent(this, (byte) 10);
        }
        this.discard();

    }

    private void createExplosion() {

        boolean breaks = (this.getOwner() instanceof Player ||
                PlatformHelper.isMobGriefingOn(this.level, this.getOwner()));

        this.level.explode(null, this.getX(), this.getY(), this.getZ(),
                3.5F, breaks ? Explosion.BlockInteraction.BREAK : Explosion.BlockInteraction.NONE);

    }
}
