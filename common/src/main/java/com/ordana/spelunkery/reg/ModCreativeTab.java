package com.ordana.spelunkery.reg;

import com.ordana.spelunkery.Spelunkery;
import com.ordana.spelunkery.configs.CommonConfigs;
import net.mehvahdjukaar.moonlight.api.platform.PlatformHelper;
import net.minecraft.world.item.CreativeModeTab;

public class ModCreativeTab {
    public static final CreativeModeTab TAB = !CommonConfigs.CREATIVE_TAB.get() ? null :
            PlatformHelper.createModTab(Spelunkery.res(Spelunkery.MOD_ID),
                    () -> ModBlocks.ROCK_SALT_BLOCK.get().asItem().getDefaultInstance(), false);

    public static CreativeModeTab getTab(CreativeModeTab tab) {
        return TAB == null ? tab : TAB;
    }
}
