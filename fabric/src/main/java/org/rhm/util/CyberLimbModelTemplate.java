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

// No way in hell am i making 4 models for each cyberlimb item
// Code can be as bad and inefficient as i want as this is never run after the mod is built anyway
public class CyberLimbModelTemplate extends ModelTemplate {
    private final boolean isScavenged;
    private final boolean isRight;
    public CyberLimbModelTemplate(boolean isScavenged, boolean isRight) {
        super(Optional.empty(), Optional.empty());
        this.isScavenged = isScavenged;
        this.isRight = isRight;
    }

    @Override
    public @NotNull ResourceLocation create(ResourceLocation modelLocation, TextureMapping textureMapping, BiConsumer<ResourceLocation, Supplier<JsonElement>> modelOutput, JsonFactory factory) {
        ResourceLocation actualLocation = (isScavenged ? modelLocation.withSuffix("_scavenged") : modelLocation).withSuffix(isRight ? "_right" : "");
        modelOutput.accept(actualLocation, () -> {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("parent", String.valueOf((isScavenged || isRight) ?  modelLocation : ResourceLocation.withDefaultNamespace("item/generated")));

            if (!isScavenged && !isRight) {
                JsonArray overrides = new JsonArray();

                JsonObject override = new JsonObject();
                JsonObject predicate = new JsonObject();
                predicate.addProperty(CyberRewaredDataGenerator.IS_SCAVENGED, 1);
                override.addProperty(
                    "model",
                    actualLocation + "_scavenged"
                );
                override.add("predicate",predicate);
                overrides.add(override);

                override = new JsonObject();
                predicate = new JsonObject();
                predicate.addProperty(CyberRewaredDataGenerator.IS_RIGHT, 1);
                override.addProperty(
                    "model",
                    actualLocation + "_right"
                );
                override.add("predicate",predicate);
                overrides.add(override);

                override = new JsonObject();
                predicate = new JsonObject();
                predicate.addProperty(CyberRewaredDataGenerator.IS_SCAVENGED, 1);
                predicate.addProperty(CyberRewaredDataGenerator.IS_RIGHT, 1);
                override.addProperty(
                    "model",
                    actualLocation + "_scavenged_right"
                );
                override.add("predicate",predicate);
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
