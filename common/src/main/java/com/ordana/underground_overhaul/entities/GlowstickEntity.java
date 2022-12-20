package com.ordana.underground_overhaul.entities;

import com.ordana.underground_overhaul.blocks.GlowstickBlock;
import com.ordana.underground_overhaul.reg.ModBlocks;
import com.ordana.underground_overhaul.reg.ModEntities;
import com.ordana.underground_overhaul.reg.ModItems;
import net.mehvahdjukaar.moonlight.api.entity.ImprovedProjectileEntity;
import net.mehvahdjukaar.moonlight.api.platform.ForgeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RodBlock;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GlowstickEntity extends ImprovedProjectileEntity {
    public GlowstickEntity(EntityType<? extends GlowstickEntity> type, Level world) {
        super(type, world);
    }

    public GlowstickEntity(Level level, LivingEntity thrower) {
        super(ModEntities.GLOWSTICK.get(), thrower, level);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.GLOWSTICK.get();
    }

    public static void placeGlowstick(Level level, BlockPos pos, BlockHitResult hitResult) {
        Direction dir = hitResult.getDirection();
        BlockState state = level.getBlockState(pos);

            if (!(dir == Direction.DOWN)) level.setBlockAndUpdate(pos, ModBlocks.GLOWSTICK.get().defaultBlockState().setValue(RodBlock.FACING, dir));
            else level.setBlockAndUpdate(pos.relative(dir), ModBlocks.GLOWSTICK.get().defaultBlockState().setValue(RodBlock.FACING, dir));

        //else if (level instanceof ServerLevel) GlowstickBlock.getDrops(ModBlocks.GLOWSTICK.get().defaultBlockState(), (ServerLevel) level, pos, null);
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
                deceleration = this.waterDeceleration;
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
}
