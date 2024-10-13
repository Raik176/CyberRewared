package org.rhm.registries;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.util.Identifier;
import org.rhm.CyberRewaredMod;

public class ComponentRegistry {
    public static final ComponentType<Boolean> IS_RIGHT = register(
        "is_right",
        ComponentType.<Boolean>builder().codec(Codec.BOOL).build()
    );
    public static final ComponentType<Boolean> SCAVENGED = register(
        "scavenged",
        ComponentType.<Boolean>builder().codec(Codec.BOOL).build()
    );
    public static final ComponentType<Integer> XP_STORED = register(
        "xp_stored",
        ComponentType.<Integer>builder().codec(Codec.INT).build()
    );

    public static <T> ComponentType<T> register(String path, ComponentType<T> component) {
        CyberRewaredMod.componentRegisterFunc.accept(Identifier.of(CyberRewaredMod.MOD_ID, path), component);
        return component;
    }

    public static void initialize() {

    }
}
