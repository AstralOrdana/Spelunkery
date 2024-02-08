package com.ordana.spelunkery.forge;

import com.ordana.spelunkery.entities.ParachuteLayer;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ClientEventsForge {

    public static void init() {
        MinecraftForge.EVENT_BUS.register(ClientEventsForge.class);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientEventsForge::onAddLayers);
    }

    @SuppressWarnings("unchecked")
    public static void onAddLayers(EntityRenderersEvent.AddLayers event) {
        for (String skinType : event.getSkins()) {
            var renderer = event.getSkin(skinType);
            if (renderer != null) {
                renderer.addLayer(new ParachuteLayer(renderer));
            }
        }
    }
}
