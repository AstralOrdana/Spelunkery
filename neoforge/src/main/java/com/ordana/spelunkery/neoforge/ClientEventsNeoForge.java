package com.ordana.spelunkery.neoforge;

import com.ordana.spelunkery.Spelunkery;
import com.ordana.spelunkery.SpelunkeryClient;
import com.ordana.spelunkery.entities.ParachuteLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = Spelunkery.MOD_ID, value = Dist.CLIENT)
public class ClientEventsNeoForge {

    public static void init() {
    }

    @SubscribeEvent
    public static void loadComplete(FMLLoadCompleteEvent event){
        SpelunkeryClient.checkIfFailed();
    }

    @SuppressWarnings("unchecked")
    @SubscribeEvent
    public static void onAddLayers(EntityRenderersEvent.AddLayers event) {
        for (PlayerSkin.Model skinType : event.getSkins()) {
            PlayerRenderer renderer = event.getSkin(skinType);
            if (renderer != null) {
                renderer.addLayer(new ParachuteLayer(renderer));
            }
        }
    }
}
