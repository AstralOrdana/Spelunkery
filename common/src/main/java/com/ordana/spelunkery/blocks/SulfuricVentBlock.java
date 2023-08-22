package com.ordana.spelunkery.blocks;

import com.ordana.spelunkery.events.ClientBoundParticlePacket;
import com.ordana.spelunkery.events.ClientBoundSendKnockbackPacket;
import com.ordana.spelunkery.events.NetworkHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class SulfuricVentBlock extends DirectionalBlock {
    public SulfuricVentBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.SOUTH));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite());
    }

    /*
    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        super.stepOn(level, pos, state, entity);
        RandomSource random = level.random;
        if (random.nextInt(20) == 1) geyserEruption(level, pos);
    }
     */

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (getSourceBlock(level, state, pos).is(Blocks.MAGMA_BLOCK)) geyserEruption(level, state, pos);
    }

    public BlockState getSourceBlock (Level level, BlockState state, BlockPos pos) {
        return level.getBlockState(pos.relative(state.getValue(BlockStateProperties.FACING).getOpposite()));
    }

    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!oldState.is(state.getBlock())) {
            if (level.hasNeighborSignal(pos)) {
                geyserEruption(level, state, pos);
            }
        }
    }

    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (level.hasNeighborSignal(pos)) {
            geyserEruption(level, state, pos);
        }
    }

    public void geyserEruption(Level level, BlockState state, BlockPos pos) {
        Direction facing = state.getValue(FACING);

        if (getSourceBlock(level, state, pos).getBlock() instanceof FallingBlock && level instanceof ServerLevel) {
            var entity = FallingBlockEntity.fall(level, pos.relative(facing), getSourceBlock(level, state, pos));
            //entity.setDeltaMovement(new Vec3(level.random.nextIntBetweenInclusive(-1, 1), 0, level.random.nextIntBetweenInclusive(-1, 1)));
        }

        AABB area = new AABB(pos.relative(facing));
        List<Entity> entities = level.getEntities(null, area);

        if (!level.isClientSide) {

            NetworkHandler.CHANNEL.sendToAllClientPlayersInRange(level, pos, 64,
                    new ClientBoundParticlePacket(Vec3.atCenterOf(pos),
                            ClientBoundParticlePacket.EventType.SULFUR_VENT));

            level.playSound(null, pos, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 1.0F, 1.7F);

            if (entities.isEmpty()) return;

            for (var e : entities) {

                double x = e.getX() - pos.getCenter().x;
                double y = e.getY() - pos.getCenter().y;
                double z = e.getZ() - pos.getCenter().z;
                double aa = Math.sqrt(x * x + y * y + z * z);
                x /= aa;
                y /= aa;
                z /= aa;

                var ordinalX = facing.getStepX();
                var ordinalY = facing.getStepY();
                var ordinalZ = facing.getStepZ();


                double gx = level.random.nextFloat();
                double gy = level.random.nextFloat();
                double gz = level.random.nextFloat();
                Vec3 vec32 = new Vec3(
                        (ordinalX == 0 ? (level.random.nextBoolean() ? x + gx : x - gx) : x),
                        (ordinalY == 0 ? (level.random.nextBoolean() ? y + gy : y - gy) : y),
                        (ordinalZ == 0 ? (level.random.nextBoolean() ? z + gz : z - gz) : z));

                if (e instanceof ServerPlayer) {
                    NetworkHandler.CHANNEL.sendToClientPlayer((ServerPlayer) e,
                            new ClientBoundSendKnockbackPacket(e.getDeltaMovement().add(vec32), e.getId()));
                }
                else e.setDeltaMovement(e.getDeltaMovement().add(vec32));
            }
        }
    }
}
