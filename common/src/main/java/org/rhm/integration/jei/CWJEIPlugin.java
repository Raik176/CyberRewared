package org.rhm.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import org.jetbrains.annotations.NotNull;
import org.rhm.CyberRewaredMod;
import org.rhm.api.IDeconstructable;
import org.rhm.api.IScannable;
import org.rhm.block.entity.ScannerBlockEntity;
import org.rhm.gui.EngineeringTableScreen;
import org.rhm.gui.ScannerScreen;
import org.rhm.item.CyberItem;
import org.rhm.item.CyberLimbItem;
import org.rhm.item.LimbItem;
import org.rhm.recipe.BlueprintRecipe;
import org.rhm.recipe.EngineeringCraftRecipe;
import org.rhm.recipe.EngineeringSmashRecipe;
import org.rhm.registries.BlockRegistry;
import org.rhm.registries.ComponentRegistry;
import org.rhm.registries.ItemRegistry;
import org.rhm.registries.RecipeRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

@JeiPlugin
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
    // public static final RecipeType<RecipeHolder<BlueprintRecipe>> BLUEPRINT_TYPE = RecipeType.createFromVanilla(RecipeRegistry.BLUEPRINT_TYPE);
    public static final ResourceLocation PLUGIN_ID = ResourceLocation.fromNamespaceAndPath(CyberRewaredMod.MOD_ID, "jei_plugin");
    public static final int BG_WIDTH = 151;
    public static final int BG_HEIGHT = 54;
    // If i don't use suppliers it won't work on forge!
    private static final Supplier<ItemStack> ENGINEERING_ICON = () -> new ItemStack(ItemRegistry.getBlockItem(BlockRegistry.ENGINEERING_TABLE));
    private static final Supplier<ItemStack> SCANNING_ICON = () -> new ItemStack(ItemRegistry.getBlockItem(BlockRegistry.SCANNER));

    public static IDrawable getEngineeringIcon(IGuiHelper helper) {
        return helper.createDrawableItemStack(ENGINEERING_ICON.get());
    }

    public static IDrawable getScanningIcon(IGuiHelper helper) {

        return helper.createDrawableItemStack(SCANNING_ICON.get());
    }

    public static IDrawable getEngineeringBackground(IGuiHelper helper) {
        return helper.createDrawable(EngineeringTableScreen.TEXTURE, 14, 16, BG_WIDTH, BG_HEIGHT);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper helper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new SmashingCategory(helper));
        registration.addRecipeCategories(new CraftingCategory(helper));
        registration.addRecipeCategories(new ScanningCategory(helper));

    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(ENGINEERING_ICON.get(), SMASHING_TYPE);
        registration.addRecipeCatalyst(ENGINEERING_ICON.get(), CRAFTING_TYPE);
        registration.addRecipeCatalyst(SCANNING_ICON.get(), SCANNING_TYPE);
    }

    // Doesn't work?
    @Override
    public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
        registration.getCraftingCategory().addExtension(BlueprintRecipe.class, new BlueprintRecipeExtension());
    }

    @Override
    public void registerItemSubtypes(@NotNull ISubtypeRegistration registration) {
        for (Item modItem : ItemRegistry.modItems) {
            if (modItem instanceof LimbItem item) {
                registration.registerSubtypeInterpreter(item, LimbSubtypeInterpreter.INSTANCE);
            } else if (modItem instanceof CyberLimbItem item) {
                registration.registerSubtypeInterpreter(item, CyberLimbSubtypeInterpreter.INSTANCE);
            } else if (modItem instanceof CyberItem item) {
                registration.registerSubtypeInterpreter(item, CyberItemSubtypeInterpreter.INSTANCE);
            }
        }
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration) {
        List<EngineeringSmashRecipe> smashingRecipes = new ArrayList<>();
        List<ShapelessRecipe> scanningRecipes = new ArrayList<>();
        for (Item item : BuiltInRegistries.ITEM.stream().toList()) {
            if (item instanceof IDeconstructable deconstructable) {
                smashingRecipes.add(new EngineeringSmashRecipe(
                    Ingredient.of(item),
                    Arrays.stream(deconstructable.getDestructComponents()).toList()
                ));
            }
            if (item instanceof IScannable scannable) {
                if (item instanceof CyberLimbItem cli) {
                    NonNullList<Ingredient> ingredients = NonNullList.withSize(2, Ingredient.EMPTY);
                    ItemStack stack = new ItemStack(cli);
                    stack.set(ComponentRegistry.IS_RIGHT, true);
                    ingredients.set(0, Ingredient.of(stack));
                    if (scannable.scanNeedsPaper()) ingredients.set(1, Ingredient.of(ScannerBlockEntity.PAPER_ITEM));
                    scanningRecipes.add(new ShapelessRecipe(
                        "", CraftingBookCategory.MISC, scannable.getScanResult(), ingredients)
                    );
                }
                NonNullList<Ingredient> ingredients = NonNullList.withSize(2, Ingredient.EMPTY);
                ingredients.set(0, Ingredient.of(item));
                if (scannable.scanNeedsPaper()) ingredients.set(1, Ingredient.of(ScannerBlockEntity.PAPER_ITEM));
                scanningRecipes.add(new ShapelessRecipe(
                    "", CraftingBookCategory.MISC, scannable.getScanResult(), ingredients)
                );
            }
        }


        registration.addRecipes(SMASHING_TYPE, smashingRecipes);
        registration.addRecipes(SCANNING_TYPE, scanningRecipes);


        assert Minecraft.getInstance().level != null; // i hope this won't backfire
        registration.addRecipes(
            SMASHING_TYPE,
            Minecraft.getInstance().level
                .getRecipeManager().getAllRecipesFor(RecipeRegistry.SMASHING_TYPE)
                .stream().map(RecipeHolder::value).toList()
        );
        registration.addRecipes(
            CRAFTING_TYPE,
            Minecraft.getInstance().level
                .getRecipeManager().getAllRecipesFor(RecipeRegistry.CRAFTING_TYPE)
                .stream().map(RecipeHolder::value).toList()
        );
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        // Crafting arrows
        registration.addRecipeClickArea(EngineeringTableScreen.class, 107, 23, 30, 11, CRAFTING_TYPE);
        registration.addRecipeClickArea(EngineeringTableScreen.class, 121, 34, 3, 17, CRAFTING_TYPE);

        // Smashing arrows
        registration.addRecipeClickArea(EngineeringTableScreen.class, 33, 56, 34, 11, SMASHING_TYPE);
        registration.addRecipeClickArea(EngineeringTableScreen.class, 33, 22, 34, 11, SMASHING_TYPE);

        registration.addRecipeClickArea(ScannerScreen.class, 53, 58, 81, 11, SCANNING_TYPE);
    }

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return PLUGIN_ID;
    }
}
