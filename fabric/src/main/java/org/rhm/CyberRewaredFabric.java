package org.rhm;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import org.rhm.block.entity.renderer.ScannerBlockEntityRenderer;
import org.rhm.entity.renderer.CyberzombieEntityRenderer;
import org.rhm.gui.BlueprintArchiveScreen;
import org.rhm.gui.ComponentBoxScreen;
import org.rhm.gui.EngineeringTableScreen;
import org.rhm.gui.ScannerScreen;
import org.rhm.item.LimbItem;
import org.rhm.registries.*;
import org.rhm.util.IEnergyStorage;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.SimpleEnergyStorage;

// TODO: switch to parchment
@SuppressWarnings("unchecked")
public class CyberRewaredFabric implements ModInitializer, ClientModInitializer {
    @Override
    @SuppressWarnings("rawtypes")
    public void onInitialize() {
        CyberRewaredMod.blockEntityRegisterFunc = (id, bef, block) -> {
            BlockEntityType<?> blockEntityType = BlockEntityType.Builder.create(bef::create, block).build();
            Registry.register(Registries.BLOCK_ENTITY_TYPE, id, blockEntityType);

            return blockEntityType;
        };
        CyberRewaredMod.energyStorageRegisterFunc = (bet) -> EnergyStorage.SIDED.registerForBlockEntity((be, dir) -> {
            // i think this is creating a new storage every tick but it doesn't impact performance
            // that much so i really don't care about it for now, as im too lazy to keep track of a map
            // and add/remove storages depending on which blocks are broken or placed
            SimpleEnergyStorage storage = new SimpleEnergyStorage(be.getCapacity(), be.getMaxIn(), be.getMaxOut()) {
                @Override
                protected void onFinalCommit() {
                    super.onFinalCommit();
                    be.onFinal();
                }

                @Override
                public long insert(long maxAmount, TransactionContext transaction) {
                    be.insert(amount);
                    return super.insert(maxAmount, transaction);
                }

                @Override
                public long extract(long maxAmount, TransactionContext transaction) {
                    be.extract(amount);
                    return super.extract(maxAmount, transaction);
                }
            };
            storage.amount = be.getEnergy();
            return storage;
        }, (BlockEntityType<? extends IEnergyStorage>) bet);
        CyberRewaredMod.blockRegisterFunc = (id, block) -> Registry.register(Registries.BLOCK, id, block);
        CyberRewaredMod.itemRegisterFunc = (id, item) -> Registry.register(Registries.ITEM, id, item);
        CyberRewaredMod.screenHandlerRegisterFunc = (id, factory) -> Registry.register(Registries.SCREEN_HANDLER, id, new ScreenHandlerType<>(factory::create, FeatureFlags.DEFAULT_ENABLED_FEATURES));
        CyberRewaredMod.entityRegisterFunc = (id, ef, etb, ab) -> {
            EntityType<? extends Entity> entityType = Registry.register(Registries.ENTITY_TYPE, id, EntityType.Builder.create(ef::create, SpawnGroup.MONSTER)
                .dimensions(etb.getWidth(), etb.getHeight()).eyeHeight(etb.getEyeHeight())
                .passengerAttachments(etb.getPassengerOffsetY()).vehicleAttachment(etb.getVehicleOffsetY())
                .maxTrackingRange(etb.getMaxTrackingRange()).build(id.getPath())
            );
            if (ab != null) {
                FabricDefaultAttributeRegistry.register((EntityType<? extends LivingEntity>) entityType, ab);
            }
            return entityType;
        };
        CyberRewaredMod.recipeRegisterFunc = (id, type, serializer) -> {
            Registry.register(Registries.RECIPE_SERIALIZER, id, serializer);
            Registry.register(Registries.RECIPE_TYPE, id, type);
        };
        CyberRewaredMod.componentRegisterFunc = (id, component) -> {
            Registry.register(
                Registries.DATA_COMPONENT_TYPE,
                id,
                component
            );
        };
        CyberRewaredMod.potionRegisterFunc = (id, effect, duration) -> {
            Potion potion = new Potion(
                id.getPath(),
                new StatusEffectInstance(Registry.registerReference(Registries.STATUS_EFFECT, id, effect), duration)
            );
            return Registry.registerReference(Registries.POTION, id, potion);
        };

        CyberRewaredMod.packetS2CRegisterFunc = (id, codec) -> PayloadTypeRegistry.playS2C().register((CustomPayload.Id) id, (PacketCodec) codec);
        CyberRewaredMod.packetC2SRegisterFunc = (id, codec) -> PayloadTypeRegistry.playC2S().register((CustomPayload.Id) id, (PacketCodec) codec);
        CyberRewaredMod.packetC2SHandlerRegisterFunc = (id, handler) -> {
            ServerPlayNetworking.registerGlobalReceiver(id, ((payload, context) -> {
                handler.accept(payload, context.player(), context.server());
            }));
        };
        PacketRegistry.sendPacketS2C = ServerPlayNetworking::send;

        CyberRewaredMod.init();


        CyberRewaredMod.ITEM_GROUP = FabricItemGroup.builder()
            .icon(CyberRewaredMod.ITEM_GROUP::getIcon)
            .displayName(CyberRewaredMod.ITEM_GROUP.getDisplayName())
            .entries(((displayContext, entries) -> {
                ItemRegistry.modItems.forEach(i -> {
                    if (i instanceof LimbItem li) {
                        entries.add(li);
                        ItemStack stack = new ItemStack(li);
                        stack.set(ComponentRegistry.IS_RIGHT,true);
                        entries.add(stack);
                    } else {
                        entries.add(i);
                    }
                });
            })).build();
        Registry.register(Registries.ITEM_GROUP, CyberRewaredMod.ITEM_GROUP_KEY, CyberRewaredMod.ITEM_GROUP);
    }

    @Override
    public void onInitializeClient() {
        for (Item modItem : ItemRegistry.modItems) {
            if (modItem instanceof LimbItem) {
                ModelPredicateProviderRegistry.register(modItem,
                    Identifier.of(CyberRewaredMod.MOD_ID, "is_right"), (is, world, le, seed) -> {
                        if (is.getItem() instanceof LimbItem) {
                            // I have no idea why IDEA wants to do this but its probably right
                            return Boolean.TRUE.equals(is.get(ComponentRegistry.IS_RIGHT)) ? 1 : 0;
                        }
                        return 0;
                    }
                );
            }
        }
        CyberRewaredMod.packetS2CHandlerRegisterFunc = (id, handler) -> {
            ClientPlayNetworking.registerGlobalReceiver(id, ((payload, context) -> {
                handler.accept(payload, context.player(), context.client());
            }));
        };
        PacketRegistry.sendPacketC2S = ClientPlayNetworking::send;
        CyberRewaredMod.initClient();

        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.RADIO_KIT, RenderLayer.getCutout());

        HandledScreens.register(ScreenHandlerRegistry.SCANNER, ScannerScreen::new);
        HandledScreens.register(ScreenHandlerRegistry.BLUEPRINT_ARCHIVE, BlueprintArchiveScreen::new);
        HandledScreens.register(ScreenHandlerRegistry.COMPONENT_BOX, ComponentBoxScreen::new);
        HandledScreens.register(ScreenHandlerRegistry.ENGINEERING_TABLE, EngineeringTableScreen::new);

        BlockEntityRendererFactories.register(BlockEntityRegistry.SCANNER, ScannerBlockEntityRenderer::new);

        EntityRendererRegistry.register(EntityRegistry.CYBERZOMBIE, CyberzombieEntityRenderer::new);
    }
}
