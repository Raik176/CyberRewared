package org.rhm.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.rhm.api.IRoboticPart;
import org.rhm.entity.CyberzombieEntity;
import org.rhm.registries.ComponentRegistry;
import org.rhm.registries.ItemRegistry;

import java.util.function.Consumer;

public class CyberUtil {
    public static String prettyTime(long seconds) {
        return String.format("%02d:%02d", seconds / 60, seconds % 60);
    }

    public static ItemStack getBlueprintWithItem(Item item) {
        ItemStack stack = new ItemStack(ItemRegistry.BLUEPRINT);
        stack.set(ComponentRegistry.BLUEPRINT_RESULT, new ItemStack(item));
        return stack;
    }

    public static String getStringId(ResourceLocation location) {
        return location.getNamespace() + ":" + location.getPath();
    }

    public static void addRandomCyberware(CyberzombieEntity entity) {
        for (IRoboticPart.Slot value : IRoboticPart.Slot.values()) {

        }
    }

    public static void addPlayerInventorySlots(Inventory playerInventory, Consumer<Slot> addSlotFunc) {
        addPlayerInventorySlots(playerInventory, addSlotFunc, 0, 0);
    }
    public static void addPlayerInventorySlots(Inventory playerInventory, Consumer<Slot> addSlotFunc, int offsetX, int offsetY) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotFunc.accept(new Slot(playerInventory, j + i * 9 + 9, offsetX + 8 + j * 18, offsetY + i * 18));
            }
        }
        for (int i = 0; i < 9; i++) {
            addSlotFunc.accept(new Slot(playerInventory, i, offsetX + 8 + i * 18, offsetY + 58));
        }
    }
}
