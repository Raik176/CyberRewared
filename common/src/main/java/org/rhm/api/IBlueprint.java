package org.rhm.api;

import net.minecraft.world.item.ItemStack;

/**
 * Interface representing a blueprint that produces an {@link ItemStack}. Implementations of this interface should
 * define the specific item produced by the blueprint.
 */
public interface IBlueprint {
    /**
     * Gets the result of this blueprint as an {@link ItemStack}.
     *
     * @return The resulting {@link ItemStack} produced by this blueprint.
     */
    ItemStack getResult();
}
