package com.ordana.spelunkery.reg;

import com.ordana.spelunkery.Spelunkery;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.core.Registry;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.function.Supplier;

public class ModGameEvents {
    public static void init() {
    }

    public static final Supplier<GameEvent> COMPASS_PING_EVENT = RegHelper.register(Spelunkery.res("compass_ping"),
            () -> new GameEvent("compass_ping", 128), Registry.GAME_EVENT);
}
