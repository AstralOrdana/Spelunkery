package com.ordana.underground_overhaul.reg;

import com.ordana.underground_overhaul.UndergroundOverhaul;
import com.ordana.underground_overhaul.blocks.entity.CarvedNephriteBlockEntity;
import com.ordana.underground_overhaul.blocks.entity.NephriteSpoutEntity;
import com.ordana.underground_overhaul.entities.GlowstickEntity;
import net.mehvahdjukaar.moonlight.api.entity.ImprovedProjectileEntity;
import net.mehvahdjukaar.moonlight.api.platform.PlatformHelper;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class ModEntities {
    public static <T extends Entity> Supplier<EntityType<T>> regEntity(String name, Supplier<EntityType.Builder<T>> builder) {
        return RegHelper.registerEntityType(UndergroundOverhaul.res(name), () -> builder.get().build(name));
    }

    public static void init() {
    }

    public static final Supplier<BlockEntityType<CarvedNephriteBlockEntity>> NEPHRITE_TILE = RegHelper.registerBlockEntityType(
            UndergroundOverhaul.res("carved_nephrite"),
            () -> PlatformHelper.newBlockEntityType(CarvedNephriteBlockEntity::new, ModBlocks.CARVED_NEPHRITE.get()));

    public static final Supplier<BlockEntityType<NephriteSpoutEntity>> NEPHRITE_SPOUT = RegHelper.registerBlockEntityType(
            UndergroundOverhaul.res("nephrite_spout"),
            () -> PlatformHelper.newBlockEntityType(NephriteSpoutEntity::new, ModBlocks.NEPHRITE_SPOUT.get()));

    public static Supplier<EntityType<GlowstickEntity>> GLOWSTICK = RegHelper.registerEntityType(
            UndergroundOverhaul.res("glowstick"),
            GlowstickEntity::new, MobCategory.MISC, 0.28F, 0.98F, 10, 20);

}
