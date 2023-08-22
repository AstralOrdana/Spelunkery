package com.ordana.spelunkery;

import com.ordana.spelunkery.configs.ClientConfigs;
import com.ordana.spelunkery.configs.CommonConfigs;
import com.ordana.spelunkery.events.NetworkHandler;
import com.ordana.spelunkery.loot_modifiers.ModLootInjects;
import com.ordana.spelunkery.loot_modifiers.ModLootOverrides;
import com.ordana.spelunkery.reg.*;
import net.mehvahdjukaar.moonlight.api.events.IDropItemOnDeathEvent;
import net.mehvahdjukaar.moonlight.api.events.MoonlightEventsHelper;
import net.mehvahdjukaar.moonlight.api.platform.ClientHelper;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Spelunkery {

    public static final String MOD_ID = "spelunkery";
    public static final Logger LOGGER = LogManager.getLogger();
    private static boolean initiated = false;

    public static ResourceLocation res(String name) {
        return new ResourceLocation(MOD_ID, name);
    }

    public static void commonInit() {

        if (initiated) {
            return;
        }
        initiated = true;

        NetworkHandler.registerMessages();
        CommonConfigs.init();

        if(PlatHelper.getPhysicalSide().isClient()){
            ClientConfigs.init();

            ClientHelper.registerOptionalTexturePack(Spelunkery.res("better_vanilla_gems"), Component.literal("Better Vanilla Gems"), true);
            ClientHelper.registerOptionalTexturePack(Spelunkery.res("unlit_redstone_ores"), Component.literal("Unlit Redstone Ores"), true);
            ClientHelper.registerOptionalTexturePack(Spelunkery.res("emissive_ores"), Component.literal("Emissive Ores"), false);
            ClientHelper.registerOptionalTexturePack(Spelunkery.res("emissive_better_vanilla_ores"), Component.literal("Emissive Better Vanilla Ores"), false);

        }


        ModGameEvents.init();
        ModLootOverrides.INSTANCE.register();
        ModFeatures.init();
        ModBlocks.init();
        ModFluids.init();
        ModItems.init();
        ModEntities.init();
        ModParticles.init();
        ModRecipes.init();
        ModCreativeTabs.init();

        MoonlightEventsHelper.addListener(Spelunkery::compassLogic, IDropItemOnDeathEvent.class);

        RegHelper.addLootTableInjects(ModLootInjects::onLootInject);
    }

    private static void compassLogic(IDropItemOnDeathEvent event) {
        if (event.getItemStack().is(Items.DRAGON_EGG)) {
            if (event.getPlayer() instanceof ServerPlayer serverPlayer && serverPlayer.getBlockY() < -64) {
                ItemStack itemStack = event.getItemStack();
                itemStack.shrink(1);
                event.setReturnItemStack(new ItemStack(ModItems.EGGPLANT.get()));
                event.setCanceled(true);
            }
        }
        if (event.getItemStack().is(ModTags.KEEP_ON_DEATH)) {
            if (event.getPlayer() instanceof ServerPlayer serverPlayer) CriteriaTriggers.USING_ITEM.trigger(serverPlayer, event.getItemStack());
            event.setCanceled(true);
        }
    }

}