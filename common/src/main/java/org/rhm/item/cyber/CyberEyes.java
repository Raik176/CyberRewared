package org.rhm.item.cyber;

import net.minecraft.world.item.ItemStack;
import org.rhm.item.CyberItem;

public class CyberEyes extends CyberItem {
    public CyberEyes() {
        super(new Properties());
    }

    @Override
    public ItemStack[] getDestructComponents() {
        return new ItemStack[0];
    }

    @Override
    public Slot getSlot() {
        return Slot.EYES;
    }
}
