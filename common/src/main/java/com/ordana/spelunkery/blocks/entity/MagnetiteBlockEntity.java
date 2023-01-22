package com.ordana.spelunkery.blocks.entity;

import com.ordana.spelunkery.configs.CommonConfigs;
import com.ordana.spelunkery.events.ModEvents;
import com.ordana.spelunkery.items.MagneticCompassItem;
import com.ordana.spelunkery.reg.ModEntities;
import com.ordana.spelunkery.reg.ModGameEvents;
import com.ordana.spelunkery.reg.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.PositionSource;

public class MagnetiteBlockEntity extends BlockEntity implements GameEventListener {

    protected final PositionSource listenerSource;

    public MagnetiteBlockEntity(BlockPos pos, BlockState state) {
        super(ModEntities.MAGNETITE.get(), pos, state);
        this.listenerSource = new BlockPositionSource(this.worldPosition);
    }

    @Override
    public PositionSource getListenerSource() {
        return listenerSource;
    }

    @Override
    public int getListenerRadius() {
        return 128;
    }

    @Override
    public boolean handleGameEvent(ServerLevel level, GameEvent.Message eventMessage) {
        if (ModGameEvents.COMPASS_PING_EVENT.get() == eventMessage.gameEvent()) {
            Entity entity = eventMessage.context().sourceEntity();

                if (entity instanceof Player player) {
                    var inventory = player.getInventory();

                    for (int i = 0; i < inventory.getContainerSize(); i++) {
                        ItemStack compass = inventory.getItem(i);
                        if (compass.is(ModItems.MAGNETIC_COMPASS.get())) {
                            MagneticCompassItem.addMagnetiteTags(level.dimension(), getBlockPos(), compass.getOrCreateTag());
                            level.playSound(null, entity.blockPosition(), SoundEvents.LODESTONE_COMPASS_LOCK, SoundSource.BLOCKS, 0.7f, 1.0f);
                        }
                    }
                }
        }
        return false;
    }
}
