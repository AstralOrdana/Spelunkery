package com.ordana.spelunkery;

import com.ordana.spelunkery.blocks.rock_salt.RockSaltBlock;
import com.ordana.spelunkery.entities.DustBunnyModel;
import com.ordana.spelunkery.entities.DustBunnyRenderer;
import com.ordana.spelunkery.items.AmethystTuningForkItem;
import com.ordana.spelunkery.items.HandheldCompactorItem;
import com.ordana.spelunkery.items.MagneticCompassItem;
import com.ordana.spelunkery.items.magnetic_compass.MagneticCompassItemPropertyFunction;
import com.ordana.spelunkery.particles.SulfurParticle;
import com.ordana.spelunkery.reg.*;
import net.mehvahdjukaar.moonlight.api.client.renderer.FallingBlockRendererGeneric;
import net.mehvahdjukaar.moonlight.api.misc.EventCalled;
import net.mehvahdjukaar.moonlight.api.platform.ClientHelper;
import net.mehvahdjukaar.moonlight.api.util.math.colors.RGBColor;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.particle.ExplodeParticle;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

public class SpelunkeryClient {

    public static final ModelLayerLocation DUST_BUNNY = loc("dust_bunny");
    public static final ResourceLocation PARACHUTE_3D_MODEL = Spelunkery.res("entity/parachute");

    public static void init() {
        ClientHelper.addClientSetup(SpelunkeryClient::setup);
        ClientHelper.addModelLayerRegistration(SpelunkeryClient::registerLayers);
        ClientHelper.addEntityRenderersRegistration(SpelunkeryClient::registerEntityRenderers);
        ClientHelper.addSpecialModelRegistration(SpelunkeryClient::registerSpecialModels);
        ClientHelper.addParticleRegistration(SpelunkeryClient::registerParticles);
    }

    private static boolean finishedSetup = false;

    public static void setup() {
        ClientHelper.registerFluidRenderType(ModFluids.FLOWING_SPRING_WATER.get(), RenderType.translucent());
        ClientHelper.registerFluidRenderType(ModFluids.SPRING_WATER.get(), RenderType.translucent());

        ClientHelper.registerRenderType(ModBlocks.POLISHED_QUARTZ_BLOCK.get(), RenderType.translucent());

        ClientHelper.registerRenderType(ModBlocks.SPRING_WATER.get(), RenderType.translucent());
        ClientHelper.registerRenderType(ModBlocks.WOODEN_SLUICE.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.STONE_SLUICE.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.MINEOMITE.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.SALT_LAMP.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.SALT.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.WOODEN_RAIL.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.ROPE_LADDER.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.TANGLE_ROOTS.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.TANGLE_ROOTS_PLANT.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.SPOROPHYTE.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.TALL_SPOROPHYTE.get(), RenderType.cutout());

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

        ItemProperties.register(ModItems.TUNING_FORK.get(), Spelunkery.res("angle"),
                new MagneticCompassItemPropertyFunction(((clientLevel, itemStack, entity) -> AmethystTuningForkItem.getAmethystPos(itemStack.getOrCreateTag()))));

        ItemProperties.register(ModItems.SALT_BUCKET.get(), Spelunkery.res("salt"),
                (stack, world, entity, seed) -> stack.getTag() != null ? (stack.getTag().getInt("salt") / 8f) : 0);

        ItemProperties.register(ModItems.PARACHUTE.get(), Spelunkery.res("active"),
                (stack, world, entity, seed) -> stack.getTag() != null ? (stack.getTag().getBoolean("active") ? 0.5f : 0) : 0);

        ItemProperties.register(ModItems.PARACHUTE.get(), Spelunkery.res("used"),
                (stack, world, entity, seed) -> stack.getTag() != null ? (stack.getTag().getBoolean("used") ? 0.5f : 0) : 0);

        ItemProperties.register(ModBlocks.DIAMOND_GRINDSTONE.get().asItem(), Spelunkery.res("depletion"),
                (stack, world, entity, seed) -> stack.getTag() != null ? (stack.getTag().getInt("depletion") / 8f) : 0);

        finishedSetup = true;
    }

    private static ModelLayerLocation loc(String name) {
        return new ModelLayerLocation(Spelunkery.res(name), name);
    }

    private static void registerLayers(ClientHelper.ModelLayerEvent event) {
        event.register(DUST_BUNNY, DustBunnyModel::createBodyLayer);
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

    @EventCalled
    private static void registerBlockColors(ClientHelper.BlockColorEvent event) {
        event.register((blockState, blockAndTintGetter, blockPos, i) -> getSaltTint(event, blockState, blockAndTintGetter, blockPos, i), ModBlocks.ROCK_SALT_BLOCK.get());
        event.register((blockState, blockAndTintGetter, blockPos, i) -> getSaltTint(event, blockState, blockAndTintGetter, blockPos, i), ModBlocks.ROCK_SALT_STAIRS.get());
        event.register((blockState, blockAndTintGetter, blockPos, i) -> getSaltTint(event, blockState, blockAndTintGetter, blockPos, i), ModBlocks.ROCK_SALT_SLAB.get());
        event.register((blockState, blockAndTintGetter, blockPos, i) -> getSaltTint(event, blockState, blockAndTintGetter, blockPos, i), ModBlocks.ROCK_SALT_WALL.get());
        event.register((blockState, blockAndTintGetter, blockPos, i) -> getSaltTint(event, blockState, blockAndTintGetter, blockPos, i), ModBlocks.POLISHED_ROCK_SALT.get());
        event.register((blockState, blockAndTintGetter, blockPos, i) -> getSaltTint(event, blockState, blockAndTintGetter, blockPos, i), ModBlocks.POLISHED_ROCK_SALT_STAIRS.get());
        event.register((blockState, blockAndTintGetter, blockPos, i) -> getSaltTint(event, blockState, blockAndTintGetter, blockPos, i), ModBlocks.POLISHED_ROCK_SALT_SLAB.get());
        event.register((blockState, blockAndTintGetter, blockPos, i) -> getSaltTint(event, blockState, blockAndTintGetter, blockPos, i), ModBlocks.POLISHED_ROCK_SALT_WALL.get());
        event.register((blockState, blockAndTintGetter, blockPos, i) -> getSaltTint(event, blockState, blockAndTintGetter, blockPos, i), ModBlocks.ROCK_SALT_BRICKS.get());
        event.register((blockState, blockAndTintGetter, blockPos, i) -> getSaltTint(event, blockState, blockAndTintGetter, blockPos, i), ModBlocks.ROCK_SALT_BRICK_STAIRS.get());
        event.register((blockState, blockAndTintGetter, blockPos, i) -> getSaltTint(event, blockState, blockAndTintGetter, blockPos, i), ModBlocks.ROCK_SALT_BRICK_SLAB.get());
        event.register((blockState, blockAndTintGetter, blockPos, i) -> getSaltTint(event, blockState, blockAndTintGetter, blockPos, i), ModBlocks.ROCK_SALT_BRICK_WALL.get());
    }

    private static int getSaltTint(ClientHelper.BlockColorEvent event, BlockState state, BlockAndTintGetter level, BlockPos pos, int i) {
        //int original = event.getColor(state, level, pos, i);

        //interpolate between color and brown
        float percentage = 10f / state.getValue(RockSaltBlock.LIGHT);
        int brown = 0xf5df9d;
        return new RGBColor(0).asLAB().mixWith(new RGBColor(brown).asLAB(), percentage).asRGB().toInt();
    }

    private static void registerEntityRenderers(ClientHelper.EntityRendererEvent event) {
        event.register(ModEntities.FALLING_LAYER.get(), FallingBlockRendererGeneric::new);
        event.register(ModEntities.DUST_BUNNY.get(), DustBunnyRenderer::new);
        event.register(ModEntities.GLOWSTICK.get(), context -> new ThrownItemRenderer<>(context, 1, true));
        event.register(ModEntities.MINEOMITE.get(), context -> new ThrownItemRenderer<>(context, 1, false));
        event.register(ModEntities.PEBBLE.get(), context -> new ThrownItemRenderer<>(context, 1, false));
        event.register(ModEntities.PICK.get(), context -> new ThrownItemRenderer<>(context, 1, false));
        event.register(ModEntities.EGGPLANT.get(), context -> new ThrownItemRenderer<>(context, 1, false));
    }

    private static void registerParticles(ClientHelper.ParticleEvent event) {
        event.register(ModParticles.SULFUR.get(), SulfurParticle.Provider::new);
        event.register(ModParticles.SULFUR_DUSTING.get(), SulfurParticle.Provider::new);
        event.register(ModParticles.DUST_POOF.get(), ExplodeParticle.Provider::new);
    }
}