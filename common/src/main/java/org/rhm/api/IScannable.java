package org.rhm.api;

import net.minecraft.world.item.ItemStack;

/**
 * Interface representing an object that can be scanned to produce a result. Implementations of this interface define
 * the scanning behavior, including the result of the scan, the time required, and additional scanning parameters.
 * <p>
 * This interface provides methods to determine the scan result, time, whether paper is required, the chance of success,
 * and if the result can also be obtained through an Engineering Table.
 * </p>
 */
public interface IScannable {
    /**
     * Retrieves the result of the scan, represented as an {@link ItemStack}.
     *
     * @return the result of the scan.
     */
    ItemStack getScanResult();

    /**
     * Returns the time required to complete the scan. If the value is -1, it uses the scanner's default time.
     *
     * @return the scan time, or -1 for default.
     */
    default int getScanTime() {
        return -1;
    }

    /**
     * Indicates whether paper is required for the scanning process.
     *
     * @return {@code true} if paper is needed, {@code false} otherwise.
     */
    default boolean scanNeedsPaper() {
        return true;
    }

    /**
     * Calculates the chance of the scan succeeding.
     *
     * @return The success rate of the scan as a percentage (from 0 to 100).
     */
    default float scanSuccessChance() {
        return 20;
    }

    /**
     * Determines whether the result obtained from {@link #getScanResult()} can also be outputted by smashing the item
     * in an Engineering Table.
     *
     * @return {@code true} if the result can be obtained by smashing, {@code false} otherwise.
     */
    default boolean scanCanOutputFromSmash() {
        return true;
    }
}
