package org.rhm.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.NotNull;
import org.rhm.CyberRewaredMod;
import org.rhm.api.IDeconstructable;
import org.rhm.api.IScannable;
import org.rhm.block.entity.ScannerBlockEntity;
import org.rhm.gui.EngineeringTableScreen;
import org.rhm.gui.ScannerScreen;
import org.rhm.recipe.EngineeringCraftRecipe;
import org.rhm.recipe.EngineeringSmashRecipe;
import org.rhm.registries.BlockRegistry;
import org.rhm.registries.ItemRegistry;
import org.rhm.registries.RecipeRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CWJEIPlugin implements IModPlugin {
    public static final RecipeType<EngineeringSmashRecipe> SMASHING_TYPE = RecipeType.create(
        CyberRewaredMod.MOD_ID,
        "engineering_smashing",
        EngineeringSmashRecipe.class
    );
    public static final RecipeType<EngineeringCraftRecipe> CRAFTING_TYPE = RecipeType.create(
        CyberRewaredMod.MOD_ID,
        "engineering_crafting",
        EngineeringCraftRecipe.class
    );
    // Don't wanna make my own recipe class for this
    public static final RecipeType<ShapelessRecipe> SCANNING_TYPE = RecipeType.create(
        CyberRewaredMod.MOD_ID,
        "scanning",
        ShapelessRecipe.class
    );
    public static final Identifier PLUGIN_ID = Identifier.of(CyberRewaredMod.MOD_ID, "jei_plugin");

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper helper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new SmashingCategory(helper));
        registration.addRecipeCategories(new CraftingCategory(helper));
        registration.addRecipeCategories(new ScanningCategory(helper));
    }

    private static final ItemStack ENGINEERING_ICON = new ItemStack(ItemRegistry.getBlockItem(BlockRegistry.ENGINEERING_TABLE));
    private static final ItemStack SCANNING_ICON = new ItemStack(ItemRegistry.getBlockItem(BlockRegistry.SCANNER));
    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(ENGINEERING_ICON, SMASHING_TYPE);
        registration.addRecipeCatalyst(ENGINEERING_ICON, CRAFTING_TYPE);
        registration.addRecipeCatalyst(SCANNING_ICON, SCANNING_TYPE);
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration) {
        List<EngineeringSmashRecipe> smashingRecipes = new ArrayList<>();
        List<ShapelessRecipe> scanningRecipes = new ArrayList<>();
        for (Item item : Registries.ITEM.stream().toList()) {
            if (item instanceof IDeconstructable deconstructable) {
                smashingRecipes.add(new EngineeringSmashRecipe(
                    Ingredient.ofItems(item),
                    Arrays.stream(deconstructable.getDestructComponents()).toList())
                );
            }
            if (item instanceof IScannable scannable) {

                DefaultedList<Ingredient> ingredients = DefaultedList.ofSize(2, Ingredient.EMPTY);
                ingredients.set(0, Ingredient.ofItems(item));
                if (scannable.needsPaper()) ingredients.set(1,Ingredient.ofItems(ScannerBlockEntity.PAPER_ITEM));
                scanningRecipes.add(new ShapelessRecipe(
                    "", CraftingRecipeCategory.MISC,scannable.getResult(), ingredients)
                );
            }

        }
        registration.addRecipes(SMASHING_TYPE, smashingRecipes);
        registration.addRecipes(SCANNING_TYPE, scanningRecipes);

        assert MinecraftClient.getInstance().world != null; // i hope this won't backfire
        registration.addRecipes(
            SMASHING_TYPE,
            MinecraftClient.getInstance().world
                .getRecipeManager().listAllOfType(RecipeRegistry.SMASHING_TYPE)
                .stream().map(RecipeEntry::value).toList()
        );
        registration.addRecipes(
            CRAFTING_TYPE,
            MinecraftClient.getInstance().world
                .getRecipeManager().listAllOfType(RecipeRegistry.CRAFTING_TYPE)
                .stream().map(RecipeEntry::value).toList()
        );
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        //Crafting arrows
        registration.addRecipeClickArea(EngineeringTableScreen.class, 107, 23, 30, 11, CRAFTING_TYPE);
        registration.addRecipeClickArea(EngineeringTableScreen.class, 121, 34, 3, 17, CRAFTING_TYPE);

        //Smashing arrows
        registration.addRecipeClickArea(EngineeringTableScreen.class, 33, 56, 34, 11, SMASHING_TYPE);
        registration.addRecipeClickArea(EngineeringTableScreen.class, 33, 22, 34, 11, SMASHING_TYPE);

        registration.addRecipeClickArea(ScannerScreen.class, 53, 58, 81, 11, SCANNING_TYPE);
    }

    @Override
    public @NotNull Identifier getPluginUid() {
        return PLUGIN_ID;
    }

    public static IDrawable getEngineeringIcon(IGuiHelper helper) {
        return helper.createDrawableItemStack(ENGINEERING_ICON);
    }
    public static IDrawable getScanningIcon(IGuiHelper helper) {

        return helper.createDrawableItemStack(SCANNING_ICON);
    }
    public static final int BG_WIDTH = 151;
    public static final int BG_HEIGHT = 54;
    public static IDrawable getEngineeringBackground(IGuiHelper helper) {
        return helper.createDrawable(EngineeringTableScreen.TEXTURE, 14, 16, BG_WIDTH, BG_HEIGHT);
    }
}
