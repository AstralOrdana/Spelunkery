package com.ordana.spelunkery.reg;

import com.ordana.spelunkery.Spelunkery;
import com.ordana.spelunkery.blocks.entity.*;
import com.ordana.spelunkery.entities.*;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class ModEntities {
    public static <T extends Entity> Supplier<EntityType<T>> regEntity(String name, Supplier<EntityType.Builder<T>> builder) {
        return RegHelper.registerEntityType(Spelunkery.res(name), () -> builder.get().build(name));
    }

    public static void init() {
    }

    //Living Entities
    public static Supplier<EntityType<DustBunnyEntity>> DUST_BUNNY = RegHelper.registerEntityType(
            Spelunkery.res("dust_bunny"),
            DustBunnyEntity::new, MobCategory.CREATURE, 0.8F, 0.5F, 10, 20);

    //Tile Entities
    public static final Supplier<BlockEntityType<CarvedNephriteBlockEntity>> NEPHRITE_TILE = RegHelper.registerBlockEntityType(
            Spelunkery.res("carved_nephrite"),
            () -> PlatHelper.newBlockEntityType(CarvedNephriteBlockEntity::new, ModBlocks.CARVED_NEPHRITE.get()));
    public static final Supplier<BlockEntityType<NephriteFountainEntity>> NEPHRITE_FOUNTAIN = RegHelper.registerBlockEntityType(
            Spelunkery.res("nephrite_fountain"),
            () -> PlatHelper.newBlockEntityType(NephriteFountainEntity::new, ModBlocks.NEPHRITE_FOUNTAIN.get()));
    public static final Supplier<BlockEntityType<MagnetiteBlockEntity>> MAGNETITE = RegHelper.registerBlockEntityType(
            Spelunkery.res("raw_magnetite_block"),
            () -> PlatHelper.newBlockEntityType(MagnetiteBlockEntity::new, ModBlocks.RAW_MAGNETITE_BLOCK.get()));
    public static final Supplier<BlockEntityType<SluiceBlockEntity>> WOODEN_SLUICE = RegHelper.registerBlockEntityType(
            Spelunkery.res("wooden_sluice"),
            () -> PlatHelper.newBlockEntityType(SluiceBlockEntity::new, ModBlocks.WOODEN_SLUICE.get()));
    public static final Supplier<BlockEntityType<SluiceBlockEntity>> STONE_SLUICE = RegHelper.registerBlockEntityType(
            Spelunkery.res("stone_sluice"),
            () -> PlatHelper.newBlockEntityType(SluiceBlockEntity::new, ModBlocks.STONE_SLUICE.get()));

    //Thrown Entities
    public static Supplier<EntityType<GlowstickEntity>> GLOWSTICK = RegHelper.registerEntityType(
            Spelunkery.res("glowstick"),
            GlowstickEntity::new, MobCategory.MISC, 0.28F, 0.98F, 10, 20);
    public static Supplier<EntityType<MineomiteEntity>> MINEOMITE = RegHelper.registerEntityType(
            Spelunkery.res("mineomite"),
            MineomiteEntity::new, MobCategory.MISC, 0.28F, 0.98F, 10, 20);
    public static Supplier<EntityType<ThrownPrimedMineomiteEntity>> THROWN_PRIMED_MINEOMITE = RegHelper.registerEntityType(
            Spelunkery.res("thrown_primed_mineomite"),
            ThrownPrimedMineomiteEntity::new, MobCategory.MISC, 0.28F, 0.98F, 10, 20);
    public static Supplier<EntityType<PrimedMineomiteEntity>> PRIMED_MINEOMITE = RegHelper.registerEntityType(
            Spelunkery.res("primed_mineomite"),
            PrimedMineomiteEntity::new, MobCategory.MISC, 0.98F, 0.98F, 10, 20);
    public static Supplier<EntityType<PebbleEntity>> PEBBLE = RegHelper.registerEntityType(
            Spelunkery.res("pebble"),
            PebbleEntity::new, MobCategory.MISC, 0.7F, 0.7F, 10, 20);
    public static Supplier<EntityType<EggplantEntity>> EGGPLANT = RegHelper.registerEntityType(
            Spelunkery.res("eggplant"),
            EggplantEntity::new, MobCategory.MISC, 0.7F, 0.7F, 10, 20);

    //Other Entities
    public static Supplier<EntityType<FallingLayerEntity>> FALLING_LAYER = RegHelper.registerEntityType(
            Spelunkery.res("falling_layer"),
            FallingLayerEntity::new, MobCategory.MISC, 0.98F, 0.98F, 10, 20);

}
