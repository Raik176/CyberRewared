package org.rhm.item.cyber;

import net.minecraft.world.item.ItemStack;
import org.rhm.item.CyberLimbItem;

public class CyberLeg extends CyberLimbItem {
    public CyberLeg() {
        super(new Properties());
    }

    @Override
    public ItemStack[] getDestructComponents() {
        return new ItemStack[0];
    }

    @Override
    public Slot getSlot() {
        return null;
    }
}
