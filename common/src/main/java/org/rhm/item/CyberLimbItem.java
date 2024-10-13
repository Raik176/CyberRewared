package org.rhm.item;

import net.minecraft.item.ItemStack;
import org.rhm.registries.ComponentRegistry;

// TODO: probably uniform this and LimbItem
public abstract class CyberLimbItem extends CyberItem {
    public CyberLimbItem(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack getDefaultStack() {
        ItemStack itemStack = super.getDefaultStack();
        itemStack.set(ComponentRegistry.IS_RIGHT, false);
        return itemStack;
    }
}
