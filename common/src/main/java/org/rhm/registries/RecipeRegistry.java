package org.rhm.registries;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import org.rhm.CyberRewaredMod;
import org.rhm.recipe.EngineeringCraftRecipe;
import org.rhm.recipe.EngineeringCraftSerializer;
import org.rhm.recipe.EngineeringSmashRecipe;
import org.rhm.recipe.EngineeringSmashSerializer;

public class RecipeRegistry {

    public static final RecipeType<EngineeringSmashRecipe> SMASHING_TYPE = register(
        EngineeringSmashRecipe.Type.ID,
        EngineeringSmashRecipe.Type.INSTANCE,
        EngineeringSmashSerializer.INSTANCE
    );
    public static final RecipeType<EngineeringCraftRecipe> CRAFTING_TYPE = register(
        EngineeringCraftRecipe.Type.ID,
        EngineeringCraftRecipe.Type.INSTANCE,
        EngineeringCraftSerializer.INSTANCE
    );

    @SuppressWarnings("unchecked")
    public static <T extends Recipe<?>> RecipeType<T> register(String path, RecipeType<T> type, RecipeSerializer<T> serializer) {
        CyberRewaredMod.recipeRegisterFunc.accept(Identifier.of(CyberRewaredMod.MOD_ID, path), (RecipeType<Recipe<?>>) type, (RecipeSerializer<Recipe<?>>) serializer);
        return type;
    }

    public static void initialize() {

    }
}
