package com.ordana.spelunkery.events;

import com.ordana.spelunkery.reg.ModParticles;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ClientReceivers {

    private static void withLevelDo(Consumer<Level> action) {
        var level = Minecraft.getInstance().level;
        if (level != null) action.accept(level);
    }

    public static void handleSendBombKnockbackPacket(ClientBoundSendKnockbackPacket message) {
        withLevelDo(l -> {
            Entity e = l.getEntity(message.id);
            if (e != null) e.setDeltaMovement(e.getDeltaMovement()
                    .add(message.knockbackX, message.knockbackY, message.knockbackZ));
        });
    }

    public static void handleSpawnBlockParticlePacket(ClientBoundParticlePacket message) {
        withLevelDo(l -> {
            if (message.id == ClientBoundParticlePacket.EventType.SLUICE) {
                //ParticleUtils.spawnParticleOnFace(l, message.pos, );
            }
            if (message.id == ClientBoundParticlePacket.EventType.SULFUR_VENT) {
                Vec3 vec = Vec3.atCenterOf(ClientReceivers.containing(message.pos));
                var dir = l.getBlockState(ClientReceivers.containing(vec)).getValue(BlockStateProperties.FACING);
                var water = l.getFluidState(ClientReceivers.containing(vec).relative(dir)).is(Fluids.WATER);

                var dustBlockPos = ClientReceivers.containing(vec).relative(dir.getOpposite());
                var dustBlockState = l.getBlockState(dustBlockPos);
                var dustBlock = dustBlockState.getBlock();

                var norm = dir.getNormal();
                var x = norm.getX();
                var y = norm.getY();
                var z = norm.getZ();
                var orientation = dir.get3DDataValue();
                var color = 0;
                if (dustBlock instanceof FallingBlock block) color = block.getDustColor(dustBlockState, l, dustBlockPos);


                for (int j = 0; j < 40; ++j) {
                    if (water) {
                        var xSpeed = (x == 0 ? Mth.nextDouble(l.random, -0.6D, 0.6D) : (x < 0 ? x - Mth.nextDouble(l.random, 0.1D, 3.5D) : x + Mth.nextDouble(l.random, 0.1D, 3.5D)));
                        var ySpeed = (y == 0 ? Mth.nextDouble(l.random, -0.6D, 0.6D) : (y < 0 ? y - Mth.nextDouble(l.random, 0.1D, 3.5D) : y + Mth.nextDouble(l.random, 0.1D, 3.5D)));
                        var zSpeed = (z == 0 ? Mth.nextDouble(l.random, -0.6D, 0.6D) : (z < 0 ? z - Mth.nextDouble(l.random, 0.1D, 3.5D) : z + Mth.nextDouble(l.random, 0.1D, 3.5D)));
                        l.addAlwaysVisibleParticle(ParticleTypes.BUBBLE_COLUMN_UP, true, vec.x + (x / 2f), vec.y + (y / 2f), vec.z + (z / 2f), xSpeed, ySpeed, zSpeed);
                    }
                    else {
                        l.addAlwaysVisibleParticle(ModParticles.SULFUR.get(), true, vec.x + (x / 2f), vec.y + (y / 2f), vec.z + (z / 2f), 0, orientation, 0);
                        if (color != 0) l.addAlwaysVisibleParticle(ModParticles.SULFUR_DUSTING.get(), true, vec.x + (x / 2f), vec.y + (y / 2f), vec.z + (z / 2f), color, orientation, 0);
                    }
                }
            }
        });
    }

    private static BlockPos containing(@NotNull final Vec3 input) {
        return new BlockPos(Mth.floor(input.x()), Mth.floor(input.y()), Mth.floor(input.z()));
    }
}