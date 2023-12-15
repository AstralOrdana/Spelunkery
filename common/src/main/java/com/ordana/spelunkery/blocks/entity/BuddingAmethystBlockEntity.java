package com.ordana.spelunkery.blocks.entity;

import com.ordana.spelunkery.configs.CommonConfigs;
import com.ordana.spelunkery.items.AmethystTuningForkItem;
import com.ordana.spelunkery.reg.ModEntities;
import com.ordana.spelunkery.reg.ModGameEvents;
import com.ordana.spelunkery.reg.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.phys.Vec3;

public class BuddingAmethystBlockEntity extends BlockEntity implements GameEventListener {

    protected final PositionSource listenerSource;

    public BuddingAmethystBlockEntity(BlockPos pos, BlockState state) {
        super(ModEntities.BUDDING_AMETHYST.get(), pos, state);
        this.listenerSource = new BlockPositionSource(this.worldPosition);
    }

    @Override
    public PositionSource getListenerSource() {
        return listenerSource;
    }

    @Override
    public int getListenerRadius() {
        return CommonConfigs.TUNING_FORK_RANGE.get();
    }

    @Override
    public boolean handleGameEvent(ServerLevel level, GameEvent gameEvent, GameEvent.Context context, Vec3 pos) {
        if (ModGameEvents.FORK_TONE_EVENT.get() == gameEvent) {
            Entity entity = context.sourceEntity();

            if (entity instanceof Player player) {
                var inventory = player.getInventory();

                for (int i = 0; i < inventory.getContainerSize(); i++) {
                    ItemStack compass = inventory.getItem(i);
                    if (compass.is(ModItems.TUNING_FORK.get())) {
                        AmethystTuningForkItem.addAmethystTags(level.dimension(), getBlockPos(), compass.getOrCreateTag());

                        level.playSound(null, entity.blockPosition(), SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.BLOCKS, 0.4f, 1.0f);
                    }
                }
            }
        }
        return false;
    }
}