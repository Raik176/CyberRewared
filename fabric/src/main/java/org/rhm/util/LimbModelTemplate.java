package org.rhm.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

// This is just lazy but it is the way it is, eh?
public class LimbModelTemplate extends ModelTemplate {
    private final boolean isRight;

    public LimbModelTemplate(boolean isRight) {
        super(Optional.empty(), Optional.empty());
        this.isRight = isRight;
    }

    @Override
    public @NotNull ResourceLocation create(ResourceLocation modelLocation, TextureMapping textureMapping, BiConsumer<ResourceLocation, Supplier<JsonElement>> modelOutput, JsonFactory factory) {
        ResourceLocation actualLocation = isRight ? modelLocation.withSuffix("_right") : modelLocation;
        modelOutput.accept(actualLocation, () -> {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("parent", String.valueOf(isRight ? modelLocation : ResourceLocation.withDefaultNamespace("item/generated")));

            if (!isRight) {
                JsonArray overrides = new JsonArray();
                JsonObject override = new JsonObject();
                JsonObject predicate = new JsonObject();
                predicate.addProperty(CyberRewaredDataGenerator.IS_RIGHT, 1);
                override.addProperty(
                    "model",
                    actualLocation + "_right"
                );
                override.add("predicate", predicate);
                overrides.add(override);

                jsonObject.add("overrides", overrides);
            }
            JsonObject textures = new JsonObject();
            textures.addProperty("layer0", actualLocation.toString());
            jsonObject.add("textures", textures);
            return jsonObject;
        });
        return modelLocation;
    }
}
