package org.rhm.api;

public interface ICyberware {
    Slot getSlot();
    int getEssenceCost();
    default int getPowerRequirement() {
        return 2;
    }

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
