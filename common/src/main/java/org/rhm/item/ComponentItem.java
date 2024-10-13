package org.rhm.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.rhm.api.IDeconstructable;

// This is just utility for the data generation
public class ComponentItem extends Item implements IDeconstructable {
    public ComponentItem() {
        super(new Settings());
    }

    @Override
    public ItemStack[] getDestructComponents() {
        return new ItemStack[]{};
    }
}
