package org.rhm;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import org.rhm.gui.BlueprintArchiveScreen;
import org.rhm.gui.ComponentBoxScreen;
import org.rhm.gui.EngineeringTableScreen;
import org.rhm.gui.ScannerScreen;
import org.rhm.registries.BlockEntityRegistry;
import org.rhm.registries.ItemRegistry;
import org.rhm.registries.ScreenHandlerRegistry;


public class CyberRewaredFabric implements ModInitializer, ClientModInitializer {
    @Override
    public void onInitialize() {
        CyberRewaredMod.init();

        // TODO: make this less janky
        BlockEntityRegistry.BLOCK_ENTITY_RAW.forEach((id, entry) -> {
            BlockEntityType<?> blockEntityType = BlockEntityType.Builder.create(entry.getKey()::create, entry.getValue()).build();
            Registry.register(Registries.BLOCK_ENTITY_TYPE, id, blockEntityType);
            BlockEntityRegistry.BLOCK_ENTITY_TYPES.put(id, blockEntityType);
        });
        ScreenHandlerRegistry.SCREEN_HANDLER_RAW.forEach((id, factory) -> {
            ScreenHandlerType<? extends ScreenHandler> screenHandlerType = new ScreenHandlerType<>(factory::create, FeatureFlags.DEFAULT_ENABLED_FEATURES);
            Registry.register(Registries.SCREEN_HANDLER, id, screenHandlerType);
            ScreenHandlerRegistry.SCREEN_HANDLER_TYPES.put(id, screenHandlerType);
        });
        Registry.register(Registries.ITEM_GROUP, CyberRewaredMod.ITEM_GROUP_KEY, FabricItemGroup.builder()
            .icon(CyberRewaredMod.ITEM_GROUP::getIcon)
            .displayName(CyberRewaredMod.ITEM_GROUP.getDisplayName())
            .entries(((displayContext, entries) -> {
                ItemRegistry.modItems.forEach(entries::add);
            })).build());
    }

    @Override
    public void onInitializeClient() {
        HandledScreens.register(ScreenHandlerRegistry.SCANNER.get(), ScannerScreen::new);
        HandledScreens.register(ScreenHandlerRegistry.BLUEPRINT_ARCHIVE.get(), BlueprintArchiveScreen::new);
        HandledScreens.register(ScreenHandlerRegistry.COMPONENT_BOX.get(), ComponentBoxScreen::new);
        HandledScreens.register(ScreenHandlerRegistry.ENGINEERING_TABLE.get(), EngineeringTableScreen::new);

        CyberRewaredMod.initClient();
    }
}
