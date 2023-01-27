package com.ordana.spelunkery.fluids;

import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;

import java.util.function.Supplier;

import com.google.common.collect.Lists;
import net.mehvahdjukaar.moonlight.api.platform.PlatformHelper;
import net.minecraft.world.level.material.Fluids;

import java.lang.reflect.Field;

public class ModLiquidBlock extends LiquidBlock {

    private static final Field FORGE_BLOCK_SUPPLIER = PlatformHelper.findField(LiquidBlock.class, "supplier");
    private static final Field FLUID = PlatformHelper.findField(LiquidBlock.class, "fluid");
    private static final Field STATE_CACHE = PlatformHelper.findField(LiquidBlock.class, "stateCache");
    private static final Field INIT = PlatformHelper.findField(LiquidBlock.class, "fluidStateCacheInitialized");


    public ModLiquidBlock(Supplier<? extends FlowingFluid> supplier, Properties arg) {
        super(PlatformHelper.getPlatform().isFabric() ? supplier.get() : Fluids.WATER, arg);

        if (INIT != null) {
            INIT.setAccessible(true);
            try {
                INIT.set(this, false);
            } catch (Exception ignored) {
            }
        }
        if (FLUID != null) {
            FLUID.setAccessible(true);
            try {
                FLUID.set(this, null);
            } catch (Exception ignored) {
            }
        }
        if (STATE_CACHE != null) {
            STATE_CACHE.setAccessible(true);
            try {
                STATE_CACHE.set(this, Lists.newArrayList());
            } catch (Exception ignored) {
            }
        }
        this.registerDefaultState(this.stateDefinition.any().setValue(LEVEL, 0));
        if (FORGE_BLOCK_SUPPLIER != null) {
            FORGE_BLOCK_SUPPLIER.setAccessible(true);
            try {
                FORGE_BLOCK_SUPPLIER.set(this, supplier);
            } catch (Exception ignored) {
            }
        }
    }
}
