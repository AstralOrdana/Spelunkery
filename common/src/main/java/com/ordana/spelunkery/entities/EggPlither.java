package com.ordana.spelunkery.entities;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PowerableMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.entity.projectile.windcharge.WindCharge;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public class EggPlither extends WitherBoss implements PowerableMob, RangedAttackMob {
    private static final EntityDataAccessor<Integer> DATA_TARGET_A;
    private static final EntityDataAccessor<Integer> DATA_TARGET_B;
    private static final EntityDataAccessor<Integer> DATA_TARGET_C;
    private static final List<EntityDataAccessor<Integer>> DATA_TARGETS;
    private static final EntityDataAccessor<Integer> DATA_ID_INV;
    private static final int INVULNERABLE_TICKS = 220;
    private final float[] xRotHeads = new float[2];
    private final float[] yRotHeads = new float[2];
    private final float[] xRotOHeads = new float[2];
    private final float[] yRotOHeads = new float[2];
    private final int[] nextHeadUpdate = new int[2];
    private final int[] idleHeadUpdates = new int[2];
    private int destroyBlocksTick;
    private final ServerBossEvent bossEvent;
    private static final Predicate<LivingEntity> LIVING_ENTITY_SELECTOR;
    private static final TargetingConditions TARGETING_CONDITIONS;

    public EggPlither(EntityType<? extends WitherBoss> entityType, Level level) {
        super(entityType, level);
        this.bossEvent = (ServerBossEvent)(new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.PROGRESS)).setDarkenScreen(true);
        this.moveControl = new FlyingMoveControl(this, 10, false);
        this.setHealth(this.getMaxHealth());
        this.xpReward = 50;
    }

    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation flyingPathNavigation = new FlyingPathNavigation(this, level);
        flyingPathNavigation.setCanOpenDoors(false);
        flyingPathNavigation.setCanFloat(true);
        flyingPathNavigation.setCanPassDoors(true);
        return flyingPathNavigation;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new EggPlither.WitherDoNothingGoal());
        //this.goalSelector.addGoal(2, new RangedAttackGoal(this, 1.0, 40, 20.0F));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomFlyingGoal(this, 1.0));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        //this.targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[0]));
        //this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, LivingEntity.class, 0, false, false, LIVING_ENTITY_SELECTOR));
    }

    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_TARGET_A, 0);
        builder.define(DATA_TARGET_B, 0);
        builder.define(DATA_TARGET_C, 0);
        builder.define(DATA_ID_INV, 0);
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Invul", this.getInvulnerableTicks());
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setInvulnerableTicks(compound.getInt("Invul"));
        if (this.hasCustomName()) {
            this.bossEvent.setName(this.getDisplayName());
        }

    }

    public void setCustomName(@Nullable Component name) {
        super.setCustomName(name);
        this.bossEvent.setName(this.getDisplayName());
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.RABBIT_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.RABBIT_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.RABBIT_DEATH;
    }

    public void aiStep() {
        Vec3 vec3 = this.getDeltaMovement().multiply(1.0, 0.6, 1.0);
        if (!this.level().isClientSide && this.getAlternativeTarget(0) > 0) {
            Entity entity = this.level().getEntity(this.getAlternativeTarget(0));
            if (entity != null) {
                double d = vec3.y;
                if (this.getY() < entity.getY() || !this.isPowered() && this.getY() < entity.getY() + 5.0) {
                    d = Math.max(0.0, d);
                    d += 0.3 - d * 0.6000000238418579;
                }

                vec3 = new Vec3(vec3.x, d, vec3.z);
                Vec3 vec32 = new Vec3(entity.getX() - this.getX(), 0.0, entity.getZ() - this.getZ());
                if (vec32.horizontalDistanceSqr() > 9.0) {
                    Vec3 vec33 = vec32.normalize();
                    vec3 = vec3.add(vec33.x * 0.3 - vec3.x * 0.6, 0.0, vec33.z * 0.3 - vec3.z * 0.6);
                }
            }
        }

        this.setDeltaMovement(vec3);
        if (vec3.horizontalDistanceSqr() > 0.05) {
            this.setYRot((float) Mth.atan2(vec3.z, vec3.x) * 57.295776F - 90.0F);
        }

        super.aiStep();

        int i;
        for(i = 0; i < 2; ++i) {
            this.yRotOHeads[i] = this.yRotHeads[i];
            this.xRotOHeads[i] = this.xRotHeads[i];
        }

        int j;
        for(i = 0; i < 2; ++i) {
            j = this.getAlternativeTarget(i + 1);
            Entity entity2 = null;
            if (j > 0) {
                entity2 = this.level().getEntity(j);
            }

            if (entity2 != null) {
                double e = this.getHeadX(i + 1);
                double f = this.getHeadY(i + 1);
                double g = this.getHeadZ(i + 1);
                double h = entity2.getX() - e;
                double k = entity2.getEyeY() - f;
                double l = entity2.getZ() - g;
                double m = Math.sqrt(h * h + l * l);
                float n = (float)(Mth.atan2(l, h) * 57.2957763671875) - 90.0F;
                float o = (float)(-(Mth.atan2(k, m) * 57.2957763671875));
                this.xRotHeads[i] = this.rotlerp(this.xRotHeads[i], o, 40.0F);
                this.yRotHeads[i] = this.rotlerp(this.yRotHeads[i], n, 10.0F);
            } else {
                this.yRotHeads[i] = this.rotlerp(this.yRotHeads[i], this.yBodyRot, 10.0F);
            }
        }

        boolean bl = this.isPowered();

        for(j = 0; j < 3; ++j) {
            double p = this.getHeadX(j);
            double q = this.getHeadY(j);
            double r = this.getHeadZ(j);
            float s = 0.3F * this.getScale();
            this.level().addParticle(ParticleTypes.HEART, p + this.random.nextGaussian() * (double)s, q + this.random.nextGaussian() * (double)s, r + this.random.nextGaussian() * (double)s, 0.0, 0.0, 0.0);
            if (bl && this.level().random.nextInt(4) == 0) {
                this.level().addParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, 0.7F, 0.7F, 0.5F), p + this.random.nextGaussian() * (double)s, q + this.random.nextGaussian() * (double)s, r + this.random.nextGaussian() * (double)s, 0.0, 0.0, 0.0);
            }
        }

        if (this.getInvulnerableTicks() > 0) {
            float t = 3.3F * this.getScale();

            for(int u = 0; u < 3; ++u) {
                this.level().addParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, 0.7F, 0.7F, 0.9F), this.getX() + this.random.nextGaussian(), this.getY() + (double)(this.random.nextFloat() * t), this.getZ() + this.random.nextGaussian(), 0.0, 0.0, 0.0);
            }
        }

    }

    protected void customServerAiStep() {
        int i;
        if (this.getInvulnerableTicks() > 0) {
            i = this.getInvulnerableTicks() - 1;
            this.bossEvent.setProgress(1.0F - (float)i / 220.0F);
            if (i <= 0) {
                this.level().explode(this, this.getX(), this.getEyeY(), this.getZ(), 7.0F, false, Level.ExplosionInteraction.MOB);
                if (!this.isSilent()) {
                    this.level().globalLevelEvent(1023, this.blockPosition(), 0);
                }
            }

            this.setInvulnerableTicks(i);
            if (this.tickCount % 10 == 0) {
                this.heal(10.0F);
            }

        } else {
            super.customServerAiStep();

            int j;
            for(i = 1; i < 3; ++i) {
                if (this.tickCount >= this.nextHeadUpdate[i - 1]) {
                    this.nextHeadUpdate[i - 1] = this.tickCount + 10 + this.random.nextInt(10);
                    if (this.level().getDifficulty() == Difficulty.NORMAL || this.level().getDifficulty() == Difficulty.HARD) {
                        int[] var10000 = this.idleHeadUpdates;
                        int var10001 = i - 1;
                        int var10003 = var10000[i - 1];
                        var10000[var10001] = var10000[i - 1] + 1;
                        if (var10003 > 15) {
                            float f = 10.0F;
                            float g = 5.0F;
                            double d = Mth.nextDouble(this.random, this.getX() - 10.0, this.getX() + 10.0);
                            double e = Mth.nextDouble(this.random, this.getY() - 5.0, this.getY() + 5.0);
                            double h = Mth.nextDouble(this.random, this.getZ() - 10.0, this.getZ() + 10.0);
                            this.performRangedAttack(i + 1, d, e, h, true);
                            this.idleHeadUpdates[i - 1] = 0;
                        }
                    }

                    j = this.getAlternativeTarget(i);
                    if (j > 0) {
                        LivingEntity livingEntity = (LivingEntity)this.level().getEntity(j);
                        if (livingEntity != null && this.canAttack(livingEntity) && !(this.distanceToSqr(livingEntity) > 900.0) && this.hasLineOfSight(livingEntity)) {
                            this.performRangedAttack(i + 1, livingEntity);
                            this.nextHeadUpdate[i - 1] = this.tickCount + 40 + this.random.nextInt(20);
                            this.idleHeadUpdates[i - 1] = 0;
                        } else {
                            this.setAlternativeTarget(i, 0);
                        }
                    } else {
                        List<LivingEntity> list = this.level().getNearbyEntities(LivingEntity.class, TARGETING_CONDITIONS, this, this.getBoundingBox().inflate(20.0, 8.0, 20.0));
                        if (!list.isEmpty()) {
                            LivingEntity livingEntity2 = (LivingEntity)list.get(this.random.nextInt(list.size()));
                            this.setAlternativeTarget(i, livingEntity2.getId());
                        }
                    }
                }
            }

            if (this.getTarget() != null) {
                this.setAlternativeTarget(0, this.getTarget().getId());
            } else {
                this.setAlternativeTarget(0, 0);
            }

            if (this.destroyBlocksTick > 0) {
                --this.destroyBlocksTick;
                if (this.destroyBlocksTick == 0 && this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                    boolean bl = false;
                    j = Mth.floor(this.getBbWidth() / 2.0F + 1.0F);
                    int k = Mth.floor(this.getBbHeight());
                    Iterator var16 = BlockPos.betweenClosed(this.getBlockX() - j, this.getBlockY(), this.getBlockZ() - j, this.getBlockX() + j, this.getBlockY() + k, this.getBlockZ() + j).iterator();

                    label74:
                    while(true) {
                        BlockPos blockPos;
                        BlockState blockState;
                        do {
                            if (!var16.hasNext()) {
                                if (bl) {
                                    this.level().levelEvent(null, 1022, this.blockPosition(), 0);
                                }
                                break label74;
                            }

                            blockPos = (BlockPos)var16.next();
                            blockState = this.level().getBlockState(blockPos);
                        } while(!canDestroy(blockState));

                        bl = this.level().destroyBlock(blockPos, true, this) || bl;
                    }
                }
            }

            if (this.tickCount % 20 == 0) {
                this.heal(1.0F);
            }

            this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
        }
    }

    public static boolean canDestroy(BlockState state) {
        return false;
    }

    public void makeInvulnerable() {
        this.setInvulnerableTicks(220);
        this.bossEvent.setProgress(0.0F);
        this.setHealth(this.getMaxHealth() / 3.0F);
    }

    public void makeStuckInBlock(BlockState state, Vec3 motionMultiplier) {
    }

    public void startSeenByPlayer(ServerPlayer serverPlayer) {
        super.startSeenByPlayer(serverPlayer);
        this.bossEvent.addPlayer(serverPlayer);
    }

    public void stopSeenByPlayer(ServerPlayer serverPlayer) {
        super.stopSeenByPlayer(serverPlayer);
        this.bossEvent.removePlayer(serverPlayer);
    }

    private double getHeadX(int head) {
        if (head <= 0) {
            return this.getX();
        } else {
            float f = (this.yBodyRot + (float)(180 * (head - 1))) * 0.017453292F;
            float g = Mth.cos(f);
            return this.getX() + (double)g * 1.3 * (double)this.getScale();
        }
    }

    private double getHeadY(int head) {
        float f = head <= 0 ? 3.0F : 2.2F;
        return this.getY() + (double)(f * this.getScale());
    }

    private double getHeadZ(int head) {
        if (head <= 0) {
            return this.getZ();
        } else {
            float f = (this.yBodyRot + (float)(180 * (head - 1))) * 0.017453292F;
            float g = Mth.sin(f);
            return this.getZ() + (double)g * 1.3 * (double)this.getScale();
        }
    }

    private float rotlerp(float angle, float targetAngle, float max) {
        float f = Mth.wrapDegrees(targetAngle - angle);
        if (f > max) {
            f = max;
        }

        if (f < -max) {
            f = -max;
        }

        return angle + f;
    }

    private void performRangedAttack(int head, LivingEntity target) {
        this.performRangedAttack(head, target.getX(), target.getY() + (double)target.getEyeHeight() * 0.5, target.getZ(), head == 0 && this.random.nextFloat() < 0.001F);
    }

    private void performRangedAttack(int head, double x, double y, double z, boolean isDangerous) {
        if (!this.isSilent()) {
            this.level().levelEvent((Player)null, 1024, this.blockPosition(), 0);
        }

        double d = this.getHeadX(head);
        double e = this.getHeadY(head);
        double f = this.getHeadZ(head);
        double g = x - d;
        double h = y - e;
        double i = z - f;
        Vec3 vec3 = new Vec3(g, h, i);
        WitherSkull witherSkull = new WitherSkull(this.level(), this, vec3.normalize());
        witherSkull.setOwner(this);
        if (isDangerous) {
            witherSkull.setDangerous(true);
        }

        witherSkull.setPosRaw(d, e, f);
        this.level().addFreshEntity(witherSkull);
    }

    public void performRangedAttack(LivingEntity target, float velocity) {
        this.performRangedAttack(0, target);
    }

    public boolean hurt(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else if (!source.is(DamageTypeTags.WITHER_IMMUNE_TO) && !(source.getEntity() instanceof WitherBoss)) {
            if (this.getInvulnerableTicks() > 0 && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
                return false;
            } else {
                Entity entity;
                if (this.isPowered()) {
                    entity = source.getDirectEntity();
                    if (entity instanceof AbstractArrow || entity instanceof WindCharge) {
                        return false;
                    }
                }

                entity = source.getEntity();
                if (entity != null && entity.getType().is(EntityTypeTags.WITHER_FRIENDS)) {
                    return false;
                } else {
                    if (this.destroyBlocksTick <= 0) {
                        this.destroyBlocksTick = 20;
                    }

                    for(int i = 0; i < this.idleHeadUpdates.length; ++i) {
                        int[] var10000 = this.idleHeadUpdates;
                        var10000[i] += 3;
                    }

                    return super.hurt(source, amount);
                }
            }
        } else {
            return false;
        }
    }

    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
    }

    public void checkDespawn() {
        this.noActionTime = 0;
    }

    public boolean addEffect(MobEffectInstance effectInstance, @Nullable Entity entity) {
        return false;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 300.0).add(Attributes.MOVEMENT_SPEED, 0.6000000238418579).add(Attributes.FLYING_SPEED, 0.6000000238418579).add(Attributes.FOLLOW_RANGE, 40.0).add(Attributes.ARMOR, 4.0);
    }

    public float getHeadYRot(int head) {
        return this.yRotHeads[head];
    }

    public float getHeadXRot(int head) {
        return this.xRotHeads[head];
    }

    public int getInvulnerableTicks() {
        return (Integer)this.entityData.get(DATA_ID_INV);
    }

    public void setInvulnerableTicks(int invulnerableTicks) {
        this.entityData.set(DATA_ID_INV, invulnerableTicks);
    }

    public int getAlternativeTarget(int head) {
        return (Integer)this.entityData.get((EntityDataAccessor)DATA_TARGETS.get(head));
    }

    public void setAlternativeTarget(int targetOffset, int newId) {
        this.entityData.set((EntityDataAccessor)DATA_TARGETS.get(targetOffset), newId);
    }

    public boolean isPowered() {
        return this.getHealth() <= this.getMaxHealth() / 2.0F;
    }

    protected boolean canRide(Entity vehicle) {
        return true;
    }

    public boolean canUsePortal(boolean allowPassengers) {
        return false;
    }

    public boolean canBeAffected(MobEffectInstance effectInstance) {
        return effectInstance.is(MobEffects.WITHER) ? false : super.canBeAffected(effectInstance);
    }

    static {
        DATA_TARGET_A = SynchedEntityData.defineId(EggPlither.class, EntityDataSerializers.INT);
        DATA_TARGET_B = SynchedEntityData.defineId(EggPlither.class, EntityDataSerializers.INT);
        DATA_TARGET_C = SynchedEntityData.defineId(EggPlither.class, EntityDataSerializers.INT);
        DATA_TARGETS = ImmutableList.of(DATA_TARGET_A, DATA_TARGET_B, DATA_TARGET_C);
        DATA_ID_INV = SynchedEntityData.defineId(EggPlither.class, EntityDataSerializers.INT);
        LIVING_ENTITY_SELECTOR = (livingEntity) -> false;
        TARGETING_CONDITIONS = TargetingConditions.forCombat().range(20.0).selector(LIVING_ENTITY_SELECTOR);
    }

    class WitherDoNothingGoal extends Goal {
        public WitherDoNothingGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
        }

        public boolean canUse() {
            return EggPlither.this.getInvulnerableTicks() > 0;
        }
    }
}
