package org.rhm.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public record EngineeringSmashRecipe(Ingredient cyberware,
                                     List<ItemStack> output) implements Recipe<SingleRecipeInput> {
    public EngineeringSmashRecipe(Ingredient cyberware, List<ItemStack> output) {
        this.cyberware = cyberware;
        this.output = Collections.unmodifiableList(output.subList(0, Math.min(4, output.size())));
    }

    @Override
    public boolean matches(SingleRecipeInput input, @NotNull Level world) {
        return cyberware.test(input.item());
    }

    public List<ItemStack> craftRecipe(SingleRecipeInput input) {
        input.item().shrink(1);
        return output.stream().toList();
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull SingleRecipeInput input, HolderLookup.@NotNull Provider lookup) {
        return Items.AIR.getDefaultInstance();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.@NotNull Provider registriesLookup) {
        return Items.AIR.getDefaultInstance();
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return EngineeringSmashSerializer.INSTANCE;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<EngineeringSmashRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "engineering_smash";

        private Type() {
        }
    }
}
