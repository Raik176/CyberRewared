package org.rhm.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.rhm.registries.ComponentRegistry;
import org.rhm.util.CyberUtil;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public record EngineeringCraftRecipe(List<ItemStack> ingredients, ItemStack blueprint,
                                     ItemStack output,
                                     boolean useBlueprint) implements Recipe<EngineeringCraftRecipe.EngineeringCraftInput> {
    public EngineeringCraftRecipe(List<ItemStack> ingredients, ItemStack blueprint, ItemStack output, Optional<Boolean> useBlueprint) {
        this(ingredients, blueprint, output, useBlueprint.orElse(false));
    }

    public EngineeringCraftRecipe(List<ItemStack> ingredients, ItemStack blueprint, ItemStack output, boolean useBlueprint) {
        this.ingredients = ingredients;
        this.blueprint = blueprint;
        this.output = output;
        this.useBlueprint = useBlueprint;
}

    @Override
    public boolean matches(@NotNull EngineeringCraftInput input, @NotNull Level world) {
        if (blueprint == null || blueprint.isEmpty() || (input.blueprint != null && !input.blueprint.isEmpty() &&
            CyberUtil.getStringId(BuiltInRegistries.ITEM.getKey(input.blueprint().get(ComponentRegistry.BLUEPRINT_RESULT).getItem()))
                .equals(CyberUtil.getStringId(BuiltInRegistries.ITEM.getKey(blueprint.get(ComponentRegistry.BLUEPRINT_RESULT).getItem()))))) {

            for (ItemStack ingredient : ingredients) {
                boolean foundMatch = false;
                Iterator<ItemStack> iterator = Arrays.stream(input.items.clone()).iterator();

                while (iterator.hasNext()) {
                    ItemStack inputItem = iterator.next();

                    if (ItemStack.isSameItemSameComponents(ingredient, inputItem) && inputItem.getCount() >= ingredient.getCount()) {
                        foundMatch = true;
                        break;
                    }
                }

                if (!foundMatch) return false;
            }

            return true;
        }

        return false;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull EngineeringCraftInput input, HolderLookup.@NotNull Provider lookup) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.@NotNull Provider registriesLookup) {
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return EngineeringCraftSerializer.INSTANCE;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<EngineeringCraftRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "engineering_craft";

        private Type() {
        }
    }

    public record EngineeringCraftInput(ItemStack[] items, ItemStack blueprint) implements RecipeInput {
        @Override
        public @NotNull ItemStack getItem(int slot) {
            if (slot > size()) {
                throw new IllegalArgumentException("No item for index " + slot);
            } else {
                return this.items[slot];
            }
        }

        @Override
        public int size() {
            return items.length;
        }
    }

}
