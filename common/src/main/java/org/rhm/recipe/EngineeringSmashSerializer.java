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

public class EngineeringSmashSerializer implements RecipeSerializer<EngineeringSmashRecipe> {
    public static final EngineeringSmashSerializer INSTANCE = new EngineeringSmashSerializer();
    public static final PacketCodec<RegistryByteBuf, EngineeringSmashRecipe> PACKET_CODEC = PacketCodec.ofStatic(
        EngineeringSmashSerializer::write, EngineeringSmashSerializer::read
    );
    private static final MapCodec<EngineeringSmashRecipe> CODEC = RecordCodecBuilder.mapCodec(
        instance -> instance.group(
                Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("cyberware").forGetter(EngineeringSmashRecipe::cyberware),
                Codec.list(ItemStack.CODEC).fieldOf("output").forGetter(EngineeringSmashRecipe::output)
            )
            .apply(instance, EngineeringSmashRecipe::new)
    );

    private static EngineeringSmashRecipe read(RegistryByteBuf buf) {
        Ingredient cyberware = Ingredient.PACKET_CODEC.decode(buf);
        return new EngineeringSmashRecipe(cyberware, PacketCodecs.codec(Codec.list(ItemStack.CODEC)).decode(buf));
    }

    private static void write(RegistryByteBuf buf, EngineeringSmashRecipe recipe) {
        Ingredient.PACKET_CODEC.encode(buf, recipe.cyberware());
        PacketCodecs.codec(Codec.list(ItemStack.CODEC)).encode(buf, recipe.output());
    }

    @Override
    public MapCodec<EngineeringSmashRecipe> codec() {
        return CODEC;
    }

    @Override
    public PacketCodec<RegistryByteBuf, EngineeringSmashRecipe> packetCodec() {
        return PACKET_CODEC;
    }
}
