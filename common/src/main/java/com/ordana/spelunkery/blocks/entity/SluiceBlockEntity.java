package com.ordana.spelunkery.blocks.entity;

import com.ordana.spelunkery.Spelunkery;
import com.ordana.spelunkery.blocks.ChannelSluiceBlock;
import com.ordana.spelunkery.reg.ModBlocks;
import com.ordana.spelunkery.reg.ModEntities;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.valueproviders.BiasedToBottomInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.BooleanSupplier;

public class SluiceBlockEntity extends RandomizableContainerBlockEntity {
    private NonNullList<ItemStack> items;
    private final ContainerOpenersCounter openersCounter;
    private int cooldownTime;
    private long tickedGameTime;

    public SluiceBlockEntity(BlockPos blockPos, BlockState blockState) {
        super((blockState.is(ModBlocks.WOODEN_SLUICE.get()) ? ModEntities.WOODEN_SLUICE.get() : ModEntities.STONE_SLUICE.get()), blockPos, blockState);
        this.items = NonNullList.withSize(9, ItemStack.EMPTY);
        this.openersCounter = new ContainerOpenersCounter() {
            protected void onOpen(Level level, BlockPos pos, BlockState state) {
                SluiceBlockEntity.this.playSound(state, SoundEvents.BARREL_OPEN);
            }

            protected void onClose(Level level, BlockPos pos, BlockState state) {
                SluiceBlockEntity.this.playSound(state, SoundEvents.BARREL_CLOSE);
            }

            protected void openerCountChanged(Level level, BlockPos pos, BlockState state, int count, int openCount) {
            }

            protected boolean isOwnContainer(Player player) {
                if (player.containerMenu instanceof ChestMenu) {
                    Container container = ((ChestMenu)player.containerMenu).getContainer();
                    return container == SluiceBlockEntity.this;
                } else {
                    return false;
                }
            }
        };
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider lookup) {
        super.saveAdditional(tag, lookup);
        if (!this.trySaveLootTable(tag)) {
            ContainerHelper.saveAllItems(tag, this.items, lookup);
        }

    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider lookup) {
        super.loadAdditional(tag, lookup);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(tag)) {
            ContainerHelper.loadAllItems(tag, this.items, lookup);
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

    protected Component getDefaultName() {
        return Component.translatable("container.spelunkery.sluice");
    }

    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new ChestMenu(MenuType.GENERIC_9x1, containerId, inventory, this, 1);
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

    void playSound(BlockState state, SoundEvent sound) {
        double d = (double)this.worldPosition.getX() + 0.5D;
        double e = (double)this.worldPosition.getY() + 0.5D;
        double f = (double)this.worldPosition.getZ() + 0.5D;
        this.level.playSound((Player)null, d, e, f, sound, SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
    }



    private void setCooldown(int cooldownTime) {
        this.cooldownTime = cooldownTime;
    }

    private boolean isOnCooldown() {
        return this.cooldownTime > 0;
    }

    public static void pushItemsTick(Level level, BlockPos pos, BlockState state, SluiceBlockEntity blockEntity) {
        --blockEntity.cooldownTime;
        blockEntity.tickedGameTime = level.getGameTime();
        if (!blockEntity.isOnCooldown()) {
            blockEntity.setCooldown(0);
            int flow = ChannelSluiceBlock.getFlow(level, state, pos);
            tryFilterItems(level, pos, state, blockEntity, flow, ()
                    -> createFilteredItems(pos, level, blockEntity));
        }
    }

    public static boolean tryFilterItems(Level level, BlockPos pos, BlockState state, SluiceBlockEntity blockEntity, int flow, BooleanSupplier validator) {
        if (!level.isClientSide && !blockEntity.isOnCooldown()) {
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

    public static List<Entity> getItemsAtAndAbove(BlockPos pos, Level level) {
        var aABB = new AABB(pos.above());
        return new ArrayList<>(level.getEntitiesOfClass(ItemEntity.class, aABB,
                EntitySelector.ENTITY_STILL_ALIVE));
    }

    public static boolean createFilteredItems(BlockPos pos, Level level, SluiceBlockEntity entity) {

        Iterator<Entity> itemList = getItemsAtAndAbove(pos, level).iterator();

        if (!itemList.hasNext()) {
            return false;
        }

        ItemEntity itemEntity = (ItemEntity)itemList.next();
        var itemName = Utils.getID(itemEntity.getItem().getItem()).getPath();
        var fluidName = Utils.getID(level.getFluidState(pos.above()).getType()).getPath();

        if (fluidName.contains("flowing_")) fluidName = fluidName.replace("flowing_", "");

        if (!Objects.equals(fluidName, "empty")) {

            var tablePath = Spelunkery.res("gameplay/sluice/" + fluidName + "/" + itemName);
            var lootTable = Objects.requireNonNull(level.getServer()).reloadableRegistries().getLootTable(ResourceKey.create(Registries.LOOT_TABLE, tablePath));

            LootParams.Builder builder = (new LootParams.Builder((ServerLevel) level))
                    .withParameter(LootContextParams.BLOCK_STATE, level.getBlockState(pos))
                    .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
                    .withParameter(LootContextParams.TOOL, ItemStack.EMPTY)
                    .withOptionalParameter(LootContextParams.BLOCK_ENTITY, entity);

            var lootList = lootTable.getRandomItems(builder.create(LootContextParamSets.BLOCK));
            if (lootList.isEmpty()) return false;
            var lootItem = lootList.getFirst();
            if (lootItem.isEmpty()) return false;

            if (lootItem.getItem() instanceof SpawnEggItem egg) {

                Entity eggEntity = egg.getType(lootItem).create(level);
                if (eggEntity != null) {
                    eggEntity.moveTo(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
                    level.addFreshEntity(eggEntity);
                    itemEntity.getItem().shrink(1);
                    return true;
                }
            }

            suckInItems(entity, lootItem);
            var random = level.random;
            itemEntity.getItem().shrink(1);
            if (lootItem.isEmpty()) return false;
            ParticleUtils.spawnParticlesOnBlockFace(level, pos, new ItemParticleOption(ParticleTypes.ITEM, lootItem),
                    UniformInt.of(3, 5), Direction.UP,
                    (() -> new Vec3(Mth.nextDouble(random, -0.5D, 0.5D), Mth.nextDouble(random, -0.5D, 0.5D), Mth.nextDouble(random, -0.5D, 0.5D))),
                    0.55D);
            return true;
        }
        return false;

    }

    private static void spawnParticlesOnServer(ServerLevel level, ItemStack item, BlockPos pos){
        int count = BiasedToBottomInt.of(0,4).sample(level.random);
        Vec3 vec3 = Vec3.atCenterOf(pos);

        for (int i = 0; i < count; i++) {
            level.sendParticles( new ItemParticleOption(ParticleTypes.ITEM, item),
                    vec3.x,vec3.y + 0.8D, vec3.z,1,
                    Mth.nextDouble(level.random, -0.3D, 0.3D),Mth.nextDouble(level.random, 0.0D, 0.15D),Mth.nextDouble(level.random, -0.3D, 0.3D),
                    Mth.nextDouble(level.random, 0.0D, 0.1D)
            );
        }
    }

    public static boolean suckInItems(Container container, ItemStack itemStack) {
        ItemStack itemStack2 = addItem(container, itemStack);
        return itemStack2.isEmpty();
    }

    public static ItemStack addItem(Container destination, ItemStack stack) {
        int containerSize = destination.getContainerSize();

        for(int i = 0; i < containerSize && !stack.isEmpty(); ++i) {
            stack = tryMoveInItem(destination, stack, i);
        }

        return stack;
    }

    private static ItemStack tryMoveInItem(Container destination, ItemStack stack, int slot) {
        ItemStack itemStack = destination.getItem(slot);
        if (canPlaceItemInContainer(destination, stack, slot)) {
            boolean bl = false;
            boolean bl2 = destination.isEmpty();
            if (itemStack.isEmpty()) {
                destination.setItem(slot, stack);
                stack = ItemStack.EMPTY;
                bl = true;
            } else if (canMergeItems(itemStack, stack)) {
                int i = stack.getMaxStackSize() - itemStack.getCount();
                int j = Math.min(stack.getCount(), i);
                stack.shrink(j);
                itemStack.grow(j);
                bl = j > 0;
            }

            if (bl) {
                if (bl2 && destination instanceof SluiceBlockEntity sluiceBlockEntity) {
                    int j = 0;
                    sluiceBlockEntity.setCooldown(8 - j);
                }

                destination.setChanged();
            }
        }

        return stack;
    }

    private static boolean canPlaceItemInContainer(Container container, ItemStack stack, int slot) {
        if (!container.canPlaceItem(slot, stack)) {
            return false;
        } else {
            if (container instanceof WorldlyContainer worldlyContainer) {
                return worldlyContainer.canPlaceItemThroughFace(slot, stack, null);
            }

            return true;
        }
    }

    private static boolean canMergeItems(ItemStack stack1, ItemStack stack2) {
        return stack1.getCount() <= stack1.getMaxStackSize() && ItemStack.isSameItemSameComponents(stack1, stack2);
    }

}