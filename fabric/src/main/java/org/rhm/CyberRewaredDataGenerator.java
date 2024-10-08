package org.rhm;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.data.DataOutput;
import net.minecraft.registry.RegistryWrapper;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class CyberRewaredDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider((dataOutput, registryLookup) -> new LanguageProvider(dataOutput, "en_us", registryLookup) {
            @Override
            public void generateTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder builder) {
                builder.add(CyberRewaredMod.MOD_ID + ".itemGroup", "Cyber Rewared");
            }
        });
    }

    private abstract static class LanguageProvider extends FabricLanguageProvider {
        public final String LANGUAGE_CODE;

        protected LanguageProvider(FabricDataOutput dataOutput, String language, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
            super(dataOutput, language, registryLookup);
            LANGUAGE_CODE = language;
        }

        @Override
        public void generateTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder builder) {
            Optional<Path> path = dataOutput.getModContainer().findPath("assets/" + CyberRewaredMod.MOD_ID + "/lang/" + LANGUAGE_CODE + ".existing.json");
            if (path.isPresent()) {
                try {
                    builder.add(path.get());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
