package org.rhm.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.rhm.CyberRewaredMod;
import org.rhm.registries.ComponentRegistry;

// TODO: probably uniform this and CyberLimbItem
public class LimbItem extends OrganItem {
    public LimbItem(Properties settings) {
        super(settings.component(ComponentRegistry.IS_RIGHT, false));
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.translatable(
            stack.getDescriptionId(),
            Component.translatable("item." + CyberRewaredMod.MOD_ID + ".limb." +
                (Boolean.TRUE.equals(stack.get(ComponentRegistry.IS_RIGHT)) ? "right" : "left")
            )
        );
    }
}
