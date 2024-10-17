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

// No way in hell am i making 2 models for each cybernetic item
public class CyberModelTemplate extends ModelTemplate {
    private final boolean isScavenged;

    public CyberModelTemplate(boolean isScavenged) {
        super(Optional.empty(), Optional.empty());
        this.isScavenged = isScavenged;
    }

    @Override
    public @NotNull ResourceLocation create(ResourceLocation modelLocation, TextureMapping textureMapping, BiConsumer<ResourceLocation, Supplier<JsonElement>> modelOutput, JsonFactory factory) {
        ResourceLocation actualLocation = isScavenged ? modelLocation.withSuffix("_scavenged") : modelLocation;
        modelOutput.accept(actualLocation, () -> {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("parent", String.valueOf(isScavenged ? modelLocation : ResourceLocation.withDefaultNamespace("item/generated")));

            if (!isScavenged) {
                JsonArray overrides = new JsonArray();
                JsonObject override = new JsonObject();
                JsonObject predicate = new JsonObject();
                predicate.addProperty(CyberRewaredDataGenerator.IS_SCAVENGED, 1);
                override.addProperty(
                    "model",
                    actualLocation + "_scavenged"
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
