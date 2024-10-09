package org.rhm.api;

import net.minecraft.item.ItemStack;

public interface IDeconstructable {
    boolean canDestroy(ItemStack stack);

    ItemStack[] getComponents(ItemStack stack);
}
