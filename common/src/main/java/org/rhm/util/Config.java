package org.rhm.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.rhm.CyberRewaredMod;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

public class Config {
    public static final float ENGINEERING_CHANCE_DEFAULT = 15;
    public static final float NATURAL_BRUTE_CHANCE_DEFAULT = 5;
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static float ENGINEERING_CHANCE = ENGINEERING_CHANCE_DEFAULT;
    public static float NATURAL_BRUTE_CHANCE = NATURAL_BRUTE_CHANCE_DEFAULT;

    private static File currentConfig = null;

    public static void save() {
        if (currentConfig == null) {
            CyberRewaredMod.LOGGER.warn("Config file not set, skipping save.");
            return;
        }

        try (FileWriter writer = new FileWriter(currentConfig)) {
            gson.toJson(new ConfigData(
                ENGINEERING_CHANCE,
                NATURAL_BRUTE_CHANCE
            ), writer);
        } catch (IOException e) {
            CyberRewaredMod.LOGGER.error("Failed to save config to {}", currentConfig.getPath(), e);
        }

    }

    public static void load(Path configFolder) {
        Path modConfigPath = configFolder.resolve(CyberRewaredMod.MOD_ID + ".json");
        File file = currentConfig = new File(String.valueOf(modConfigPath));
        if (!file.exists()) {
            CyberRewaredMod.LOGGER.warn("Config file doesn't exist. Will use default values.");
            return;
        }

        try (FileReader reader = new FileReader(file)) {
            Optional.ofNullable(gson.fromJson(reader, ConfigData.class))
                .ifPresentOrElse(data -> {
                        ENGINEERING_CHANCE = data.engineeringChance;
                        NATURAL_BRUTE_CHANCE = data.naturalBruteChance;
                    },
                    () -> CyberRewaredMod.LOGGER.warn("Config file was empty or invalid, using default values.")
                );
        } catch (IOException e) {
            CyberRewaredMod.LOGGER.error("Failed to load config from {}", file.getPath(), e);
        }
    }

    // Pretty much only used to make saving easier
    private static class ConfigData {
        float engineeringChance;
        float naturalBruteChance;

        ConfigData(float engineeringChance, float naturalBruteChance) {
            this.engineeringChance = engineeringChance;
            this.naturalBruteChance = naturalBruteChance;
        }
    }
}
