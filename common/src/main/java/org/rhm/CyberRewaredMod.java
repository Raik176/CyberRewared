package org.rhm;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.rhm.registries.ItemRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CyberRewaredMod {
    public static final String MOD_ID = "cyber-rewared";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final RegistryKey<ItemGroup> ITEM_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(MOD_ID, "item_group"));
    public static final ItemGroup ITEM_GROUP = ItemGroup.create(ItemGroup.Row.TOP, 0)
            .icon(() -> new ItemStack(Items.DIRT))
            .displayName(Text.translatable(CyberRewaredMod.MOD_ID + ".itemGroup"))
            .build();


    public static void init() {


        ItemRegistry.initialize();
    }
}