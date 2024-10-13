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

@SuppressWarnings("unchecked")
public class ScreenHandlerRegistry {
    public static <M extends ScreenHandler> ScreenHandlerType<M> register(String path, ScreenHandlerFactory<M> shFactory) {
        return (ScreenHandlerType<M>) CyberRewaredMod.screenHandlerRegisterFunc.apply(Identifier.of(CyberRewaredMod.MOD_ID, path), shFactory);
    }

    public static void initialize() {

    }

    @FunctionalInterface
    public interface ScreenHandlerFactory<T extends ScreenHandler> {
        T create(int syncId, PlayerInventory playerInventory);
    }

    public static final ScreenHandlerType<ScannerScreenHandler> SCANNER = register(
        "scanner",
        ScannerScreenHandler::new
    );
    public static final ScreenHandlerType<BlueprintArchiveScreenHandler> BLUEPRINT_ARCHIVE = register(
        "blueprint_archive",
        BlueprintArchiveScreenHandler::new
    );
    public static final ScreenHandlerType<ComponentBoxScreenHandler> COMPONENT_BOX = register(
        "component_box",
        ComponentBoxScreenHandler::new
    );
    public static final ScreenHandlerType<EngineeringTableScreenHandler> ENGINEERING_TABLE = register(
        "engineering_table",
        EngineeringTableScreenHandler::new
    );
}
