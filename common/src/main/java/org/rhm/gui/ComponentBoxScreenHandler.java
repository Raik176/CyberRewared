package org.rhm.gui;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.rhm.api.IBlueprint;
import org.rhm.registries.ScreenHandlerRegistry;

// todo: implement, fix, etc
public class ComponentBoxScreenHandler extends ScreenHandler {
    // i dont know why the original makes this variable, but since it has it im porting it over
    public static final int SLOT_COUNT = 2 * 9;

    private final Inventory inventory;
    private final Slot[] componentSlots;

    public ComponentBoxScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(SLOT_COUNT));
    }

    public ComponentBoxScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(ScreenHandlerRegistry.COMPONENT_BOX.get(), syncId);
        this.inventory = inventory;
        this.componentSlots = new Slot[SLOT_COUNT];

        int rows = (int) Math.ceil((double) SLOT_COUNT / 9);

        int totalSlots = SLOT_COUNT;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < 9 && totalSlots > 0; col++) {
                int slotIndex = row * 9 + col;
                componentSlots[slotIndex] = new Slot(inventory, slotIndex, 8 + col * 18, 18 + row * 18) {
                    @Override
                    public boolean canInsert(ItemStack stack) {
                        return stack.getItem() instanceof IBlueprint;
                    }
                };
                this.addSlot(componentSlots[slotIndex]);
                totalSlots--;
            }
        }

        playerInventory.onOpen(playerInventory.player);


        int playerInventoryOffset = 31 + SLOT_COUNT * 2;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, playerInventoryOffset + i * 18));
            }
        }
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, playerInventoryOffset + 58));
        }
    }

    public Slot[] getComponentSlots() {
        return componentSlots;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;

        Slot slot = this.slots.get(invSlot);
        if (slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return newStack;
    }


    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }
}
