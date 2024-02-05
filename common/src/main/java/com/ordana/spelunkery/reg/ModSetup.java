package com.ordana.spelunkery.reg;

import com.google.common.base.Stopwatch;
import com.ordana.spelunkery.Spelunkery;
import com.ordana.spelunkery.blocks.dispenser_interactions.MineomiteBehavior;
import com.ordana.spelunkery.blocks.dispenser_interactions.PebbleBehavior;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.mehvahdjukaar.moonlight.api.util.DispenserHelper;
import net.minecraft.core.Registry;

import java.util.ArrayList;
import java.util.List;

public class ModSetup {

    private static int setupStage = 0;

    private static final List<Runnable> MOD_SETUP_WORK = List.of(
            ModSetup::registerFabricFlammable,
            ModSetup::registerDispenserBehvaiors
    );

    public static void setup() {
        var list = new ArrayList<Long>();
        try {
            Stopwatch watch = Stopwatch.createStarted();

            for (int i = 0; i < MOD_SETUP_WORK.size(); i++) {
                setupStage = i;
                MOD_SETUP_WORK.get(i).run();
                list.add(watch.elapsed().toMillis());
                watch.reset();
                watch.start();
            }

            Spelunkery.LOGGER.info("Finished mod setup in: {} ms", list);

        } catch (Exception e) {
            Spelunkery.LOGGER.error(e);
            terminateWhenSetupFails();
        }
    }

    private static void terminateWhenSetupFails() {
        //if setup fails crash the game. idk why it doesn't do that on its own wtf
        throw new IllegalStateException("Mod setup has failed to complete (" + setupStage + ").\n" +
                " This might be due to some mod incompatibility or outdated dependencies (check if everything is up to date).\n" +
                " Refusing to continue loading with a broken modstate. Next step: crashing this game, no survivors");
    }

    private static void registerFabricFlammable() {
        RegHelper.registerBlockFlammability(ModBlocks.WOODEN_CHANNEL.get(), 5, 20);
        RegHelper.registerBlockFlammability(ModBlocks.WOODEN_SLUICE.get(), 5, 20);
    }


    private static void registerDispenserBehvaiors() {
        DispenserHelper.registerCustomBehavior(new MineomiteBehavior(ModItems.MINEOMITE.get()));
        Registry.ITEM.getTagOrEmpty(ModTags.PEBBLES).iterator().forEachRemaining(h ->
                DispenserHelper.registerCustomBehavior(new PebbleBehavior(h.value()))
        );
        Registry.ITEM.getTagOrEmpty(ModTags.GLOWSTICKS).iterator().forEachRemaining(h ->
                DispenserHelper.registerCustomBehavior(new PebbleBehavior(h.value()))
        );
    }
}