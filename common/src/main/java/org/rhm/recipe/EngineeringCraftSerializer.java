package org.rhm.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class EngineeringCraftSerializer implements RecipeSerializer<EngineeringCraftRecipe> {
    public static final EngineeringCraftSerializer INSTANCE = new EngineeringCraftSerializer();
    public static final StreamCodec<RegistryFriendlyByteBuf, EngineeringCraftRecipe> PACKET_CODEC = StreamCodec.of(
        EngineeringCraftSerializer::write, EngineeringCraftSerializer::read
    );
    private static final MapCodec<EngineeringCraftRecipe> CODEC = RecordCodecBuilder.mapCodec(
        instance -> instance.group(
                Codec.list(ItemStack.CODEC).fieldOf("ingredients").forGetter(EngineeringCraftRecipe::ingredients),
                ItemStack.CODEC.fieldOf("blueprint").orElse(ItemStack.EMPTY).forGetter(EngineeringCraftRecipe::blueprint),
                ItemStack.CODEC.fieldOf("output").forGetter(EngineeringCraftRecipe::output),
                Codec.BOOL.optionalFieldOf("blueprint_use").forGetter((s) -> Optional.of(s.useBlueprint()))
            )
            .apply(instance, EngineeringCraftRecipe::new)
    );

    private static EngineeringCraftRecipe read(RegistryFriendlyByteBuf buf) {
        ItemStack output = ItemStack.STREAM_CODEC.decode(buf);
        return new EngineeringCraftRecipe(
            ByteBufCodecs.fromCodec(Codec.list(ItemStack.CODEC)).decode(buf),
            ItemStack.STREAM_CODEC.decode(buf),
            output,
            ByteBufCodecs.BOOL.decode(buf)
        );
    }

    private static void write(RegistryFriendlyByteBuf buf, EngineeringCraftRecipe recipe) {
        ItemStack.STREAM_CODEC.encode(buf, recipe.output());
        ByteBufCodecs.fromCodec(Codec.list(ItemStack.CODEC)).encode(buf, recipe.ingredients());
        ItemStack.STREAM_CODEC.encode(buf, recipe.blueprint());
        ByteBufCodecs.BOOL.encode(buf, recipe.useBlueprint());
    }

    @Override
    public @NotNull MapCodec<EngineeringCraftRecipe> codec() {
        return CODEC;
    }

    @Override
    public @NotNull StreamCodec<RegistryFriendlyByteBuf, EngineeringCraftRecipe> streamCodec() {
        return PACKET_CODEC;
    }
}
