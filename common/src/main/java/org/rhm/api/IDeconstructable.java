package org.rhm.api;

import net.minecraft.item.ItemStack;

public interface IDeconstructable {
    /**
     * Returns an array of {@link ItemStack} representing the components
     * that are recovered when this item is broken down in an Engineering Table.
     *
     * @return the array of {@link ItemStack} components obtained upon deconstruction.
     */
    ItemStack[] getDestructComponents();

    /**
     * Returns the maximum variation in the number of components produced when
     * this item is deconstructed
     *
     * @return the maximum variation in the deconstruction output, defaulting to 3.
     */
    default int getOutputVariation() {
        return 3;
    }
}
