package org.rhm.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.rhm.registries.ComponentRegistry;
import org.rhm.util.IGeneratedModel;

import java.util.List;

@SuppressWarnings("all")
public class XPCapsuleItem extends Item implements IGeneratedModel {
    public XPCapsuleItem() {
        super(new Properties().component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true).component(ComponentRegistry.XP_STORED, 100));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        ItemStack stack = user.getItemInHand(hand);
        if (!world.isClientSide && stack.getComponents().has(ComponentRegistry.XP_STORED)) {
            user.giveExperiencePoints(stack.get(ComponentRegistry.XP_STORED));
            stack.shrink(1);
            return InteractionResultHolder.pass(stack);
        } else {
            return super.use(world, user, hand);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
        super.appendHoverText(stack, context, tooltip, type);
        tooltip.add(
            Component.translatable(
                getDescriptionId() + ".ttp",
                stack.getComponents().has(ComponentRegistry.XP_STORED) ? stack.get(ComponentRegistry.XP_STORED) : 0
            ).withStyle(ChatFormatting.RED)
        );
    }
}
