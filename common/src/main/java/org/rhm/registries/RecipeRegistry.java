package org.rhm.registries;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import org.rhm.CyberRewaredMod;
import org.rhm.recipe.BlueprintRecipe;
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
    public static final RecipeType<BlueprintRecipe> BLUEPRINT_TYPE = register(
        BlueprintRecipe.Type.ID,
        BlueprintRecipe.Type.INSTANCE,
        BlueprintRecipe.SERIALIZER
    );

    @SuppressWarnings("unchecked")
    public static <T extends Recipe<?>> RecipeType<T> register(String path, RecipeType<T> type, RecipeSerializer<T> serializer) {
        CyberRewaredMod.recipeRegisterFunc.accept(ResourceLocation.fromNamespaceAndPath(CyberRewaredMod.MOD_ID, path), (RecipeType<Recipe<?>>) type, (RecipeSerializer<Recipe<?>>) serializer);
        return type;
    }

    public static void initialize() {

    }
}
