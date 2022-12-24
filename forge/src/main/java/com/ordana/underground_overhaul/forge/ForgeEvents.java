package com.ordana.underground_overhaul.forge;

import com.ordana.underground_overhaul.events.ModLootInjects;
import net.minecraft.world.InteractionResult;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = UndergroundOverhaulForge.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEvents {
    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        var ret = com.ordana.underground_overhaul.events.ModEvents.onBlockCLicked(event.getItemStack(),
                event.getEntity(), event.getLevel(), event.getHand(), event.getHitVec());
        if (ret != InteractionResult.PASS) {
            event.setCanceled(true);
            event.setCancellationResult(ret);
        }
    }

    @SubscribeEvent
    public static void onAddLootTables(LootTableLoadEvent event) {
        ModLootInjects.onLootInject(event.getLootTableManager(), event.getName(), (b) -> event.getTable().addPool(b.build()));
    }
}
