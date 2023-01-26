package com.ordana.spelunkery.reg;

import com.ordana.spelunkery.Spelunkery;
import com.ordana.spelunkery.blocks.GlowstickBlock;
import com.ordana.spelunkery.blocks.rock_salt.RockSaltWall;
import com.ordana.spelunkery.fluids.ModLiquidBlock;
import com.ordana.spelunkery.fluids.PortalFluid;
import net.mehvahdjukaar.moonlight.api.fluids.ModFlowingFluid;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.*;

import java.util.function.Supplier;

public class ModFluids {
    public static void  init() {
    }

    public static final Supplier<FlowingFluid> FLOWING_PORTAL_FLUID = RegHelper.registerFluid(Spelunkery.res("flowing_portal_fluid"), () ->
            new PortalFluid.Flowing(ModFlowingFluid.properties().canDrown(true), ModBlocks.PORTAL_FLUID));
    public static final Supplier<FlowingFluid> PORTAL_FLUID = RegHelper.registerFluid(Spelunkery.res("portal_fluid"), () ->
            new PortalFluid.Source(ModFlowingFluid.properties().canDrown(true), ModBlocks.PORTAL_FLUID));

}
