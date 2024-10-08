package org.rhm;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import org.rhm.registries.ItemRegistry;

public class CyberRewaredFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        CyberRewaredMod.init();
        Registry.register(Registries.ITEM_GROUP, CyberRewaredMod.ITEM_GROUP_KEY, FabricItemGroup.builder()
                .icon(CyberRewaredMod.ITEM_GROUP::getIcon)
                .displayName(CyberRewaredMod.ITEM_GROUP.getDisplayName())
                .entries(((displayContext, entries) -> {
                    ItemRegistry.modItems.forEach(entries::add);
                })).build());
    }
}