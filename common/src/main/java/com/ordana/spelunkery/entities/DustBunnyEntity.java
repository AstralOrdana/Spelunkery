package com.ordana.spelunkery.entities;

import com.ordana.spelunkery.reg.ModItems;
import com.ordana.spelunkery.reg.ModTags;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class DustBunnyEntity extends PathfinderMob {
    private static final EntityDataAccessor<Integer> DATA_TYPE_ID;
    int moreCarrotTicks;

    public DustBunnyEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new DustBunnyEntity.RabbitMoveControl(this);
    }

    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (player instanceof ServerPlayer serverPlayer) CriteriaTriggers.PLAYER_INTERACTED_WITH_ENTITY.trigger(serverPlayer, itemStack, this);
        player.playSound(SoundEvents.WOOL_BREAK, 1.0F, 1.0F);
        player.addItem(new ItemStack(ModItems.DUST_BUN.get()));
        if (this.level().isClientSide) {
            for(int i = 0; i < 20; ++i) {
                double d0 = this.random.nextGaussian() * 0.02D;
                double d1 = this.random.nextGaussian() * 0.02D;
                double d2 = this.random.nextGaussian() * 0.02D;
                this.level().addParticle(ParticleTypes.POOF, this.getX() - d0 * 10.0D, this.getRandomY() - d1 * 10.0D, this.getZ() - d2 * 10.0D, d0, d1, d2);
            }
        }
        this.remove(RemovalReason.DISCARDED);
        return InteractionResult.SUCCESS;

    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(1, new ClimbOnTopOfPowderSnowGoal(this, this.level()));
        this.goalSelector.addGoal(1, new DustBunnyEntity.RabbitPanicGoal(this, 2.2D));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.0D, Ingredient.of(Items.CARROT, Items.GOLDEN_CARROT, Blocks.DANDELION), false));
        this.goalSelector.addGoal(4, new DustBunnyEntity.RabbitAvoidEntityGoal(this, Player.class, 8.0F, 2.2D, 2.2D));
        this.goalSelector.addGoal(4, new DustBunnyEntity.RabbitAvoidEntityGoal(this, Wolf.class, 10.0F, 2.2D, 2.2D));
        this.goalSelector.addGoal(4, new DustBunnyEntity.RabbitAvoidEntityGoal(this, Monster.class, 4.0F, 2.2D, 2.2D));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 0.6D));
        this.goalSelector.addGoal(11, new LookAtPlayerGoal(this, Player.class, 10.0F));
    }

    public void setSpeedModifier(double speedModifier) {
        this.getNavigation().setSpeedModifier(speedModifier);
        this.moveControl.setWantedPosition(this.moveControl.getWantedX(), this.moveControl.getWantedY(), this.moveControl.getWantedZ(), speedModifier);
    }


    public void customServerAiStep() {

        if (this.moreCarrotTicks > 0) {
            this.moreCarrotTicks -= this.random.nextInt(3);
            if (this.moreCarrotTicks < 0) {
                this.moreCarrotTicks = 0;
            }
        }

        this.onGround();
    }

    public boolean canSpawnSprintParticle() {
        return false;
    }


    public void aiStep() {
        super.aiStep();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 3.0D).add(Attributes.MOVEMENT_SPEED, 0.3D);
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("MoreCarrotTicks", this.moreCarrotTicks);
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.moreCarrotTicks = compound.getInt("MoreCarrotTicks");
    }

    protected SoundEvent getJumpSound() {
        return SoundEvents.RABBIT_JUMP;
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

    public SoundSource getSoundSource() {
        return SoundSource.NEUTRAL;
    }

    private static boolean isTemptingItem(ItemStack stack) {
        return stack.is(Items.CARROT) || stack.is(Items.GOLDEN_CARROT) || stack.is(Blocks.DANDELION.asItem());
    }

    public boolean isFood(ItemStack stack) {
        return isTemptingItem(stack);
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
        return super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
    }

    public static boolean checkRabbitSpawnRules(EntityType<Rabbit> rabbit, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return level.getBlockState(pos.below()).is(BlockTags.RABBITS_SPAWNABLE_ON);
    }

    boolean wantsMoreFood() {
        return this.moreCarrotTicks <= 0;
    }

    public void handleEntityEvent(byte id) {
        if (id == 1) {
            this.spawnSprintParticle();
        } else {
            super.handleEntityEvent(id);
        }

    }

    public Vec3 getLeashOffset() {
        return new Vec3(0.0D, 0.6F * this.getEyeHeight(), this.getBbWidth() * 0.4F);
    }

    static {
        DATA_TYPE_ID = SynchedEntityData.defineId(DustBunnyEntity.class, EntityDataSerializers.INT);
    }


    private static class RabbitMoveControl extends MoveControl {
        private final DustBunnyEntity rabbit;

        public RabbitMoveControl(DustBunnyEntity rabbit) {
            super(rabbit);
            this.rabbit = rabbit;
        }

        public void tick() {
            super.tick();
        }

        public void setWantedPosition(double x, double y, double z, double speed) {
            if (this.rabbit.isInWater()) {
                speed = 1.5D;
            }
            super.setWantedPosition(x, y, z, speed);
        }
    }

    static class RabbitPanicGoal extends PanicGoal {
        private final DustBunnyEntity rabbit;

        public RabbitPanicGoal(DustBunnyEntity rabbit, double d) {
            super(rabbit, d);
            this.rabbit = rabbit;
        }

        public void tick() {
            super.tick();
            this.rabbit.setSpeedModifier(this.speedModifier);
        }
    }

    static class RabbitAvoidEntityGoal<T extends LivingEntity> extends AvoidEntityGoal<T> {
        private final DustBunnyEntity rabbit;

        public RabbitAvoidEntityGoal(DustBunnyEntity rabbit, Class<T> class_, float f, double d, double e) {
            super(rabbit, class_, f, d, e);
            this.rabbit = rabbit;
        }

        public boolean canUse() {
            return super.canUse();
        }
    }
}