package com.ordana.spelunkery.fabric;

import com.ordana.spelunkery.Spelunkery;
import com.ordana.spelunkery.SpelunkeryClient;
import com.ordana.spelunkery.events.ModEvents;
import com.ordana.spelunkery.reg.ModSetup;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.fabric.MLFabricSetupCallbacks;
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
        Spelunkery.commonInit();

        ServerLifecycleEvents.SERVER_STARTING.register(s -> currentServer = s);
        MLFabricSetupCallbacks.COMMON_SETUP.add(SpelunkeryFabric::onSetup);


        UseBlockCallback.EVENT.register(SpelunkeryFabric::onRightClickBlock);

        if(PlatHelper.getPhysicalSide().isClient()) {
            //MLFabricSetupCallbacks.CLIENT_SETUP.add(SpelunkeryClient::init);
            ClientEventsFabric.init();
            SpelunkeryClient.init();
        }

    }

    public static void onSetup(){
        ModSetup.setup();
    }

    public static InteractionResult onRightClickBlock(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        return ModEvents.onBlockCLicked(player.getItemInHand(hand), player, level, hand, hitResult);
    }

}
