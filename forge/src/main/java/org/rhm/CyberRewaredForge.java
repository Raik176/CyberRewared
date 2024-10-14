package org.rhm;

import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.MenuScreens;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import org.rhm.gui.BlueprintArchiveScreen;
import org.rhm.gui.ComponentBoxScreen;
import org.rhm.gui.EngineeringTableScreen;
import org.rhm.gui.ScannerScreen;
import org.rhm.item.LimbItem;
import org.rhm.registries.ComponentRegistry;
import org.rhm.registries.ItemRegistry;
import org.rhm.registries.ScreenHandlerRegistry;
import org.rhm.util.Config;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.function.Supplier;

@Mod(CyberRewaredMod.MOD_ID)
@SuppressWarnings("unchecked")
public class CyberRewaredForge {
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


    public CyberRewaredForge(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        Config.load(FMLPaths.CONFIGDIR.get());

        MinecraftForge.EVENT_BUS.register(this);

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

        event.register(ForgeRegistries.BLOCK_ENTITY_TYPES.getRegistryKey(), registry -> blockEntityTypes.forEach(registry::register));
        event.register(ForgeRegistries.BLOCKS.getRegistryKey(), registry -> blocks.forEach(registry::register));
        event.register(Registries.DATA_COMPONENT_TYPE, registry -> components.forEach(registry::register));
        event.register(ForgeRegistries.ITEMS.getRegistryKey(), registry -> items.forEach(registry::register));
        event.register(ForgeRegistries.POTIONS.getRegistryKey(), registry -> potions.forEach(registry::register));
        event.register(ForgeRegistries.MOB_EFFECTS.getRegistryKey(), registry -> effects.forEach(registry::register));
        event.register(ForgeRegistries.MENU_TYPES.getRegistryKey(), registry -> screenHandlers.forEach(registry::register));
        event.register(ForgeRegistries.ENTITY_TYPES.getRegistryKey(), registry -> entities.forEach(registry::register));
        CyberRewaredMod.recipeRegisterFunc = (id, type, serializer) -> {
            recipeTypes.put(id, type);
            recipeSerializers.put(id, serializer);
        };
        event.register(ForgeRegistries.RECIPE_TYPES.getRegistryKey(), registry -> recipeTypes.forEach(registry::register));
        event.register(ForgeRegistries.RECIPE_SERIALIZERS.getRegistryKey(), registry -> recipeSerializers.forEach(registry::register));

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
            ItemRegistry.modItems.forEach(i -> {
                if (i instanceof LimbItem li) {
                    event.accept(li);
                    ItemStack stack = new ItemStack(li);
                    stack.set(ComponentRegistry.IS_RIGHT, true);
                    event.accept(stack);
                } else {
                    event.accept(i);
                }
            });
        }
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    @Mod.EventBusSubscriber(modid = CyberRewaredMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            MinecraftForge.registerConfigScreen(CyberRewaredModClient::getConfigScreen);

            for (Item modItem : ItemRegistry.modItems) {
                if (modItem instanceof LimbItem) {
                    ItemProperties.register(modItem,
                        ResourceLocation.fromNamespaceAndPath(CyberRewaredMod.MOD_ID, "is_right"), (is, world, le, seed) -> {
                            if (is.getItem() instanceof LimbItem) {
                                // I have no idea why IDEA wants to do this but its probably right
                                return Boolean.TRUE.equals(is.get(ComponentRegistry.IS_RIGHT)) ? 1 : 0;
                            }
                            return 0;
                        }
                    );
                }
            }

            MenuScreens.register(ScreenHandlerRegistry.SCANNER, ScannerScreen::new);
            MenuScreens.register(ScreenHandlerRegistry.BLUEPRINT_ARCHIVE, BlueprintArchiveScreen::new);
            MenuScreens.register(ScreenHandlerRegistry.COMPONENT_BOX, ComponentBoxScreen::new);
            MenuScreens.register(ScreenHandlerRegistry.ENGINEERING_TABLE, EngineeringTableScreen::new);
        }
    }
}
