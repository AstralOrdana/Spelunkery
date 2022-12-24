package com.ordana.underground_overhaul.entities;

import com.google.common.base.Suppliers;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.ordana.underground_overhaul.items.GlowstickItem;
import com.ordana.underground_overhaul.reg.ModBlocks;
import com.ordana.underground_overhaul.reg.ModEntities;
import com.ordana.underground_overhaul.reg.ModItems;
import net.mehvahdjukaar.moonlight.api.entity.ImprovedProjectileEntity;
import net.mehvahdjukaar.moonlight.api.platform.ForgeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RodBlock;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Optional;
import java.util.function.Supplier;

public class GlowstickEntity extends ImprovedProjectileEntity {
    private static final EntityDataAccessor<Integer> DATA_GLOWSTICK_COLOR;


    public GlowstickEntity(EntityType<? extends GlowstickEntity> type, Level world) {
        super(type, world);
    }

    public GlowstickEntity(Level level, LivingEntity thrower) {
        super(ModEntities.GLOWSTICK.get(), thrower, level);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_GLOWSTICK_COLOR, DyeColor.RED.getId());
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putByte("GlowstickColor", (byte)this.getColor().getId());
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("GlowstickColor", 99)) {
            this.setColor(DyeColor.byId(compound.getInt("GlowstickColor")));
        }
    }

    public DyeColor getColor() {
        return DyeColor.byId(this.entityData.get(DATA_GLOWSTICK_COLOR));
    }

    public void setColor(DyeColor glowstickColor) {
        this.entityData.set(DATA_GLOWSTICK_COLOR, glowstickColor.getId());
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.GLOWSTICK.get();
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            ParticleOptions particle = ParticleTypes.GLOW;

            for (int i = 0; i < 8; ++i) {
                this.level.addParticle(particle, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
            }
        }

    }

    public static boolean canPlace (Level level, BlockPos pos, BlockState state) {
        if (state.isAir()) return true;
        else return false;
    }

    public void placeGlowstick(Level level, BlockPos pos, BlockHitResult hitResult) {

        Direction dir = hitResult.getDirection();
        BlockPos replacePos = pos;
        var glowstickCheck = getGlowstickBlock(this.getColor());
        var glowstickItem = getGlowstick(this.getColor());


        if (dir == Direction.NORTH || dir == Direction.WEST || dir == Direction.DOWN) replacePos = pos.relative(dir);
        BlockState replaceState = level.getBlockState(replacePos);
        if (!replaceState.isAir()) {
            if (glowstickItem.isPresent()) level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(glowstickItem.get())));
            else level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ModItems.GLOWSTICK.get())));
        }

        else if (dir == Direction.NORTH || dir == Direction.WEST || dir == Direction.DOWN) {
            replacePos = pos.relative(dir);
            if (glowstickCheck.isPresent()) level.setBlockAndUpdate(replacePos, glowstickCheck.get().defaultBlockState().setValue(RodBlock.FACING, dir));
            else level.setBlockAndUpdate(replacePos, ModBlocks.GLOWSTICK.get().defaultBlockState().setValue(RodBlock.FACING, dir));
        }
        else if (glowstickCheck.isPresent()) level.setBlockAndUpdate(pos, glowstickCheck.get().defaultBlockState().setValue(RodBlock.FACING, dir));
        else level.setBlockAndUpdate(replacePos, ModBlocks.GLOWSTICK.get().defaultBlockState().setValue(RodBlock.FACING, dir));

    }

    Supplier<BiMap<DyeColor, Block>> DYE_TO_BLOCK = Suppliers.memoize(() -> {
        var builder = ImmutableBiMap.<DyeColor, Block>builder()
                .put(DyeColor.RED, ModBlocks.RED_GLOWSTICK.get())
                .put(DyeColor.ORANGE, ModBlocks.ORANGE_GLOWSTICK.get())
                .put(DyeColor.YELLOW, ModBlocks.YELLOW_GLOWSTICK.get())
                .put(DyeColor.LIME, ModBlocks.LIME_GLOWSTICK.get())
                .put(DyeColor.GREEN, ModBlocks.GREEN_GLOWSTICK.get())
                .put(DyeColor.CYAN, ModBlocks.CYAN_GLOWSTICK.get())
                .put(DyeColor.LIGHT_BLUE, ModBlocks.LIGHT_BLUE_GLOWSTICK.get())
                .put(DyeColor.BLUE, ModBlocks.BLUE_GLOWSTICK.get())
                .put(DyeColor.PURPLE, ModBlocks.PURPLE_GLOWSTICK.get())
                .put(DyeColor.MAGENTA, ModBlocks.MAGENTA_GLOWSTICK.get())
                .put(DyeColor.PINK, ModBlocks.PINK_GLOWSTICK.get())
                .put(DyeColor.BROWN, ModBlocks.BROWN_GLOWSTICK.get())
                .put(DyeColor.BLACK, ModBlocks.BLACK_GLOWSTICK.get())
                .put(DyeColor.WHITE, ModBlocks.WHITE_GLOWSTICK.get())
                .put(DyeColor.GRAY, ModBlocks.GRAY_GLOWSTICK.get())
                .put(DyeColor.LIGHT_GRAY, ModBlocks.LIGHT_GRAY_GLOWSTICK.get());
        return builder.build();
    });

    public Optional<Block> getGlowstickBlock(DyeColor color) {
        return Optional.ofNullable(DYE_TO_BLOCK.get().get(color));
    }


    Supplier<BiMap<Item, DyeColor>> ITEM_TO_DYE = Suppliers.memoize(() -> {
        var builder = ImmutableBiMap.<Item, DyeColor>builder()
                .put(ModItems.RED_GLOWSTICK.get(), DyeColor.RED)
                .put(ModItems.ORANGE_GLOWSTICK.get(), DyeColor.ORANGE)
                .put(ModItems.YELLOW_GLOWSTICK.get(), DyeColor.YELLOW)
                .put(ModItems.LIME_GLOWSTICK.get(), DyeColor.LIME)
                .put(ModItems.GREEN_GLOWSTICK.get(), DyeColor.GREEN)
                .put(ModItems.CYAN_GLOWSTICK.get(), DyeColor.CYAN)
                .put(ModItems.LIGHT_BLUE_GLOWSTICK.get(), DyeColor.LIGHT_BLUE)
                .put(ModItems.BLUE_GLOWSTICK.get(), DyeColor.BLUE)
                .put(ModItems.PURPLE_GLOWSTICK.get(), DyeColor.PURPLE)
                .put(ModItems.MAGENTA_GLOWSTICK.get(), DyeColor.MAGENTA)
                .put(ModItems.PINK_GLOWSTICK.get(), DyeColor.PINK)
                .put(ModItems.BROWN_GLOWSTICK.get(), DyeColor.BROWN)
                .put(ModItems.BLACK_GLOWSTICK.get(), DyeColor.BLACK)
                .put(ModItems.WHITE_GLOWSTICK.get(), DyeColor.WHITE)
                .put(ModItems.GRAY_GLOWSTICK.get(), DyeColor.GRAY)
                .put(ModItems.LIGHT_GRAY_GLOWSTICK.get(), DyeColor.LIGHT_GRAY);
        return builder.build();
    });

    Supplier<BiMap<DyeColor, Item>> DYE_TO_ITEM = Suppliers.memoize(() -> ITEM_TO_DYE.get().inverse());

    public Optional<Item> getGlowstick(DyeColor color) {
        return Optional.ofNullable(DYE_TO_ITEM.get().get(color));
    }

    public void tick() {
        //base tick stuff
        this.baseTick();

        if (!this.hasBeenShot) {
            this.gameEvent(GameEvent.PROJECTILE_SHOOT, this.getOwner());
            this.hasBeenShot = true;
        }

        //fixed vanilla arrow code. You're welcome
        Vec3 movement = this.getDeltaMovement();

        double velX = movement.x;
        double velY = movement.y;
        double velZ = movement.z;

        /*
        //set initial rot
        if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
            float horizontalVel = MathHelper.sqrt(getHorizontalDistanceSqr(movement));
            this.yRot = (float) (MathHelper.atan2(velX, velZ) * (double) (180F / (float) Math.PI));
            this.xRot = (float) (MathHelper.atan2(velY, horizontalVel) * (double) (180F / (float) Math.PI));
            this.yRotO = this.yRot;
            this.xRotO = this.xRot;
        }*/

        boolean noPhysics = this.isNoPhysics();

        BlockPos blockpos = this.blockPosition();
        BlockState blockstate = this.level.getBlockState(blockpos);
        //sets on ground
        if (!blockstate.isAir() && !noPhysics) {
            VoxelShape voxelshape = blockstate.getCollisionShape(this.level, blockpos);
            if (!voxelshape.isEmpty()) {
                Vec3 vector3d1 = this.position();

                for (AABB aabb : voxelshape.toAabbs()) {
                    if (aabb.move(blockpos).contains(vector3d1)) {
                        this.touchedGround = true;
                        break;
                    }
                }
            }
        }

        if (this.isInWaterOrRain()) {
            this.clearFire();
        }


        if (this.touchedGround && !noPhysics) {
            this.groundTime++;
        } else {
            this.groundTime = 0;

            this.updateRotation();

            Vec3 pos = this.position();
            boolean client = this.level.isClientSide;

            Vec3 newPos = pos.add(movement);

            HitResult blockHitResult = this.level.clip(new ClipContext(pos, newPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));

            if (blockHitResult.getType() != HitResult.Type.MISS) {
                //get correct land pos
                if (!noPhysics) {
                    newPos = blockHitResult.getLocation();
                    BlockPos newBlockPos = new BlockPos(newPos.x, newPos.y, newPos.z);
                    placeGlowstick(level, newBlockPos, (BlockHitResult) blockHitResult);
                    this.remove(RemovalReason.DISCARDED);
                }
                //no physics clips through blocks
            }

            if (client) {
                this.spawnTrailParticles(pos, newPos);
            }

            double posX = newPos.x;
            double posY = newPos.y;
            double posZ = newPos.z;


            if (!this.isNoGravity() && !noPhysics) {
                this.setDeltaMovement(velX, velY - this.getGravity(), velZ);
            }

            float deceleration = this.getDeceleration();

            if (this.isInWater()) {
                if (client) {
                    for (int j = 0; j < 4; ++j) {
                        double pY = posY + this.getBbHeight() / 2d;
                        this.level.addParticle(ParticleTypes.BUBBLE, posX - velX * 0.25D, pY - velY * 0.25D, posZ - velZ * 0.25D, velX, velY, velZ);
                    }
                }
                //deceleration = this.waterDeceleration;
            }

            this.setDeltaMovement(this.getDeltaMovement().scale(deceleration));

            //first sets correct position, then call hit
            this.setPos(posX, posY, posZ);
            this.checkInsideBlocks();

            //calls on hit
            if (!this.isRemoved()) {
                //try hit entity
                EntityHitResult hitEntity = this.findHitEntity(pos, newPos);
                if (hitEntity != null) {
                    blockHitResult = hitEntity;
                }

                HitResult.Type type = blockHitResult.getType();
                boolean portalHit = false;
                if (type == HitResult.Type.ENTITY) {
                    Entity entity = ((EntityHitResult) blockHitResult).getEntity();
                    if (entity instanceof Player p1 && this.getOwner() instanceof Player p2 && !p2.canHarmPlayer(p1)) {
                        blockHitResult = null;
                    }
                } else if (type == HitResult.Type.BLOCK) {
                    //portals. done here and not in onBlockHit to prevent any further calls
                    BlockPos hitPos = ((BlockHitResult) blockHitResult).getBlockPos();
                    BlockState hitState = this.level.getBlockState(hitPos);

                    if (hitState.is(Blocks.NETHER_PORTAL)) {
                        this.handleInsidePortal(hitPos);
                        portalHit = true;
                    } else if (hitState.is(Blocks.END_GATEWAY)) {
                        if (this.level.getBlockEntity(hitPos) instanceof TheEndGatewayBlockEntity tile && TheEndGatewayBlockEntity.canEntityTeleport(this)) {
                            TheEndGatewayBlockEntity.teleportEntity(level, hitPos, hitState, this, tile);
                        }
                        portalHit = true;
                    }
                }

                if (!portalHit && blockHitResult != null && type != HitResult.Type.MISS && !noPhysics &&
                        !ForgeHelper.onProjectileImpact(this, blockHitResult)) {
                    this.onHit(blockHitResult);
                    this.hasImpulse = true; //idk what this does
                }
            }
        }
        if (this.hasReachedEndOfLife()) {
            this.reachedEndOfLife();
        }
    }

    static {
        DATA_GLOWSTICK_COLOR = SynchedEntityData.defineId(GlowstickEntity.class, EntityDataSerializers.INT);
    }
}
