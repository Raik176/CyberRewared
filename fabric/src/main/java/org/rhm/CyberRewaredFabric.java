package org.rhm;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.math.BlockPos;
import org.rhm.block.entity.renderer.ScannerBlockEntityRenderer;
import org.rhm.gui.BlueprintArchiveScreen;
import org.rhm.gui.ComponentBoxScreen;
import org.rhm.gui.EngineeringTableScreen;
import org.rhm.gui.ScannerScreen;
import org.rhm.registries.BlockEntityRegistry;
import org.rhm.registries.ItemRegistry;
import org.rhm.registries.ScreenHandlerRegistry;
import org.rhm.util.IEnergyStorage;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.SimpleEnergyStorage;

@SuppressWarnings("unchecked")
public class CyberRewaredFabric implements ModInitializer, ClientModInitializer {
    @Override
    public void onInitialize() {
        CyberRewaredMod.init();

        //loader specific code
        // TODO: make this less janky
        BlockEntityRegistry.BLOCK_ENTITY_RAW.forEach((id, entry) -> {
            BlockEntityType<?> blockEntityType = BlockEntityType.Builder.create(entry.getKey()::create, entry.getValue()).build();
            Registry.register(Registries.BLOCK_ENTITY_TYPE, id, blockEntityType);
            BlockEntityRegistry.BLOCK_ENTITY_TYPES.put(id, blockEntityType);

            // bad code but eh
            // if these ever, for whatever reason desync i have a huge problem
            if (entry.getKey().create(new BlockPos(0, 0, 0), entry.getValue().getDefaultState()) instanceof IEnergyStorage) {
                EnergyStorage.SIDED.registerForBlockEntity((be, dir) -> {
                    //i think this is creating a new storage every tick but it doesn't impact performance
                    //that much so i really don't care about it for now, as im too lazy to keep track of a map
                    //and add/remove storages depending on which blocks are broken or placed
                    SimpleEnergyStorage storage = new SimpleEnergyStorage(be.getCapacity(), be.getMaxIn(), be.getMaxOut()) {
                        @Override
                        protected void onFinalCommit() {
                            super.onFinalCommit();
                            be.onFinal();
                        }

                        @Override
                        public long insert(long maxAmount, TransactionContext transaction) {
                            be.insert(amount);
                            return super.insert(maxAmount,transaction);
                        }

                        @Override
                        public long extract(long maxAmount, TransactionContext transaction) {
                            be.extract(amount);
                            return super.extract(maxAmount, transaction);
                        }
                    };
                    storage.amount = be.getEnergy();
                    return storage;
                }, (BlockEntityType<? extends IEnergyStorage>) blockEntityType);
            }
        });
        ScreenHandlerRegistry.SCREEN_HANDLER_RAW.forEach((id, factory) -> {
            ScreenHandlerType<? extends ScreenHandler> screenHandlerType = new ScreenHandlerType<>(factory::create, FeatureFlags.DEFAULT_ENABLED_FEATURES);
            Registry.register(Registries.SCREEN_HANDLER, id, screenHandlerType);
            ScreenHandlerRegistry.SCREEN_HANDLER_TYPES.put(id, screenHandlerType);
        });
        CyberRewaredMod.ITEM_GROUP = FabricItemGroup.builder()
            .icon(CyberRewaredMod.ITEM_GROUP::getIcon)
            .displayName(CyberRewaredMod.ITEM_GROUP.getDisplayName())
            .entries(((displayContext, entries) -> ItemRegistry.modItems.forEach(entries::add))).build();
        Registry.register(Registries.ITEM_GROUP, CyberRewaredMod.ITEM_GROUP_KEY, CyberRewaredMod.ITEM_GROUP);
    }

    @Override
    public void onInitializeClient() {
        CyberRewaredMod.initClient();

        HandledScreens.register(ScreenHandlerRegistry.SCANNER.get(), ScannerScreen::new);
        HandledScreens.register(ScreenHandlerRegistry.BLUEPRINT_ARCHIVE.get(), BlueprintArchiveScreen::new);
        HandledScreens.register(ScreenHandlerRegistry.COMPONENT_BOX.get(), ComponentBoxScreen::new);
        HandledScreens.register(ScreenHandlerRegistry.ENGINEERING_TABLE.get(), EngineeringTableScreen::new);

        BlockEntityRendererFactories.register(BlockEntityRegistry.SCANNER.get(), ScannerBlockEntityRenderer::new);
    }
}
