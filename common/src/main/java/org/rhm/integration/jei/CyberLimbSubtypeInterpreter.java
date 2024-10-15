package org.rhm.integration.jei;

import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.rhm.registries.ComponentRegistry;

public class CyberLimbSubtypeInterpreter extends CyberItemSubtypeInterpreter {
    public static final CyberLimbSubtypeInterpreter INSTANCE = new CyberLimbSubtypeInterpreter();

    @Override
    public @Nullable Object getSubtypeData(@NotNull ItemStack ingredient, @NotNull UidContext context) {
        return super.getSubtypeData(ingredient, context) + ";" + ingredient.get(ComponentRegistry.IS_RIGHT);
    }
}
