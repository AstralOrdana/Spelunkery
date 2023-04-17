package com.ordana.spelunkery.blocks.fungi;

import com.ordana.spelunkery.Spelunkery;
import com.ordana.spelunkery.reg.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GrowableMushroomBlock extends ModMushroomBlock implements BonemealableBlock {
    protected static final VoxelShape SHAPE = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 12.0D, 11.0D);
    protected static final float AABB_OFFSET = 3.0F;

    public GrowableMushroomBlock(Properties properties) {
        super(properties);
    }

    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Vec3 vec3 = state.getOffset(level, pos);
        return SHAPE.move(vec3.x, vec3.y, vec3.z);
    }

    public boolean growMushroom(ServerLevel level, BlockPos pos, BlockState state, RandomSource random) {
        level.removeBlock(pos, false);
        Holder<ConfiguredFeature<?, ?>> feature = null;

        if (state.is(ModBlocks.INKCAP_MUSHROOM.get())) feature = (level.registryAccess().registry(Registry.CONFIGURED_FEATURE_REGISTRY).get().getHolder(
                ResourceKey.create(Registry.CONFIGURED_FEATURE_REGISTRY, Spelunkery.res("huge_inkcap_mushroom_bonemeal"))).get());
        if (state.is(ModBlocks.WHITE_INKCAP_MUSHROOM.get())) feature = (level.registryAccess().registry(Registry.CONFIGURED_FEATURE_REGISTRY).get().getHolder(
                ResourceKey.create(Registry.CONFIGURED_FEATURE_REGISTRY, Spelunkery.res("huge_white_inkcap_mushroom_bonemeal"))).get());
        if (state.is(ModBlocks.PORTABELLA.get())) feature = (level.registryAccess().registry(Registry.CONFIGURED_FEATURE_REGISTRY).get().getHolder(
                ResourceKey.create(Registry.CONFIGURED_FEATURE_REGISTRY, Spelunkery.res("huge_portabella_bonemeal"))).get());


        if (feature != null) {
            if ((feature.value()).place(level, level.getChunkSource().getGenerator(), random, pos)) {
                return true;
            } else {
                level.setBlock(pos, state, 3);
                return false;
            }
        }
        else return false;
    }

    @Override
    public boolean isValidBonemealTarget(BlockGetter level, BlockPos pos, BlockState state, boolean isClient) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return (double)random.nextFloat() < 0.4D;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        this.growMushroom(level, pos, state, random);
    }
}
