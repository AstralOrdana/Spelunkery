package com.ordana.spelunkery.fluids;

import com.ordana.spelunkery.reg.ModFluids;
import com.ordana.spelunkery.reg.ModItems;
import net.mehvahdjukaar.moonlight.api.client.ModFluidRenderProperties;
import net.mehvahdjukaar.moonlight.api.fluids.ModFlowingFluid;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Supplier;

public class SpringWater extends ModFlowingFluid {

    public SpringWater(Properties properties, Supplier<? extends LiquidBlock> block) {
        super(properties, block);
    }

    @Override
    public ModFluidRenderProperties createRenderProperties() {
        return new SpringWaterRenderer(
                new ResourceLocation("minecraft", "block/water_still"),
                new ResourceLocation("minecraft", "block/water_flow"),
                PlatHelper.isModLoaded("sodium") ? 0xffEED543 : 0xff43D5EE,
                new ResourceLocation("minecraft", "block/water_overlay"),
                new ResourceLocation("minecraft", "block/water_overlay"),
                new Vec3(68, 69, 103));

    }

    @NotNull
    public Fluid getFlowing() {
        return ModFluids.FLOWING_SPRING_WATER.get();
    }

    @NotNull
    public Fluid getSource() {
        return ModFluids.SPRING_WATER.get();
    }

    @NotNull
    public Item getBucket() {
        return ModItems.SPRING_WATER_BUCKET.get();
    }

    @NotNull
    public Optional<SoundEvent> getPickupSound() {
        return Optional.of(SoundEvents.BUCKET_FILL);
    }

    @Override
    protected ParticleOptions getDripParticle() {
        return ParticleTypes.DRIPPING_WATER;
    }


    public void animateTick(Level level, BlockPos pos, FluidState state, RandomSource random) {
        if (random.nextInt(3) == 0) {
            double d = (double)pos.getX() + random.nextDouble();
            double e = (double)pos.getY() + 1.0D;
            double f = (double)pos.getZ() + random.nextDouble();
            level.addParticle(random.nextFloat() > 0.8 ? ParticleTypes.CAMPFIRE_COSY_SMOKE : ParticleTypes.BUBBLE_POP, d, e + (random.nextFloat() / 10), f, 0.0D, 0.05D + (random.nextFloat() / 10), 0.0D);
        }
        if (!state.isSource() && !(Boolean)state.getValue(FALLING)) {
            if (random.nextInt(64) == 0) {
                level.playLocalSound((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, SoundEvents.WATER_AMBIENT, SoundSource.BLOCKS, random.nextFloat() * 0.25F + 0.75F, random.nextFloat() + 0.5F, false);
            }
        } else if (random.nextInt(10) == 0) {
            level.addParticle(ParticleTypes.UNDERWATER, (double)pos.getX() + random.nextDouble(), (double)pos.getY() + random.nextDouble(), (double)pos.getZ() + random.nextDouble(), 0.0D, 0.0D, 0.0D);
        }

    }

    @Override
    protected int getSlopeFindDistance(@NotNull LevelReader level) {
        return 4;
    }

    @Override
    protected int getDropOff(@NotNull LevelReader level) {
        return 1;
    }

    @Override
    public int getTickDelay(@NotNull LevelReader level) {
        return 5;
    }

    @Override
    protected float getExplosionResistance() {
        return 100f;
    }

    @Override
    public boolean isSource(@NotNull FluidState state) {
        return false;
    }

    @Override
    public int getAmount(@NotNull FluidState state) {
        return 0;
    }

    public static class Flowing extends SpringWater {
        public Flowing(Properties properties, Supplier<? extends LiquidBlock> block) {
            super(properties, block);
        }

        @Override
        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }

        @Override
        public int getAmount(@NotNull FluidState state) {
            return state.getValue(LEVEL);
        }

        @Override
        public boolean isSource(@NotNull FluidState state) {
            return false;
        }
    }

    public static class Source extends SpringWater {
        public Source(Properties properties, Supplier<? extends LiquidBlock> block) {
            super(properties, block);
        }

        @Override
        public int getAmount(@NotNull FluidState state) {
            return 8;
        }

        @Override
        public boolean isSource(@NotNull FluidState state) {
            return true;
        }
    }
}