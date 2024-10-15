package org.rhm.integration.jei;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.placement.HorizontalAlignment;
import mezz.jei.api.gui.placement.IPlaceable;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.rhm.CyberRewaredMod;
import org.rhm.api.IScannable;
import org.rhm.block.entity.ScannerBlockEntity;
import org.rhm.gui.ScannerScreen;

public class ScanningCategory implements IRecipeCategory<ShapelessRecipe> {
    private final IGuiHelper helper;
    private final IDrawable icon;
    private final int BG_WIDTH = 151;
    private final int BG_HEIGHT = 30;
    private final MutableComponent title = Component.translatable(CyberRewaredMod.MOD_ID + ".jei.scan");

    public ScanningCategory(IGuiHelper guiHelper) {
        this.helper = guiHelper;
        this.icon = CWJEIPlugin.getScanningIcon(guiHelper);
    }

    @Override
    public @NotNull RecipeType<ShapelessRecipe> getRecipeType() {
        return CWJEIPlugin.SCANNING_TYPE;
    }

    @Override
    public void draw(@NotNull ShapelessRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        helper.createDrawable(ScannerScreen.TEXTURE, 12, 49, BG_WIDTH, BG_HEIGHT)
            .draw(guiGraphics);
    }

    @Override
    public @NotNull Component getTitle() {
        return title;
    }

    @Override
    public int getHeight() {
        return BG_HEIGHT;
    }

    @Override
    public int getWidth() {
        return BG_WIDTH;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull ShapelessRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addInputSlot(3, 4).addIngredients(recipe.getIngredients().get(1)).setSlotName("paper");

        builder.addInputSlot(23, 4).addIngredients(recipe.getIngredients().getFirst());
        builder.addOutputSlot(129, 8).addItemStack(recipe.getResultItem(null));
    }

    @Override
    @SuppressWarnings("rawtypes")
    public void createRecipeExtras(IRecipeExtrasBuilder builder, ShapelessRecipe recipe, @NotNull IFocusGroup focuses) {
        IScannable scannable = (IScannable) recipe.getIngredients().getFirst().getItems()[0].getItem();
        long maxTicks = scannable.getScanTime() == -1 ? ScannerBlockEntity.TICKS_PER_OPERATION : scannable.getScanTime();
        int arrowX = 41;

        IPlaceable arrow = builder.addDrawable(new IDrawable() {
            // pfft who needs performance
            int ticks = 0;

            @Override
            public int getWidth() {
                return 82;
            }

            @Override
            public int getHeight() {
                return 11;
            }

            @Override
            public void draw(GuiGraphics guiGraphics, int xOffset, int yOffset) {
                ticks += 12;
                int filledWidth = (int) ((float) Math.min(ticks, maxTicks) / maxTicks * getWidth());
                helper.createDrawable(ScannerScreen.TEXTURE, 0, 184, filledWidth, getHeight()).draw(guiGraphics, xOffset, yOffset);
                if (ticks >= maxTicks) ticks = 0;
            }
        }).setPosition(arrowX, 7);

        Component timeString = Component.translatable("gui.jei.category.smelting.time.seconds", maxTicks / 20);
        builder.addText(timeString, arrow.getWidth() / 2, 10)
            .setPosition(arrowX + arrow.getWidth() / 2 - 24, 15)
            .setTextAlignment(HorizontalAlignment.CENTER)
            .setColor(0xFF808080);
    }
}
