package org.rhm.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;

public class EngineeringSmashSerializer implements RecipeSerializer<EngineeringSmashRecipe> {
    public static final EngineeringSmashSerializer INSTANCE = new EngineeringSmashSerializer();
    public static final StreamCodec<RegistryFriendlyByteBuf, EngineeringSmashRecipe> PACKET_CODEC = StreamCodec.of(
        EngineeringSmashSerializer::write, EngineeringSmashSerializer::read
    );
    private static final MapCodec<EngineeringSmashRecipe> CODEC = RecordCodecBuilder.mapCodec(
        instance -> instance.group(
                Ingredient.CODEC_NONEMPTY.fieldOf("cyberware").forGetter(EngineeringSmashRecipe::cyberware),
                Codec.list(ItemStack.CODEC).fieldOf("output").forGetter(EngineeringSmashRecipe::output)
            )
            .apply(instance, EngineeringSmashRecipe::new)
    );

    private static EngineeringSmashRecipe read(RegistryFriendlyByteBuf buf) {
        Ingredient cyberware = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
        return new EngineeringSmashRecipe(cyberware, ByteBufCodecs.fromCodec(Codec.list(ItemStack.CODEC)).decode(buf));
    }

    private static void write(RegistryFriendlyByteBuf buf, EngineeringSmashRecipe recipe) {
        Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.cyberware());
        ByteBufCodecs.fromCodec(Codec.list(ItemStack.CODEC)).encode(buf, recipe.output());
    }

    @Override
    public @NotNull MapCodec<EngineeringSmashRecipe> codec() {
        return CODEC;
    }

    @Override
    public @NotNull StreamCodec<RegistryFriendlyByteBuf, EngineeringSmashRecipe> streamCodec() {
        return PACKET_CODEC;
    }
}
