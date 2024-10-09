package org.rhm.registries;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.rhm.CyberRewaredMod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemRegistry {
    public static List<Item> modItems = new ArrayList<>();

    public static Item register(String path, Item entry) {
        modItems.add(entry);
        return Registry.register(Registries.ITEM, Identifier.of(CyberRewaredMod.MOD_ID, path), entry);
    }

    public static void initialize() {
        modItems = Collections.unmodifiableList(modItems);
    }
}
