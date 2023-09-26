package com.ordana.spelunkery.fabric;

import com.ordana.spelunkery.Spelunkery;
import com.ordana.spelunkery.events.ModEvents;
import com.ordana.spelunkery.loot_modifiers.ModLootInjects;
import com.ordana.spelunkery.reg.ModSetup;
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
    /* FIXME
        java.lang.AbstractMethodError: Receiver class net.mehvahdjukaar.moonlight.api.platform.fabric.ClientPlatformHelperImpl$3 does not define or inherit an implementation of the resolved method 'abstract java.util.concurrent.CompletableFuture reload(net.minecraft.server.packs.resources.PreparableReloadListener$PreparationBarrier, net.minecraft.server.packs.resources.ResourceManager, net.minecraft.util.profiling.ProfilerFiller, net.minecraft.util.profiling.ProfilerFiller, java.util.concurrent.Executor, java.util.concurrent.Executor)' of interface net.minecraft.server.packs.resources.PreparableReloadListener.
        at net.minecraft.server.packs.resources.SimpleReloadInstance.method_18368(SimpleReloadInstance.java:32)
        at net.minecraft.server.packs.resources.SimpleReloadInstance.<init>(SimpleReloadInstance.java:44)
        at net.minecraft.server.packs.resources.SimpleReloadInstance.of(SimpleReloadInstance.java:32)
        at net.minecraft.server.packs.resources.SimpleReloadInstance.create(SimpleReloadInstance.java:101)
        at net.minecraft.server.packs.resources.ReloadableResourceManager.createReload(ReloadableResourceManager.java:47)
        at net.minecraft.client.Minecraft.<init>(Minecraft.java:648)
        at net.minecraft.client.main.Main.run(Main.java:205)
        at net.minecraft.client.main.Main.main(Main.java:51)
        at net.fabricmc.loader.impl.game.minecraft.MinecraftGameProvider.launch(MinecraftGameProvider.java:468)
        at net.fabricmc.loader.impl.launch.knot.Knot.launch(Knot.java:74)
        at net.fabricmc.loader.impl.launch.knot.KnotClient.main(KnotClient.java:23)
        at net.fabricmc.devlaunchinjector.Main.main(Main.java:86)
        at dev.architectury.transformer.TransformerRuntime.main(TransformerRuntime.java:219)
    */


    public static MinecraftServer currentServer;

    @Override
    public void onInitialize() {
        Spelunkery.commonInit();

        ServerLifecycleEvents.SERVER_STARTING.register(s -> currentServer = s);
        FabricSetupCallbacks.COMMON_SETUP.add(SpelunkeryFabric::onSetup);

        UseBlockCallback.EVENT.register(SpelunkeryFabric::onRightClickBlock);
        LootTableEvents.MODIFY.register((m, t, r, b, s) -> ModLootInjects.onLootInject(t, r, b::withPool));

        if(PlatformHelper.getEnv().isClient()) {
            FabricSetupCallbacks.CLIENT_SETUP.add(SpelunkeryFabricClient::initClient);
            ClientEventsFabric.init();
        }
    }

    public static void onSetup(){
        ModSetup.setup();
    }

    public static InteractionResult onRightClickBlock(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        return ModEvents.onBlockCLicked(player.getItemInHand(hand), player, level, hand, hitResult);
    }

}
