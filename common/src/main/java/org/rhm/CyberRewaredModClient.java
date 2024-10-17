package org.rhm;

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.api.Requirement;
import me.shedaniel.clothconfig2.impl.builders.FieldBuilder;
import me.shedaniel.clothconfig2.impl.builders.FloatFieldBuilder;
import me.shedaniel.clothconfig2.impl.builders.IntFieldBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.rhm.registries.PacketRegistry;
import org.rhm.util.config.Config;
import org.rhm.util.config.ConfigDepends;
import org.rhm.util.config.ConfigRange;
import org.rhm.util.config.ConfigRequiresRestart;

import java.lang.reflect.Field;
import java.util.HashMap;

public class CyberRewaredModClient {
    public static void init() {
        PacketRegistry.initializeClient();
    }

    private static Component getConfigName(String field) {
        return Component.translatable("config." + CyberRewaredMod.MOD_ID + ".option." + field);
    }

    private static Component getConfigTtp(String field) {
        return Component.translatable("config." + CyberRewaredMod.MOD_ID + ".option." + field + ".ttp");
    }

    public static Screen getConfigScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create();
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        ConfigCategory general = builder.getOrCreateCategory(Component.literal("N/A")); // This isn't shown unless we have more than 1 tab

        HashMap<String, AbstractConfigListEntry<?>> fields = new HashMap<>(); // only doing this so requirements work

        for (Field field : Config.class.getDeclaredFields()) {
            if (!field.getType().equals(String.class)) continue;
            String fieldName;
            try {
                fieldName = (String) field.get(null);
            } catch (IllegalAccessException e) {
                CyberRewaredMod.LOGGER.error("Failed to access field: {}", field.getName(), e);
                continue;
            }
            Object configValue = Config.values.get(fieldName);
            FieldBuilder<?, ?, ?> genericFieldBuilder = null;
            switch (configValue) {
                case Float value -> {
                    FloatFieldBuilder fieldBuilder = entryBuilder.startFloatField(getConfigName(fieldName), value)
                        .setDefaultValue(() -> Config.getDefaultCast(fieldName, Float.class))
                        .setTooltip(getConfigTtp(fieldName))
                        .setSaveConsumer(newValue -> Config.set(fieldName, newValue));
                    if (field.isAnnotationPresent(ConfigRange.class)) {
                        ConfigRange range = field.getAnnotation(ConfigRange.class);
                        fieldBuilder.setMin(range.minFloat()).setMax(range.maxFloat());
                    }
                    genericFieldBuilder = fieldBuilder;
                }
                case Integer value -> {
                    IntFieldBuilder fieldBuilder = entryBuilder.startIntField(getConfigName(fieldName), value)
                        .setDefaultValue(() -> Config.getDefaultCast(fieldName, Integer.class))
                        .setTooltip(getConfigTtp(fieldName))
                        .setSaveConsumer(newValue -> Config.set(fieldName, newValue));
                    if (field.isAnnotationPresent(ConfigRange.class)) {
                        ConfigRange range = field.getAnnotation(ConfigRange.class);
                        fieldBuilder.setMin(range.minInt()).setMax(range.maxInt());
                    }
                    genericFieldBuilder = fieldBuilder;
                }
                case Boolean value -> {
                    genericFieldBuilder = entryBuilder.startBooleanToggle(getConfigName(fieldName), value)
                        .setDefaultValue(() -> Config.getDefaultCast(fieldName, Boolean.class))
                        .setTooltip(getConfigTtp(fieldName))
                        .setSaveConsumer(newValue -> Config.set(fieldName, newValue));
                }
                case null, default ->
                    CyberRewaredMod.LOGGER.error("Field {} of type {} is not handled.", fieldName, configValue.getClass().getSimpleName());
            }
            if (genericFieldBuilder != null) {
                if (field.isAnnotationPresent(ConfigRequiresRestart.class)) {
                    genericFieldBuilder.requireRestart(true);
                }
                if (field.isAnnotationPresent(ConfigDepends.class)) {
                    ConfigDepends depends = field.getAnnotation(ConfigDepends.class);
                    for (String s : depends.fields()) {
                        if (Config.values.containsKey(s) && Config.values.get(s) instanceof Boolean) {
                            // TODO: i have no idea how requirements work in cloth config
                        }
                    }
                }
                AbstractConfigListEntry<?> entry = genericFieldBuilder.build();
                general.addEntry(entry);
                fields.put(fieldName, entry);
            }
        }

        return builder
            .setParentScreen(parent)
            .setTitle(Component.translatable("config." + CyberRewaredMod.MOD_ID + ".title"))
            .setSavingRunnable(Config::save)
            .build();
    }
}
