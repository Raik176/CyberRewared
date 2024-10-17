package org.rhm.util.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.rhm.CyberRewaredMod;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;


// This is a really weird dynamic class however i don't care lol. I know im really lazy and
// I could've used Cloth's Autoconfig however it says not to use it on Server's and this should work
public class Config {
    public static final int VERSION = 0; // bump whenever you add/remove/modify a value


    @ConfigRange(minFloat = 1, maxFloat = 100)
    @ConfigDefault(floatValue = 15)
    @ConfigTranslation(name = "Engineering Table Blueprint Chance", description = {
        "Chance to get a blueprint when smashing a Cyberitem",
        "in an Engineering Table."
    })
    public static final String ENGINEERING_CHANCE = "engineering_chance";

    @ConfigRange(minFloat = 1, maxFloat = 100)
    @ConfigDefault(floatValue = 5)
    @ConfigTranslation(name = "Natural Brute Chance", description = {
        "Chance to spawn a Brute instead of a normal Cyberzombie."
    })
    public static final String NATURAL_BRUTE_CHANCE = "natural_brute_chance";

    @ConfigRange(minInt = 1, maxInt = 50)
    @ConfigDefault(intValue = 25)
    @ConfigTranslation(name = "Critical Essence", description = {
        "Threshold below which you will take damage and require Anti-Rejection shots.",
    })
    public static final String CRITICAL_ESSENCE = "critical_essence";

    @ConfigDefault(boolValue = ConfigDefault.BoolState.TRUE)
    @ConfigTranslation(name = "Is Katana enabled", description = {
        "If true, Katana is enabled and can be obtained,\nif false, Katana is not obtainable",
        "§cWARNING: THIS WILL ALSO REMOVE EVERY KATANA FROM\n§cEVERY INVENTORY ONCE YOU LOAD A WORLD"
    })
    @ConfigRequiresRestart
    public static final String KATANA_ENABLED = "katana_enabled";

    @ConfigDefault(boolValue = ConfigDefault.BoolState.TRUE)
    @ConfigTranslation(name = "Can Cyberentities Spawn", description = {
        "If true, Cyberentities like the Cyberzombie will be able to replace their",
        "Default counterpart."
    })
    public static final String CYBERENTITIES_ENABLED = "cyberentities_enabled";

    @ConfigDefault(floatValue = 5)
    @ConfigDepends(fields = {CYBERENTITIES_ENABLED})
    @ConfigTranslation(name = "Cyberzombie Spawn Chance", description = {
        "Base chance for a cyberzombie to replace a normal zombie"
    })
    public static final String CYBERZOMBIE_SPAWN_CHANCE = "cyberzombie_chance";


    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final Map<String, Object> defaults = new HashMap<>();
    public static final Map<String, Object> values = new HashMap<>(defaults);
    private static File currentConfig = null;

    static {
        for (Field field : Config.class.getDeclaredFields()) {
            if (!field.getType().equals(String.class)) continue;
            String fieldName;
            try {
                fieldName = (String) field.get(null); // Gets the value
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("Failed to get field value for field: " + field.getName(), e);
            }
            // keeps getting more lazy
            if (field.isAnnotationPresent(ConfigDefault.class)) {
                ConfigDefault defaultValue = field.getAnnotation(ConfigDefault.class);
                for (Method method : ConfigDefault.class.getDeclaredMethods()) {
                    try {
                        Object value = method.invoke(defaultValue);
                        if (value instanceof String strValue && !strValue.equals("N/A")) {
                            defaults.put(fieldName, strValue);
                        } else if (value instanceof Float floatValue && floatValue != Float.MIN_VALUE) {
                            defaults.put(fieldName, floatValue);
                        } else if (value instanceof Integer intValue && intValue != Integer.MIN_VALUE) {
                            defaults.put(fieldName, intValue);
                        } else if (value instanceof ConfigDefault.BoolState boolValue && boolValue != ConfigDefault.BoolState.UNDEFINED) {
                            defaults.put(fieldName, boolValue == ConfigDefault.BoolState.TRUE);
                        }
                    } catch (Exception e) {
                        throw new IllegalStateException("Failed to get value from annotation for field " + fieldName, e);
                    }
                }
                if (!defaults.containsKey(fieldName)) {
                    throw new UnknownError("Field " + field.getName() + " annotated with @ConfigDefault but couldn't assign a default value.");
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getCast(@NotNull String key, @NotNull Class<T> type) {
        Object value = values.get(key);
        if (!type.isInstance(value)) {
            logCastError(key, type.getSimpleName());
        }
        return (T) value;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getDefaultCast(@NotNull String key, @NotNull Class<T> type) {
        Object value = defaults.get(key);
        if (!type.isInstance(value)) {
            logCastError(key, type.getSimpleName());
        }
        return (T) value;
    }

    private static void logCastError(@NotNull String key, String type) {
        CyberRewaredMod.LOGGER.error("Value for key '{}' is not of expected type: {}", key, type);
    }

    public static void set(@NotNull String key, @NotNull Object value) {
        if (!values.containsKey(key)) {
            CyberRewaredMod.LOGGER.error("Invalid config key: {}", key);
            return;
        }
        if (!values.get(key).getClass().isInstance(value)) {
            CyberRewaredMod.LOGGER.error("Type mismatch for key {}: expected {}, but got {}",
                key, values.get(key).getClass().getSimpleName(), value.getClass().getSimpleName());
            return;
        }
        values.put(key, value);
    }

    public static void save() {
        if (currentConfig == null) {
            CyberRewaredMod.LOGGER.warn("Config file not set, skipping save.");
            return;
        }

        try (FileWriter writer = new FileWriter(currentConfig)) {
            gson.toJson(values, writer);
        } catch (IOException e) {
            CyberRewaredMod.LOGGER.error("Failed to save config to {}", currentConfig.getPath(), e);
        }
    }

    @SuppressWarnings("unchecked")
    public static void load(Path configFolder) {
        values.putAll(defaults);
        Path modConfigPath = configFolder.resolve(CyberRewaredMod.MOD_ID + ".json");
        File file = currentConfig = new File(String.valueOf(modConfigPath));
        if (!file.exists()) {
            CyberRewaredMod.LOGGER.warn("Config file doesn't exist. Will use default values.");
            return;
        }

        try (FileReader reader = new FileReader(file)) {
            JsonObject data = gson.fromJson(reader, JsonObject.class);
            if (data != null) {
                for (String s : data.keySet()) {
                    values.put(s, gson.fromJson(data.get(s), defaults.get(s).getClass()));
                }
            } else {
                CyberRewaredMod.LOGGER.warn("Config file was empty or invalid, using default values.");
            }
        } catch (IOException e) {
            CyberRewaredMod.LOGGER.error("Failed to load config from {}, will use defaults", file.getPath(), e);
        }
    }
}
