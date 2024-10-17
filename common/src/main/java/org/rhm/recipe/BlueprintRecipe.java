package org.rhm.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.rhm.registries.ComponentRegistry;
import org.rhm.registries.ItemRegistry;

public class BlueprintRecipe extends CustomRecipe {
    public static final BlueprintRecipe.Serializer SERIALIZER = new Serializer();
    private final Ingredient item;

    public BlueprintRecipe(Ingredient item) {
        super(CraftingBookCategory.MISC);
        this.item = item;
    }

    public BlueprintRecipe(CraftingBookCategory craftingBookCategory) {
        this((Ingredient) null);
    }

    public Ingredient getItem() {
        return item;
    }

    @Override
    public boolean matches(@NotNull CraftingInput input, @NotNull Level level) {
        boolean hasBlueprint = false;
        boolean hasItem = false;

        for (ItemStack stack : input.items()) { // probably not optimal
            if (stack.getItem() == ItemRegistry.BLUEPRINT) {
                hasBlueprint = true;
            } else if (item.test(stack)) {
                hasItem = true;
            }
        }

        return hasBlueprint && hasItem;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CraftingInput input, HolderLookup.@NotNull Provider registries) {
        ItemStack item = ItemRegistry.BLUEPRINT.getDefaultInstance();
        ItemStack target = null;

        for (ItemStack stack : input.items()) {
            if (this.item.test(stack)) {
                target = stack;
                break;
            }
        }
        if (target != null && !target.isEmpty()) {
            item.set(ComponentRegistry.BLUEPRINT_RESULT, target);
        }
        return item;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public boolean isSpecial() {
        return false;
    }

    public static class Type implements RecipeType<BlueprintRecipe> {
        public static final BlueprintRecipe.Type INSTANCE = new BlueprintRecipe.Type();
        public static final String ID = "blueprint_combine";

        private Type() {
        }
    }
    public static class Serializer extends SimpleCraftingRecipeSerializer<BlueprintRecipe> {
        public Serializer() {
            super(BlueprintRecipe::new);
        }

        private static BlueprintRecipe read(RegistryFriendlyByteBuf buf) {
            return new BlueprintRecipe(Ingredient.CONTENTS_STREAM_CODEC.decode(buf));
        }

        private static void write(RegistryFriendlyByteBuf buf, BlueprintRecipe recipe) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.getItem());
        }

        @Override
        public @NotNull MapCodec<BlueprintRecipe> codec() {
            return RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                    Ingredient.CODEC_NONEMPTY.fieldOf("target").forGetter(BlueprintRecipe::getItem)
                ).apply(instance, BlueprintRecipe::new)
            );
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, BlueprintRecipe> streamCodec() {
            return StreamCodec.of(
                Serializer::write, Serializer::read
            );
        }
    }
}
