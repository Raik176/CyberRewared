package org.rhm.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.rhm.CyberRewaredMod;

public class EngineeringTableScreen extends HandledScreen<EngineeringTableScreenHandler> {
    public static final Identifier TEXTURE = Identifier.of(CyberRewaredMod.MOD_ID, "textures/gui/engineering_table.png");
    static final Identifier BUTTON_TEXTURE = Identifier.ofVanilla("engineering_table/hammer");
    static final Identifier BUTTON_HIGHLIGHTED_TEXTURE = Identifier.ofVanilla("engineering_table/hammer_selected");

    private static ButtonWidget button;

    public EngineeringTableScreen(EngineeringTableScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);

        button = new ButtonWidget(x - 39, y - 39, 20, 20, Text.empty(), (widget) -> {
        }, (text) ->
            Text.empty()) {
            @Override
            protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
                context.drawGuiTexture(
                    this.isSelected() ? BUTTON_HIGHLIGHTED_TEXTURE : BUTTON_TEXTURE,
                    this.getX(), this.getY(),
                    this.width, this.height
                );
            }
        };
        this.addDrawable(button);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        context.drawTexture(TEXTURE, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }
}
