package org.rhm.gui;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class CyberScreenHandler extends AbstractContainerMenu {
    protected Container inventory;
    protected Inventory playerInventory;
    protected int containerId;

    protected CyberScreenHandler(@Nullable MenuType<?> menuType, int containerId) {
        super(menuType, containerId);
        this.containerId = containerId;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;

        Slot slot = this.slots.get(invSlot);
        if (slot.hasItem()) {
            ItemStack originalStack = slot.getItem();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.getContainerSize()) {
                if (!this.moveItemStackTo(originalStack, this.inventory.getContainerSize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(originalStack, 0, this.inventory.getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return newStack;
    }


    @Override
    public boolean stillValid(@NotNull Player player) {
        return this.inventory.stillValid(player);
    }
}
