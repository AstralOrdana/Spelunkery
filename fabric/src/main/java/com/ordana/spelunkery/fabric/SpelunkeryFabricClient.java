package com.ordana.spelunkery.fabric;

import com.ordana.spelunkery.Spelunkery;
import com.ordana.spelunkery.SpelunkeryClient;
import com.ordana.spelunkery.reg.ModFluids;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;

public class SpelunkeryFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // TODO 1.19.2 :: Adding to FabricSetupCallbacks.CLIENT_SETUP in SpelunkeryFabric causes ConcurrentModification issues because it happens too late
        Spelunkery.commonInit();
        ClientEventsFabric.init();
        SpelunkeryClient.init();
        SpelunkeryClient.setup();

        BlockRenderLayerMap.INSTANCE.putFluids(RenderType.translucent(), ModFluids.PORTAL_FLUID.get(), ModFluids.FLOWING_PORTAL_FLUID.get());
        BlockRenderLayerMap.INSTANCE.putFluids(RenderType.translucent(), ModFluids.SPRING_WATER.get(), ModFluids.FLOWING_SPRING_WATER.get());
    }
}
