package com.ordana.spelunkery.fabric;

import com.ordana.spelunkery.SpelunkeryClient;
import com.ordana.spelunkery.reg.ModFluids;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;

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
