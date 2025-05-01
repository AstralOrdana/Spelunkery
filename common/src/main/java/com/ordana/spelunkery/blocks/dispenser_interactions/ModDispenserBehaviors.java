package com.ordana.spelunkery.blocks.dispenser_interactions;

import com.ordana.spelunkery.reg.ModItems;
import com.ordana.spelunkery.reg.ModTags;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.mehvahdjukaar.moonlight.api.util.DispenserHelper;
import net.minecraft.core.Registry;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.DispenserBlock;

public class ModDispenserBehaviors {

    public static void registerBehaviors() {
        DispenserBlock.registerBehavior(ModItems.COMPRESSION_BLAST_MINER.get(), new CBMBehavior());
        DispenserBlock.registerBehavior(ModItems.EGGPLANT.get(), new EggplantBehavior());
        DispenserBlock.registerBehavior(ModItems.MINEOMITE.get(), new MineOMiteBehavior());
        Registry.ITEM.getTagOrEmpty(ModTags.GRINDSTONE_REPAIR_ITEM).iterator().forEachRemaining(h ->
                DispenserBlock.registerBehavior(h.value(), new GrindstoneRepairBehavior(h.value()))
        );
        Registry.ITEM.getTagOrEmpty(ModTags.DIAMOND_GRINDABLE).iterator().forEachRemaining(h ->
                DispenserBlock.registerBehavior(h.value(), new GrindstoneBehavior(h.value()))
        );
        Registry.ITEM.getTagOrEmpty(ModTags.GRINDABLE).iterator().forEachRemaining(h ->
                DispenserBlock.registerBehavior(h.value(), new GrindstoneBehavior(h.value()))
        );
        Registry.ITEM.getTagOrEmpty(ModTags.GLOWSTICKS).iterator().forEachRemaining(h ->
                DispenserBlock.registerBehavior(h.value(), new GlowstickBehavior(h.value()))
        );
        Registry.ITEM.getTagOrEmpty(ModTags.PEBBLES).iterator().forEachRemaining(h ->
                DispenserBlock.registerBehavior(h.value(), new PebbleBehavior(h.value()))
        );
    }
}