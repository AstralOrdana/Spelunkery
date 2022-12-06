package com.ordana.underground_overhaul.reg;

import com.ordana.underground_overhaul.UndergroundOverhaul;
import com.ordana.underground_overhaul.configs.CommonConfigs;
import net.mehvahdjukaar.moonlight.api.platform.PlatformHelper;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Blocks;

public class ModCreativeTab {
    public static final CreativeModeTab TAB = !CommonConfigs.CREATIVE_TAB.get() ? null :
            PlatformHelper.createModTab(UndergroundOverhaul.res(UndergroundOverhaul.MOD_ID),
                    () -> ModBlocks.ROCK_SALT_BLOCK.get().asItem().getDefaultInstance(), false);

    public static CreativeModeTab getTab(CreativeModeTab tab) {
        return TAB == null ? tab : TAB;
    }
}
