package org.rhm.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import org.rhm.util.CyberUtil;

import java.util.List;

public class ModBlockItem extends BlockItem {
    public ModBlockItem(Block block) {
        super(block, new Item.Properties());
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
        super.appendHoverText(stack, context, tooltip, type);
        tooltip.addAll(CyberUtil.splitNewlineComponent(
            Component.translatable(getDescriptionId() + ".ttp"),
            ChatFormatting.GRAY)
        );
    }
}
