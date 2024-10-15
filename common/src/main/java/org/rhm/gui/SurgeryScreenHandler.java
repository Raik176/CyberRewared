package org.rhm.gui;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import org.rhm.block.entity.RobosurgeonBlockEntity;
import org.rhm.registries.ScreenHandlerRegistry;
import org.rhm.util.CyberUtil;

public class SurgeryScreenHandler extends CyberScreenHandler {
    public SurgeryScreenHandler(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, new SimpleContainer(2), null);
    }

    public SurgeryScreenHandler(int syncId, Inventory playerInventory, Container inventory, RobosurgeonBlockEntity rsbe) {
        super(ScreenHandlerRegistry.SURGERY, syncId);
        this.inventory = inventory;
        this.playerInventory = playerInventory;

        playerInventory.startOpen(playerInventory.player);
        CyberUtil.addPlayerInventorySlots(playerInventory, this::addSlot);
    }
}
