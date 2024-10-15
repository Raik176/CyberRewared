package org.rhm.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.NotNull;
import org.rhm.CyberRewaredMod;
import org.rhm.util.CyberUtil;

public class ScannerScreen extends AbstractContainerScreen<ScannerScreenHandler> {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(CyberRewaredMod.MOD_ID, "textures/gui/scanner.png");
    public static final ResourceLocation PAPER_TEXTURE = ResourceLocation.fromNamespaceAndPath(CyberRewaredMod.MOD_ID, "empty_slot_paper");
    public static final String[] SAYINGS = Component.translatable(CyberRewaredMod.MOD_ID + ".gui.scannerSayings").getString().split("\n");

    private static final int TICKS_PER_DOT = 15;
    private final String[] messages;
    private final Player player;
    public long scanTime = 0;
    public long scanTimeMax = 0;
    public float chance = 0;
    private int textTicks = 0;
    private String message = "";

    public ScannerScreen(ScannerScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
        this.player = inventory.player;

        messages = Component.translatable(
            CyberRewaredMod.MOD_ID + ".gui.scannerSayings"
        ).getString().split("\n");
    }

    @Override
    protected void renderBg(GuiGraphics context, float delta, int mouseX, int mouseY) {
        context.blit(TEXTURE, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);

        Slot paperSlot = menu.getPaperSlot();
        if (!paperSlot.hasItem()) {
            context.blitSprite(PAPER_TEXTURE, leftPos + paperSlot.x, topPos + paperSlot.y, 16, 16);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics context, int mouseX, int mouseY) {
        context.drawString(this.font, this.title, 6, 7, 0x1DA9C1, false);
        context.drawString(
            this.font,
            this.playerInventoryTitle,
            this.inventoryLabelX,
            this.inventoryLabelY,
            4210752,
            false
        );
        Component text = Component.translatable(CyberRewaredMod.MOD_ID + ".gui.percent", chance);
        context.drawString(
            this.font,
            text,
            imageWidth - 6 - this.font.width(text),
            7,
            0x1DA9C1,
            false
        );
        if (message != null) {
            context.drawString(this.font, message, 6, 20, 0x1F6D7C, false);
        }

        int progress = (int) Math.ceil(((double) scanTime / scanTimeMax) * 162);
        context.setColor(1, 1, 1, 0.6f);
        context.blit(TEXTURE, 5 + progress, 32, progress, 166, 162 - progress, 9);
        context.blit(TEXTURE, 5, 32, 0, 175, progress, 9);
        context.setColor(1, 1, 1, 1);
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        if (scanTimeMax != 0) {
            if (message == null || message.isEmpty()) getNewMessage();
            scanTime++;
            textTicks++;
            if (textTicks % (TICKS_PER_DOT * 5) == 0) { // new message every 5 dots
                getNewMessage();
                textTicks = 0;
            } else if (textTicks % TICKS_PER_DOT == 0) {
                message += ".";
            }
        } else {
            textTicks = 0;
            message = null;
        }
    }

    private void getNewMessage() {
        message = messages[player.level().getRandom().nextInt(messages.length)];
    }

    @Override
    protected void renderTooltip(@NotNull GuiGraphics context, int x, int y) {
        super.renderTooltip(context, x, y);
        if (this.hoveredSlot != null && !this.hoveredSlot.hasItem()) {
            if (this.hoveredSlot == menu.getPaperSlot()) {
                context.renderTooltip(this.font, Component.translatable(CyberRewaredMod.MOD_ID + ".gui.insertPaper"), x, y);
            } else if (this.hoveredSlot == menu.getCyberPartSlot()) {
                context.renderTooltip(this.font, Component.translatable(CyberRewaredMod.MOD_ID + ".gui.toScan"), x, y);
            }

        } else if (x >= leftPos + 5 && x <= leftPos + 167 && // Scanning tooltip
            y >= topPos + 32 && y <= topPos + 41) {
            context.renderTooltip(
                this.font,
                scanTimeMax == 0 ? Component.translatable(CyberRewaredMod.MOD_ID + ".gui.notScanning") : Component.translatable(CyberRewaredMod.MOD_ID + ".gui.scanProgress", CyberUtil.prettyTime((scanTimeMax - scanTime) / 20), scanTime, scanTimeMax),
                x, y
            );
        }
    }

    @Override
    public void render(@NotNull GuiGraphics context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        renderTooltip(context, mouseX, mouseY);
    }
}
