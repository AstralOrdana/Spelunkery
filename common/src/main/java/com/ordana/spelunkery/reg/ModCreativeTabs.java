package com.ordana.spelunkery.reg;

import com.ordana.spelunkery.Spelunkery;
import com.ordana.spelunkery.configs.CommonConfigs;
import net.mehvahdjukaar.moonlight.api.misc.RegSupplier;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ModCreativeTabs {

    public static final RegSupplier<CreativeModeTab> MOD_TAB = !CommonConfigs.CREATIVE_TAB.get() ? null :
            RegHelper.registerCreativeModeTab(Spelunkery.res("spelunkery"),
                    (c) -> c.title(Component.translatable("itemGroup.spelunkery"))
                            .icon(() -> ModItems.ROCK_SALT.get().getDefaultInstance()));


    public static void init() {
        RegHelper.addItemsToTabsRegistration(ModCreativeTabs::registerItemsToTabs);
    }


    public static void registerItemsToTabs(RegHelper.ItemToTabEvent e) {


        after(e, Items.DEEPSLATE_COAL_ORE, CreativeModeTabs.NATURAL_BLOCKS,
                ModConstants.COAL_ORE,
                ModBlocks.ANDESITE_COAL_ORE, ModBlocks.DIORITE_COAL_ORE, ModBlocks.GRANITE_COAL_ORE, ModBlocks.TUFF_COAL_ORE
        );

        after(e, Items.DEEPSLATE_IRON_ORE, CreativeModeTabs.NATURAL_BLOCKS,
                ModConstants.IRON_ORE,
                ModBlocks.ANDESITE_IRON_ORE, ModBlocks.DIORITE_IRON_ORE, ModBlocks.GRANITE_IRON_ORE, ModBlocks.TUFF_IRON_ORE
        );
        after(e, Items.DEEPSLATE_COPPER_ORE, CreativeModeTabs.NATURAL_BLOCKS,
                ModConstants.COPPER_ORE,
                ModBlocks.ANDESITE_COPPER_ORE, ModBlocks.DIORITE_COPPER_ORE, ModBlocks.GRANITE_COPPER_ORE, ModBlocks.TUFF_COPPER_ORE
        );
        after(e, Items.DEEPSLATE_GOLD_ORE, CreativeModeTabs.NATURAL_BLOCKS,
                ModConstants.GOLD_ORE,
                ModBlocks.ANDESITE_GOLD_ORE, ModBlocks.DIORITE_GOLD_ORE, ModBlocks.GRANITE_GOLD_ORE, ModBlocks.TUFF_GOLD_ORE
        );
        after(e, Items.DEEPSLATE_REDSTONE_ORE, CreativeModeTabs.NATURAL_BLOCKS,
                ModConstants.REDSTONE_ORE,
                ModBlocks.ANDESITE_REDSTONE_ORE, ModBlocks.DIORITE_REDSTONE_ORE, ModBlocks.GRANITE_REDSTONE_ORE, ModBlocks.TUFF_REDSTONE_ORE,
                ModBlocks.CALCITE_REDSTONE_ORE
        );
        after(e, Items.DEEPSLATE_LAPIS_ORE, CreativeModeTabs.NATURAL_BLOCKS,
                ModConstants.LAPIS_ORE,
                ModBlocks.ANDESITE_LAPIS_ORE, ModBlocks.DIORITE_LAPIS_ORE, ModBlocks.GRANITE_LAPIS_ORE, ModBlocks.TUFF_LAPIS_ORE,
                ModBlocks.SANDSTONE_LAPIS_ORE
        );
        after(e, Items.DEEPSLATE_EMERALD_ORE, CreativeModeTabs.NATURAL_BLOCKS,
                ModConstants.EMERALD_ORE,
                ModBlocks.ANDESITE_EMERALD_ORE, ModBlocks.DIORITE_EMERALD_ORE, ModBlocks.GRANITE_EMERALD_ORE, ModBlocks.TUFF_EMERALD_ORE
        );
        after(e, Items.DEEPSLATE_DIAMOND_ORE, CreativeModeTabs.NATURAL_BLOCKS,
                ModConstants.DIAMOND_ORE,
                ModBlocks.ANDESITE_DIAMOND_ORE, ModBlocks.DIORITE_DIAMOND_ORE, ModBlocks.GRANITE_DIAMOND_ORE, ModBlocks.TUFF_DIAMOND_ORE,
                ModBlocks.SMOOTH_BASALT_DIAMOND_ORE
        );

        after(e, Items.RAW_GOLD_BLOCK, CreativeModeTabs.NATURAL_BLOCKS,
                ModConstants.ROUGH_BLOCKS,
                ModBlocks.ROUGH_CINNABAR_BLOCK, ModBlocks.ROUGH_LAZURITE_BLOCK, ModBlocks.ROUGH_EMERALD_BLOCK,
                ModBlocks.ROUGH_DIAMOND_BLOCK, ModBlocks.ROUGH_QUARTZ_BLOCK, ModBlocks.RAW_MAGNETITE_BLOCK
        );

        after(e, Items.REDSTONE_BLOCK, CreativeModeTabs.BUILDING_BLOCKS,
                ModConstants.CINNABAR_BLOCK,
                ModBlocks.CINNABAR_BLOCK
        );
        after(e, Items.REDSTONE_BLOCK, CreativeModeTabs.REDSTONE_BLOCKS,
                ModConstants.CINNABAR_BLOCK,
                ModBlocks.CINNABAR_BLOCK
        );

        after(e, Items.GRINDSTONE, CreativeModeTabs.FUNCTIONAL_BLOCKS,
                ModConstants.DIAMOND_GRINDSTONE,
                ModBlocks.DIAMOND_GRINDSTONE
        );

        after(e, Items.REDSTONE_TORCH, CreativeModeTabs.FUNCTIONAL_BLOCKS,
                ModConstants.SALT_LAMP,
                ModBlocks.SALT_LAMP
        );

        after(e, Items.RED_SANDSTONE, CreativeModeTabs.NATURAL_BLOCKS,
                ModConstants.ROCK_SALT,
                ModBlocks.ROCK_SALT_BLOCK, ModBlocks.ROCK_SALT
        );

        after(e, Items.CUT_RED_SANDSTONE_SLAB, CreativeModeTabs.BUILDING_BLOCKS,
                ModConstants.SALT,
                ModBlocks.ROCK_SALT_BLOCK, ModBlocks.ROCK_SALT_STAIRS, ModBlocks.ROCK_SALT_SLAB, ModBlocks.ROCK_SALT_WALL,
                ModBlocks.POLISHED_ROCK_SALT, ModBlocks.POLISHED_ROCK_SALT_STAIRS, ModBlocks.POLISHED_ROCK_SALT_SLAB, ModBlocks.POLISHED_ROCK_SALT_WALL,
                ModBlocks.ROCK_SALT_BRICKS, ModBlocks.ROCK_SALT_BRICK_STAIRS, ModBlocks.ROCK_SALT_BRICK_SLAB, ModBlocks.ROCK_SALT_BRICK_WALL
        );

        after(e, Items.DARK_PRISMARINE_SLAB, CreativeModeTabs.BUILDING_BLOCKS,
                ModConstants.NEPHRITE_BLOCKS,
                ModBlocks.NEPHRITE, ModBlocks.NEPHRITE_STAIRS, ModBlocks.NEPHRITE_SLAB, ModBlocks.NEPHRITE_WALL,
                ModBlocks.POLISHED_NEPHRITE, ModBlocks.POLISHED_NEPHRITE_STAIRS, ModBlocks.POLISHED_NEPHRITE_SLAB, ModBlocks.POLISHED_NEPHRITE_WALL,
                ModBlocks.POLISHED_NEPHRITE_BRICKS, ModBlocks.POLISHED_NEPHRITE_BRICK_STAIRS, ModBlocks.POLISHED_NEPHRITE_BRICK_SLAB, ModBlocks.POLISHED_NEPHRITE_BRICK_WALL
        );

        after(e, Items.VINE, CreativeModeTabs.NATURAL_BLOCKS,
                ModConstants.TANGLE_ROOTS,
                ModBlocks.TANGLE_ROOTS, ModBlocks.SPOROPHYTE, ModBlocks.TALL_SPOROPHYTE
        );

        after(e, Items.RED_MUSHROOM, CreativeModeTabs.NATURAL_BLOCKS,
                ModConstants.MUSHROOMS,
                ModBlocks.BUTTON_MUSHROOM, ModBlocks.CRIMINI, ModBlocks.PORTABELLA,
                ModBlocks.CONK_FUNGUS, ModBlocks.INKCAP_MUSHROOM, ModBlocks.WHITE_INKCAP_MUSHROOM,
                ModBlocks.PHOSPHOR_FUNGUS, ModBlocks.MUSHGLOOM, ModBlocks.MILLY_BUBCAP
        );

        after(e, Items.RED_MUSHROOM_BLOCK, CreativeModeTabs.NATURAL_BLOCKS,
                ModConstants.MUSHROOM_BLOCKS,
                ModBlocks.PORTABELLA_BLOCK,
                ModBlocks.CONK_FUNGUS_BLOCK, ModBlocks.INKCAP_MUSHROOM_BLOCK,
                ModBlocks.WHITE_INKCAP_MUSHROOM_BLOCK, ModBlocks.MILLY_BUBCAP_BLOCK
        );

        after(e, Items.GUNPOWDER, CreativeModeTabs.INGREDIENTS,
                ModConstants.SALT_POWDER,
                ModBlocks.ROCK_SALT, ModBlocks.SALT
        );

        after(e, Items.AMETHYST_SHARD, CreativeModeTabs.INGREDIENTS,
                ModConstants.NEPHRITE_CHUNK,
                ModItems.NEPHRITE_CHUNK
        );

        after(e, Items.IRON_NUGGET, CreativeModeTabs.INGREDIENTS,
                ModConstants.COPPER_NUGGET,
                ModItems.COPPER_NUGGET
        );

        after(e, Items.RAW_GOLD, CreativeModeTabs.INGREDIENTS,
                ModConstants.ROUGH_GEMS,
                ModItems.RAW_MAGNETITE, ModItems.ROUGH_CINNABAR, ModItems.ROUGH_LAZURITE, ModItems.ROUGH_EMERALD, ModItems.ROUGH_DIAMOND
        );

        before(e, Items.COAL, CreativeModeTabs.INGREDIENTS,
                ModConstants.ROUGH_NUGGETS,
                ModItems.COAL_LUMP, ModItems.RAW_IRON_NUGGET, ModItems.RAW_COPPER_NUGGET, ModItems.RAW_GOLD_NUGGET, ModItems.RAW_MAGNETITE_NUGGET,
                ModItems.ROUGH_CINNABAR_SHARD, ModItems.ROUGH_LAZURITE_SHARD, ModItems.ROUGH_EMERALD_SHARD, ModItems.ROUGH_DIAMOND_SHARD
        );

        after(e, Items.GOLD_NUGGET, CreativeModeTabs.INGREDIENTS,
                ModConstants.SHARDS,
                ModItems.CINNABAR_SHARD, ModItems.LAPIS_LAZULI_SHARD, ModItems.EMERALD_SHARD, ModItems.DIAMOND_SHARD
        );

        after(e, Items.EMERALD, CreativeModeTabs.INGREDIENTS,
                ModConstants.CINNABAR,
                ModItems.CINNABAR
        );

        after(e, Items.LIGHTNING_ROD, CreativeModeTabs.REDSTONE_BLOCKS,
                ModConstants.NEPHRITE_COMPONENTS,
                ModBlocks.CARVED_NEPHRITE, ModBlocks.NEPHRITE_SIPHON, ModBlocks.NEPHRITE_FOUNTAIN, ModBlocks.NEPHRITE_DIODE
        );

        after(e, Items.TNT, CreativeModeTabs.REDSTONE_BLOCKS,
                ModConstants.CBM,
                ModBlocks.COMPRESSION_BLAST_MINER
        );

        after(e, Items.CAULDRON, CreativeModeTabs.REDSTONE_BLOCKS,
                ModConstants.WOODEN_RAIL,
                ModBlocks.WOODEN_RAIL
        );

        after(e, Items.LADDER, CreativeModeTabs.FUNCTIONAL_BLOCKS,
                ModConstants.LADDER,
                ModBlocks.ROPE_LADDER
        );

        after(e, Items.SPIDER_EYE, CreativeModeTabs.FOOD_AND_DRINKS,
                ModConstants.BELLAS,
                ModItems.BUTTON_MUSHROOM, ModItems.CRIMINI, ModItems.PORTABELLA,
                ModItems.GRILLED_PORTABELLA
        );

        after(e, Items.NETHERITE_HOE, CreativeModeTabs.TOOLS_AND_UTILITIES,
                ModConstants.CHISELS,
                ModItems.FLINT_HAMMER_AND_CHISEL, ModItems.OBSIDIAN_HAMMER_AND_CHISEL
        );

        after(e, Items.MILK_BUCKET, CreativeModeTabs.TOOLS_AND_UTILITIES,
                ModConstants.TEARS,
                ModItems.SALT_BUCKET, ModItems.PORTAL_FLUID_BUCKET, ModItems.PORTAL_FLUID_BOTTLE
        );

        after(e, Items.RECOVERY_COMPASS, CreativeModeTabs.TOOLS_AND_UTILITIES,
                ModConstants.COMPASS,
                ModItems.MAGNETIC_COMPASS, ModItems.DEPTH_GAUGE
        );

        before(e, Items.COMPASS, CreativeModeTabs.TOOLS_AND_UTILITIES,
                ModConstants.MAGNET,
                ModItems.ITEM_MAGNET, ModItems.HANDHELD_COMPACTOR, ModItems.ECHO_FORK
        );

        before(e, Items.ELYTRA, CreativeModeTabs.TOOLS_AND_UTILITIES,
                ModConstants.PARACHUTE,
                ModItems.PARACHUTE
        );

        before(e, Items.RAIL, CreativeModeTabs.TOOLS_AND_UTILITIES,
                ModConstants.WOODEN_RAIL,
                ModBlocks.WOODEN_RAIL
        );

        after(e, Items.FIRE_CHARGE, CreativeModeTabs.TOOLS_AND_UTILITIES,
                ModConstants.MINEOMITE,
                ModItems.MINEOMITE, ModItems.COMPRESSION_BLAST_MINER
        );

        after(e, Items.BRUSH, CreativeModeTabs.TOOLS_AND_UTILITIES,
                ModConstants.DUST_BUN,
                ModItems.DUST_BUN, ModItems.GLOWSTICK
        );

        after(e, Items.END_ROD, CreativeModeTabs.FUNCTIONAL_BLOCKS,
                ModConstants.GLOWSTICKS,
                ModItems.GLOWSTICK, ModItems.WHITE_GLOWSTICK, ModItems.GRAY_GLOWSTICK, ModItems.LIGHT_GRAY_GLOWSTICK,
                ModItems.RED_GLOWSTICK, ModItems.ORANGE_GLOWSTICK, ModItems.YELLOW_GLOWSTICK, ModItems.LIME_GLOWSTICK,
                ModItems.GREEN_GLOWSTICK, ModItems.CYAN_GLOWSTICK, ModItems.LIGHT_BLUE_GLOWSTICK, ModItems.BLUE_GLOWSTICK,
                ModItems.PURPLE_GLOWSTICK, ModItems.MAGENTA_GLOWSTICK, ModItems.PINK_GLOWSTICK, ModItems.BROWN_GLOWSTICK,
                ModItems.BLACK_GLOWSTICK
        );
    }

    private static void after(RegHelper.ItemToTabEvent event, TagKey<Item> target,
                              ResourceKey<CreativeModeTab> tab, String key, Supplier<?>... items) {
        after(event, i -> i.is(target), tab, key, items);
    }

    private static void after(RegHelper.ItemToTabEvent event, Item target,
                              ResourceKey<CreativeModeTab> tab, String key, Supplier<?>... items) {
        after(event, i -> i.is(target), tab, key, items);
    }

    private static void after(RegHelper.ItemToTabEvent event, Predicate<ItemStack> targetPred,
                              ResourceKey<CreativeModeTab> tab, String key, Supplier<?>... items) {
        //if (CommonConfigs.isEnabled(key)) {
        ItemLike[] entries = Arrays.stream(items).map((s -> (ItemLike) (s.get()))).toArray(ItemLike[]::new);
        if(CommonConfigs.CREATIVE_TAB.get()){
            tab = MOD_TAB.getHolder().unwrapKey().get();
        }
        event.addAfter(tab, targetPred, entries);
        //}
    }

    private static void before(RegHelper.ItemToTabEvent event, Item target,
                               ResourceKey<CreativeModeTab> tab, String key, Supplier<?>... items) {
        before(event, i -> i.is(target), tab, key, items);
    }

    private static void before(RegHelper.ItemToTabEvent event, Predicate<ItemStack> targetPred,
                               ResourceKey<CreativeModeTab> tab, String key, Supplier<?>... items) {
        //if (CommonConfigs.isEnabled(key)) {
        ItemLike[] entries = Arrays.stream(items).map(s -> (ItemLike) s.get()).toArray(ItemLike[]::new);
        if(CommonConfigs.CREATIVE_TAB.get()){
            tab = MOD_TAB.getHolder().unwrapKey().get();
        }
        event.addBefore(tab, targetPred, entries);
        //}
    }

    private static void add(RegHelper.ItemToTabEvent event,
                            ResourceKey<CreativeModeTab> tab, String key, Supplier<?>... items) {
        //if (CommonConfigs.isEnabled(key)) {
        ItemLike[] entries = Arrays.stream(items).map((s -> (ItemLike) (s.get()))).toArray(ItemLike[]::new);
        if(CommonConfigs.CREATIVE_TAB.get()){
            tab = MOD_TAB.getHolder().unwrapKey().get();
        }
        event.add(tab, entries);
        //}
    }

    private static void afterML(RegHelper.ItemToTabEvent event, Item target,
                                ResourceKey<CreativeModeTab> tab, String key, String modLoaded,
                                Supplier<?>... items) {
        if (PlatHelper.isModLoaded(modLoaded)) {
            after(event, target, tab, key, items);
        }
    }

    private static void afterTL(RegHelper.ItemToTabEvent event, Item target,
                                ResourceKey<CreativeModeTab> tab, String key,
                                List<String> tags,
                                Supplier<?>... items) {
        if (isTagOn(tags.toArray(String[]::new))) {
            after(event, target, tab, key, items);
        }
    }

    private static void beforeML(RegHelper.ItemToTabEvent event, Item target,
                                 ResourceKey<CreativeModeTab> tab,
                                 String key, String modLoaded,
                                 Supplier<?>... items) {
        if (PlatHelper.isModLoaded(modLoaded)) {
            before(event, target, tab, key, items);
        }
    }

    private static void beforeTL(RegHelper.ItemToTabEvent event, Item target,
                                 ResourceKey<CreativeModeTab> tab, String key,
                                 List<String> tags,
                                 Supplier<?>... items) {
        if (isTagOn(tags.toArray(String[]::new))) {
            after(event, target, tab, key, items);
        }
    }

    private static boolean isTagOn(String... tags) {
        for (var t : tags)
            if (BuiltInRegistries.ITEM.getTag(TagKey.create(Registries.ITEM, new ResourceLocation(t))).isPresent()) {
                return true;
            }
        return false;
    }

}