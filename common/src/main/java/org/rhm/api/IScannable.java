package org.rhm.api;

import net.minecraft.item.ItemStack;

public interface IScannable {
    /**
     * Retrieves the result of the scan, represented as an {@link ItemStack}.
     *
     * @return the result of the scan.
     */
    ItemStack getResult();

    /**
     * Returns the time required to complete the scan. If the value is -1,
     * it uses the scanner's default time.
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
    default boolean needsPaper() {
        return true;
    }

    /**
     * Determines whether the result obtained from {@link #getResult()}
     * can also be outputted by smashing the item in an Engineering Table.
     *
     * @return {@code true} if the result can be obtained by smashing, {@code false} otherwise.
     */
    default boolean canOutputFromSmash() {
        return true;
    }
}
