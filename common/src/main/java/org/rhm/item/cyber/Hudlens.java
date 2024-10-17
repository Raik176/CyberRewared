package org.rhm.item.cyber;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.rhm.item.CyberItem;
import org.rhm.registries.ItemRegistry;

public class Hudlens extends CyberItem {
    public Hudlens() {
        super(new Item.Properties());
    }

    @Override
    public Slot getSlot() {
        return Slot.EYES;
    }

    @Override
    public int getEssenceCost() {
        return 1;
    }

    @Override
    public ItemStack[] getDestructComponents() {
        return new ItemStack[0];
    }

    @Override
    public Item getIncompatibleCyberware() {
        return ItemRegistry.CYBEREYES;
    }
}
