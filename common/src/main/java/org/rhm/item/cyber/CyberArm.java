package org.rhm.item.cyber;

import net.minecraft.world.item.ItemStack;
import org.rhm.item.CyberLimbItem;

public class CyberArm extends CyberLimbItem {
    public CyberArm() {
        super(new Properties());
    }

    @Override
    public ItemStack[] getDestructComponents() {
        return new ItemStack[] {};
    }

    @Override
    public Slot getSlot() {
        return Slot.ARM;
    }
}
