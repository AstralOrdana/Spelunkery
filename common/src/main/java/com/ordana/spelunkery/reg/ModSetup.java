package com.ordana.spelunkery.reg;

import com.google.common.base.Stopwatch;
import com.ordana.spelunkery.Spelunkery;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;

import java.util.ArrayList;
import java.util.List;

public class ModSetup {

    private static int setupStage = 0;

    private static final List<Runnable> MOD_SETUP_WORK = List.of(
            ModSetup::registerFabricFlammable
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
}