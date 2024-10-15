package org.rhm.api;

import net.minecraft.world.item.ItemStack;

public interface IDeconstructable {
    /**
     * Returns an array of {@link ItemStack} representing the components that are recovered when this item is broken
     * down in an Engineering Table. The count will be the maximum, as it can variate.
     *
     * @return the array of {@link ItemStack} components obtained upon deconstruction.
     */
    ItemStack[] getDestructComponents();
}
