package org.rhm.registries;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.alchemy.Potion;
import org.rhm.CyberRewaredMod;
import org.rhm.potion.NeuroStatusEffect;

public class PotionRegistry {
    public static final Holder<Potion> NEURO_POTION = register(
        "neuropozyne",
        new NeuroStatusEffect(),
        24000
    );

    public static Holder<Potion> register(String path, MobEffect effect, int duration) {
        return CyberRewaredMod.potionRegisterFunc.apply(
            ResourceLocation.fromNamespaceAndPath(CyberRewaredMod.MOD_ID, path),
            effect,
            duration
        );
    }

    public static void initialize() {

    }

}
