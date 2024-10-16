package org.rhm.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.rhm.CyberRewaredMod;
import org.rhm.api.ICyberware;
import org.rhm.api.IDeconstructable;
import org.rhm.api.IScannable;
import org.rhm.registries.ComponentRegistry;
import org.rhm.util.CyberUtil;

import java.util.List;

public abstract class CyberItem extends Item implements ICyberware, IScannable, IDeconstructable {
    public static final String SCAVENGED_KEY = CyberRewaredMod.MOD_ID + "item.scavenged";
    public static final String MANUFACTURED_KEY = CyberRewaredMod.MOD_ID + "item.manufactured";
    public static final String SLOT_KEY = CyberRewaredMod.MOD_ID + ".item.slot";
    public static final String TOLERANCE_KEY = CyberRewaredMod.MOD_ID + ".item.toleranceCost";
    public static final String POWER_KEY = CyberRewaredMod.MOD_ID + ".item.power";

    public CyberItem(Properties settings) {
        super(settings.component(ComponentRegistry.SCAVENGED, false));
    }

    @Override
    public ItemStack getScanResult() {
        return CyberUtil.getBlueprintWithItem(this);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        if (getPowerRequirement() > 0) {
            tooltipComponents.add(
                Component.translatable(POWER_KEY,getPowerRequirement()).withStyle(ChatFormatting.GREEN)
            );
        }
        tooltipComponents.add(
            Component.translatable(TOLERANCE_KEY,getEssenceCost()).withStyle(ChatFormatting.DARK_PURPLE)
        );
        if (getSlot() != null) {
            tooltipComponents.add(
                Component.translatable(SLOT_KEY,CyberUtil.toCamelCase(getSlot().name())).withStyle(ChatFormatting.RED)
            );
        }
        if (stack.has(ComponentRegistry.SCAVENGED)) {
            tooltipComponents.add(Component.translatable(
                Boolean.TRUE.equals(stack.get(ComponentRegistry.SCAVENGED)) ? SCAVENGED_KEY : MANUFACTURED_KEY
            ).withStyle(ChatFormatting.GRAY));
        }
    }
}
