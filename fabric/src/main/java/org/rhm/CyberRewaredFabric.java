package org.rhm;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.Vec3;
import org.rhm.block.entity.renderer.ScannerBlockEntityRenderer;
import org.rhm.entity.renderer.CyberzombieEntityRenderer;
import org.rhm.gui.BlueprintArchiveScreen;
import org.rhm.gui.ComponentBoxScreen;
import org.rhm.gui.EngineeringTableScreen;
import org.rhm.gui.ScannerScreen;
import org.rhm.gui.SurgeryScreen;
import org.rhm.item.CyberItem;
import org.rhm.item.CyberLimbItem;
import org.rhm.item.LimbItem;
import org.rhm.registries.BlockEntityRegistry;
import org.rhm.registries.BlockRegistry;
import org.rhm.registries.ComponentRegistry;
import org.rhm.registries.EntityRegistry;
import org.rhm.registries.ItemRegistry;
import org.rhm.registries.ScreenHandlerRegistry;
import org.rhm.util.Config;
import org.rhm.util.IEnergyStorage;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import java.util.Objects;

@SuppressWarnings("unchecked")
public class CyberRewaredFabric implements ModInitializer, ClientModInitializer {
    @Override
    @SuppressWarnings("rawtypes")
    public void onInitialize() {
        Config.load(FabricLoader.getInstance().getConfigDir());
        CyberRewaredMod.blockEntityRegisterFunc = (id, bef, block) -> {
            BlockEntityType<?> blockEntityType = BlockEntityType.Builder.of(bef::create, block).build();
            Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id, blockEntityType);

            return blockEntityType;
        };
        CyberRewaredMod.energyStorageRegisterFunc = (bet, block) -> EnergyStorage.SIDED.registerForBlockEntity((be, dir) -> {
            /* i think this is creating a new storage every tick but it doesn't impact performance
             that much so i really don't care about it for now, as im too lazy to keep track of a map
             and add/remove storages depending on which blocks are broken or placed
            */
            SimpleEnergyStorage storage = new SimpleEnergyStorage(be.getCapacity(), be.getMaxIn(), be.getMaxOut()) {
                @Override
                protected void onFinalCommit() {
                    super.onFinalCommit();
                    be.onFinal();
                }

                @Override
                public long insert(long maxAmount, TransactionContext transaction) {
                    be.insert((int) Math.min(Integer.MAX_VALUE, amount));
                    return super.insert(maxAmount, transaction);
                }

                @Override
                public long extract(long maxAmount, TransactionContext transaction) {
                    be.extract((int) Math.min(Integer.MAX_VALUE, amount));
                    return super.extract(maxAmount, transaction);
                }
            };
            storage.amount = be.getEnergy();
            return storage;
        }, (BlockEntityType<? extends IEnergyStorage>) bet);
        CyberRewaredMod.blockRegisterFunc = (id, block) -> Registry.register(BuiltInRegistries.BLOCK, id, block);
        CyberRewaredMod.itemRegisterFunc = (id, item) -> Registry.register(BuiltInRegistries.ITEM, id, item);
        CyberRewaredMod.screenHandlerRegisterFunc = (id, factory) -> Registry.register(BuiltInRegistries.MENU, id, new MenuType(factory::create, FeatureFlags.DEFAULT_FLAGS));
        CyberRewaredMod.entityRegisterFunc = (id, ef, etb, ab) -> {
            EntityType idk = EntityType.Builder.of(ef::create, MobCategory.MONSTER)
                .sized(etb.getWidth(), etb.getHeight()).eyeHeight(etb.getEyeHeight())
                .passengerAttachments(etb.getPassengerOffsetY()).vehicleAttachment(new Vec3(0, etb.getVehicleOffsetY(), 0))
                .clientTrackingRange(etb.getMaxTrackingRange()).build(id.getPath());
            EntityType<? extends Entity> entityType = Registry.register(BuiltInRegistries.ENTITY_TYPE, id, idk);
            if (ab.get() != null) {
                FabricDefaultAttributeRegistry.register((EntityType<? extends LivingEntity>) entityType, ab.get().build());
            }
            return entityType;
        };
        CyberRewaredMod.recipeRegisterFunc = (id, type, serializer) -> {
            Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, id, serializer);
            Registry.register(BuiltInRegistries.RECIPE_TYPE, id, type);
        };
        CyberRewaredMod.componentRegisterFunc = (id, component) -> {
            Registry.register(
                BuiltInRegistries.DATA_COMPONENT_TYPE,
                id,
                component
            );
        };
        CyberRewaredMod.potionRegisterFunc = (id, effect, duration) -> {
            Potion potion = new Potion(
                id.getPath(),
                new MobEffectInstance(Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, id, effect), duration)
            );
            return Registry.registerForHolder(BuiltInRegistries.POTION, id, potion);
        };

        CyberRewaredMod.init();


        CyberRewaredMod.ITEM_GROUP = FabricItemGroup.builder()
            .icon(CyberRewaredMod.ITEM_GROUP::getIconItem)
            .title(CyberRewaredMod.ITEM_GROUP.getDisplayName())
            .displayItems((displayContext, entries) -> {
                for (Object creativeEntry : ItemRegistry.getCreativeEntries()) {
                    if (creativeEntry instanceof Item item) {
                        entries.accept(item);
                    } else if (creativeEntry instanceof ItemStack stack) {
                        entries.accept(stack);
                    } else if (creativeEntry instanceof ItemLike il) {
                        entries.accept(il);
                    }
                }
            }).build();
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, CyberRewaredMod.ITEM_GROUP_KEY, CyberRewaredMod.ITEM_GROUP);
    }

    @Override
    public void onInitializeClient() {
        for (Item modItem : ItemRegistry.modItems) {
            if (modItem instanceof LimbItem) {
                ItemProperties.register(modItem,
                    ResourceLocation.fromNamespaceAndPath(CyberRewaredMod.MOD_ID, "is_right"), (is, world, le, seed) -> {
                        if (is.getItem() instanceof LimbItem) {
                            return Boolean.TRUE.equals(is.get(ComponentRegistry.IS_RIGHT)) ? 1 : 0;
                        }
                        return 0;
                    }
                );
            } else if (modItem instanceof CyberLimbItem) {
                ItemProperties.register(modItem,
                    ResourceLocation.fromNamespaceAndPath(CyberRewaredMod.MOD_ID, "is_right"), (is, world, le, seed) -> {
                        if (is.getItem() instanceof CyberLimbItem) {
                            return Boolean.TRUE.equals(is.get(ComponentRegistry.IS_RIGHT)) ? 1 : 0;
                        }
                        return 0;
                    }
                );
                ItemProperties.register(modItem,
                    ResourceLocation.fromNamespaceAndPath(CyberRewaredMod.MOD_ID, "is_scavenged"), (is, world, le, seed) -> {
                        if (is.getItem() instanceof CyberLimbItem) {
                            return Boolean.TRUE.equals(is.get(ComponentRegistry.SCAVENGED)) ? 1 : 0;
                        }
                        return 0;
                    }
                );
            } else if (modItem instanceof CyberItem) {
                ItemProperties.register(modItem,
                    ResourceLocation.fromNamespaceAndPath(CyberRewaredMod.MOD_ID, "is_scavenged"), (is, world, le, seed) -> {
                        if (is.getItem() instanceof CyberItem) {
                            return Boolean.TRUE.equals(is.get(ComponentRegistry.SCAVENGED)) ? 1 : 0;
                        }
                        return 0;
                    }
                );
            } else if (modItem == ItemRegistry.BLUEPRINT) {
                ItemProperties.register(modItem,
                    ResourceLocation.fromNamespaceAndPath(CyberRewaredMod.MOD_ID, "blueprint_is_empty"), (is, world, le, seed) -> {
                        if (is.getItem() == ItemRegistry.BLUEPRINT) {
                            return Objects.requireNonNull(is.get(ComponentRegistry.BLUEPRINT_RESULT)).isEmpty() ? 1 : 0;
                        }
                        return 0;
                    }
                );
            }
        }
        CyberRewaredMod.initClient();

        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.RADIO_KIT, RenderType.cutout());

        MenuScreens.register(ScreenHandlerRegistry.SCANNER, ScannerScreen::new);
        MenuScreens.register(ScreenHandlerRegistry.BLUEPRINT_ARCHIVE, BlueprintArchiveScreen::new);
        MenuScreens.register(ScreenHandlerRegistry.COMPONENT_BOX, ComponentBoxScreen::new);
        MenuScreens.register(ScreenHandlerRegistry.ENGINEERING_TABLE, EngineeringTableScreen::new);
        MenuScreens.register(ScreenHandlerRegistry.SURGERY, SurgeryScreen::new);

        BlockEntityRenderers.register(BlockEntityRegistry.SCANNER, ScannerBlockEntityRenderer::new);

        EntityRendererRegistry.register(EntityRegistry.CYBERZOMBIE, CyberzombieEntityRenderer::new);
    }
}
