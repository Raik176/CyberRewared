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
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.rhm.CyberRewaredMod;
import org.rhm.recipe.EngineeringSmashRecipe;

// SO MUCH redefining :(
public class SmashingCategory implements IRecipeCategory<EngineeringSmashRecipe> {
    private final IDrawable icon;
    private final IDrawable background;
    private final MutableText TITLE = Text.translatable(CyberRewaredMod.MOD_ID + ".jei.smash");
    private final MutableText PAPER_TTP = Text.translatable(CyberRewaredMod.MOD_ID + ".jei.paper_ttp");


    public SmashingCategory(IGuiHelper guiHelper) {
        this.icon = CWJEIPlugin.getEngineeringIcon(guiHelper);
        this.background = CWJEIPlugin.getEngineeringBackground(guiHelper);
    }

    @Override
    public @NotNull RecipeType<EngineeringSmashRecipe> getRecipeType() {
        return CWJEIPlugin.SMASHING_TYPE;
    }

    @Override
    public @NotNull Text getTitle() {
        return TITLE;
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
    public void draw(@NotNull EngineeringSmashRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull DrawContext guiGraphics, double mouseX, double mouseY) {
        background.draw(guiGraphics);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, EngineeringSmashRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addInputSlot(1, 4).addIngredients(recipe.cyberware());
        builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 1, 37);
        /*
        builder.addInputSlot(1, 37).addIngredients(Ingredient.ofItems(Items.PAPER, Items.AIR)).addRichTooltipCallback((sv, ttp) -> {
            ttp.addAll(Arrays.stream(PAPER_TTP.getString().split("\n")).map(StringVisitable::plain).collect(Collectors.toList()));
        });
         */
        builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 101, 37);
        builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 131, 5);
        int slotIndex = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                IRecipeSlotBuilder rsb = builder.addOutputSlot(57 + j * 18, 1 + i * 18);
                if (recipe.output().size() > slotIndex) {
                    rsb.addItemStack(recipe.output().get(slotIndex));
                }
                slotIndex++;
            }
        }
    }
}
