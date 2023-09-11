package com.ordana.spelunkery.blocks.entity;

import com.ordana.spelunkery.blocks.WoodenSluiceBlock;
import com.ordana.spelunkery.reg.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.BooleanSupplier;

public class WoodenSluiceBlockEntity extends RandomizableContainerBlockEntity {
    private NonNullList<ItemStack> items;
    private final ContainerOpenersCounter openersCounter;
    private int cooldownTime;
    private long tickedGameTime;

    public WoodenSluiceBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModEntities.WOODEN_SLUICE.get(), blockPos, blockState);
        this.items = NonNullList.withSize(9, ItemStack.EMPTY);
        this.openersCounter = new ContainerOpenersCounter() {
            protected void onOpen(Level level, BlockPos pos, BlockState state) {
                //WoodenSluiceBlockEntity.this.playSound(state, SoundEvents.BARREL_OPEN);
                //WoodenSluiceBlockEntity.this.updateBlockState(state, true);
            }

            protected void onClose(Level level, BlockPos pos, BlockState state) {
                //WoodenSluiceBlockEntity.this.playSound(state, SoundEvents.BARREL_CLOSE);
                //WoodenSluiceBlockEntity.this.updateBlockState(state, false);
            }

            protected void openerCountChanged(Level level, BlockPos pos, BlockState state, int count, int openCount) {
            }

            protected boolean isOwnContainer(Player player) {
                if (player.containerMenu instanceof ChestMenu) {
                    Container container = ((ChestMenu)player.containerMenu).getContainer();
                    return container == WoodenSluiceBlockEntity.this;
                } else {
                    return false;
                }
            }
        };
    }


    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (!this.trySaveLootTable(tag)) {
            ContainerHelper.saveAllItems(tag, this.items);
        }

    }

    public void load(CompoundTag tag) {
        super.load(tag);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(tag)) {
            ContainerHelper.loadAllItems(tag, this.items);
        }

    }

    public int getContainerSize() {
        return 9;
    }

    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    protected void setItems(NonNullList<ItemStack> itemStacks) {
        this.items = itemStacks;
    }

    private void setCooldown(int cooldownTime) {
        this.cooldownTime = cooldownTime;
    }

    private boolean isOnCooldown() {
        return this.cooldownTime > 0;
    }

    private boolean isOnCustomCooldown() {
        return this.cooldownTime > 8;
    }


    protected Component getDefaultName() {
        return Component.translatable("container.spelunkery.sluice");
    }

    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return ChestMenu.oneRow(containerId, inventory);
    }

    public void startOpen(Player player) {
        if (!this.remove && !player.isSpectator()) {
            this.openersCounter.incrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
        }

    }

    public void stopOpen(Player player) {
        if (!this.remove && !player.isSpectator()) {
            this.openersCounter.decrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
        }

    }

    public void recheckOpen() {
        if (!this.remove) {
            this.openersCounter.recheckOpeners(this.getLevel(), this.getBlockPos(), this.getBlockState());
        }

    }

    public static void pushItemsTick(Level level, BlockPos pos, BlockState state, WoodenSluiceBlockEntity blockEntity) {
        --blockEntity.cooldownTime;
        blockEntity.tickedGameTime = level.getGameTime();
        if (!blockEntity.isOnCooldown()) {
            blockEntity.setCooldown(0);
            tryFilterItems(level, pos, state, blockEntity, ()
                    -> createFilteredItems(pos, level, blockEntity));
        }

    }

    private static boolean tryFilterItems(Level level, BlockPos pos, BlockState state, WoodenSluiceBlockEntity blockEntity, BooleanSupplier validator) {
        if (!level.isClientSide && !blockEntity.isOnCooldown()) {
            int flow = WoodenSluiceBlock.getFlow(level, state, pos);
            boolean bl = false;
            int delay = 20;
            for (int i = 0; i < flow; ++i) {

                blockEntity.setCooldown(level.random.nextInt(blockEntity.cooldownTime + delay));
                if (!blockEntity.inventoryFull()) {
                    bl |= validator.getAsBoolean();
                } else bl = false;
            }
            setChanged(level, pos, state);
            return bl;

        }
        return false;
    }

    private boolean inventoryFull() {
        Iterator var1 = this.items.iterator();

        ItemStack itemStack;
        do {
            if (!var1.hasNext()) {
                return true;
            }

            itemStack = (ItemStack)var1.next();
        } while(!itemStack.isEmpty() && itemStack.getCount() == itemStack.getMaxStackSize());

        return false;
    }

    public static List getItemsAtAndAbove(BlockPos pos, Level level) {
        var aABB = new AABB(pos.above());
        return new ArrayList<>(level.getEntitiesOfClass(ItemEntity.class, aABB,
                EntitySelector.ENTITY_STILL_ALIVE));
    }

    public static boolean createFilteredItems(BlockPos pos, Level level, WoodenSluiceBlockEntity hopper) {
        {
            Iterator var3 = getItemsAtAndAbove(pos, level).iterator();

            ItemEntity itemEntity;
            do {
                if (!var3.hasNext()) {
                    return false;
                }

                itemEntity = (ItemEntity)var3.next();
            } while(!addItem(hopper, itemEntity));

            return true;
        }
    }

    public static boolean addItem(Container container, ItemEntity item) {
        boolean bl = false;
        ItemStack itemStack = item.getItem().copy();
        if (itemStack.isEmpty()) {
            bl = true;
            item.discard();
        } else {
            item.setItem(itemStack);
        }

        return bl;
    }




    /*
    void updateBlockState(BlockState state, boolean open) {
        this.level.setBlock(this.getBlockPos(), (BlockState)state.setValue(BarrelBlock.OPEN, open), 3);
    }


    void playSound(BlockState state, SoundEvent sound) {
        Vec3i vec3i = ((Direction)state.getValue(BarrelBlock.FACING)).getNormal();
        double d = (double)this.worldPosition.getX() + 0.5D + (double)vec3i.getX() / 2.0D;
        double e = (double)this.worldPosition.getY() + 0.5D + (double)vec3i.getY() / 2.0D;
        double f = (double)this.worldPosition.getZ() + 0.5D + (double)vec3i.getZ() / 2.0D;
        this.level.playSound((Player)null, d, e, f, sound, SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
    }
     */
}