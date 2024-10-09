package org.rhm.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.rhm.CyberRewaredMod;

public class BlueprintArchiveScreen extends HandledScreen<BlueprintArchiveScreenHandler> {
    public static final Identifier TEXTURE = Identifier.of(CyberRewaredMod.MOD_ID, "textures/gui/blueprint_archive.png");
    public static final Identifier PAPER_TEXTURE = Identifier.of(CyberRewaredMod.MOD_ID, "empty_slot_paper");

    public BlueprintArchiveScreen(BlueprintArchiveScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        int lol = BlueprintArchiveScreenHandler.SLOT_COUNT * 2;
        context.drawTexture(TEXTURE, i, j, 0, 0, this.backgroundWidth, lol + 17);
        context.drawTexture(TEXTURE, i, j + lol + 17, 0, 126, this.backgroundWidth, 96);

        for (Slot slot : handler.getBlueprintSlots()) {
            if (slot != null && !slot.hasStack()) {
                context.drawGuiTexture(PAPER_TEXTURE, x + slot.x, y + slot.y, 16, 16);
            }
        }
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        context.drawText(this.textRenderer, this.title, 8, 6, 4210752, false);
        context.drawText(
            this.textRenderer,
            this.playerInventoryTitle,
            8,
            19 + BlueprintArchiveScreenHandler.SLOT_COUNT * 2,
            4210752,
            false
        );
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }
}
