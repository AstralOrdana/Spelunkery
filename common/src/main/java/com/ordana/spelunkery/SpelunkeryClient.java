package com.ordana.spelunkery;

import com.ordana.spelunkery.entities.PrimedMineomiteEntityRenderer;
import com.ordana.spelunkery.items.HandheldCompactorItem;
import com.ordana.spelunkery.items.MagneticCompassItem;
import com.ordana.spelunkery.items.magnetic_compass.MagneticCompassItemPropertyFunction;
import com.ordana.spelunkery.particles.PortalFluidFlameParticle;
import com.ordana.spelunkery.particles.SulfurParticle;
import com.ordana.spelunkery.reg.*;
import net.mehvahdjukaar.moonlight.api.misc.EventCalled;
import net.mehvahdjukaar.moonlight.api.platform.ClientHelper;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;

public class SpelunkeryClient {

    public static final ResourceLocation PARACHUTE_3D_MODEL = Spelunkery.res("entity/parachute");

    public static void init() {
        ClientHelper.addClientSetup(SpelunkeryClient::setup);
        ClientHelper.addEntityRenderersRegistration(SpelunkeryClient::registerEntityRenderers);
        ClientHelper.addSpecialModelRegistration(SpelunkeryClient::registerSpecialModels);
        ClientHelper.addParticleRegistration(SpelunkeryClient::registerParticles);
    }

    private static boolean finishedSetup = false;

    public static void setup() {
        ClientHelper.registerFluidRenderType(ModFluids.FLOWING_PORTAL_FLUID.get(), RenderType.translucent());
        ClientHelper.registerFluidRenderType(ModFluids.PORTAL_FLUID.get(), RenderType.translucent());

        ClientHelper.registerRenderType(ModBlocks.PORTAL_FLUID.get(), RenderType.translucent());
        ClientHelper.registerRenderType(ModBlocks.PORTAL_CAULDRON.get(), RenderType.translucent());
        ClientHelper.registerRenderType(ModBlocks.WOODEN_SLUICE.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.MINEOMITE.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.SALT_LAMP.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.SALT.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.WOODEN_RAIL.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.ROPE_LADDER.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.TANGLE_ROOTS.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.TANGLE_ROOTS_PLANT.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.SPOROPHYTE.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.TALL_SPOROPHYTE.get(), RenderType.cutout());

        ClientHelper.registerRenderType(ModBlocks.CONK_FUNGUS.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.INKCAP_MUSHROOM.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.WHITE_INKCAP_MUSHROOM.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.PHOSPHOR_FUNGUS.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.MUSHGLOOM.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.MILLY_BUBCAP.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.PORTABELLA.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.CRIMINI.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.BUTTON_MUSHROOM.get(), RenderType.cutout());

        ClientHelper.registerRenderType(ModBlocks.POTTED_INKCAP_MUSHROOM.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.POTTED_WHITE_INKCAP_MUSHROOM.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.POTTED_PHOSPHOR_FUNGUS.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.POTTED_MUSHGLOOM.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.POTTED_MILLY_BUBCAP.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.POTTED_PORTABELLA.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.POTTED_CRIMINI.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.POTTED_BUTTON_MUSHROOM.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.POTTED_SPOROPHYTE.get(), RenderType.cutout());

        ClientHelper.registerRenderType(ModBlocks.NEPHRITE_FOUNTAIN.get(), RenderType.cutout());

        ItemProperties.register(ModItems.DEPTH_GAUGE.get(), Spelunkery.res("depth"),
                (stack, world, entity, seed) -> entity != null ? (((float) entity.getBlockY() + 64) / 384) : 0);

        ItemProperties.register(ModItems.NEPHRITE_CHARM.get(), Spelunkery.res("charge"),
                (stack, world, entity, seed) -> stack.getTag() != null ? (stack.getTag().getInt("xp") / 1395f) : 0);

        ItemProperties.register(ModItems.ITEM_MAGNET.get(), Spelunkery.res("active"),
                (stack, world, entity, seed) -> stack.getTag() != null && stack.getTag().getBoolean("active") ? 0.5f : 0);

        ItemProperties.register(ModItems.HANDHELD_COMPACTOR.get(), Spelunkery.res("mode"),
                (stack, world, entity, seed) -> stack.getTag() != null ? (HandheldCompactorItem.getMode(stack).ordinal() / 4f) : 0f);

        ItemProperties.register(ModItems.MAGNETIC_COMPASS.get(), Spelunkery.res("angle"),
                new MagneticCompassItemPropertyFunction(((clientLevel, itemStack, entity) -> MagneticCompassItem.isMagnetiteNearby(itemStack) ? MagneticCompassItem.getMagnetitePos(itemStack.getOrCreateTag()) : MagneticCompassItem.getNorthPosition(clientLevel))));

        ItemProperties.register(ModItems.SALT_BUCKET.get(), Spelunkery.res("salt"),
                (stack, world, entity, seed) -> stack.getTag() != null ? (stack.getTag().getInt("salt") / 8f) : 0);

        ItemProperties.register(ModItems.PARACHUTE.get(), Spelunkery.res("active"),
                (stack, world, entity, seed) -> stack.getTag() != null ? (stack.getTag().getBoolean("active") ? 0.5f : 0) : 0);

        ItemProperties.register(ModItems.PARACHUTE.get(), Spelunkery.res("used"),
                (stack, world, entity, seed) -> stack.getTag() != null ? (stack.getTag().getBoolean("used") ? 0.5f : 0) : 0);

        finishedSetup = true;
    }

    @EventCalled
    private static void registerSpecialModels(ClientHelper.SpecialModelEvent event) {
        event.register(PARACHUTE_3D_MODEL);
    }

    public static void checkIfFailed() {
        if(!finishedSetup){
            throw new RuntimeException("Failed to run client setup. This is likely due to the mod integration code being outdated, crashing with other mods new versions. Terminating");
        }
    }

    private static void registerEntityRenderers(ClientHelper.EntityRendererEvent event) {
        event.register(ModEntities.GLOWSTICK.get(), context -> new ThrownItemRenderer<>(context, 1, false));
        event.register(ModEntities.MINEOMITE.get(), context -> new ThrownItemRenderer<>(context, 1, false));
        event.register(ModEntities.THROWN_PRIMED_MINEOMITE.get(), context -> new ThrownItemRenderer<>(context, 1, false));
        event.register(ModEntities.PRIMED_MINEOMITE.get(), PrimedMineomiteEntityRenderer::new);
        event.register(ModEntities.EGGPLANT.get(), context -> new ThrownItemRenderer<>(context, 1, false));
    }

    private static void registerParticles(ClientHelper.ParticleEvent event) {
        event.register(ModParticles.SULFUR.get(), SulfurParticle.Provider::new);
        event.register(ModParticles.SULFUR_DUSTING.get(), SulfurParticle.Provider::new);
        event.register(ModParticles.PORTAL_FLAME.get(), PortalFluidFlameParticle.Provider::new);
    }
}