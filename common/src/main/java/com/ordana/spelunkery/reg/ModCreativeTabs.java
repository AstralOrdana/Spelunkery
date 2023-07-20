package com.ordana.spelunkery.reg;

import com.ordana.spelunkery.Spelunkery;
import net.mehvahdjukaar.moonlight.api.misc.RegSupplier;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.*;

public class ModCreativeTabs {

    private static final Set<Item> HIDDEN_ITEMS = new HashSet<>();
    private static final List<ItemStack> NON_HIDDEN_ITEMS = new ArrayList<>();

    public static final RegSupplier<CreativeModeTab> MOD_TAB =
            RegHelper.registerCreativeModeTab(Spelunkery.res("spelunkery"),
                    (c) -> c.title(Component.translatable("itemGroup.spelunkery"))
                            .icon(() -> ModItems.ROCK_SALT.get().getDefaultInstance()));

    public static void init() {
        RegHelper.addItemsToTabsRegistration(ModCreativeTabs::registerItemsToTabs);
    }

    private static boolean isRunningSetup = false;

    public static void setup() {
        isRunningSetup = true;
        List<Item> all = new ArrayList<>(BuiltInRegistries.ITEM.entrySet().stream().filter(e -> e.getKey().location().getNamespace()
                .equals(Spelunkery.MOD_ID)).map(Map.Entry::getValue).toList());
        Map<ResourceKey<CreativeModeTab>,List<ItemStack>> map = new HashMap<>();
        CreativeModeTabs.tabs().forEach(t->map.putIfAbsent(BuiltInRegistries.CREATIVE_MODE_TAB.getResourceKey(t).get(), new ArrayList<>()));
        var dummy = new RegHelper.ItemToTabEvent((creativeModeTab, itemStackPredicate, reverse, itemStacks) -> {
            var l = map.computeIfAbsent(creativeModeTab,t->new ArrayList<>());
            if (reverse) {
                var v = new ArrayList<>(itemStacks);
                Collections.reverse(v);
                l.addAll(v);
            } else l.addAll(itemStacks);
        });
        registerItemsToTabs(dummy);
        for(var e : map.values()){
            NON_HIDDEN_ITEMS.addAll(e);
        }

        for (var v : NON_HIDDEN_ITEMS) {
            all.remove(v.getItem());
        }
        HIDDEN_ITEMS.addAll(all);
        isRunningSetup = false;
    }

    public static boolean isHidden(Item item) {
        return HIDDEN_ITEMS.contains(item);
    }

    public static void registerItemsToTabs(RegHelper.ItemToTabEvent e) {
        if (MOD_TAB != null && !isRunningSetup) {
            e.add(MOD_TAB.getHolder().unwrapKey().get(), NON_HIDDEN_ITEMS.toArray(ItemStack[]::new));
        }
    }
}
