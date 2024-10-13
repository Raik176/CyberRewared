package org.rhm.item;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.rhm.registries.ComponentRegistry;

import java.util.List;

@SuppressWarnings("all")
public class XPCapsuleItem extends Item {
    public XPCapsuleItem() {
        super(new Settings().component(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true).component(ComponentRegistry.XP_STORED,100));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (!world.isClient && stack.getComponents().contains(ComponentRegistry.XP_STORED)) {
            user.addExperience(stack.get(ComponentRegistry.XP_STORED));
            stack.decrement(1);
            return TypedActionResult.pass(stack);
        } else {
            return super.use(world, user, hand);
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(
            Text.translatable(
                getTranslationKey() + ".ttp",
                stack.getComponents().contains(ComponentRegistry.XP_STORED) ? stack.get(ComponentRegistry.XP_STORED) : 0
            ).formatted(Formatting.RED)
        );
    }
}
