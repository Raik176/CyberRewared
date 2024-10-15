package org.rhm.gui;

import commonnetwork.api.Dispatcher;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.rhm.block.entity.ScannerBlockEntity;
import org.rhm.network.ScannerProgressPacket;
import org.rhm.registries.ScreenHandlerRegistry;
import org.rhm.util.CyberUtil;

public class ScannerScreenHandler extends CyberScreenHandler {
    private final Slot paperSlot;
    private final Slot cyberPartSlot;
    private final Slot outputSlot;
    private final ScannerBlockEntity scannerBlockEntity;
    private final Player player;

    public ScannerScreenHandler(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, new SimpleContainer(3), null);
    }

    public ScannerScreenHandler(int syncId, Inventory playerInventory, Container inventory, ScannerBlockEntity sbe) {
        super(ScreenHandlerRegistry.SCANNER, syncId);
        this.inventory = inventory;
        this.scannerBlockEntity = sbe;
        this.player = playerInventory.player;

        paperSlot = new Slot(inventory, 0, 15, 53) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getItem() == ScannerBlockEntity.PAPER_ITEM;
            }
        };
        cyberPartSlot = new Slot(inventory, 1, 35, 53);
        outputSlot = new Slot(inventory, 2, 141, 57) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        };

        this.addSlot(paperSlot);
        this.addSlot(cyberPartSlot);
        this.addSlot(outputSlot);

        playerInventory.startOpen(playerInventory.player);
        CyberUtil.addPlayerInventorySlots(playerInventory, this::addSlot, 84);

        if (sbe != null) {
            scannerBlockEntity.updateCallback = this::sendProgressPacket;
            sendProgressPacket();
        }
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int invSlot) {
        ItemStack fuckQuickMove = super.quickMoveStack(player, invSlot);
        if (!player.level().isClientSide) sendProgressPacket();
        return fuckQuickMove;
    }

    private void sendProgressPacket() {
        if (player instanceof ServerPlayer serverPlayer) {
            Dispatcher.sendToClient(new ScannerProgressPacket(
                containerId,
                scannerBlockEntity.getTicks(),
                scannerBlockEntity.getScanTimeCurrent(),
                scannerBlockEntity.getCurrentChance()
            ), serverPlayer);
        }
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
}
