package com.ordana.underground_overhaul.fabric;

import com.ordana.underground_overhaul.events.ModEvents;
import com.ordana.underground_overhaul.events.ModLootInjects;
import net.fabricmc.api.ModInitializer;
import com.ordana.underground_overhaul.UndergroundOverhaul;
import com.ordana.underground_overhaul.UndergroundOverhaulClient;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.mehvahdjukaar.moonlight.api.platform.PlatformHelper;
import net.mehvahdjukaar.moonlight.fabric.FabricSetupCallbacks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

public class UndergroundOverhaulFabric implements ModInitializer {

    public static MinecraftServer currentServer;

    @Override
    public void onInitialize() {

        ServerLifecycleEvents.SERVER_STARTING.register(s -> currentServer = s);

        UndergroundOverhaul.commonInit();

        UseBlockCallback.EVENT.register(UndergroundOverhaulFabric::onRightClickBlock);
        LootTableEvents.MODIFY.register((m, t, r, b, s) -> ModLootInjects.onLootInject(t, r, b::withPool));

        if(PlatformHelper.getEnv().isClient()){
            FabricSetupCallbacks.CLIENT_SETUP.add(UndergroundOverhaulClientFabric::initClient);
        }
    }

    public static InteractionResult onRightClickBlock(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        return ModEvents.onBlockCLicked(player.getItemInHand(hand), player, level, hand, hitResult);
    }

}
