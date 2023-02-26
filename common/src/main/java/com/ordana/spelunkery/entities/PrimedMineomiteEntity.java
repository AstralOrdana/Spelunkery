package com.ordana.spelunkery.entities;

import com.ordana.spelunkery.blocks.GlowstickBlock;
import com.ordana.spelunkery.reg.ModBlocks;
import com.ordana.spelunkery.reg.ModEntities;
import com.ordana.spelunkery.reg.ModItems;
import net.mehvahdjukaar.moonlight.api.entity.ImprovedProjectileEntity;
import net.mehvahdjukaar.moonlight.api.platform.ForgeHelper;
import net.mehvahdjukaar.moonlight.api.platform.PlatformHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RodBlock;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.HashMap;

public class PrimedMineomiteEntity extends ImprovedProjectileEntity {

    public PrimedMineomiteEntity(EntityType<? extends PrimedMineomiteEntity> type, Level world) {
        super(type, world);
    }

    public PrimedMineomiteEntity(Level level, LivingEntity thrower) {
        super(ModEntities.PRIMED_MINEOMITE.get(), thrower, level);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.MINEOMITE.get();
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            ParticleOptions particle = ParticleTypes.ELECTRIC_SPARK;

            for (int i = 0; i < 8; ++i) {
                this.level.addParticle(particle, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
            }
        }

    }

    @Override
    public void tick() {
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

        if (!this.level.isClientSide) {
            this.createExplosion();
            this.level.broadcastEntityEvent(this, (byte) 10);
        }
        this.discard();

    }

    private void createExplosion() {

        boolean breaks = this.getOwner() instanceof Player ||
                PlatformHelper.isMobGriefingOn(this.level, this.getOwner());

        Explosion explosion = new Explosion(this.level,this,
                this.getX(), this.getY() + 0.25, this.getZ(), 4.0f, false, breaks ? Explosion.BlockInteraction.BREAK : Explosion.BlockInteraction.NONE);
        explosion.explode();
        explosion.finalizeExplosion(true);
    }
}
