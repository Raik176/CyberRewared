package org.rhm.integration.jei;

import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.rhm.registries.ComponentRegistry;

public class LimbSubtypeInterpreter implements ISubtypeInterpreter<ItemStack> {
    public static final LimbSubtypeInterpreter INSTANCE = new LimbSubtypeInterpreter();

    @Override
    public @Nullable Object getSubtypeData(@NotNull ItemStack ingredient, @NotNull UidContext context) {
        return ingredient.get(ComponentRegistry.IS_RIGHT);
    }

    @Override
    public @NotNull String getLegacyStringSubtypeInfo(@NotNull ItemStack ingredient, @NotNull UidContext context) {
        return "";
    }
}
