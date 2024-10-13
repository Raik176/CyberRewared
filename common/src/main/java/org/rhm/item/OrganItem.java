package org.rhm.item;

import net.minecraft.item.Item;

// this could probably be removed
public class OrganItem extends Item {
    public OrganItem(Settings settings) {
        super(settings.maxCount(1));
    }
}
