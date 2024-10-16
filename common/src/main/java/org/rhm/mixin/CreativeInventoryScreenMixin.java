package org.rhm.mixin;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.CreativeModeTab;
import org.rhm.CyberRewaredMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// i should probably find a better way or just leave it out altogether
// TODO: implement
@Mixin(CreativeModeInventoryScreen.class)
public abstract class CreativeInventoryScreenMixin extends EffectRenderingInventoryScreen<CreativeModeInventoryScreen.ItemPickerMenu> {
    @Shadow
    private static CreativeModeTab selectedTab;
    @Unique
    public ResourceLocation cyberRewared$EXPANSION = ResourceLocation.fromNamespaceAndPath(CyberRewaredMod.MOD_ID, "creative_addon");

    public CreativeInventoryScreenMixin(CreativeModeInventoryScreen.ItemPickerMenu screenHandler, Inventory playerInventory, Component text) {
        super(screenHandler, playerInventory, text);
    }

    @Inject(method = "renderBg", at = @At("TAIL"))
    public void drawBackground(GuiGraphics context, float delta, int mouseX, int mouseY, CallbackInfo ci) {
        if (selectedTab == CyberRewaredMod.ITEM_GROUP) {

            context.blitSprite(cyberRewared$EXPANSION, leftPos + imageWidth, topPos, 29, imageHeight);
        }
    }
}
