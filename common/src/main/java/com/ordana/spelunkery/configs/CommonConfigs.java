package com.ordana.spelunkery.configs;

import com.ordana.spelunkery.Spelunkery;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigBuilder;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigType;
import net.mehvahdjukaar.moonlight.api.platform.configs.ModConfigHolder;

import java.util.function.Supplier;

public class CommonConfigs {


    public static ModConfigHolder SERVER_SPEC;

    public static Supplier<Boolean> CREATIVE_TAB;
    //public static Supplier<Boolean> CROSS_SECTION;
    public static Supplier<Integer> DIAMOND_GRINDSTONE_DEPLETE_CHANCE;
    public static Supplier<Integer> PARACHUTE_DELAY;
    public static Supplier<Boolean> ENABLE_ROUGH_GEMS;
    public static Supplier<Boolean> ENABLE_RAW_NUGGETS;
    public static Supplier<Boolean> ORE_STONE_DROPS;
    //public static Supplier<Boolean> ENABLE_GEM_SHARDS;

    public static Supplier<Boolean> INCREASED_SLIME_SPAWN_RATE;
    public static Supplier<Boolean> SLIME_CAULDRONS;
    public static Supplier<Boolean> SLIMES_FRIENDLY_REGEN;
    public static Supplier<Boolean> SLIMES_ALWAYS_FRIENDLY;
    public static Supplier<Integer> SLIME_EAT_COOLDOWN;
    public static Supplier<Boolean> SLIME_GROWTH;
    public static Supplier<Integer> SLIME_GROWTH_CHANCE;
    public static Supplier<Integer> SLIME_GROWTH_MAX;

    public static Supplier<Integer> HAMMER_CHISEL_CHARGE_TIME;
    public static Supplier<Integer> TUNING_FORK_RANGE;
    public static Supplier<Integer> ECHO_FORK_RANGE;
    public static Supplier<Integer> ECHO_DURRATION;
    public static Supplier<Integer> ECHO_COOLDOWN;
    public static Supplier<Boolean> SCULK_SHEARING;
    public static Supplier<Integer> MAGNET_RANGE;
    public static Supplier<Integer> MAGNETITE_RANGE;

    public static Supplier<Boolean> STONE_STRIPE_FEATURES;
    public static Supplier<Boolean> ENABLE_MORES;


    public static void init() {
        // bump class load init
    }

    static {
        ConfigBuilder builder = ConfigBuilder.create(Spelunkery.res("common"), ConfigType.COMMON);

//        builder.setSynced();

        builder.push("misc");
        CREATIVE_TAB = builder.define("spelunkery_creative_tab", false);
        DIAMOND_GRINDSTONE_DEPLETE_CHANCE = builder.define("diamond_grindstone_deplete_chance", 64, 0, 128);
        PARACHUTE_DELAY = builder.define("parachute_opening_delay", 10, 0, 128);
        ENABLE_MORES = builder.define("enable_mores", true);
        ENABLE_ROUGH_GEMS = builder.define("enable_rough_gems", true);
        ENABLE_RAW_NUGGETS = builder.define("enable_raw_nuggets", true);
        PlatHelper.getPlatform().ifFabric(() -> {
            ORE_STONE_DROPS = builder.define("ores_drop_base_stone", false);
        });
        builder.pop();

        builder.push("utilities");
        HAMMER_CHISEL_CHARGE_TIME = builder.define("hammer_and_chisel_charge_time", 20, 1, 128);
        TUNING_FORK_RANGE = builder.define("tuning_fork_range", 128, 1, 256);
        ECHO_FORK_RANGE = builder.define("echo_fork_range", 16, 1, 256);
        ECHO_COOLDOWN = builder.define("echo_fork_cooldown", 600, 1, 72000);
        ECHO_DURRATION = builder.define("echo_glow_duration", 1200, 1, 72000);
        SCULK_SHEARING = builder.define("sculk_drops_with_shears", true);
        MAGNET_RANGE = builder.define("magnet_range", 8, 1, 32);
        MAGNETITE_RANGE = builder.define("magnetite_range", 64, 1, 512);
        builder.pop();

        builder.push("slimes");
        INCREASED_SLIME_SPAWN_RATE = builder.define("slimes_spawn_in_all_caves", true);
        SLIME_CAULDRONS = builder.define("create_slimes_in_cauldrons", true);
        SLIMES_FRIENDLY_REGEN = builder.define("slimes_friendly_to_players_with_regeneration", true);
        SLIMES_ALWAYS_FRIENDLY = builder.define("slimes_always_friendly", false);
        SLIME_EAT_COOLDOWN = builder.define("slime_eating_cooldown", 150, 1, 2400);
        SLIME_GROWTH = builder.define("enable_slime_growth", true);
        SLIME_GROWTH_CHANCE = builder.define("slime_growth_chance", 10, 1, 127);
        SLIME_GROWTH_MAX = builder.define("slime_growth_max", 5, 1, 127);
        builder.pop();

        builder.push("worldgen");
        STONE_STRIPE_FEATURES = builder.define("stone_stripe_features", true);
        builder.pop();

        SERVER_SPEC = builder.build();
        SERVER_SPEC.forceLoad();
    }
}
