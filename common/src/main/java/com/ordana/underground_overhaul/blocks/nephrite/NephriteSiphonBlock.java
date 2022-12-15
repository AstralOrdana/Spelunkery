package com.ordana.underground_overhaul.blocks.nephrite;

import com.ordana.underground_overhaul.blocks.entity.CarvedNephriteBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NephriteSiphonBlock extends Block {
    protected static final VoxelShape SHAPE;

    public NephriteSiphonBlock(Properties properties) {
        super(properties);
    }

    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    public boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }


    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        List<CarvedNephriteBlockEntity> neighbors = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            if (direction.equals(Direction.UP)) continue;

            if (level.getBlockEntity(pos.relative(direction)) instanceof CarvedNephriteBlockEntity neighborTile) {
                neighbors.add(neighborTile);
            }
        }

        if (neighbors.isEmpty()) return;
        CarvedNephriteBlockEntity chosenNeighbor = neighbors.get(level.random.nextInt(neighbors.size()));

        if (entity instanceof ExperienceOrb xpOrb) {
            if ((chosenNeighbor.getCharge() + xpOrb.getValue()) < 100) {
                chosenNeighbor.setCharge(chosenNeighbor.getCharge() + xpOrb.getValue());
                xpOrb.discard();
                float f = level.random.nextFloat();
                level.playSound(null, pos, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.BLOCKS, f * 0.9F, (f + 1F) / 2 );
            }
        }

        if (entity instanceof Player player && (player.isCrouching() || player.tickCount % 10 == 0)) {
            if ((player.totalExperience > 0 || (player.experienceLevel > 0 && player.totalExperience == 0)) && chosenNeighbor.getCharge() < 100) {
                chosenNeighbor.setCharge(chosenNeighbor.getCharge() + 1);
                player.giveExperiencePoints(-1);
                float f = level.random.nextFloat();
                level.playSound(null, pos, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.BLOCKS, f * 0.9F, ((f + 1F) / 2) -0.3f);
            }
        }
    }

    static {
        SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D);
    }
}
