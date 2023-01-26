package com.ordana.spelunkery.fluids;

import com.ordana.spelunkery.Spelunkery;
import com.ordana.spelunkery.reg.ModBlocks;
import com.ordana.spelunkery.reg.ModFluids;
import com.ordana.spelunkery.reg.ModItems;
import net.mehvahdjukaar.moonlight.api.client.ModFluidRenderProperties;
import net.mehvahdjukaar.moonlight.api.fluids.ModFlowingFluid;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.*;
import org.jetbrains.annotations.Nullable;

import javax.sound.sampled.Port;
import java.util.Optional;
import java.util.function.Supplier;

public class PortalFluid extends ModFlowingFluid {

    public PortalFluid(Properties properties, Supplier<? extends LiquidBlock> block) {
        super(properties, block);
    }

    @Override
    public ModFluidRenderProperties createRenderProperties() {
        return new ModFluidRenderProperties(
                Spelunkery.res("block/portal_fluid"),
                Spelunkery.res("block/portal_fluid_flow"));
    }

    public Fluid getFlowing() {
        return ModFluids.FLOWING_PORTAL_FLUID.get();
    }

    public Fluid getSource() {
        return ModFluids.PORTAL_FLUID.get();
    }

    public Item getBucket() {
        return ModItems.PORTAL_FLUID_BUCKET.get();
    }

    @Override
    protected int getSlopeFindDistance(LevelReader level) {
        return 4;
    }

    @Override
    protected int getDropOff(LevelReader level) {
        return 1;
    }

    @Override
    public int getTickDelay(LevelReader level) {
        return 5;
    }

    @Override
    protected float getExplosionResistance() {
        return 100f;
    }

    @Override
    public boolean isSource(FluidState state) {
        return false;
    }

    @Override
    public int getAmount(FluidState state) {
        return 0;
    }

    public static class Flowing extends PortalFluid {
        public Flowing(Properties properties, Supplier<? extends LiquidBlock> block) {
            super(properties, block);
        }

        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }

        public int getAmount(FluidState state) {
            return state.getValue(LEVEL);
        }

        public boolean isSource(FluidState state) {
            return false;
        }
    }

    public static class Source extends PortalFluid {
        public Source(Properties properties, Supplier<? extends LiquidBlock> block) {
            super(properties, block);
        }

        public int getAmount(FluidState state) {
            return 5;
        }

        public boolean isSource(FluidState state) {
            return true;
        }
    }
}