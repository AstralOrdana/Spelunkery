package com.ordana.spelunkery.fabric;

import com.ordana.spelunkery.SpelunkeryClient;
import com.ordana.spelunkery.reg.ModFluids;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import org.intellij.lang.annotations.Identifier;

public class SpelunkeryFabricClient implements ClientModInitializer {


    public static void initClient() {
        SpelunkeryClient.init();
        SpelunkeryClient.setup();

        BlockRenderLayerMap.INSTANCE.putFluids(RenderType.translucent(), ModFluids.PORTAL_FLUID.get(), ModFluids.FLOWING_PORTAL_FLUID.get());
    }

    @Override
    public void onInitializeClient() {
        //no
    }
}
