package com.ordana.spelunkery.reg;

import com.ordana.spelunkery.Spelunkery;
import com.ordana.spelunkery.fluids.PortalFluid;
import net.mehvahdjukaar.moonlight.api.fluids.ModFlowingFluid;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluids;

import java.util.function.Supplier;

public class ModFluids extends Fluids {
    public static void  init() {
    }

    public static final Supplier<FlowingFluid> FLOWING_PORTAL_FLUID = RegHelper.registerFluid(Spelunkery.res("flowing_portal_fluid"), () ->
            new PortalFluid.Flowing(ModFlowingFluid.properties().supportsBoating(true).lightLevel(5), ModBlocks.PORTAL_FLUID));
    public static final Supplier<FlowingFluid> PORTAL_FLUID = RegHelper.registerFluid(Spelunkery.res("portal_fluid"), () ->
            new PortalFluid.Source(ModFlowingFluid.properties().supportsBoating(true).lightLevel(5), ModBlocks.PORTAL_FLUID));

}
