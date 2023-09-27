package com.ordana.spelunkery.entities;

import com.ordana.spelunkery.Spelunkery;
import com.ordana.spelunkery.SpelunkeryClient;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class DustBunnyRenderer extends MobRenderer<DustBunnyEntity, DustBunnyModel<DustBunnyEntity>> {
    private static final ResourceLocation LOCATION = Spelunkery.res("textures/entity/dust_bunny/dust_bunny.png");

    public DustBunnyRenderer(EntityRendererProvider.Context context) {
        super(context, new DustBunnyModel<>(context.bakeLayer(SpelunkeryClient.DUST_BUNNY)), 0.7F);
    }

    @Override
    public ResourceLocation getTextureLocation(DustBunnyEntity bunny) {
        return LOCATION;
    }
}
