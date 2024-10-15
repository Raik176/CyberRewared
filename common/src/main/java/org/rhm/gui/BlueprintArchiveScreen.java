package org.rhm.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import org.rhm.CyberRewaredMod;

public class BlueprintArchiveScreen extends AbstractContainerScreen<BlueprintArchiveScreenHandler> {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(CyberRewaredMod.MOD_ID, "textures/gui/blueprint_archive.png");
    public static final ResourceLocation PAPER_TEXTURE = ResourceLocation.fromNamespaceAndPath(CyberRewaredMod.MOD_ID, "empty_slot_paper");

    public BlueprintArchiveScreen(BlueprintArchiveScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    @Override
    protected void renderBg(GuiGraphics context, float delta, int mouseX, int mouseY) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        int lol = BlueprintArchiveScreenHandler.SLOT_COUNT * 2;
        context.blit(TEXTURE, i, j, 0, 0, this.imageWidth, lol + 17);
        context.blit(TEXTURE, i, j + lol + 17, 0, 126, this.imageWidth, 96);

        for (Slot slot : menu.getBlueprintSlots()) {
            if (slot != null && !slot.hasItem()) {
                context.blitSprite(PAPER_TEXTURE, leftPos + slot.x, topPos + slot.y, 16, 16);
            }
        }
    }

    @Override
    protected void renderLabels(GuiGraphics context, int mouseX, int mouseY) {
        context.drawString(this.font, this.title, 8, 6, 4210752, false);
        context.drawString(
            this.font,
            this.playerInventoryTitle,
            8,
            19 + BlueprintArchiveScreenHandler.SLOT_COUNT * 2,
            4210752,
            false
        );
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        renderTooltip(context, mouseX, mouseY);
    }
}
