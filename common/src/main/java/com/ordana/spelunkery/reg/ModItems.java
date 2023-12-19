package com.ordana.spelunkery.reg;

import com.ordana.spelunkery.Spelunkery;
import com.ordana.spelunkery.items.*;
import com.ordana.spelunkery.utils.DisabledItem;
import net.mehvahdjukaar.moonlight.api.item.FuelItem;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.world.item.*;

import java.util.Objects;
import java.util.function.Supplier;

public class ModItems {

    public static void init() {
    }

    public static <T extends Item> Supplier<T> regItem(String name, Supplier<T> itemSup) {
        return RegHelper.registerItem(Spelunkery.res(name), itemSup);
    }

    private static boolean isCompatItemEanbled(String requiredMod) {
        if(Objects.equals(requiredMod, "create")) return requiredMod.equals("create");
        if(Objects.equals(requiredMod, "sullysmod")) return requiredMod.equals("sullysmod");
        if(Objects.equals(requiredMod, "oreganized")) return requiredMod.equals("oreganized");
        return PlatHelper.isModLoaded(requiredMod);
    }


    public static final Supplier<Item> STONE_PEBBLE = regItem("stone_pebble", () ->
            new PebbleItem(new Item.Properties()));
    public static final Supplier<Item> DEEPSLATE_PEBBLE = regItem("deepslate_pebble", () ->
            new PebbleItem(new Item.Properties()));
    public static final Supplier<Item> NETHERRACK_PEBBLE = regItem("netherrack_pebble", () ->
            new PebbleItem(new Item.Properties()));
    public static final Supplier<Item> BLACKSTONE_PEBBLE = regItem("blackstone_pebble", () ->
            new PebbleItem(new Item.Properties()));
    public static final Supplier<Item> BASALT_PEBBLE = regItem("basalt_pebble", () ->
            new PebbleItem(new Item.Properties()));
    public static final Supplier<Item> END_STONE_PEBBLE = regItem("end_stone_pebble", () ->
            new PebbleItem(new Item.Properties()));

    public static final Supplier<Item> ROCK_SALT = regItem("rock_salt", () ->
            new RockSaltBlockItem(ModBlocks.ROCK_SALT.get(), new Item.Properties()));
    public static final Supplier<Item> SALT = regItem("salt", () ->
            new SaltBlockItem(ModBlocks.SALT.get(), new Item.Properties()));
    public static final Supplier<Item> NEPHRITE_CHUNK = regItem("nephrite_chunk", () ->
            new Item(new Item.Properties()));
    public static final Supplier<Item> RAW_MAGNETITE = regItem("raw_magnetite", () ->
            new Item(new Item.Properties()));

    public static final Supplier<Item> RAW_IRON_NUGGET = regItem("raw_iron_nugget", () ->
            new Item(new Item.Properties()));
    public static final Supplier<Item> RAW_COPPER_NUGGET = regItem("raw_copper_nugget", () ->
            new Item(new Item.Properties()));
    public static final Supplier<Item> RAW_GOLD_NUGGET = regItem("raw_gold_nugget", () ->
            new Item(new Item.Properties()));
    public static final Supplier<Item> RAW_ZINC_NUGGET = regItem("raw_zinc_nugget", () ->
            new Item(new Item.Properties()));
    public static final Supplier<Item> RAW_LEAD_NUGGET = regItem("raw_lead_nugget", () ->
            new Item(new Item.Properties()));
    public static final Supplier<Item> RAW_SILVER_NUGGET = regItem("raw_silver_nugget", () ->
            new Item(new Item.Properties()));
    public static final Supplier<Item> RAW_MAGNETITE_NUGGET = regItem("raw_magnetite_nugget", () ->
            new Item(new Item.Properties()));

    public static final Supplier<Item> COPPER_NUGGET = regItem("copper_nugget", () ->
            new Item(new Item.Properties()));

    public static final Supplier<Item> COAL_LUMP = regItem("coal_lump", () ->
            new FuelItem(new Item.Properties(), () -> 200));
    public static final Supplier<Item> CHARCOAL_LUMP = regItem("charcoal_lump", () ->
            new FuelItem(new Item.Properties(), () -> 200));
    public static final Supplier<Item> ROUGH_CINNABAR_SHARD = regItem("rough_cinnabar_shard", () ->
            new Item(new Item.Properties()));
    public static final Supplier<Item> ROUGH_LAZURITE_SHARD = regItem("rough_lazurite_shard", () ->
            new Item(new Item.Properties()));
    public static final Supplier<Item> ROUGH_EMERALD_SHARD = regItem("rough_emerald_shard", () ->
            new Item(new Item.Properties()));
    public static final Supplier<Item> ROUGH_DIAMOND_SHARD = regItem("rough_diamond_shard", () ->
            new Item(new Item.Properties()));
    public static final Supplier<Item> ROUGH_QUARTZ_SHARD = regItem("rough_quartz_shard", () ->
        new Item(new Item.Properties()));
    public static final Supplier<Item> ROUGH_JADE_SHARD = regItem("rough_jade_shard", () ->
            new Item(new Item.Properties()));

    public static final Supplier<Item> ROUGH_CINNABAR = regItem("rough_cinnabar", () ->
            new Item(new Item.Properties()));
    public static final Supplier<Item> ROUGH_LAZURITE = regItem("rough_lazurite", () ->
            new Item(new Item.Properties()));
    public static final Supplier<Item> ROUGH_EMERALD = regItem("rough_emerald", () ->
            new Item(new Item.Properties()));
    public static final Supplier<Item> ROUGH_DIAMOND = regItem("rough_diamond", () ->
            new Item(new Item.Properties()));

    public static final Supplier<Item> CINNABAR_SHARD = regItem("cinnabar_shard", () ->
            new Item(new Item.Properties()));
    public static final Supplier<Item> LAPIS_LAZULI_SHARD = regItem("lapis_lazuli_shard", () ->
            new Item(new Item.Properties()));
    public static final Supplier<Item> EMERALD_SHARD = regItem("emerald_shard", () ->
            new Item(new Item.Properties()));
    public static final Supplier<Item> DIAMOND_SHARD = regItem("diamond_shard", () ->
            new Item(new Item.Properties()));
    public static final Supplier<Item> JADE_SHARD = regItem("jade_shard", () ->
            new Item(new Item.Properties()));

    public static final Supplier<Item> CINNABAR = regItem("cinnabar", () ->
            new Item(new Item.Properties()));

    public static final Supplier<Item> CARVED_NEPHRITE = regItem("carved_nephrite", () ->
            new CarvedNephriteItem(ModBlocks.CARVED_NEPHRITE.get(), new Item.Properties()));
    public static final Supplier<Item> NEPHRITE_SIPHON = regItem("nephrite_siphon", () ->
            new NephriteSiphonItem(ModBlocks.NEPHRITE_SIPHON.get(), new Item.Properties()));
    public static final Supplier<Item> NEPHRITE_FOUNTAIN = regItem("nephrite_fountain", () ->
            new NephriteSpoutItem(ModBlocks.NEPHRITE_FOUNTAIN.get(), new Item.Properties()));
    public static final Supplier<Item> NEPHRITE_DIODE = regItem("nephrite_diode", () ->
            new NephriteDiodeItem(ModBlocks.NEPHRITE_DIODE.get(), new Item.Properties()));


    public static final Supplier<Item> NEPHRITE_CHARM = regItem("nephrite_charm", () ->
            new NephriteCharmItem(new Item.Properties().stacksTo(1)));

    public static final Supplier<Item> MINEOMITE = regItem("mineomite", () ->
            new MineomiteItem(ModBlocks.MINEOMITE.get(), new Item.Properties()));
    public static final Supplier<Item> COMPRESSION_BLAST_MINER = regItem("compression_blast_miner", () ->
            new CompressionBlastMinerItem(ModBlocks.COMPRESSION_BLAST_MINER.get(), new Item.Properties()));

    //food
    public static final Supplier<Item> BUTTON_MUSHROOM = regItem("button_mushroom", () ->
            new BlockItem(ModBlocks.BUTTON_MUSHROOM.get(), new Item.Properties().food(ModFoods.BUTTON_MUSHROOM)));
    public static final Supplier<Item> CRIMINI = regItem("crimini", () ->
            new BlockItem(ModBlocks.CRIMINI.get(), new Item.Properties().food(ModFoods.CRIMINI)));
    public static final Supplier<Item> PORTABELLA = regItem("portabella", () ->
            new BlockItem(ModBlocks.PORTABELLA.get(), new Item.Properties().food(ModFoods.PORTABELLA)));
    public static final Supplier<Item> GRILLED_PORTABELLA = regItem("grilled_portabella", () ->
            new Item(new Item.Properties().food(ModFoods.GRILLED_PORTABELLA)));


    public static final Supplier<Item> DUST_BUN = regItem("dust_bun", () ->
            new DustBunItem(new Item.Properties().stacksTo(16)));

    //mining gear
    public static final Supplier<Item> ROPE_LADDER = regItem("rope_ladder", () ->
            new RopeLadderBlockItem(ModBlocks.ROPE_LADDER.get(), new Item.Properties()));
    public static final Supplier<Item> WOODEN_RAIL = regItem("wooden_rail", () ->
            new WoodenRailBlockItem(ModBlocks.WOODEN_RAIL.get(), new Item.Properties()));
    public static final Supplier<Item> PARACHUTE = regItem("parachute", () ->
            new ParachuteItem(new Item.Properties().stacksTo(1).durability(16)));
    public static final Supplier<Item> FLINT_HAMMER_AND_CHISEL = regItem("flint_hammer_and_chisel", () ->
            new HammerAndChiselItem(new Item.Properties().stacksTo(1).durability(128)));
    public static final Supplier<Item> OBSIDIAN_HAMMER_AND_CHISEL = regItem("obsidian_hammer_and_chisel", () ->
            new HammerAndChiselItem(new Item.Properties().stacksTo(1).durability(1024)));
    public static final Supplier<Item> DEPTH_GAUGE = regItem("depth_gauge", () ->
            new DepthGaugeItem(new Item.Properties()));
    public static final Supplier<Item> MAGNETIC_COMPASS = regItem("magnetic_compass", () ->
            new MagneticCompassItem(new Item.Properties()));
    public static final Supplier<Item> ITEM_MAGNET = regItem("item_magnet", () ->
            new MagnetItem(new Item.Properties().stacksTo(1)));
    public static final Supplier<Item> HANDHELD_COMPACTOR = regItem("handheld_compactor", () ->
            new HandheldCompactorItem(new Item.Properties().stacksTo(1)));
    public static final Supplier<Item> PORTAL_FLUID_BOTTLE = regItem("portal_fluid_bottle", () ->
            new PortalFluidBottleitem(new Item.Properties().food(PortalFluidBottleitem.PORTAL_FLUID).stacksTo(16).rarity(Rarity.UNCOMMON)));

    public static final Supplier<Item> PORTAL_FLUID_BUCKET = regItem("portal_fluid_bucket", () ->
            new PortalFluidBucketItem(ModFluids.PORTAL_FLUID.get(), (new Item.Properties().stacksTo(1))));
    public static final Supplier<Item> SPRING_WATER_BUCKET = regItem("spring_water_bucket", () ->
            new SpringWaterBucketItem(ModFluids.SPRING_WATER.get(), (new Item.Properties().stacksTo(1))));
    public static final Supplier<Item> SALT_BUCKET = regItem("salt_bucket", () ->
            new SaltBucketItem(new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET)));
    public static final Supplier<Item> TUNING_FORK = regItem("tuning_fork", () ->
            new AmethystTuningForkItem(new Item.Properties().stacksTo(1)));
    public static final Supplier<Item> ECHO_FORK = regItem("echo_fork", () ->
            new EchoForkItem(new Item.Properties().stacksTo(1)));
    public static final Supplier<Item> GLOWSTICK = regItem("glowstick", () ->
            new GlowstickItem(null, ModBlocks.GLOWSTICK.get(), new Item.Properties()));

    //glowsticks
    public static final Supplier<Item> RED_GLOWSTICK = regItem("red_glowstick", () ->
            new GlowstickItem(DyeColor.RED, ModBlocks.RED_GLOWSTICK.get(), new Item.Properties()));
    public static final Supplier<Item> ORANGE_GLOWSTICK = regItem("orange_glowstick", () ->
            new GlowstickItem(DyeColor.ORANGE, ModBlocks.ORANGE_GLOWSTICK.get(), new Item.Properties()));
    public static final Supplier<Item> YELLOW_GLOWSTICK = regItem("yellow_glowstick", () ->
            new GlowstickItem(DyeColor.YELLOW, ModBlocks.YELLOW_GLOWSTICK.get(), new Item.Properties()));
    public static final Supplier<Item> LIME_GLOWSTICK = regItem("lime_glowstick", () ->
            new GlowstickItem(DyeColor.LIME, ModBlocks.LIME_GLOWSTICK.get(), new Item.Properties()));
    public static final Supplier<Item> GREEN_GLOWSTICK = regItem("green_glowstick", () ->
            new GlowstickItem(DyeColor.GREEN, ModBlocks.GREEN_GLOWSTICK.get(), new Item.Properties()));
    public static final Supplier<Item> CYAN_GLOWSTICK = regItem("cyan_glowstick", () ->
            new GlowstickItem(DyeColor.CYAN, ModBlocks.CYAN_GLOWSTICK.get(), new Item.Properties()));
    public static final Supplier<Item> LIGHT_BLUE_GLOWSTICK = regItem("light_blue_glowstick", () ->
            new GlowstickItem(DyeColor.LIGHT_BLUE, ModBlocks.LIGHT_BLUE_GLOWSTICK.get(), new Item.Properties()));
    public static final Supplier<Item> BLUE_GLOWSTICK = regItem("blue_glowstick", () ->
            new GlowstickItem(DyeColor.BLUE, ModBlocks.BLUE_GLOWSTICK.get(), new Item.Properties()));
    public static final Supplier<Item> PURPLE_GLOWSTICK = regItem("purple_glowstick", () ->
            new GlowstickItem(DyeColor.PURPLE, ModBlocks.PURPLE_GLOWSTICK.get(), new Item.Properties()));
    public static final Supplier<Item> MAGENTA_GLOWSTICK = regItem("magenta_glowstick", () ->
            new GlowstickItem(DyeColor.MAGENTA, ModBlocks.MAGENTA_GLOWSTICK.get(), new Item.Properties()));
    public static final Supplier<Item> PINK_GLOWSTICK = regItem("pink_glowstick", () ->
            new GlowstickItem(DyeColor.PINK, ModBlocks.PINK_GLOWSTICK.get(), new Item.Properties()));
    public static final Supplier<Item> BROWN_GLOWSTICK = regItem("brown_glowstick", () ->
            new GlowstickItem(DyeColor.BROWN, ModBlocks.BROWN_GLOWSTICK.get(), new Item.Properties()));
    public static final Supplier<Item> BLACK_GLOWSTICK = regItem("black_glowstick", () ->
            new GlowstickItem(DyeColor.BLACK, ModBlocks.BLACK_GLOWSTICK.get(), new Item.Properties()));
    public static final Supplier<Item> WHITE_GLOWSTICK = regItem("white_glowstick", () ->
            new GlowstickItem(DyeColor.WHITE, ModBlocks.WHITE_GLOWSTICK.get(), new Item.Properties()));
    public static final Supplier<Item> GRAY_GLOWSTICK = regItem("gray_glowstick", () ->
            new GlowstickItem(DyeColor.GRAY, ModBlocks.GRAY_GLOWSTICK.get(), new Item.Properties()));
    public static final Supplier<Item> LIGHT_GRAY_GLOWSTICK = regItem("light_gray_glowstick", () ->
            new GlowstickItem(DyeColor.LIGHT_GRAY, ModBlocks.LIGHT_GRAY_GLOWSTICK.get(), new Item.Properties()));

    public static final Supplier<Item> DUST_BUNNY_SPAWN_EGG = RegHelper.registerItem(Spelunkery.res("dust_bunny_spawn_egg"), () ->
            PlatHelper.newSpawnEgg(ModEntities.DUST_BUNNY, 0x5E625E, 0x3F3C39, new Item.Properties()));

    @DisabledItem
    public static final Supplier<Item> EGGPLANT = regItem("eggplant", () ->
            new EggplantItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));

}
