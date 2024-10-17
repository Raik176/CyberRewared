package org.rhm.util;

import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.rhm.api.ICyberEntity;
import org.rhm.api.ICyberware;
import org.rhm.registries.ComponentRegistry;
import org.rhm.registries.ItemRegistry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class CyberUtil {
    private static final int DEFAULT_X_OFFSET = 8;

    public static String prettyTime(long seconds) {
        return String.format("%02d:%02d", seconds / 60, seconds % 60);
    }

    public static String toCamelCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        StringBuilder camelCaseString = new StringBuilder();
        String[] words = input.split("\\s+");

        for (String word : words) {
            if (!word.isEmpty()) {
                camelCaseString.append(Character.toUpperCase(word.charAt(0)))
                    .append(word.substring(1).toLowerCase());
            }
        }

        return camelCaseString.toString();
    }

    public static ItemStack getBlueprintWithItem(Item item) {
        ItemStack stack = new ItemStack(ItemRegistry.BLUEPRINT);
        stack.set(ComponentRegistry.BLUEPRINT_RESULT, new ItemStack(item));
        return stack;
    }

    public static int getScavengedEssenceCost(ICyberware ware) {
        return (int) Math.ceil(ware.getEssenceCost() * ICyberware.SCAVENGED_ESSENCE_MULTIPLIER);
    }

    public static List<Component> splitNewlineComponent(Component component) {
        return splitNewlineComponent(component, ChatFormatting.RESET); // Reset is probably not ideal, should use null
    }

    public static List<Component> splitNewlineComponent(Component component, ChatFormatting formatting) {
        List<Component> components = new ArrayList<>();
        for (String s : component.getString().split("\n")) {
            components.add(Component.literal(s).withStyle(formatting));
        }
        return components;
    }

    public static void addPlayerInventorySlots(Inventory playerInventory, Consumer<Slot> addSlotFunc, int offsetY) {
        addPlayerInventorySlots(playerInventory, addSlotFunc, DEFAULT_X_OFFSET, offsetY);
    }

    public static void addPlayerInventorySlots(Inventory playerInventory, Consumer<Slot> addSlotFunc) {
        addPlayerInventorySlots(playerInventory, addSlotFunc, DEFAULT_X_OFFSET, 0);
    }

    public static void addPlayerInventorySlots(Inventory playerInventory, Consumer<Slot> addSlotFunc, int offsetX, int offsetY) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotFunc.accept(new Slot(playerInventory, j + i * 9 + 9, offsetX + j * 18, offsetY + i * 18));
            }
        }
        for (int i = 0; i < 9; i++) {
            addSlotFunc.accept(new Slot(playerInventory, i, offsetX + i * 18, offsetY + 58));
        }
    }

    public static CompoundTag generateCyberwareCompound(ICyberEntity entity) {
        CompoundTag tag = new CompoundTag();
        entity.writeCyberData(tag); // In case it already had data, don't wanna remove it.

        NonNullList<NonNullList<ItemStack>> wares = NonNullList.create();

        for (ICyberware.Slot slot : ICyberware.Slot.values()) {
            NonNullList<ItemStack> toAdd = entity.getInstalledCyberware(tag, slot);
            toAdd.removeAll(Collections.singleton(ItemStack.EMPTY));
            wares.add(toAdd);
        }


        return tag;
    }
}
