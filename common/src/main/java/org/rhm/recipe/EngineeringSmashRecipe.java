package org.rhm.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;

public class EngineeringSmashRecipe implements Recipe<SingleStackRecipeInput> {
    private final Ingredient cyberware;
    private final List<ItemStack> output;

    public EngineeringSmashRecipe(Ingredient cyberware, List<ItemStack> output) {
        this.cyberware = cyberware;
        this.output = Collections.unmodifiableList(output.subList(0,Math.min(4,output.size())));
    }

    public List<ItemStack> output() {
        return output;
    }

    public Ingredient cyberware() {
        return cyberware;
    }

    @Override
    public boolean matches(SingleStackRecipeInput input, World world) {
        return cyberware.test(input.item());
    }

    public List<ItemStack> craftRecipe(SingleStackRecipeInput input) {
        input.item().decrement(1);
        return output.stream().toList();
    }

    @Override
    public ItemStack craft(SingleStackRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        return Items.AIR.getDefaultStack();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResult(RegistryWrapper.WrapperLookup registriesLookup) {
        return Items.AIR.getDefaultStack();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return EngineeringSmashSerializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<EngineeringSmashRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "engineering_smash";
        private Type() {
        }
    }
}
