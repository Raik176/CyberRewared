package org.rhm.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.rhm.CyberRewaredMod;

import java.util.function.Supplier;

public class EngineeringTableScreen extends HandledScreen<EngineeringTableScreenHandler> {
    public static final Identifier TEXTURE = Identifier.of(CyberRewaredMod.MOD_ID, "textures/gui/engineering_table.png");
    public static final Identifier BUTTON_TEXTURE = Identifier.of(CyberRewaredMod.MOD_ID, "engineering_table/hammer");
    public static final Identifier BUTTON_CLICKED_TEXTURE = Identifier.of(CyberRewaredMod.MOD_ID, "engineering_table/hammer_selected");

    public static final MutableText SMASH_TEXT = Text.translatable(CyberRewaredMod.MOD_ID + ".gui.smash");
    public static final MutableText INSERT_PAPER_TEXT = Text.translatable(CyberRewaredMod.MOD_ID + ".gui.insertPaper");
    public static final MutableText INSERT_SALVAGE_TEXT = Text.translatable(CyberRewaredMod.MOD_ID + ".gui.insertSalvage");
    public static final MutableText INSERT_BLUEPRINT_TEXT = Text.translatable(CyberRewaredMod.MOD_ID + ".gui.insertBlueprint");


    private final ButtonWidget button;

    public EngineeringTableScreen(EngineeringTableScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);

        button = new ButtonWidget(0, 0, 21, 21, SMASH_TEXT, (widget) -> {
        }, Supplier::get) {
            boolean pressed = false;

            @Override
            protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
                context.drawGuiTexture(
                    pressed ? BUTTON_CLICKED_TEXTURE : BUTTON_TEXTURE,
                    this.getX(), this.getY(),
                    this.width, this.height
                );
            }

            @Override
            public void onPress() {
                pressed = true;
                super.onPress();
            }

            @Override
            public void onRelease(double mouseX, double mouseY) {
                pressed = false;
                super.onRelease(mouseX, mouseY);
            }
        };
        this.addDrawable(button);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.button.mouseClicked(mouseX, mouseY, button);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.button.mouseReleased(mouseX, mouseY, button);
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        context.drawTexture(TEXTURE, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);
        button.setPosition((this.width - this.backgroundWidth) / 2 + 39, (this.height - this.backgroundHeight) / 2 + 34);
    }

    @Override
    protected void drawMouseoverTooltip(DrawContext context, int x, int y) {
        super.drawMouseoverTooltip(context, x, y);
        if (this.button.isHovered()) {
            context.drawTooltip(this.textRenderer, SMASH_TEXT, x, y);
        }
        if (focusedSlot != null && !focusedSlot.hasStack()) {
            if (focusedSlot == handler.getPaperSlot()) context.drawTooltip(this.textRenderer, INSERT_PAPER_TEXT, x, y);
            if (focusedSlot == handler.getSalvageSlot()) {
                context.drawTooltip(this.textRenderer, INSERT_SALVAGE_TEXT, x, y);
            }
            if (focusedSlot == handler.getBlueprintSlot()) {
                context.drawTooltip(this.textRenderer, INSERT_BLUEPRINT_TEXT, x, y);
            }
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }
}
