package org.rhm.gui;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.rhm.api.IBlueprint;
import org.rhm.api.IDeconstructable;
import org.rhm.block.entity.BlueprintArchiveBlockEntity;
import org.rhm.block.entity.ComponentBoxBlockEntity;
import org.rhm.block.entity.EngineeringTableBlockEntity;
import org.rhm.registries.ScreenHandlerRegistry;

// todo: implement, fix, etc
public class EngineeringTableScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final Slot salvageSlot;
    private final Slot paperSlot;
    private final Slot[] outputSlots;
    private final Slot blueprintSlot;
    private final Slot craftingOutputSlot;
    private BlueprintArchiveBlockEntity babe = null;
    private ComponentBoxBlockEntity cbbe = null;

    public EngineeringTableScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(11), null);
    }

    public EngineeringTableScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, EngineeringTableBlockEntity be) {
        super(ScreenHandlerRegistry.ENGINEERING_TABLE.get(), syncId);
        this.inventory = inventory;

        playerInventory.onOpen(playerInventory.player);

        salvageSlot = new Slot(inventory, 0, 15, 20) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.getItem() instanceof IDeconstructable;
            }
        };
        paperSlot = new Slot(inventory, 1, 15, 53) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.getItem() == Items.PAPER;
            }
        };
        outputSlots = new Slot[3 * 2];
        int slotIndex = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                outputSlots[slotIndex] = new Slot(inventory, 2 + slotIndex, 71 + j * 18, 17 + i * 18) {
                    @Override
                    public boolean canInsert(ItemStack stack) {
                        return false;
                    }
                };
                this.addSlot(outputSlots[slotIndex]);
                slotIndex++;
            }
        }

        blueprintSlot = new Slot(inventory, 9, 115, 53) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.getItem() instanceof IBlueprint;
            }
        };
        craftingOutputSlot = new Slot(inventory, 10, 145, 21) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }
        };

        this.addSlot(salvageSlot);
        this.addSlot(paperSlot);
        this.addSlot(blueprintSlot);
        this.addSlot(craftingOutputSlot);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }

        if (!playerInventory.player.getEntityWorld().isClient) {
            BlockEntity temp = be.getWorld().getBlockEntity(be.getPos().east());
            if (temp instanceof BlueprintArchiveBlockEntity lol) {
                babe = lol;
            } else if (temp instanceof ComponentBoxBlockEntity lol) {
                cbbe = lol;
            }

            temp = be.getWorld().getBlockEntity(be.getPos().west());
            if (temp instanceof BlueprintArchiveBlockEntity lol) {
                babe = lol;
            } else if (temp instanceof ComponentBoxBlockEntity lol) {
                cbbe = lol;
            }
        }
    }

    public Slot getSalvageSlot() {
        return salvageSlot;
    }

    public Slot getPaperSlot() {
        return paperSlot;
    }

    public Slot[] getOutputSlots() {
        return outputSlots;
    }

    public Slot getBlueprintSlot() {
        return blueprintSlot;
    }

    public Slot getCraftingOutputSlot() {
        return craftingOutputSlot;
    }

    public BlueprintArchiveBlockEntity getBlueprintArchive() {
        return babe;
    }

    public ComponentBoxBlockEntity getComponentBox() {
        return cbbe;
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
