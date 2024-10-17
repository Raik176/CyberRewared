package org.rhm.api;

import net.minecraft.world.item.ItemStack;

/**
 * Interface representing an item that can be deconstructed in an Engineering Table. Implementations of this interface
 * define the components that are recovered when the item is broken down.
 * <p>
 * The components are returned as an array of {@link ItemStack}, with each {@link ItemStack} representing a different
 * component that can vary in count.
 * </p>
 */
public interface IDeconstructable {
    /**
     * Returns an array of {@link ItemStack} representing the components that are recovered when this item is broken
     * down in an Engineering Table. The count will be the maximum, as it can variate.
     *
     * @return the array of {@link ItemStack} components obtained upon deconstruction.
     */
    ItemStack[] getDestructComponents();
}
