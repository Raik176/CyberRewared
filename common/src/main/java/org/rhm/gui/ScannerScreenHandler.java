package org.rhm.gui;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.rhm.block.entity.ScannerBlockEntity;
import org.rhm.registries.ScreenHandlerRegistry;

public class ScannerScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final Slot paperSlot;
    private final Slot cyberPartSlot;
    private final Slot outputSlot;
    private ScannerBlockEntity scannerBlockEntity;
    private PlayerInventory playerInventory;

    public ScannerScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new ScreenInventory(3), null);
    }

    public ScannerScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, ScannerBlockEntity sbe) {
        super(ScreenHandlerRegistry.SCANNER, syncId);
        this.inventory = inventory;
        this.scannerBlockEntity = sbe;
        this.playerInventory = playerInventory;
        if (this.inventory instanceof ScreenInventory si) {
            si.setHandler(this);
        }

        playerInventory.onOpen(playerInventory.player);

        paperSlot = new Slot(inventory, 0, 15, 53) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.getItem() == ScannerBlockEntity.PAPER_ITEM;
            }
        };
        cyberPartSlot = new Slot(inventory, 1, 35, 53);
        outputSlot = new Slot(inventory, 2, 141, 57) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }
        };

        this.addSlot(paperSlot);
        this.addSlot(cyberPartSlot);
        this.addSlot(outputSlot);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        System.out.println(playerInventory.player.getWorld().isClient);
    }

    public Slot getOutputSlot() {
        return outputSlot;
    }

    public Slot getCyberPartSlot() {
        return cyberPartSlot;
    }

    public Slot getPaperSlot() {
        return paperSlot;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        System.out.println(player.getWorld().isClient);
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
