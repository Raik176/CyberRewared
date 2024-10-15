package org.rhm.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;
import org.rhm.CyberRewaredMod;

public class SurgeryScreen extends AbstractContainerScreen<SurgeryScreenHandler> {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(CyberRewaredMod.MOD_ID, "textures/gui/robosurgeon.png");

    private int centerX;
    private final Inventory playerInv;

    public SurgeryScreen(SurgeryScreenHandler menu, Inventory playerInventory, Component ignoredTitle) {
        super(menu, playerInventory, Component.empty());
        this.playerInv = playerInventory;
        imageWidth = 175;
        imageHeight = 222;
    }

    @Override
    protected void init() {
        super.init();
        centerX = (this.width - this.imageWidth) / 2;
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        String playerName = "_" + playerInv.player.getName().getString().toUpperCase();
        guiGraphics.drawString(this.font,
            playerName,
            (centerX + this.font.width(playerName))/2, 115,
            0x1DA9C1, false
        );
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);
    }
}
