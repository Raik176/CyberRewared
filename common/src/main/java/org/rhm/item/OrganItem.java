package org.rhm.item;

import net.minecraft.world.item.Item;
import org.rhm.util.IGeneratedModel;

public class OrganItem extends Item implements IGeneratedModel {
    public OrganItem(Properties settings) {
        super(settings.stacksTo(1));
    }
}
