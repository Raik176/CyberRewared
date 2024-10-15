package org.rhm.gui;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.rhm.CyberRewaredMod;
import org.rhm.registries.ScreenHandlerRegistry;
import org.rhm.util.CyberUtil;

// todo: implement, fix, etc
public class ComponentBoxScreenHandler extends CyberScreenHandler {
    // i dont know why the original makes this variable, but since it has it im porting it over
    public static final int SLOT_COUNT = 2 * 9;

    private final Slot[] componentSlots;

    public ComponentBoxScreenHandler(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, new SimpleContainer(SLOT_COUNT));
    }

    public ComponentBoxScreenHandler(int syncId, Inventory playerInventory, Container inventory) {
        super(ScreenHandlerRegistry.COMPONENT_BOX, syncId);
        this.inventory = inventory;
        this.componentSlots = new Slot[SLOT_COUNT];

        int rows = (int) Math.ceil((double) SLOT_COUNT / 9);

        int totalSlots = SLOT_COUNT;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < 9 && totalSlots > 0; col++) {
                int slotIndex = row * 9 + col;
                componentSlots[slotIndex] = new Slot(inventory, slotIndex, 8 + col * 18, 18 + row * 18) {
                    @Override
                    public boolean mayPlace(ItemStack stack) {
                        return stack.is(CyberRewaredMod.COMPONENT_TAG);
                    }
                };
                this.addSlot(componentSlots[slotIndex]);
                totalSlots--;
            }
        }

        playerInventory.startOpen(playerInventory.player);
        CyberUtil.addPlayerInventorySlots(playerInventory, this::addSlot,  31 + SLOT_COUNT * 2, 0);
    }

    public Slot[] getComponentSlots() {
        return componentSlots;
    }
}
