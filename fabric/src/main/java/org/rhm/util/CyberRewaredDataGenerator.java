package org.rhm.util;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;
import org.rhm.CyberRewaredMod;
import org.rhm.registries.BlockRegistry;

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

                builder.add(BlockRegistry.SCANNER, "Scanner");
                builder.add(CyberRewaredMod.MOD_ID + ".gui.percent", "%1$s%% Chance");
                builder.add(CyberRewaredMod.MOD_ID + ".gui.insertPaper", "Insert Paper");
                builder.add(CyberRewaredMod.MOD_ID + ".gui.toScan", "Insert Cyberware to Scan");
                builder.add(CyberRewaredMod.MOD_ID + ".gui.scannerSayings", String.join("\n", new String[]{
                    "Reticulating Splines", "Resolving GUID Conflict", "Perturbing Matrices",
                    "Obfuscating Quigley Matrix", "Integrating Curves", "Burning SINs",
                    "Graphing Whale Migration", "Asserting Packed Exemplars", "Simulating Thermonuclear War",
                    "Updating to Windows 10", "Predicting Lottery Numbers", "Uprooting Data Trees",
                    "Attempting POST Operations", "Subscribing Event Handlers", "Installing Ask Toolbar",
                    "Suppressing Propaganda", "Seizing Means of Production", "Rebalancing Other Mods",
                    "Refreshing Reddit Buffer", "Deciphering Zodiac Letters", "Paging Through Rulebook",
                    "Rolling 4d6", "Unwithering Passive Flowers", "Merging Pull Requests",
                    "Repeating Seen Sayings", "Converting Binary to Octal", "Dicing Models",
                    "Gesticulating Mimes", "Splatting Transforms", "Sequencing Particles",
                    "Searching for Llamas", "Retracting Phong Shader", "Reimplementing Quickplay",
                    "Removing Microtransactions", "Lecturing Errant Subsystems", "Iterating Cellular Automata",
                    "Applying Feng Shui Shaders", "Bypassing DRM Measures", "Generating Stupid Messages",
                    "Gathering Particle Sources", "Splicing Genetic Sources", "Unveiling Herobrine",
                    "Flashing Customized BIOS", "Recommending Roots", "Enforcing EULA Stipulations",
                    "Quelling Uprising", "Calculating Screw Direction", "Modeling Cyberware Housing",
                    "Polishing Scanner Lens", "Extracting Firmware", "Cracking License Key",
                    "Subdividing Strands", "Constructing Simulation", "Routing Neural Networks",
                    "Monitoring Electron Travel", "Zeroing Instrument Error", "Recalculating Route",
                    "Partitioning Singularities", "Compressing Internal Clock", "Resetting Water Indicators",
                    "Analyzing Branding Details", "Performing Yulife Calculation", "Abstracting Noise Layers",
                    "Sifting Neural Particles", "Solving Differential Equations", "Commencing Spatial Simulation",
                    "Instantiating Child Elements", "Wiping System32", "Remembering Lycaon"
                }));

                builder.add(BlockRegistry.BLUEPRINT_ARCHIVE, "Blueprint Archive");

                builder.add(BlockRegistry.COMPONENT_BOX, "Component Box");

                builder.add(BlockRegistry.ENGINEERING_TABLE, "Engineering Table");
                builder.add(CyberRewaredMod.MOD_ID + ".gui.smash", "Void Warranty");
                // this is "to Destroy" in the original but i think salvage sounds better
                builder.add(CyberRewaredMod.MOD_ID + ".gui.insertSalvage", "Insert Cyberware to Salvage");
                builder.add(CyberRewaredMod.MOD_ID + ".gui.insertBlueprint", "Insert Blueprint");

                builder.add(BlockRegistry.SURGERY_CHAMBER, "Surgery Chamber");
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
