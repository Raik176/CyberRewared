package org.rhm.integration.jei;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.rhm.CyberRewaredMod;
import org.rhm.recipe.EngineeringCraftRecipe;

public class CraftingCategory implements IRecipeCategory<EngineeringCraftRecipe> {
    private final IDrawable icon;
    private final IDrawable background;
    private final MutableComponent title = Component.translatable(CyberRewaredMod.MOD_ID + ".jei.craft");

    public CraftingCategory(IGuiHelper guiHelper) {
        this.icon = CWJEIPlugin.getEngineeringIcon(guiHelper);
        this.background = CWJEIPlugin.getEngineeringBackground(guiHelper);
    }

    @Override
    public @NotNull RecipeType<EngineeringCraftRecipe> getRecipeType() {
        return CWJEIPlugin.CRAFTING_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return title;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public int getWidth() {
        return CWJEIPlugin.BG_WIDTH;
    }

    @Override
    public int getHeight() {
        return CWJEIPlugin.BG_HEIGHT;
    }

    @Override
    public void draw(@NotNull EngineeringCraftRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        background.draw(guiGraphics);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, EngineeringCraftRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 1, 4);
        builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 1, 37);
        builder.addInputSlot(101, 37).addItemStack(recipe.blueprint());
        builder.addOutputSlot(131, 5).addItemStack(recipe.output());
        int slotIndex = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                IRecipeSlotBuilder rsb = builder.addInputSlot(57 + j * 18, 1 + i * 18);
                if (recipe.ingredients().size() > slotIndex) {
                    rsb.addItemStack(recipe.ingredients().get(slotIndex));
                }
                slotIndex++;
            }
        }
    }
}
