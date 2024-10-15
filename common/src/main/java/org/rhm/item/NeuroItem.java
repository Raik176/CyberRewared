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
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import org.rhm.registries.PotionRegistry;
import org.rhm.util.IGeneratedModel;

import java.util.List;
import java.util.Objects;

public class NeuroItem extends Item implements IGeneratedModel {

    public NeuroItem() {
        super(new Item.Properties()
            .stacksTo(32)
            .component(DataComponents.POTION_CONTENTS, new PotionContents(PotionRegistry.NEURO_POTION))
        );
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
        tooltip.add(Component.translatable(getDescriptionId() + ".ttp").withStyle(ChatFormatting.BLUE));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        ItemStack stack = user.getItemInHand(hand);
        if (!world.isClientSide && stack.getComponents().has(DataComponents.POTION_CONTENTS)) {
            Objects.requireNonNull(stack.get(DataComponents.POTION_CONTENTS)).forEachEffect(user::addEffect);
            if (!user.isCreative()) stack.shrink(1);
            return InteractionResultHolder.pass(stack);
        } else {
            return super.use(world, user, hand);
        }
    }
}
