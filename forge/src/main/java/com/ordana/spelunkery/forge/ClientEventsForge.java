package com.ordana.spelunkery.forge;

import com.ordana.spelunkery.entities.ParachuteLayer;
import com.ordana.spelunkery.fluids.PortalFluidRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ClientEventsForge {

    public static void init() {
        MinecraftForge.EVENT_BUS.register(ClientEventsForge.class);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientEventsForge::onAddLayers);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientEventsForge::onTextureStitchEvent);
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

    public static void onTextureStitchEvent(final TextureStitchEvent.Pre event) {
        if (event.getAtlas().location() != TextureAtlas.LOCATION_BLOCKS) {
            return;
        }

        // FIXME :: Otherwise they're missing from the atlas - maybe because they're not part of a model.json?
        event.addSprite(PortalFluidRenderer.portalFluidLargeSW);
        event.addSprite(PortalFluidRenderer.portalFluidLargeSE);
        event.addSprite(PortalFluidRenderer.portalFluidLargeNW);
        event.addSprite(PortalFluidRenderer.portalFluidLargeNE);
        event.addSprite(PortalFluidRenderer.portalFluidN);
        event.addSprite(PortalFluidRenderer.portalFluidE);
        event.addSprite(PortalFluidRenderer.portalFluidS);
        event.addSprite(PortalFluidRenderer.portalFluidW);
        event.addSprite(PortalFluidRenderer.portalFluidSW);
        event.addSprite(PortalFluidRenderer.portalFluidSE);
        event.addSprite(PortalFluidRenderer.portalFluidNW);
        event.addSprite(PortalFluidRenderer.portalFluidNE);
        event.addSprite(PortalFluidRenderer.portalFluidNS);
        event.addSprite(PortalFluidRenderer.portalFluidEW);
        event.addSprite(PortalFluidRenderer.portalFluidWNE);
        event.addSprite(PortalFluidRenderer.portalFluidNES);
        event.addSprite(PortalFluidRenderer.portalFluidESW);
        event.addSprite(PortalFluidRenderer.portalFluidSWN);
        /* Already works without this */ event.addSprite(PortalFluidRenderer.portalFluidNESW);
        event.addSprite(PortalFluidRenderer.portalFluidNONE);
        event.addSprite(PortalFluidRenderer.portalFluidUncommon);
        event.addSprite(PortalFluidRenderer.portalFluidRare);
        event.addSprite(PortalFluidRenderer.portalFluidSnence);
        event.addSprite(PortalFluidRenderer.portalFluidMaple);
    }
}
