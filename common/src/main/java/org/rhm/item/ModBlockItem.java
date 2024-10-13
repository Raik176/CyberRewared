package org.rhm.item;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.rhm.api.IScannable;

import java.util.List;

public class ModBlockItem extends BlockItem implements IScannable {
    public ModBlockItem(Block block) {
        super(block, new Item.Settings());
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        for (String s : Text.translatable(getTranslationKey() + ".ttp").getString().split("\n")) {
            tooltip.add(Text.literal(s).formatted(Formatting.GRAY));
        }
    }

    @Override
    public ItemStack getResult() {
        return new ItemStack(Items.DIAMOND);
    }

    @Override
    public int getScanTime() {
        return 300;
    }
}
