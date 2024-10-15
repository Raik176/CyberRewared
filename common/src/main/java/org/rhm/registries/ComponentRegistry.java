package org.rhm.registries;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.rhm.CyberRewaredMod;

public class ComponentRegistry {
    public static final DataComponentType<Boolean> IS_RIGHT = register(
        "is_right",
        DataComponentType.<Boolean>builder().persistent(Codec.BOOL).build()
    );
    public static final DataComponentType<Boolean> SCAVENGED = register(
        "scavenged",
        DataComponentType.<Boolean>builder().persistent(Codec.BOOL).build()
    );
    public static final DataComponentType<Integer> XP_STORED = register(
        "xp_stored",
        DataComponentType.<Integer>builder().persistent(Codec.INT).build()
    );
    public static final DataComponentType<ItemStack> BLUEPRINT_RESULT = register(
        "blueprint_result",
        DataComponentType.<ItemStack>builder().persistent(ItemStack.SINGLE_ITEM_CODEC).build()
    );
    // TODO: WIP
    public static final DataComponentType<Boolean> BLUEPRINT_SHOW_RECIPE = register(
        "blueprint_show_recipe",
        DataComponentType.<Boolean>builder().persistent(Codec.BOOL).build()
    );

    public static <T> DataComponentType<T> register(String path, DataComponentType<T> component) {
        CyberRewaredMod.componentRegisterFunc.accept(ResourceLocation.fromNamespaceAndPath(CyberRewaredMod.MOD_ID, path), component);
        return component;
    }

    public static void initialize() {

    }
}
