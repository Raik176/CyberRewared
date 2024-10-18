package org.rhm.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.jetbrains.annotations.NotNull;
import org.rhm.CyberRewaredMod;
import org.rhm.util.config.Config;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class SurgeryScreen extends AbstractContainerScreen<SurgeryScreenHandler> {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(CyberRewaredMod.MOD_ID, "textures/gui/robosurgeon.png");
    private final Inventory playerInv;
    private int centerX;
    private int centerY;
    private InterfaceButton interfaceButton;

    public SurgeryScreen(SurgeryScreenHandler menu, Inventory playerInventory, Component ignoredTitle) {
        super(menu, playerInventory, Component.empty());
        this.playerInv = playerInventory;
        imageWidth = 175;
        imageHeight = 222;
    }

    @Override
    protected void init() {
        super.init();
        recalculateCenters();
        addRenderableWidget(interfaceButton = new InterfaceButton(leftPos + imageWidth - 5, topPos + 5, (widget) -> {

        }, InterfaceButton.Type.INDEX));
    }

    @Override
    public void resize(@NotNull Minecraft minecraft, int width, int height) {
        super.resize(minecraft, width, height);
        recalculateCenters();
    }

    protected void recalculateCenters() {
        centerX = (this.width - this.imageWidth) / 2;
        centerY = (this.height - this.imageHeight) / 2;
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        String playerName = "_" + playerInv.player.getName().getString().toUpperCase();
        guiGraphics.drawString(this.font,
            playerName,
            (centerX + this.font.width(playerName)) / 2, 115,
            0x1DA9C1, false
        );
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        interfaceButton.mouseMoved(mouseX, mouseY);
        super.mouseMoved(mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        interfaceButton.mouseClicked(mouseX,mouseY, button);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);

        int essence = (int) ((80 * 1F / 100) * 49);
        int criticalEssence = (int) ((Config.getCast(Config.CRITICAL_ESSENCE, Integer.class) * 1F / 100) * 49);
        int warningEssence = criticalEssence; // TODO: make warning essence work

        guiGraphics.blit(TEXTURE, centerX + 5, centerY + 5 + (49 - essence), 176, 61 + (49 - essence), 9, Math.max(0, essence - warningEssence));
        guiGraphics.blit(TEXTURE, centerX + 5, centerY + 5 + (49 - Math.min(warningEssence, essence)), 229, 61 + (49 - Math.min(warningEssence, essence)), 9, Math.max(0, Math.min(warningEssence, essence) - criticalEssence));
        guiGraphics.blit(TEXTURE, centerX + 5, centerY + 5 + (49 - Math.min(criticalEssence, essence)), 220, 61 + (49 - Math.min(criticalEssence, essence)), 9, Math.max(0, Math.min(criticalEssence, essence)));
        guiGraphics.blit(TEXTURE, centerX + 5, centerY + 5, 211, 61, 9, 49 - essence);

        if (interfaceButton != null) interfaceButton.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void render(@NotNull GuiGraphics context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        renderTooltip(context, mouseX, mouseY);
    }

    private static class InterfaceButton extends Button {
        private Type type;

        public InterfaceButton(int x, int y, Consumer<Button> onPress, Type type) {
            super(x, y, type.width, type.height, Component.empty(), onPress::accept, Supplier::get);
            this.type = type;
        }

        public void setType(Type type) {
            this.type = type;
        }

        @Override
        protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            if (!visible) return;
            int x = getX() - type.width;
            float transparency = isHovered ? 0.6f : 0.4f;
            guiGraphics.setColor(1, 1, 1, transparency);
            guiGraphics.blit(
                TEXTURE,
                x, getY(),
                type.left + type.width, type.top,
                type.width, type.height
            );
            guiGraphics.setColor(1,1,1,1);
            /*
            guiGraphics.setColor(1, 1, 1, transparency / 2);
            guiGraphics.blit(
                TEXTURE,
                x, getY(),
                type.left, type.top,
                type.width, type.height
            );
            */
        }

        public enum Type {
            BACK(176, 111, 18, 10),
            INDEX(176, 122, 12, 11);

            private final int left;
            private final int top;
            private final int width;
            private final int height;

            Type(int left, int top, int width, int height) {
                this.left = left;
                this.top = top;
                this.width = width;
                this.height = height;
            }
        }
    }
}
