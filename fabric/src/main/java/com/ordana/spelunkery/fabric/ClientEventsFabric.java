package com.ordana.spelunkery.fabric;

import com.ordana.spelunkery.entities.ParachuteLayer;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;

public class ClientEventsFabric {

    public static void init() {
        LivingEntityFeatureRendererRegistrationCallback.EVENT.register((t, r, e, c) -> {
            if (r instanceof PlayerRenderer) {
                e.register(new ParachuteLayer(r));
            }
        });
    }
}
