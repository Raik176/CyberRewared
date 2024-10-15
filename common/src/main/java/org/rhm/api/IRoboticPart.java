package org.rhm.api;

public interface IRoboticPart {
    Slot getSlot();

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
