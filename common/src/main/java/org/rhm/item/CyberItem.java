package org.rhm.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.rhm.api.IDeconstructable;
import org.rhm.api.IRoboticPart;
import org.rhm.registries.ComponentRegistry;

public abstract class CyberItem extends Item implements IRoboticPart, IDeconstructable {
    public CyberItem(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack getDefaultStack() {
        ItemStack itemStack = super.getDefaultStack();
        itemStack.set(ComponentRegistry.SCAVENGED, false);
        return itemStack;
    }

    @Override
    public boolean isCompatible(ItemStack comparison) {
        return true;
    }

    @Override
    public void onAdded(LivingEntity entity) {

    }

    @Override
    public void onRemoved(LivingEntity entity) {

    }
}
