package org.rhm.registries;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.potion.Potion;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.rhm.CyberRewaredMod;
import org.rhm.potion.NeuroStatusEffect;

// This class is not optimal!
public class PotionRegistry {
    public static final RegistryEntry<Potion> NEURO_POTION = register(
        "neuropozyne",
        new NeuroStatusEffect(),
        24000
    );

    public static RegistryEntry<Potion> register(String path, StatusEffect effect, int duration) {
        return CyberRewaredMod.potionRegisterFunc.apply(
            Identifier.of(CyberRewaredMod.MOD_ID, path),
            effect,
            duration
        );
    }

    public static void initialize() {

    }

}
