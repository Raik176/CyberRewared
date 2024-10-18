package org.rhm.item.cyber;

import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.rhm.item.CyberItem;
import org.rhm.registries.ItemRegistry;

public class Hudjack extends CyberItem {
    public Hudjack() {
        super(new Properties());
    }

    @Override
    public Slot getSlot() {
        return Slot.EYES;
    }

    @Override
    public int getEssenceCost() {
        return 1;
    }

    @Override
    public int getPowerRequirement() {
        return 0;
    }

    @Override
    public Item getRequiredCyberware() {
        return ItemRegistry.CYBEREYES;
    }

    @Override
    public ItemStack[] getDestructComponents() {
        return new ItemStack[0];
    }
}
