package com.ordana.spelunkery.reg;

import net.mehvahdjukaar.moonlight.api.platform.RegHelper;

public class ModCompostable {
    public static void register() {
        RegHelper.registerCompostable(ModBlocks.TANGLE_ROOTS.get().asItem(), 0.5f);
        RegHelper.registerCompostable(ModBlocks.TANGLE_ROOTS_BLOCK.get().asItem(), 0.85f);
    }
}