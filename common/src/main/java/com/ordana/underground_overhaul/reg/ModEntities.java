package com.ordana.underground_overhaul.reg;

import com.ordana.underground_overhaul.UndergroundOverhaul;
import com.ordana.underground_overhaul.blocks.entity.CarvedNephriteBlockEntity;
import com.ordana.underground_overhaul.blocks.entity.NephriteSpoutEntity;
import net.mehvahdjukaar.moonlight.api.platform.PlatformHelper;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class ModEntities {
    public static void init() {
    }

    public static final Supplier<BlockEntityType<CarvedNephriteBlockEntity>> NEPHRITE_TILE = RegHelper.registerBlockEntityType(
            UndergroundOverhaul.res("carved_nephrite"),
            () -> PlatformHelper.newBlockEntityType(CarvedNephriteBlockEntity::new, ModBlocks.CARVED_NEPHRITE.get()));

    public static final Supplier<BlockEntityType<NephriteSpoutEntity>> NEPHRITE_SPOUT = RegHelper.registerBlockEntityType(
            UndergroundOverhaul.res("nephrite_spout"),
            () -> PlatformHelper.newBlockEntityType(NephriteSpoutEntity::new, ModBlocks.NEPHRITE_SPOUT.get()));
}
