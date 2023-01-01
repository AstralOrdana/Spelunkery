package com.ordana.underground_overhaul;

import com.ordana.underground_overhaul.configs.ClientConfigs;
import com.ordana.underground_overhaul.items.NephriteCharmItem;
import com.ordana.underground_overhaul.reg.ModBlocks;
import com.ordana.underground_overhaul.reg.ModEntities;
import com.ordana.underground_overhaul.reg.ModItems;
import net.mehvahdjukaar.moonlight.api.platform.ClientPlatformHelper;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

public class UndergroundOverhaulClient {
    
    public static void init() {
        ClientConfigs.init();
        ClientPlatformHelper.addEntityRenderersRegistration(UndergroundOverhaulClient::registerEntityRenderers);
    }

    public static void setup() {
        ClientPlatformHelper.registerRenderType(ModBlocks.SALT_LAMP.get(), RenderType.cutout());
        ClientPlatformHelper.registerRenderType(ModBlocks.SALT.get(), RenderType.cutout());
        ClientPlatformHelper.registerRenderType(ModBlocks.WOODEN_RAIL.get(), RenderType.cutout());
        ClientPlatformHelper.registerRenderType(ModBlocks.ROPE_LADDER.get(), RenderType.cutout());
        ClientPlatformHelper.registerRenderType(ModBlocks.TANGLE_ROOTS.get(), RenderType.cutout());
        ClientPlatformHelper.registerRenderType(ModBlocks.TANGLE_ROOTS_PLANT.get(), RenderType.cutout());
        ClientPlatformHelper.registerRenderType(ModBlocks.SPOROPHYTE.get(), RenderType.cutout());

        ClientPlatformHelper.registerRenderType(ModBlocks.CONK_FUNGUS.get(), RenderType.cutout());
        ClientPlatformHelper.registerRenderType(ModBlocks.INKCAP_MUSHROOM.get(), RenderType.cutout());
        ClientPlatformHelper.registerRenderType(ModBlocks.WHITE_INKCAP_MUSHROOM.get(), RenderType.cutout());
        ClientPlatformHelper.registerRenderType(ModBlocks.PHOSPHOR_FUNGUS.get(), RenderType.cutout());
        ClientPlatformHelper.registerRenderType(ModBlocks.MILLY_BUBCAP.get(), RenderType.cutout());
        ClientPlatformHelper.registerRenderType(ModBlocks.PORTABELLA.get(), RenderType.cutout());
        ClientPlatformHelper.registerRenderType(ModBlocks.CRIMINI.get(), RenderType.cutout());
        ClientPlatformHelper.registerRenderType(ModBlocks.BUTTON_MUSHROOM.get(), RenderType.cutout());

        ClientPlatformHelper.registerItemProperty(ModItems.DEPTH_GAUGE.get(), UndergroundOverhaul.res("depth"),
                (stack, world, entity, seed) -> entity != null ? (((float) entity.getBlockY() + 64) / 384) : 0);
        ClientPlatformHelper.registerItemProperty(ModItems.NEPHRITE_CHARM.get(), UndergroundOverhaul.res("charge"),
                (stack, world, entity, seed) -> {
                    if (entity != null) {
                        assert stack.getTag() != null;
                        return ((float) stack.getTag().getInt("xp") / 1395);
                    } else {
                        return 0;
                    }
                });
    }

    private static void registerEntityRenderers(ClientPlatformHelper.EntityRendererEvent event) {
        event.register(ModEntities.GLOWSTICK.get(), context -> new ThrownItemRenderer<>(context, 1, false));
    }

}