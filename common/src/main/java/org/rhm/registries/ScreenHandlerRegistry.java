package org.rhm.registries;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import org.rhm.CyberRewaredMod;
import org.rhm.gui.BlueprintArchiveScreenHandler;
import org.rhm.gui.ComponentBoxScreenHandler;
import org.rhm.gui.EngineeringTableScreenHandler;
import org.rhm.gui.ScannerScreenHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public class ScreenHandlerRegistry {
    public static final Map<Identifier, ScreenHandlerType<? extends ScreenHandler>> SCREEN_HANDLER_TYPES = new HashMap<>();
    public static final Map<Identifier, ScreenHandlerFactory<? extends ScreenHandler>> SCREEN_HANDLER_RAW = new HashMap<>();

    public static <M extends ScreenHandler> Supplier<ScreenHandlerType<M>> register(String path, ScreenHandlerFactory<M> shFactory) {
        Identifier id = Identifier.of(CyberRewaredMod.MOD_ID, path);
        SCREEN_HANDLER_RAW.put(id, shFactory);

        return () -> (ScreenHandlerType<M>) SCREEN_HANDLER_TYPES.get(id);
    }

    public static void initialize() {

    }

    @FunctionalInterface
    public interface ScreenHandlerFactory<T extends ScreenHandler> {
        T create(int syncId, PlayerInventory playerInventory);
    }

    public static final Supplier<ScreenHandlerType<ScannerScreenHandler>> SCANNER = register(
        "scanner",
        ScannerScreenHandler::new
    );
    public static final Supplier<ScreenHandlerType<BlueprintArchiveScreenHandler>> BLUEPRINT_ARCHIVE = register(
        "blueprint_archive",
        BlueprintArchiveScreenHandler::new
    );
    public static final Supplier<ScreenHandlerType<ComponentBoxScreenHandler>> COMPONENT_BOX = register(
        "component_box",
        ComponentBoxScreenHandler::new
    );
    public static final Supplier<ScreenHandlerType<EngineeringTableScreenHandler>> ENGINEERING_TABLE = register(
        "engineering_table",
        EngineeringTableScreenHandler::new
    );
}
