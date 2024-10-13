package org.rhm;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.component.ComponentType;
import net.minecraft.data.client.BlockStateVariantMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.potion.Potion;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.function.TriConsumer;
import org.apache.commons.lang3.function.TriFunction;
import org.rhm.registries.*;
import org.rhm.util.IEnergyStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

// TODO: probably switch to parchment so i can use this in neoforge
@SuppressWarnings("unused")
public class CyberRewaredMod {
    public static final String MOD_ID = "cyber_rewared";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final RegistryKey<ItemGroup> ITEM_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(MOD_ID, "item_group"));
    // TODO: config
    public static final float ENGINEERING_CHANCE = 15;
    public static final float NATURAL_BRUTE_CHANCE = 50;
    // Register Consumers/Functions
    // I think this is better than the way i was doing it before, however i sadly can't put these in their own registry.
    public static TriFunction<Identifier, BlockEntityRegistry.BlockEntityFactory<? extends BlockEntity>, Block, BlockEntityType<? extends BlockEntity>> blockEntityRegisterFunc;
    public static Consumer<BlockEntityType<? extends IEnergyStorage>> energyStorageRegisterFunc;
    public static BiConsumer<Identifier, Block> blockRegisterFunc;
    public static BiConsumer<Identifier, Item> itemRegisterFunc;
    public static BiFunction<Identifier, ScreenHandlerRegistry.ScreenHandlerFactory<? extends ScreenHandler>, ScreenHandlerType<? extends ScreenHandler>> screenHandlerRegisterFunc;
    public static BlockStateVariantMap.QuadFunction<Identifier, EntityRegistry.EntityFactory<? extends Entity>, EntityRegistry.EntityTypeBuilder, DefaultAttributeContainer.Builder, EntityType<? extends Entity>> entityRegisterFunc;
    public static TriConsumer<Identifier, RecipeType<Recipe<?>>, RecipeSerializer<Recipe<?>>> recipeRegisterFunc;
    public static BiConsumer<Identifier, ComponentType<?>> componentRegisterFunc;
    public static BiConsumer<CustomPayload.Id<?>, PacketCodec<RegistryByteBuf, ?>> packetS2CRegisterFunc;
    public static BiConsumer<CustomPayload.Id<?>, TriConsumer<CustomPayload, ClientPlayerEntity,MinecraftClient>> packetS2CHandlerRegisterFunc;
    public static BiConsumer<CustomPayload.Id<?>, PacketCodec<RegistryByteBuf, ?>> packetC2SRegisterFunc;
    public static BiConsumer<CustomPayload.Id<?>, TriConsumer<CustomPayload, ServerPlayerEntity,MinecraftServer>> packetC2SHandlerRegisterFunc;
    public static TriFunction<Identifier, StatusEffect, Integer, RegistryEntry<Potion>> potionRegisterFunc;
    // other
    public static TagKey<Item> COMPONENT_TAG = TagKey.of(RegistryKeys.ITEM, Identifier.of(MOD_ID, "cyber_component"));
    public static ItemGroup ITEM_GROUP = ItemGroup.create(ItemGroup.Row.TOP, 0)
        .icon(() -> new ItemStack(Items.DIRT))
        .displayName(Text.translatable(CyberRewaredMod.MOD_ID + ".itemGroup"))
        .build();


    public static void init() {
        ScreenHandlerRegistry.initialize();
        BlockRegistry.initialize();
        BlockEntityRegistry.initialize();
        EntityRegistry.initialize();
        ItemRegistry.initialize();
        PacketRegistry.initialize();
        RecipeRegistry.initialize();
        ComponentRegistry.initialize();
    }

    public static void initClient() {
        PacketRegistry.initializeClient();
    }
}
