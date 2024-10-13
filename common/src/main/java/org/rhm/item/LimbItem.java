package org.rhm.item;

import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.rhm.CyberRewaredMod;
import org.rhm.registries.ComponentRegistry;

// TODO: probably uniform this and CyberLimbItem
public class LimbItem extends OrganItem {
    public LimbItem(Settings settings) {
        super(settings.component(ComponentRegistry.IS_RIGHT,false));
    }

    @Override
    public Text getName(ItemStack stack) {
        return Text.translatable(
            stack.getTranslationKey(),
            Text.translatable("item." + CyberRewaredMod.MOD_ID + ".limb."  +
                (Boolean.TRUE.equals(stack.get(ComponentRegistry.IS_RIGHT)) ? "right" : "left")
            )
        );
    }
}
