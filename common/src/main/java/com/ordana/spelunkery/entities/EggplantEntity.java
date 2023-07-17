package com.ordana.spelunkery.entities;

import com.ordana.spelunkery.reg.ModEntities;
import com.ordana.spelunkery.reg.ModItems;
import net.mehvahdjukaar.moonlight.api.entity.ImprovedProjectileEntity;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class EggplantEntity extends ImprovedProjectileEntity {

    public EggplantEntity(EntityType<? extends EggplantEntity> type, Level world) {
        super(type, world);
    }

    public EggplantEntity(Level level, LivingEntity thrower) {
        super(ModEntities.EGGPLANT.get(), thrower, level);
    }


    @Override
    protected Item getDefaultItem() {
        return ModItems.EGGPLANT.get();
    }



    public void handleEntityEvent(byte id) {
        if (id == 3) {

            for(int i = 0; i < 8; ++i) {
                this.level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(ModItems.EGGPLANT::get)), this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
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
        if (entity instanceof LivingEntity livingEntity) d = Math.max(0.0D, 1.0D - livingEntity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
        Vec3 vec3 = this.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D).normalize().scale(0.6D * d);
        if (vec3.lengthSqr() > 0.0D) {
            entity.push(vec3.x, 0.1D, vec3.z);
        }
        if (entity instanceof Sheep sheep) sheep.setColor(DyeColor.PURPLE);
        if (entity instanceof Villager villager && level instanceof ServerLevel serverLevel) {
            Witch witch = EntityType.WITCH.create(serverLevel);
            witch.moveTo(villager.getX(), villager.getY(), villager.getZ(), villager.getYRot(), villager.getXRot());
            witch.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(witch.blockPosition()), MobSpawnType.CONVERSION, null, null);
            witch.setNoAi(villager.isNoAi());
            if (villager.hasCustomName()) {
                witch.setCustomName(villager.getCustomName());
                witch.setCustomNameVisible(villager.isCustomNameVisible());
            }

            witch.setPersistenceRequired();
            serverLevel.addFreshEntityWithPassengers(witch);
            villager.discard();
        }
        if (entity instanceof EnderDragonPart dragon) dragon.hurt(DamageSource.thrown(this, this.getOwner()), 6969);
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level.isClientSide) {
            this.level.broadcastEntityEvent(this, (byte)3);
            level.playSound(null, getX(), getY(), getZ(), SoundEvents.SLIME_SQUISH, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
            this.discard();
        }
    }

}