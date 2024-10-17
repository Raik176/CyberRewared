package org.rhm.api;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;

/**
 * Interface representing a piece of cyberware that can be installed in a {@link LivingEntity}.
 */
public interface ICyberware {
    float SCAVENGED_ESSENCE_MULTIPLIER = 1.5f;

    /**
     * Gets the slot in which this piece of cyberware can be installed.
     *
     * @return The {@link Slot} enum representing the equipment slot.
     */
    Slot getSlot();

    /**
     * Gets the essence cost for equipping this cyberware.
     *
     * @return The essence cost as an integer.
     */
    int getEssenceCost();

    /**
     * Gets the power requirement (per second) for this cyberware. The default value is 2. Negative values will generate
     * power.
     *
     * @return The power requirement as an integer.
     */
    default int getPowerRequirement() {
        return 2;
    }

    /**
     * Gets the array of cyberware that is required to install this piece of cyberware.
     * <p>
     * By default, this method returns an empty array, meaning no other cyberware is required. Override this method to
     * specify dependencies for cyberware that requires other components to function.
     * </p>
     *
     * @return An {@link Item} representing the required cyberware. Returns null by default.
     */
    default Item getRequiredCyberware() {
        return null;
    }

    /**
     * Gets the array of cyberware that is incompatible with this piece of cyberware.
     * <p>
     * By default, this method returns an empty array, meaning no cyberware is incompatible. Override this method to
     * specify any cyberware that cannot coexist with this piece.
     * </p>
     *
     * @return An {@link Item} representing the incompatible cyberware. Returns null by default.
     */
    default Item getIncompatibleCyberware() {
        return null;
    }

    /**
     * Checks if the cyberware is active.
     * <p>
     * Cyberware will still tick even when not active. However, it will not use power.
     * </p>
     * Always returns true by default.
     *
     * @return {@code true} if the entity is active, {@code false} otherwise
     */
    default boolean isActive() {
        return true;
    }

    /**
     * Called each tick for each entity that has this cyberware installed. This can be used to handle periodic updates
     * or effects. For things which can't be handled on a per tick basis, use an event.
     *
     * @param entity
     *     The {@link LivingEntity} that has this cyberware installed.
     */
    default void tick(LivingEntity entity) {
    }

    /**
     * Enum representing the possible slots for installing cyberware on a {@link LivingEntity}.
     */
    enum Slot {
        EYES,
        CRANIUM,
        HEART,
        LUNGS,
        LOWER_ORGANS,
        SKIN,
        MUSCLE,
        BONE,
        ARM,
        HAND,
        LEG,
        FOOT
    }
}
