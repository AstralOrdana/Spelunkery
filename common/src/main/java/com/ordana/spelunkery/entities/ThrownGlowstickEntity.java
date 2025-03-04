package com.ordana.spelunkery.entities;

import com.ordana.spelunkery.blocks.GlowstickBlock;
import com.ordana.spelunkery.reg.ModBlocks;
import com.ordana.spelunkery.reg.ModEntities;
import com.ordana.spelunkery.reg.ModItems;
import net.mehvahdjukaar.moonlight.api.entity.ImprovedProjectileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RodBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;

public class ThrownGlowstickEntity extends ImprovedProjectileEntity {
    private static final EntityDataAccessor<Integer> DATA_GLOWSTICK_COLOR;


    public ThrownGlowstickEntity(EntityType<? extends ThrownGlowstickEntity> type, Level world) {
        super(type, world);
    }

    public ThrownGlowstickEntity(Level level, LivingEntity thrower) {
        super(ModEntities.GLOWSTICK.get(), thrower, level);
    }

    public ThrownGlowstickEntity(Level worldIn, double x, double y, double z) {
        super(ModEntities.GLOWSTICK.get(), x, y, z, worldIn);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_GLOWSTICK_COLOR, DyeColor.RED.getId());
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        var color = this.getColor();
        compound.putByte("GlowstickColor", color == null ? 16 : (byte)color.getId());
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        int i = compound.getInt("GlowstickColor");
        this.setColor(i == 16 ? null : DyeColor.byId(i));
    }

    public DyeColor getColor() {
        int i = this.entityData.get(DATA_GLOWSTICK_COLOR);
        return i == 16 ? null : DyeColor.byId(i);
    }

    public void setColor(DyeColor glowstickColor) {
        if (glowstickColor != null) {
            this.entityData.set(DATA_GLOWSTICK_COLOR, glowstickColor.getId());
        }
        else this.entityData.set(DATA_GLOWSTICK_COLOR, 16);

    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.GLOWSTICK.get();
    }


    public void spawnTrailParticles(Vec3 currentPos, Vec3 newPos) {
        ParticleOptions particle = ParticleTypes.GLOW;

        for (int i = 0; i < 4; ++i) {
            this.level.addParticle(particle, this.getX(), this.getY() + 0.5, this.getZ(), 0.0D, 0.0D, 0.0D);
        }
        super.spawnTrailParticles(currentPos, newPos);
    }

    public static Block getGlowstickBlock(DyeColor color) {
        return DYE_COLOR_TO_BLOCK.getOrDefault(color, ModBlocks.GLOWSTICK.get());
    }

    protected void onHit(HitResult result) {
        if (result instanceof BlockHitResult bResult) placeGlowstick(level, bResult);
        super.onHit(result);
    }

    public void placeGlowstick(Level level, BlockHitResult hitResult) {

        Direction dir = hitResult.getDirection();
        BlockPos pos = hitResult.getBlockPos();
        BlockPos relativePos = pos.relative(dir);
        BlockState replaceState = level.getBlockState(relativePos);
        var glowstickCheck = getGlowstickBlock(this.getColor());

        if (replaceState.getMaterial().isReplaceable()) {
            var waterlogged = replaceState.getFluidState().is(Fluids.WATER);
            level.setBlockAndUpdate(relativePos, glowstickCheck.defaultBlockState().setValue(RodBlock.FACING, dir).setValue(GlowstickBlock.WATERLOGGED, waterlogged));
        }
        else level.addFreshEntity(new ItemEntity(level, relativePos.getX(), relativePos.getY(), relativePos.getZ(), new ItemStack(glowstickCheck)));
        this.remove(RemovalReason.DISCARDED);
    }

    protected float getWaterInertia() {
        return 0.95F;
    }

    static {
        DATA_GLOWSTICK_COLOR = SynchedEntityData.defineId(ThrownGlowstickEntity.class, EntityDataSerializers.INT);
    }

    public static final HashMap<DyeColor, Block> DYE_COLOR_TO_BLOCK = new HashMap<>();

    static {
        DYE_COLOR_TO_BLOCK.put(null, ModBlocks.GLOWSTICK.get());
        DYE_COLOR_TO_BLOCK.put(DyeColor.RED, ModBlocks.RED_GLOWSTICK.get());
        DYE_COLOR_TO_BLOCK.put(DyeColor.ORANGE, ModBlocks.ORANGE_GLOWSTICK.get());
        DYE_COLOR_TO_BLOCK.put(DyeColor.YELLOW, ModBlocks.YELLOW_GLOWSTICK.get());
        DYE_COLOR_TO_BLOCK.put(DyeColor.LIME, ModBlocks.LIME_GLOWSTICK.get());
        DYE_COLOR_TO_BLOCK.put(DyeColor.GREEN, ModBlocks.GREEN_GLOWSTICK.get());
        DYE_COLOR_TO_BLOCK.put(DyeColor.CYAN, ModBlocks.CYAN_GLOWSTICK.get());
        DYE_COLOR_TO_BLOCK.put(DyeColor.LIGHT_BLUE, ModBlocks.LIGHT_BLUE_GLOWSTICK.get());
        DYE_COLOR_TO_BLOCK.put(DyeColor.BLUE, ModBlocks.BLUE_GLOWSTICK.get());
        DYE_COLOR_TO_BLOCK.put(DyeColor.PURPLE, ModBlocks.PURPLE_GLOWSTICK.get());
        DYE_COLOR_TO_BLOCK.put(DyeColor.MAGENTA, ModBlocks.MAGENTA_GLOWSTICK.get());
        DYE_COLOR_TO_BLOCK.put(DyeColor.PINK, ModBlocks.PINK_GLOWSTICK.get());
        DYE_COLOR_TO_BLOCK.put(DyeColor.BROWN, ModBlocks.BROWN_GLOWSTICK.get());
        DYE_COLOR_TO_BLOCK.put(DyeColor.BLACK, ModBlocks.BLACK_GLOWSTICK.get());
        DYE_COLOR_TO_BLOCK.put(DyeColor.WHITE, ModBlocks.WHITE_GLOWSTICK.get());
        DYE_COLOR_TO_BLOCK.put(DyeColor.GRAY, ModBlocks.GRAY_GLOWSTICK.get());
        DYE_COLOR_TO_BLOCK.put(DyeColor.LIGHT_GRAY, ModBlocks.LIGHT_GRAY_GLOWSTICK.get());
    }
}
