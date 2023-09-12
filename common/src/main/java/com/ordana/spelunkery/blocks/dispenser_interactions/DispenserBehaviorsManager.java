package com.ordana.spelunkery.blocks.dispenser_interactions;


import com.ordana.spelunkery.reg.ModItems;
import net.mehvahdjukaar.moonlight.api.util.DispenserHelper;
import net.minecraft.core.RegistryAccess;

public class DispenserBehaviorsManager {

    public static void registerBehaviors() {
        DispenserHelper.registerCustomBehavior(new MineomiteDispenserBehavior(ModItems.MINEOMITE.get()));
    }
}
