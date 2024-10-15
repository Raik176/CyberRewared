package org.rhm.integration.jei;

import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.rhm.registries.ComponentRegistry;

public class CyberItemSubtypeInterpreter implements ISubtypeInterpreter<ItemStack> {
    public static final CyberItemSubtypeInterpreter INSTANCE = new CyberItemSubtypeInterpreter();

    @Override
    public @Nullable Object getSubtypeData(@NotNull ItemStack ingredient, @NotNull UidContext context) {
        return ingredient.get(ComponentRegistry.SCAVENGED);
    }

    @Override
    public @NotNull String getLegacyStringSubtypeInfo(@NotNull ItemStack ingredient, @NotNull UidContext context) {
        return "";
    }
}
