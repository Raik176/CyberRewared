package org.rhm.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;
import org.rhm.CyberRewaredMod;
import org.rhm.registries.ComponentRegistry;

import java.util.List;
import java.util.Objects;

public class BlueprintItem extends Item {
    private final Component EMPTY_COMPONENT = Component.translatable("item." + CyberRewaredMod.MOD_ID + ".blueprint.empty");

    public BlueprintItem() {
        super(new Properties()
            .component(ComponentRegistry.BLUEPRINT_RESULT, null) // If we don't use null neoforge will complain
            .component(ComponentRegistry.BLUEPRINT_SHOW_RECIPE, true)
        );
    }

    @Override
    public @NotNull Component getName(ItemStack stack) {
        String subkey;
        if (stack.has(ComponentRegistry.BLUEPRINT_RESULT)) {
            ItemStack stack1 = stack.get(ComponentRegistry.BLUEPRINT_RESULT);
            if (stack1 == null || stack1.isEmpty()) {
                subkey = EMPTY_COMPONENT.getString();
            } else {
                subkey = stack1.getItem().getName(stack1).getString();
            }
        } else {
            subkey = EMPTY_COMPONENT.getString();

        }
        return Component.translatable(getDescriptionId(), subkey);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltip, @NotNull TooltipFlag type) {
        super.appendHoverText(stack, context, tooltip, type);
        if (!stack.has(ComponentRegistry.BLUEPRINT_RESULT)
            || stack.get(ComponentRegistry.BLUEPRINT_RESULT) == null
            || Objects.requireNonNull(stack.get(ComponentRegistry.BLUEPRINT_RESULT)).isEmpty()) {
            tooltip.add(Component.translatable(getDescriptionId() + ".empty_ttp").withStyle(ChatFormatting.GRAY));
        }
    }
}
