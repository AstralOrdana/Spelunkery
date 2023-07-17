package com.ordana.spelunkery.fabric;

import com.ordana.spelunkery.Spelunkery;
import com.ordana.spelunkery.events.ModEvents;
import com.ordana.spelunkery.events.ModLootInjects;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.mehvahdjukaar.moonlight.api.platform.PlatformHelper;
import net.mehvahdjukaar.moonlight.fabric.FabricSetupCallbacks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

public class SpelunkeryFabric implements ModInitializer {

    public static MinecraftServer currentServer;

    @Override
    public void onInitialize() {

        ServerLifecycleEvents.SERVER_STARTING.register(s -> currentServer = s);

        Spelunkery.commonInit();

        FabricLoader.getInstance().getModContainer(Spelunkery.MOD_ID).ifPresent(modContainer -> {
            ResourceManagerHelper.registerBuiltinResourcePack(Spelunkery.res("better_vanilla_gems"), modContainer, "Better Vanilla Gems", ResourcePackActivationType.DEFAULT_ENABLED);
            ResourceManagerHelper.registerBuiltinResourcePack(Spelunkery.res("unlit_redstone_ores"), modContainer, "Unlit Redstone Ores", ResourcePackActivationType.NORMAL);
            ResourceManagerHelper.registerBuiltinResourcePack(Spelunkery.res("emissive_ores"), modContainer, "Emissive Ores", ResourcePackActivationType.NORMAL);
            ResourceManagerHelper.registerBuiltinResourcePack(Spelunkery.res("emissive_better_vanilla_ores"), modContainer, "Emissive Better Vanilla Ores", ResourcePackActivationType.NORMAL);
        });

        UseBlockCallback.EVENT.register(SpelunkeryFabric::onRightClickBlock);
        LootTableEvents.MODIFY.register((m, t, r, b, s) -> ModLootInjects.onLootInject(t, r, b::withPool));

        if(PlatformHelper.getEnv().isClient()){
            FabricSetupCallbacks.CLIENT_SETUP.add(SpelunkeryFabricClient::initClient);
        }
    }

    public static InteractionResult onRightClickBlock(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        return ModEvents.onBlockCLicked(player.getItemInHand(hand), player, level, hand, hitResult);
    }

}
