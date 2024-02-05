package com.ordana.spelunkery.items.magnetic_compass;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class MagneticCompassItemPropertyFunction implements ClampedItemPropertyFunction {
    public static final int DEFAULT_ROTATION = 0;
    private final CompassWobble wobble = new CompassWobble();
    private final CompassWobble wobbleRandom = new CompassWobble();
    public final CompassTarget compassTarget;

    public MagneticCompassItemPropertyFunction(CompassTarget compassTarget) {
        this.compassTarget = compassTarget;
    }

    public float unclampedCall(ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity, int i) {
        Entity entity = livingEntity != null ? livingEntity : itemStack.getEntityRepresentation();
        if (entity == null) {
            return 0.0F;
        } else {
            clientLevel = this.tryFetchLevelIfMissing(entity, clientLevel);
            return clientLevel == null ? 0.0F : this.getCompassRotation(itemStack, clientLevel, i, entity);
        }
    }

    private float getCompassRotation(ItemStack itemStack, ClientLevel clientLevel, int i, Entity entity) {
        GlobalPos globalPos = this.compassTarget.getPos(clientLevel, itemStack, entity);
        long l = clientLevel.getGameTime();
        return !this.isValidCompassTargetPos(entity, globalPos) ? this.getRandomlySpinningRotation(i, l) : this.getRotationTowardsCompassTarget(entity, l, globalPos.pos());

/*
        if (!this.isValidCompassTargetPos(entity, globalPos)) {
            this.getRandomlySpinningRotation(i, l)
        }
        else if (this.getTag)
        this.getRotationTowardsNorth(entity, l);

 */
    }

    private float getRandomlySpinningRotation(int i, long l) {
        if (this.wobbleRandom.shouldUpdate(l)) {
            this.wobbleRandom.update(l, Math.random());
        }

        double d = this.wobbleRandom.rotation + (double)((float)this.hash(i) / 2.14748365E9F);
        return Mth.positiveModulo((float)d, 1.0F);
    }

    private float getRotationTowardsNorth(Entity entity, long l) {
        double d = this.getAngleFromEntityToNorth(entity);
        double e = this.getWrappedVisualRotationY(entity);
        double f;
        if (entity instanceof Player) {
            Player player = (Player)entity;
            if (player.isLocalPlayer()) {
                if (this.wobble.shouldUpdate(l)) {
                    this.wobble.update(l, 0.5D - (e - 0.25D));
                }

                f = d + this.wobble.rotation;
                return Mth.positiveModulo((float)f, 1.0F);
            }
        }

        f = 0.5D - (e - 0.25D - d);
        return Mth.positiveModulo((float)f, 1.0F);
    }

    public float getRotationTowardsCompassTarget(Entity entity, long l, BlockPos blockPos) {
        double d = this.getAngleFromEntityToPos(entity, blockPos);
        double e = this.getWrappedVisualRotationY(entity);
        double f;
        if (entity instanceof Player) {
            Player player = (Player)entity;
            if (player.isLocalPlayer()) {
                if (this.wobble.shouldUpdate(l)) {
                    this.wobble.update(l, 0.5D - (e - 0.25D));
                }

                f = d + this.wobble.rotation;
                return Mth.positiveModulo((float)f, 1.0F);
            }
        }

        f = 0.5D - (e - 0.25D - d);
        return Mth.positiveModulo((float)f, 1.0F);
    }

    @Nullable
    private ClientLevel tryFetchLevelIfMissing(Entity entity, @Nullable ClientLevel clientLevel) {
        return clientLevel == null && entity.level instanceof ClientLevel ? (ClientLevel)entity.level : clientLevel;
    }

    private boolean isValidCompassTargetPos(Entity entity, @Nullable GlobalPos globalPos) {
        return globalPos != null && globalPos.dimension() == entity.level.dimension() && !(globalPos.pos().distToCenterSqr(entity.position()) < 9.999999747378752E-6D);
    }

    private double getAngleFromEntityToPos(Entity entity, BlockPos blockPos) {
        Vec3 vec3 = Vec3.atCenterOf(blockPos);
        return Math.atan2(vec3.z() - entity.getZ(), vec3.x() - entity.getX()) / 6.2831854820251465D;
    }

    private double getAngleFromEntityToNorth(Entity entity) {
        Vec3 vec3 = Vec3.atCenterOf(entity.getOnPos().north());
        return Math.atan2(vec3.z() - entity.getZ(), vec3.x() - entity.getX()) / 6.2831854820251465D;
    }

    private double getWrappedVisualRotationY(Entity entity) {
        return Mth.positiveModulo(entity.getVisualRotationYInDegrees() / 360.0F, 1.0D);
    }

    private int hash(int i) {
        return i * 1327217883;
    }

    @Environment(EnvType.CLIENT)
    static class CompassWobble {
        double rotation;
        private double deltaRotation;
        private long lastUpdateTick;

        CompassWobble() {
        }

        boolean shouldUpdate(long l) {
            return this.lastUpdateTick != l;
        }

        void update(long l, double d) {
            this.lastUpdateTick = l;
            double e = d - this.rotation;
            e = Mth.positiveModulo(e + 0.5D, 1.0D) - 0.5D;
            this.deltaRotation += e * 0.1D;
            this.deltaRotation *= 0.8D;
            this.rotation = Mth.positiveModulo(this.rotation + this.deltaRotation, 1.0D);
        }
    }

    @Environment(EnvType.CLIENT)
    public interface CompassTarget {
        @Nullable
        GlobalPos getPos(ClientLevel clientLevel, ItemStack itemStack, Entity entity);
    }
}
