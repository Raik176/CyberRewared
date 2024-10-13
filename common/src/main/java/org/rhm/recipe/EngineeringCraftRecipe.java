package org.rhm.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.input.RecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;

public class EngineeringCraftRecipe implements Recipe<EngineeringCraftRecipe.EngineeringCraftInput> {
    private final ItemStack output;
    private final Ingredient blueprint;
    private final List<Ingredient> ingredients;

    public EngineeringCraftRecipe(List<Ingredient> ingredients, ItemStack output) {
        this(ingredients, Ingredient.EMPTY, output);
    }
    public EngineeringCraftRecipe(List<Ingredient> ingredients, Ingredient blueprint, ItemStack output) {
        this.ingredients = Collections.unmodifiableList(ingredients.subList(0,Math.min(4,ingredients.size())));
        this.blueprint = blueprint;
        this.output = output;
    }


    public List<Ingredient> ingredients() {
        return ingredients;
    }
    public Ingredient blueprint() {
        return blueprint;
    }

    public ItemStack output() {
        return output;
    }


    @Override
    public boolean matches(EngineeringCraftInput input, World world) {
        return false;
    }

    @Override
    public ItemStack craft(EngineeringCraftInput input, RegistryWrapper.WrapperLookup lookup) {
        return null;
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResult(RegistryWrapper.WrapperLookup registriesLookup) {
        return new ItemStack(Items.DIRT);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return EngineeringCraftSerializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<EngineeringCraftRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "engineering_craft";
        private Type() {
        }
    }

    public record EngineeringCraftInput(ItemStack item) implements RecipeInput {
        @Override
        public ItemStack getStackInSlot(int slot) {
            if (slot != 0) {
                throw new IllegalArgumentException("No item for index " + slot);
            } else {
                return this.item;
            }
        }

        @Override
        public int getSize() {
            return 1;
        }
    }

}
