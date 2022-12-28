package com.ordana.underground_overhaul.blocks.nephrite;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Fluids;

import java.util.Map;
import java.util.Optional;

public class RawNephriteBlock extends HugeMushroomBlock {
    private static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION;


    public RawNephriteBlock(Properties properties) {
        super(properties);
    }

    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos neighborPos, boolean isMoving) {
        for (Direction dir : Direction.values()) {
            this.updateShape(state, dir, level.getBlockState(neighborPos), level, pos, neighborPos);
        }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
        return neighborState.isFaceSturdy(level, neighborPos, direction) ? state.setValue(PROPERTY_BY_DIRECTION.get(direction), false) : super.updateShape(state, direction, neighborState, level, currentPos, neighborPos);
    }

    static {
        PROPERTY_BY_DIRECTION = PipeBlock.PROPERTY_BY_DIRECTION;
    }
}
