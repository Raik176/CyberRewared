package org.rhm.util;

public interface IEnergyStorage {
    default void insert(long amount) {
        long inserted = Math.min(getMaxIn(), Math.min(amount, getCapacity() - getEnergy()));

        if (inserted > 0) {
            setEnergy(getEnergy() + inserted);
        }
    }

    default void extract(long amount) {
        long extracted = Math.min(getMaxOut(), Math.min(amount, getEnergy()));

        if (extracted > 0) {
            setEnergy(getEnergy() - extracted);
        }
    }

    default void onFinal() {}

    long getCapacity();

    long getMaxIn();

    long getMaxOut();

    long getEnergy();

    void setEnergy(long value);
}
