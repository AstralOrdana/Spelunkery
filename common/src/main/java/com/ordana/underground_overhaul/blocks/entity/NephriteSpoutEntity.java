package com.ordana.underground_overhaul.blocks.entity;

import com.ordana.underground_overhaul.blocks.nephrite.CarvedNephriteBlock;
import com.ordana.underground_overhaul.blocks.nephrite.NephriteSpoutBlock;
import com.ordana.underground_overhaul.reg.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NephriteSpoutEntity extends BlockEntity {
    public NephriteSpoutEntity(BlockPos pos, BlockState state) {
        super(ModEntities.NEPHRITE_SPOUT.get(), pos, state);
    }

    private int tickCounter = 0;

    public int setTickCounter(int tick) {
        return tickCounter = tick;
    }

    /*
    public static void summonForthTheOrb (ServerLevel level, Vec3 pos, Vec3 momentum, int amount) {

        Vec3 vec3 = new Vec3((siphonPos.get().getX() + 0.5) - this.getX(), (siphonPos.get().getY() + 1) - this.getY(), (siphonPos.get().getZ() + 0.5) - this.getZ());
        double d = vec3.lengthSqr();
        if (d < 64.0D) {
            double e = 1.0D - Math.sqrt(d) / 8.0D;
            (this.setDeltaMovement(this.getDeltaMovement().add(vec3.normalize().scale(e * e * 0.1D)));
        }

                level.addFreshEntity(new ExperienceOrb(level, pos.x(), pos.y(), pos.z(), 1)).setDeltaMovement(vec3);
    }
     */

    public static void pourXp(Level level, BlockPos pos, BlockState state, NephriteSpoutEntity selfTile) {
        Direction dir = state.getValue(NephriteSpoutBlock.FACING);
        if (level.getBlockEntity(pos.relative(dir.getOpposite())) instanceof CarvedNephriteBlockEntity neighborTile && neighborTile.getCharge() > 0 && level instanceof ServerLevel) {

            var pourVector = new Vec3((double)pos.getX() + 0.5D, (double)pos.getY() + 0.125D, (double)pos.getZ() + 0.5D);

            /*
            switch(dir) {
                case Direction.NORTH:
                    do_something;
                    break;
                case Direction.SOUTH:
                    do_something_else;
                    break;
                case Direction.EAST:
                    do_something_else;
                    break;
                case Direction.WEST:
                    do_something_else;
                    break;
                default:
                    what_to_do_in_other_cases;
            }
            */

            neighborTile.setCharge(neighborTile.getCharge() - 1);
            ExperienceOrb.award((ServerLevel) level, pourVector, 1);
        }
    }

    public static void tickBlock(Level level, BlockPos pos, BlockState state, NephriteSpoutEntity selfTile) {
        selfTile.tickCounter++;
        if (!level.isClientSide && selfTile.tickCounter >= 1 && state.getValue(NephriteSpoutBlock.POWERED)) {
            pourXp(level, pos, state, selfTile);
            selfTile.setTickCounter(0);
        }
    }
}
