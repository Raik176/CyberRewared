package org.rhm.item.cyber;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.rhm.item.CyberItem;

public class CyberEyes extends CyberItem {
    public CyberEyes() {
        super(new Properties());
    }

    @Override
    public ItemStack[] getDestructComponents() {
        return new ItemStack[0];
    }

    @Override
    public Slot getSlot() {
        return Slot.EYES;
    }

    @Override
    public int getEssenceCost() {
        return 8;
    }

    @Override
    public int getPowerRequirement() {
        return 1;
    }

    @Override
    public void tick(LivingEntity entity) {
        super.tick(entity);
        entity.removeEffect(MobEffects.BLINDNESS);
    }
}
