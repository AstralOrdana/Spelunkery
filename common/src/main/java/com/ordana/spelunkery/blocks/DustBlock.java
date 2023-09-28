package com.ordana.spelunkery.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class DustBlock extends FallingLayerBlock {
    private static final VoxelShape[] SHAPE_BY_LAYER = new VoxelShape[8 + 1];

    public DustBlock(Properties properties) {
        super(properties.isViewBlocking((s, l, p) -> s.getValue(BlockStateProperties.LAYERS) == 8));
    }

    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        var random = level.getRandom();
        if (entity.getDeltaMovement().x() > 0 || entity.getDeltaMovement().z() > 0) {
            if (entity instanceof ItemEntity && entity.getDeltaMovement().y() == 0) return;

            for(int i = 0; i < 10; ++i) {
                double d0 = random.nextGaussian() * 0.02D;
                double d1 = random.nextGaussian() * 0.02D;
                double d2 = random.nextGaussian() * 0.02D;

                if (level.isClientSide()) level.addParticle(ParticleTypes.POOF, entity.getX() - d0 * 10.0D, entity.getY() - d1 * 10.0D, entity.getZ() - d2 * 10.0D, d0, d1, d2);
                else if (level instanceof ServerLevel serverLevel) serverLevel.sendParticles(ParticleTypes.POOF, entity.getX() - d0 * 10.0D, entity.getY() - d1 * 10.0D, entity.getZ() - d2 * 10.0D, 1, 0, 0, 0, d2);

            }
        }
    }


    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public int getDustColor(BlockState state, BlockGetter reader, BlockPos pos) {
        return 0x5E625E;
    }


    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float height) {
        entity.causeFallDamage(height, 0.1f, DamageSource.FALL);
    }

    @Override
    public void onLand(Level level, BlockPos pos, BlockState state, BlockState replaceableState, FallingBlockEntity fallingBlock) {
        for(int i = 0; i < 10; ++i) {
            double d0 = level.random.nextGaussian() * 0.02D;
            double d1 = level.random.nextGaussian() * 0.02D;
            double d2 = level.random.nextGaussian() * 0.02D;
            level.addParticle(ParticleTypes.POOF, pos.getX() - d0 * 10.0D, pos.getY() - d1 * 10.0D, pos.getZ() - d2 * 10.0D, d0, d1, d2);
        }
    }
}