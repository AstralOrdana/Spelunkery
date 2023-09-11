package com.ordana.spelunkery.reg;

import com.ordana.spelunkery.Spelunkery;
import com.ordana.spelunkery.blocks.entity.CarvedNephriteBlockEntity;
import com.ordana.spelunkery.blocks.entity.MagnetiteBlockEntity;
import com.ordana.spelunkery.blocks.entity.NephriteFountainEntity;
import com.ordana.spelunkery.blocks.entity.WoodenSluiceBlockEntity;
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

    public static final Supplier<BlockEntityType<CarvedNephriteBlockEntity>> NEPHRITE_TILE = RegHelper.registerBlockEntityType(
            Spelunkery.res("carved_nephrite"),
            () -> PlatHelper.newBlockEntityType(CarvedNephriteBlockEntity::new, ModBlocks.CARVED_NEPHRITE.get()));

    public static final Supplier<BlockEntityType<NephriteFountainEntity>> NEPHRITE_FOUNTAIN = RegHelper.registerBlockEntityType(
            Spelunkery.res("nephrite_fountain"),
            () -> PlatHelper.newBlockEntityType(NephriteFountainEntity::new, ModBlocks.NEPHRITE_FOUNTAIN.get()));

    public static final Supplier<BlockEntityType<MagnetiteBlockEntity>> MAGNETITE = RegHelper.registerBlockEntityType(
            Spelunkery.res("raw_magnetite_block"),
            () -> PlatHelper.newBlockEntityType(MagnetiteBlockEntity::new, ModBlocks.RAW_MAGNETITE_BLOCK.get()));

    public static final Supplier<BlockEntityType<WoodenSluiceBlockEntity>> WOODEN_SLUICE = RegHelper.registerBlockEntityType(
            Spelunkery.res("wooden_sluice"),
            () -> PlatHelper.newBlockEntityType(WoodenSluiceBlockEntity::new, ModBlocks.WOODEN_SLUICE.get()));

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

    public static Supplier<EntityType<EggplantEntity>> EGGPLANT = RegHelper.registerEntityType(
            Spelunkery.res("eggplant"),
            EggplantEntity::new, MobCategory.MISC, 0.7F, 0.7F, 10, 20);
}
