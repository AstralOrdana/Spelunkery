package com.ordana.spelunkery.neoforge;

import com.ordana.spelunkery.entities.EggPlither;
import com.ordana.spelunkery.reg.ModEntityTypes;
import net.minecraft.world.InteractionResult;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = SpelunkeryNeoForge.MOD_ID)
public class NeoForgeEvents {
    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        var ret = com.ordana.spelunkery.events.ModEvents.onBlockCLicked(event.getItemStack(),
                event.getEntity(), event.getLevel(), event.getHand(), event.getHitVec());
        if (ret != InteractionResult.PASS) {
            event.setCanceled(true);
            event.setCancellationResult(ret);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void registerClientFluid(EntityAttributeCreationEvent event) {
        event.put(ModEntityTypes.EGG_PLITHER.get(), EggPlither.createAttributes().build());
    }

}
