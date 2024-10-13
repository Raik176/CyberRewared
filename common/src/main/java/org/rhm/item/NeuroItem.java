package org.rhm.item;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.rhm.registries.PotionRegistry;

import java.util.List;
import java.util.Objects;

public class NeuroItem extends Item {

    public NeuroItem() {
        super(new Item.Settings()
            .maxCount(32)
            .component(DataComponentTypes.POTION_CONTENTS, new PotionContentsComponent(PotionRegistry.NEURO_POTION))
        );
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable(getTranslationKey() + ".ttp").formatted(Formatting.BLUE));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (!world.isClient && stack.getComponents().contains(DataComponentTypes.POTION_CONTENTS)) {
            Objects.requireNonNull(stack.get(DataComponentTypes.POTION_CONTENTS)).forEachEffect(user::addStatusEffect);
            if (!user.isCreative()) stack.decrement(1);
            return TypedActionResult.pass(stack);
        } else {
            return super.use(world, user, hand);
        }
    }
}
