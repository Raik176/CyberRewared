package org.rhm.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import org.rhm.CyberRewaredMod;

public class ComponentBoxScreen extends AbstractContainerScreen<ComponentBoxScreenHandler> {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(CyberRewaredMod.MOD_ID, "textures/gui/component_box.png");
    public static final ResourceLocation COMPONENT_TEXTURE = ResourceLocation.fromNamespaceAndPath(CyberRewaredMod.MOD_ID, "empty_slot_component");

    public ComponentBoxScreen(ComponentBoxScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    @Override
    protected void renderBg(GuiGraphics context, float delta, int mouseX, int mouseY) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        int lol = ComponentBoxScreenHandler.SLOT_COUNT * 2;
        context.blit(TEXTURE, i, j, 0, 0, this.imageWidth, lol + 17);
        context.blit(TEXTURE, i, j + lol + 17, 0, 126, this.imageWidth, 96);

        for (Slot slot : menu.getComponentSlots()) {
            if (slot != null && !slot.hasItem()) {
                context.blitSprite(COMPONENT_TEXTURE, leftPos + slot.x, topPos + slot.y, 16, 16);
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
            19 + ComponentBoxScreenHandler.SLOT_COUNT * 2,
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
