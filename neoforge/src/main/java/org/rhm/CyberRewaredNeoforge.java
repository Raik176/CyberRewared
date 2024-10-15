package org.rhm;

import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.rhm.gui.BlueprintArchiveScreen;
import org.rhm.gui.ComponentBoxScreen;
import org.rhm.gui.EngineeringTableScreen;
import org.rhm.gui.ScannerScreen;
import org.rhm.gui.SurgeryScreen;
import org.rhm.item.CyberItem;
import org.rhm.item.CyberLimbItem;
import org.rhm.item.LimbItem;
import org.rhm.registries.ComponentRegistry;
import org.rhm.registries.ItemRegistry;
import org.rhm.registries.ScreenHandlerRegistry;
import org.rhm.util.config.Config;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Objects;
import java.util.function.Supplier;

@Mod(CyberRewaredMod.MOD_ID)
@SuppressWarnings("unchecked")
public class CyberRewaredNeoforge {
    private static final Logger LOGGER = LogUtils.getLogger();

    // This is horrible but i haven't found another way to do it
    HashMap<ResourceLocation, BlockEntityType<?>> blockEntityTypes = new HashMap<>();
    HashMap<ResourceLocation, Block> blocks = new HashMap<>();
    HashMap<ResourceLocation, DataComponentType<?>> components = new HashMap<>();
    HashMap<ResourceLocation, Item> items = new HashMap<>();
    HashMap<ResourceLocation, MobEffect> effects = new HashMap<>();
    HashMap<ResourceLocation, Potion> potions = new HashMap<>();
    HashMap<ResourceLocation, RecipeType<?>> recipeTypes = new HashMap<>();
    HashMap<ResourceLocation, RecipeSerializer<?>> recipeSerializers = new HashMap<>();
    HashMap<ResourceLocation, EntityType<? extends Entity>> entities = new HashMap<>();
    HashMap<ResourceLocation, MenuType<?>> screenHandlers = new HashMap<>();
    HashMap<EntityType<? extends LivingEntity>, Supplier<AttributeSupplier.Builder>> entityAttributes = new HashMap<>();


    public CyberRewaredNeoforge(IEventBus modEventBus, ModContainer ignoredModContainer) {
        Config.load(FMLPaths.CONFIGDIR.get());

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::creativeSetup);
        modEventBus.addListener(this::entityAttributes);
        modEventBus.addListener(this::registerEvent);
    }

    private void registerEvent(final RegisterEvent event) {
        CyberRewaredMod.blockEntityRegisterFunc = (id, bef, block) -> {
            BlockEntityType<?> blockEntityType = BlockEntityType.Builder.of(bef::create, block).build(null);
            blockEntityTypes.put(id, blockEntityType);
            return blockEntityType;
        };
        CyberRewaredMod.blockRegisterFunc = blocks::put;
        CyberRewaredMod.componentRegisterFunc = components::put;
        CyberRewaredMod.itemRegisterFunc = items::put;
        CyberRewaredMod.entityRegisterFunc = (id, ef, etb, ab) -> {
            EntityType<? extends Entity> entityType = EntityType.Builder.of(ef::create, MobCategory.MONSTER)
                .sized(etb.getWidth(), etb.getHeight()).eyeHeight(etb.getEyeHeight())
                .passengerAttachments(etb.getPassengerOffsetY()).vehicleAttachment(new Vec3(0, etb.getVehicleOffsetY(), 0))
                .clientTrackingRange(etb.getMaxTrackingRange()).build(id.getPath());
            entities.put(id, entityType);
            entityAttributes.put((EntityType<? extends LivingEntity>) entityType, ab);
            return entityType;
        };

        CyberRewaredMod.energyStorageRegisterFunc = (bet, block) -> {
            // TODO: IMPORTANT! Add energy storage support
        };
        CyberRewaredMod.screenHandlerRegisterFunc = (id, factory) -> {
            MenuType<?> type = new MenuType<>(factory::create, FeatureFlags.DEFAULT_FLAGS);
            screenHandlers.put(id, type);
            return type;
        };
        CyberRewaredMod.potionRegisterFunc = (id, effect, duration) -> {
            effects.put(id, effect);
            Potion potion = new Potion(
                id.getPath(),
                new MobEffectInstance(new Holder.Direct<>(effect), duration)
            );

            potions.put(id, potion);
            return new Holder.Direct<>(potion);
        };

        event.register(Registries.BLOCK_ENTITY_TYPE, registry -> blockEntityTypes.forEach(registry::register));
        event.register(Registries.BLOCK, registry -> blocks.forEach(registry::register));
        event.register(Registries.DATA_COMPONENT_TYPE, registry -> components.forEach(registry::register));
        event.register(Registries.ITEM, registry -> items.forEach(registry::register));
        event.register(Registries.POTION, registry -> potions.forEach(registry::register));
        event.register(Registries.MOB_EFFECT, registry -> effects.forEach(registry::register));
        event.register(Registries.MENU, registry -> screenHandlers.forEach(registry::register));
        event.register(Registries.ENTITY_TYPE, registry -> entities.forEach(registry::register));
        CyberRewaredMod.recipeRegisterFunc = (id, type, serializer) -> {
            recipeTypes.put(id, type);
            recipeSerializers.put(id, serializer);
        };
        event.register(Registries.RECIPE_TYPE, registry -> recipeTypes.forEach(registry::register));
        event.register(Registries.RECIPE_SERIALIZER, registry -> recipeSerializers.forEach(registry::register));

        CreativeModeTab.builder()
            .icon(CyberRewaredMod.ITEM_GROUP::getIconItem)
            .title(CyberRewaredMod.ITEM_GROUP.getDisplayName())
            .build();
        event.register(Registries.CREATIVE_MODE_TAB, registry -> registry.register(CyberRewaredMod.ITEM_GROUP_KEY, CyberRewaredMod.ITEM_GROUP));

        CyberRewaredMod.init();
    }

    private void entityAttributes(final EntityAttributeCreationEvent event) {
        entityAttributes.forEach((et, ab) -> {
            if (ab.get() != null) {
                event.put(et, ab.get().build());
            }
        });
    }

    private void creativeSetup(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CyberRewaredMod.ITEM_GROUP_KEY) {
            for (Object creativeEntry : ItemRegistry.getCreativeEntries()) {
                if (creativeEntry instanceof Item item) {
                    event.accept(item);
                } else if (creativeEntry instanceof ItemStack stack) {
                    event.accept(stack);
                } else if (creativeEntry instanceof ItemLike il) {
                    event.accept(il);
                }
            }
        }
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    @EventBusSubscriber(modid = CyberRewaredMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void registerScreens(RegisterMenuScreensEvent event) {
            event.register(ScreenHandlerRegistry.SCANNER, ScannerScreen::new);
            event.register(ScreenHandlerRegistry.BLUEPRINT_ARCHIVE, BlueprintArchiveScreen::new);
            event.register(ScreenHandlerRegistry.COMPONENT_BOX, ComponentBoxScreen::new);
            event.register(ScreenHandlerRegistry.ENGINEERING_TABLE, EngineeringTableScreen::new);
            event.register(ScreenHandlerRegistry.SURGERY, SurgeryScreen::new);
        }

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () -> (modContainer, screen) -> CyberRewaredModClient.getConfigScreen(screen));

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
                                // I have no idea why IDEA wants to do this but its probably right
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
        }
    }
}
