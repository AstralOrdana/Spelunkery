package com.ordana.spelunkery.entities;

import com.ordana.spelunkery.reg.ModEntities;
import com.ordana.spelunkery.reg.ModItems;
import com.ordana.spelunkery.reg.ModSoundEvents;
import net.mehvahdjukaar.moonlight.api.entity.ImprovedProjectileEntity;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class PebbleEntity extends ImprovedProjectileEntity {


    public PebbleEntity(EntityType<? extends PebbleEntity> type, Level world) {
        super(type, world);
    }

    public PebbleEntity(Level level, LivingEntity thrower, ItemStack item) {
        super(ModEntities.PEBBLE.get(), thrower, level);
        this.setItem(item);
    }


    @Override
    protected Item getDefaultItem() {
        return ModItems.STONE_PEBBLE.get();
    }

    public void handleEntityEvent(byte id) {
        if (id == 3) {

            for(int i = 0; i < 8; ++i) {
                this.level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, this.getItem()), this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
            }
        }

    }

    @Override
    protected void updateRotation() {
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity entity = result.getEntity();
        double d = 0;
        if (entity instanceof LivingEntity livingEntity) {
            if (livingEntity instanceof Player) level.playSound(null, getX(), getY(), getZ(), this.getItem().is(ModItems.END_STONE_PEBBLE.get()) ? ModSoundEvents.KNOB.get() : ModSoundEvents.BONK.get(), SoundSource.NEUTRAL, 0.5F, 1.5F / (level.getRandom().nextFloat() * 1.5F + 0.8F));
            d = Math.max(0.0D, 1.0D - livingEntity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
        }
        Vec3 vec3 = this.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D).normalize().scale(0.6D * d);
        if (vec3.lengthSqr() > 0.0D) {
            entity.push(vec3.x, 0.1D, vec3.z);
        }
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level.isClientSide) {
            this.level.broadcastEntityEvent(this, (byte)3);
            level.playSound(null, getX(), getY(), getZ(), SoundEvents.TUFF_BREAK, SoundSource.NEUTRAL, 0.5F, 1.5F / (level.getRandom().nextFloat() * 1.5F + 0.8F));
            this.discard();
        }
    }

}