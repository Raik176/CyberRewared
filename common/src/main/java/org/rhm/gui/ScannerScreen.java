package org.rhm.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.rhm.CyberRewaredMod;

public class ScannerScreen extends HandledScreen<ScannerScreenHandler> {
    public static final Identifier TEXTURE = Identifier.of(CyberRewaredMod.MOD_ID, "textures/gui/scanner.png");
    public static final Identifier PAPER_TEXTURE = Identifier.of(CyberRewaredMod.MOD_ID, "empty_slot_paper");
    public static final String[] SAYINGS = Text.translatable(CyberRewaredMod.MOD_ID + ".gui.scannerSayings").getString().split("\n");

    public ScannerScreen(ScannerScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        context.drawTexture(TEXTURE, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);

        Slot paperSlot = handler.getPaperSlot();
        if (!paperSlot.hasStack()) {
            context.drawGuiTexture(PAPER_TEXTURE, x + paperSlot.x, y + paperSlot.y, 16, 16);
        }
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        context.drawText(this.textRenderer, this.title, 6, 7, 0x1DA9C1, false);
        context.drawText(
            this.textRenderer,
            this.playerInventoryTitle,
            this.playerInventoryTitleX,
            this.playerInventoryTitleY,
            4210752,
            false
        );
        float chance = 0;
        Text text = Text.translatable(CyberRewaredMod.MOD_ID + ".gui.percent", chance);
        context.drawText(
            this.textRenderer,
            text,
            backgroundWidth - 6 - this.textRenderer.getWidth(text),
            7,
            0x1DA9C1,
            false
        );

        int progress = (int) Math.ceil(0.5 * 162);
        context.setShaderColor(1, 1, 1, 0.6f);
        context.drawTexture(TEXTURE, 5 + progress, 32, progress, 166, 162 - progress, 9);
        context.drawTexture(TEXTURE, 5, 32, 0, 175, progress, 9);
        context.setShaderColor(1, 1, 1, 1);
    }

    @Override
    protected void drawMouseoverTooltip(DrawContext context, int x, int y) {
        super.drawMouseoverTooltip(context, x, y);
        if (this.focusedSlot != null && !this.focusedSlot.hasStack()) {
            if (this.focusedSlot == handler.getPaperSlot()) {
                context.drawTooltip(this.textRenderer, Text.translatable(CyberRewaredMod.MOD_ID + ".gui.paper"), x, y);
            } else if (this.focusedSlot == handler.getCyberPartSlot()) {
                context.drawTooltip(this.textRenderer, Text.translatable(CyberRewaredMod.MOD_ID + ".gui.toScan"), x, y);
            }
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }
}
