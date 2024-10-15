package org.rhm.util;

public interface IEnergyStorage {
    default void insert(int amount) {
        int inserted = Math.min(getMaxIn(), Math.min(amount, getCapacity() - getEnergy()));

        if (inserted > 0) {
            setEnergy(getEnergy() + inserted);
        }
    }

    default void extract(int amount) {
        int extracted = Math.min(getMaxOut(), Math.min(amount, getEnergy()));

        if (extracted > 0) {
            setEnergy(getEnergy() - extracted);
        }
    }

    default void onFinal() {
    }

    int getCapacity();

    int getMaxIn();

    int getMaxOut();

    int getEnergy();

    void setEnergy(int value);
}
