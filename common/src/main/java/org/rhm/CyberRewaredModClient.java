package org.rhm;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.rhm.registries.PacketRegistry;
import org.rhm.util.Config;

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

        general.addEntry(entryBuilder.startFloatField(getConfigName("engineering_chance"), Config.ENGINEERING_CHANCE)
            .setDefaultValue(Config.ENGINEERING_CHANCE_DEFAULT)
            .setTooltip(getConfigTtp("engineering_chance"))
            .setSaveConsumer(newValue -> Config.ENGINEERING_CHANCE = newValue)
            .setMax(100)
            .setMin(1)
            .build());
        general.addEntry(entryBuilder.startFloatField(getConfigName("brute_chance"), Config.NATURAL_BRUTE_CHANCE)
            .setDefaultValue(Config.NATURAL_BRUTE_CHANCE_DEFAULT)
            .setTooltip(getConfigTtp("brute_chance"))
            .setSaveConsumer(newValue -> Config.NATURAL_BRUTE_CHANCE = newValue)
            .setMax(100)
            .setMin(1)
            .build());

        return builder
            .setParentScreen(parent)
            .setTitle(Component.translatable("config." + CyberRewaredMod.MOD_ID + ".title"))
            .setSavingRunnable(Config::save)
            .build();
    }
}
