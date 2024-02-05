package com.ordana.spelunkery.fabric;

import com.ordana.spelunkery.Spelunkery;
import com.ordana.spelunkery.SpelunkeryClient;
import net.fabricmc.api.ClientModInitializer;
import net.mehvahdjukaar.moonlight.fabric.FabricSetupCallbacks;

public class SpelunkeryFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // TODO 1.19.2 :: Adding to FabricSetupCallbacks.CLIENT_SETUP in SpelunkeryFabric causes ConcurrentModification issues because it happens too late
        FabricSetupCallbacks.CLIENT_SETUP.add(SpelunkeryClient::setup);

        Spelunkery.commonInit();
        ClientEventsFabric.init();
        SpelunkeryClient.init();

        // TODO 1.19.2 :: Needed?
//        BlockRenderLayerMap.INSTANCE.putFluids(RenderType.translucent(), ModFluids.PORTAL_FLUID.get(), ModFluids.FLOWING_PORTAL_FLUID.get());
//        BlockRenderLayerMap.INSTANCE.putFluids(RenderType.translucent(), ModFluids.SPRING_WATER.get(), ModFluids.FLOWING_SPRING_WATER.get());
    }
}
