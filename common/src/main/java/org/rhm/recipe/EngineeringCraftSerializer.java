package org.rhm.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;

public class EngineeringCraftSerializer implements RecipeSerializer<EngineeringCraftRecipe> {
    public static final EngineeringCraftSerializer INSTANCE = new EngineeringCraftSerializer();
    public static final PacketCodec<RegistryByteBuf, EngineeringCraftRecipe> PACKET_CODEC = PacketCodec.ofStatic(
        EngineeringCraftSerializer::write, EngineeringCraftSerializer::read
    );
    private static final MapCodec<EngineeringCraftRecipe> CODEC = RecordCodecBuilder.mapCodec(
        instance -> instance.group(
                Codec.list(Ingredient.DISALLOW_EMPTY_CODEC).fieldOf("ingredients").forGetter(EngineeringCraftRecipe::ingredients),
                Ingredient.ALLOW_EMPTY_CODEC.fieldOf("blueprint").orElse(Ingredient.EMPTY).forGetter(EngineeringCraftRecipe::blueprint),
                ItemStack.CODEC.fieldOf("output").forGetter(EngineeringCraftRecipe::output)
            )
            .apply(instance, EngineeringCraftRecipe::new)
    );

    private static EngineeringCraftRecipe read(RegistryByteBuf buf) {
        ItemStack output = ItemStack.PACKET_CODEC.decode(buf);
        return new EngineeringCraftRecipe(
            PacketCodecs.codec(Codec.list(Ingredient.DISALLOW_EMPTY_CODEC)).decode(buf),
            Ingredient.PACKET_CODEC.decode(buf),
            output
        );
    }

    private static void write(RegistryByteBuf buf, EngineeringCraftRecipe recipe) {
        ItemStack.PACKET_CODEC.encode(buf, recipe.output());
        PacketCodecs.codec(Codec.list(Ingredient.DISALLOW_EMPTY_CODEC)).encode(buf, recipe.ingredients());
        Ingredient.PACKET_CODEC.encode(buf, recipe.blueprint());
    }

    @Override
    public MapCodec<EngineeringCraftRecipe> codec() {
        return CODEC;
    }

    @Override
    public PacketCodec<RegistryByteBuf, EngineeringCraftRecipe> packetCodec() {
        return PACKET_CODEC;
    }
}
