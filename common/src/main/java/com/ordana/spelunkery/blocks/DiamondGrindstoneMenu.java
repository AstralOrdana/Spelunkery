package com.ordana.spelunkery.blocks;

import com.ordana.spelunkery.reg.ModBlocks;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class DiamondGrindstoneMenu extends AbstractContainerMenu {
    public static final int MAX_NAME_LENGTH = 35;
    public static final int INPUT_SLOT = 0;
    public static final int ADDITIONAL_SLOT = 1;
    public static final int RESULT_SLOT = 2;
    private static final int INV_SLOT_START = 3;
    private static final int INV_SLOT_END = 30;
    private static final int USE_ROW_SLOT_START = 30;
    private static final int USE_ROW_SLOT_END = 39;
    private final Container resultSlots;
    final Container repairSlots;
    private final ContainerLevelAccess access;

    public DiamondGrindstoneMenu(int i, Inventory inventory) {
        this(i, inventory, ContainerLevelAccess.NULL);
    }

    public DiamondGrindstoneMenu(int i, Inventory inventory, ContainerLevelAccess containerLevelAccess) {
        super(MenuType.GRINDSTONE, i);
        this.resultSlots = new ResultContainer();
        this.repairSlots = new SimpleContainer(2) {
            public void setChanged() {
                super.setChanged();
                DiamondGrindstoneMenu.this.slotsChanged(this);
            }
        };
        this.access = containerLevelAccess;
        this.addSlot(new Slot(this.repairSlots, 0, 49, 19) {
            public boolean mayPlace(ItemStack stack) {
                return stack.isDamageableItem() || stack.is(Items.ENCHANTED_BOOK) || stack.isEnchanted();
            }
        });
        this.addSlot(new Slot(this.repairSlots, 1, 49, 40) {
            public boolean mayPlace(ItemStack stack) {
                return stack.isDamageableItem() || stack.is(Items.ENCHANTED_BOOK) || stack.isEnchanted();
            }
        });
        this.addSlot(new Slot(this.resultSlots, 2, 129, 34) {
            public boolean mayPlace(ItemStack stack) {
                return false;
            }

            public void onTake(Player player, ItemStack stack) {
                containerLevelAccess.execute((level, blockPos) -> {
                    if (level instanceof ServerLevel) {
                        ExperienceOrb.award((ServerLevel)level, Vec3.atCenterOf(blockPos), this.getExperienceAmount(level));
                    }

                    level.levelEvent(1042, blockPos, 0);
                });
                DiamondGrindstoneMenu.this.repairSlots.setItem(0, ItemStack.EMPTY);
                DiamondGrindstoneMenu.this.repairSlots.setItem(1, ItemStack.EMPTY);
            }

            private int getExperienceAmount(Level level) {
                int ix = 0;
                int i = ix + this.getExperienceFromItem(DiamondGrindstoneMenu.this.repairSlots.getItem(0));
                i += this.getExperienceFromItem(DiamondGrindstoneMenu.this.repairSlots.getItem(1));
                if (i > 0) {
                    int j = (int)Math.ceil((double)i / 2.0D);
                    return j + level.random.nextInt(j);
                } else {
                    return 0;
                }
            }

            private int getExperienceFromItem(ItemStack stack) {
                int i = 0;
                Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);
                Iterator var4 = map.entrySet().iterator();

                while(var4.hasNext()) {
                    Entry<Enchantment, Integer> entry = (Entry)var4.next();
                    Enchantment enchantment = entry.getKey();
                    Integer integer = entry.getValue();
                    //if (!enchantment.isCurse()) {
                        i += enchantment.getMinCost(integer);
                    //}
                }

                return i;
            }
        });

        int j;
        for(j = 0; j < 3; ++j) {
            for(int k = 0; k < 9; ++k) {
                this.addSlot(new Slot(inventory, k + j * 9 + 9, 8 + k * 18, 84 + j * 18));
            }
        }

        for(j = 0; j < 9; ++j) {
            this.addSlot(new Slot(inventory, j, 8 + j * 18, 142));
        }

    }

    public void slotsChanged(Container container) {
        super.slotsChanged(container);
        if (container == this.repairSlots) {
            this.createResult();
        }

    }

    private void createResult() {
        ItemStack itemStack = this.repairSlots.getItem(0);
        ItemStack itemStack2 = this.repairSlots.getItem(1);
        boolean bl = !itemStack.isEmpty() || !itemStack2.isEmpty();
        boolean bl2 = !itemStack.isEmpty() && !itemStack2.isEmpty();
        if (!bl) {
            this.resultSlots.setItem(0, ItemStack.EMPTY);
        } else {
            boolean bl3 = !itemStack.isEmpty() && !itemStack.is(Items.ENCHANTED_BOOK) && !itemStack.isEnchanted() || !itemStack2.isEmpty() && !itemStack2.is(Items.ENCHANTED_BOOK) && !itemStack2.isEnchanted();
            if (itemStack.getCount() > 1 || itemStack2.getCount() > 1 || !bl2 && bl3) {
                this.resultSlots.setItem(0, ItemStack.EMPTY);
                this.broadcastChanges();
                return;
            }

            int i = 1;
            int m;
            ItemStack itemStack3;
            if (bl2) {
                if (!itemStack.is(itemStack2.getItem())) {
                    this.resultSlots.setItem(0, ItemStack.EMPTY);
                    this.broadcastChanges();
                    return;
                }

                Item item = itemStack.getItem();
                int j = item.getMaxDamage() - itemStack.getDamageValue();
                int k = item.getMaxDamage() - itemStack2.getDamageValue();
                int l = j + k + item.getMaxDamage() * 5 / 100;
                m = Math.max(item.getMaxDamage() - l, 0);
                itemStack3 = this.mergeEnchants(itemStack, itemStack2);
                if (!itemStack3.isDamageableItem()) {
                    if (!ItemStack.matches(itemStack, itemStack2)) {
                        this.resultSlots.setItem(0, ItemStack.EMPTY);
                        this.broadcastChanges();
                        return;
                    }

                    i = 2;
                }
            } else {
                boolean bl4 = !itemStack.isEmpty();
                m = bl4 ? itemStack.getDamageValue() : itemStack2.getDamageValue();
                itemStack3 = bl4 ? itemStack : itemStack2;
            }

            this.resultSlots.setItem(0, this.removeEnchants(itemStack3, m, i));
        }

        this.broadcastChanges();
    }

    private ItemStack mergeEnchants(ItemStack copyTo, ItemStack copyFrom) {
        ItemStack itemStack = copyTo.copy();
        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(copyFrom);
        Iterator var5 = map.entrySet().iterator();

        while(true) {
            Entry entry;
            Enchantment enchantment;
            do {
                if (!var5.hasNext()) {
                    return itemStack;
                }

                entry = (Entry)var5.next();
                enchantment = (Enchantment)entry.getKey();
            } while(/*enchantment.isCurse() &&*/ EnchantmentHelper.getItemEnchantmentLevel(enchantment, itemStack) != 0);

            itemStack.enchant(enchantment, (Integer)entry.getValue());
        }
    }

    private ItemStack removeEnchants(ItemStack stack, int damage, int count) {
        ItemStack itemStack = stack.copy();
        itemStack.removeTagKey("Enchantments");
        itemStack.removeTagKey("StoredEnchantments");
        if (damage > 0) {
            itemStack.setDamageValue(damage);
        } else {
            itemStack.removeTagKey("Damage");
        }

        itemStack.setCount(count);
        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack).entrySet().stream().filter((entry) -> !entry.getKey().isCurse() && entry.getKey().isCurse()).collect(Collectors.toMap(Entry::getKey, Entry::getValue));
        EnchantmentHelper.setEnchantments(map, itemStack);
        itemStack.setRepairCost(0);
        if (itemStack.is(Items.ENCHANTED_BOOK) && map.size() == 0) {
            itemStack = new ItemStack(Items.BOOK);
            if (stack.hasCustomHoverName()) {
                itemStack.setHoverName(stack.getHoverName());
            }
        }

        for(int i = 0; i < map.size(); ++i) {
            itemStack.setRepairCost(AnvilMenu.calculateIncreasedRepairCost(itemStack.getBaseRepairCost()));
        }

        return itemStack;
    }

    public void removed(Player player) {
        super.removed(player);
        this.access.execute((level, blockPos) -> {
            this.clearContainer(player, this.repairSlots);
        });
    }

    public boolean stillValid(Player player) {
        return stillValid(this.access, player, ModBlocks.DIAMOND_GRINDSTONE.get());
    }

    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemStack2 = slot.getItem();
            itemStack = itemStack2.copy();
            ItemStack itemStack3 = this.repairSlots.getItem(0);
            ItemStack itemStack4 = this.repairSlots.getItem(1);
            if (index == 2) {
                if (!this.moveItemStackTo(itemStack2, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemStack2, itemStack);
            } else if (index != 0 && index != 1) {
                if (!itemStack3.isEmpty() && !itemStack4.isEmpty()) {
                    if (index >= 3 && index < 30) {
                        if (!this.moveItemStackTo(itemStack2, 30, 39, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (index >= 30 && index < 39 && !this.moveItemStackTo(itemStack2, 3, 30, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(itemStack2, 0, 2, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemStack2, 3, 39, false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemStack2);
        }

        return itemStack;
    }
}
