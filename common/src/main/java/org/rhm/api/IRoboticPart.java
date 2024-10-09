package org.rhm.api;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public interface IRoboticPart {
    ItemStack[] requiredItems();

    boolean isCompatible(ItemStack comparison);

    RoboticSlot getSlot();

    void onAdded(LivingEntity entity);

    void onRemoved(LivingEntity entity);

    enum RoboticSlot {
        EYES(12, "eyes"),
        CRANIUM(11, "cranium"),
        HEART(14, "heart"),
        LUNGS(15, "lungs"),
        LOWER_ORGANS(17, "lower_organs"),
        SKIN(18, "skin"),
        MUSCLE(19, "muscle"),
        BONE(20, "bone"),
        ARM(21, "arm", true, true),
        HAND(22, "hand", true, false),
        LEG(23, "leg", true, true),
        FOOT(24, "foot", true, false);

        private final int slotNumber;
        private final String name;
        private final boolean sidedSlot;
        private final boolean hasEssential;

        RoboticSlot(int slot, String name) {
            this(slot, name, false, true);
        }

        RoboticSlot(int slot, String name, boolean sidedSlot, boolean hasEssential) {
            this.slotNumber = slot;
            this.name = name;
            this.sidedSlot = sidedSlot;
            this.hasEssential = hasEssential;
        }

        public static RoboticSlot getSlotByPage(int page) {
            for (RoboticSlot slot : values()) {
                if (slot.getSlotNumber() == page) {
                    return slot;
                }
            }
            return null;
        }

        public int getSlotNumber() {
            return slotNumber;
        }

        public String getName() {
            return name;
        }

        public boolean isSided() {
            return sidedSlot;
        }

        public boolean hasEssential() {
            return hasEssential;
        }
    }
}
