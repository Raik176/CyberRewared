package org.rhm.gui;

import commonnetwork.api.Dispatcher;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;
import org.rhm.CyberRewaredMod;
import org.rhm.network.EngineeringTableSmashPacket;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class EngineeringTableScreen extends AbstractContainerScreen<EngineeringTableScreenHandler> {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(CyberRewaredMod.MOD_ID, "textures/gui/engineering_table.png");
    public static final ResourceLocation ADDON_TEXTURE = ResourceLocation.fromNamespaceAndPath(CyberRewaredMod.MOD_ID, "gui_addon");
    public static final ResourceLocation BUTTON_TEXTURE = ResourceLocation.fromNamespaceAndPath(CyberRewaredMod.MOD_ID, "engineering_table/hammer");
    public static final ResourceLocation BUTTON_CLICKED_TEXTURE = ResourceLocation.fromNamespaceAndPath(CyberRewaredMod.MOD_ID, "engineering_table/hammer_selected");

    public static final MutableComponent SMASH_TEXT = Component.translatable(CyberRewaredMod.MOD_ID + ".gui.smash");
    public static final MutableComponent INSERT_PAPER_TEXT = Component.translatable(CyberRewaredMod.MOD_ID + ".gui.insertPaper");
    public static final MutableComponent INSERT_SALVAGE_TEXT = Component.translatable(CyberRewaredMod.MOD_ID + ".gui.insertSalvage");
    public static final MutableComponent INSERT_BLUEPRINT_TEXT = Component.translatable(CyberRewaredMod.MOD_ID + ".gui.insertBlueprint");
    public static final String SMASH_TEXT_CHANCE_KEY = CyberRewaredMod.MOD_ID + ".gui.smash_chance";

    public float chance = 0;

    private final Button button;

    public EngineeringTableScreen(EngineeringTableScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);

        button = new Button(0, 0, 21, 21, SMASH_TEXT, (widget) -> {
        }, Supplier::get) {
            boolean pressed = false;

            @Override
            protected void renderWidget(@NotNull GuiGraphics context, int mouseX, int mouseY, float delta) {
                context.blitSprite(
                    pressed ? BUTTON_CLICKED_TEXTURE : BUTTON_TEXTURE,
                    this.getX(), this.getY(),
                    this.width, this.height
                );
            }

            @Override
            public void onPress() {
                pressed = true;
                // TODO: add craft all functionality
                Dispatcher.sendToServer(new EngineeringTableSmashPacket(false));
                super.onPress();
            }

            @Override
            public void onRelease(double mouseX, double mouseY) {
                pressed = false;
                super.onRelease(mouseX, mouseY);
            }
        };
        this.addRenderableOnly(button);
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
    protected void renderBg(GuiGraphics context, float delta, int mouseX, int mouseY) {
        context.blit(TEXTURE, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);
        button.setPosition((this.width - this.imageWidth) / 2 + 39, (this.height - this.imageHeight) / 2 + 34);
    }

    @Override
    protected void renderTooltip(@NotNull GuiGraphics context, int x, int y) {
        super.renderTooltip(context, x, y);
        if (this.button.isHovered()) {
            List<Component> tooltip = new ArrayList<>(List.of(SMASH_TEXT));
            if (chance != 0)
                tooltip.add(Component.translatable(SMASH_TEXT_CHANCE_KEY, chance));
            context.renderComponentTooltip(this.font, tooltip, x, y);
        }
        if (hoveredSlot != null && !hoveredSlot.hasItem()) {
            if (hoveredSlot == menu.getPaperSlot()) context.renderTooltip(this.font, INSERT_PAPER_TEXT, x, y);
            if (hoveredSlot == menu.getSalvageSlot()) {
                context.renderTooltip(this.font, INSERT_SALVAGE_TEXT, x, y);
            }
            if (hoveredSlot == menu.getBlueprintSlot()) {
                context.renderTooltip(this.font, INSERT_BLUEPRINT_TEXT, x, y);
            }
        }
    }

    @Override
    public void render(@NotNull GuiGraphics context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        renderTooltip(context, mouseX, mouseY);
    }
}
