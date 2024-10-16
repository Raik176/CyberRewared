package org.rhm;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.models.blockstates.PropertyDispatch;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.apache.commons.lang3.function.TriConsumer;
import org.apache.commons.lang3.function.TriFunction;
import org.rhm.registries.*;
import org.rhm.util.IEnergyStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class CyberRewaredMod {
    public static final String MOD_ID = "cyber_rewared";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final ResourceKey<CreativeModeTab> ITEM_GROUP_KEY = ResourceKey.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), ResourceLocation.fromNamespaceAndPath(MOD_ID, "item_group"));
    // Register Consumers/Functions
    // I think this is better than the way i was doing it before, however i sadly can't put these in their own registry.
    public static TriFunction<ResourceLocation, BlockEntityRegistry.BlockEntityFactory<? extends BlockEntity>, Block, BlockEntityType<? extends BlockEntity>> blockEntityRegisterFunc;
    public static BiConsumer<BlockEntityType<? extends IEnergyStorage>, Block> energyStorageRegisterFunc;
    public static BiConsumer<ResourceLocation, Block> blockRegisterFunc;
    public static BiConsumer<ResourceLocation, Item> itemRegisterFunc;
    public static BiFunction<ResourceLocation, ScreenHandlerRegistry.ScreenHandlerFactory<? extends AbstractContainerMenu>, MenuType<? extends AbstractContainerMenu>> screenHandlerRegisterFunc;
    public static PropertyDispatch.QuadFunction<ResourceLocation, EntityRegistry.EntityFactory<? extends Entity>, EntityRegistry.EntityTypeBuilder, Supplier<AttributeSupplier.Builder>, EntityType<? extends Entity>> entityRegisterFunc;
    public static TriConsumer<ResourceLocation, RecipeType<Recipe<?>>, RecipeSerializer<Recipe<?>>> recipeRegisterFunc;
    public static BiConsumer<ResourceLocation, DataComponentType<?>> componentRegisterFunc;
    public static TriFunction<ResourceLocation, MobEffect, Integer, Holder<Potion>> potionRegisterFunc;
    // other
    public static TagKey<Item> COMPONENT_TAG = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, "cyber_component"));
    public static CreativeModeTab ITEM_GROUP = CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
        .icon(() -> new ItemStack(ItemRegistry.CYBEREYES))
        .title(Component.translatable(CyberRewaredMod.MOD_ID + ".itemGroup"))
        .build();


    private static boolean hasInitialized;

    public static void init() {
        if (hasInitialized) return;
        hasInitialized = true;
        BlockRegistry.initialize();
        BlockEntityRegistry.initialize();
        EntityRegistry.initialize();
        ItemRegistry.initialize();
        RecipeRegistry.initialize();
        ScreenHandlerRegistry.initialize();
        ComponentRegistry.initialize();
        PacketRegistry.initialize();
    }

    public static void initClient() {
        CyberRewaredModClient.init();
    }
}
