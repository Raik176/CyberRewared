package org.rhm.registries;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.rhm.CyberRewaredMod;
import org.rhm.gui.BlueprintArchiveScreenHandler;
import org.rhm.gui.ComponentBoxScreenHandler;
import org.rhm.gui.EngineeringTableScreenHandler;
import org.rhm.gui.ScannerScreenHandler;
import org.rhm.gui.SurgeryScreenHandler;

@SuppressWarnings("unchecked")
public class ScreenHandlerRegistry {
    public static <M extends AbstractContainerMenu> MenuType<M> register(String path, ScreenHandlerFactory<M> shFactory) {
        return (MenuType<M>) CyberRewaredMod.screenHandlerRegisterFunc.apply(ResourceLocation.fromNamespaceAndPath(CyberRewaredMod.MOD_ID, path), shFactory);
    }

    public static void initialize() {

    }

    @FunctionalInterface
    public interface ScreenHandlerFactory<T extends AbstractContainerMenu> {
        T create(int syncId, Inventory playerInventory);
    }

    public static final MenuType<ScannerScreenHandler> SCANNER = register(
        "scanner",
        ScannerScreenHandler::new
    );
    public static final MenuType<BlueprintArchiveScreenHandler> BLUEPRINT_ARCHIVE = register(
        "blueprint_archive",
        BlueprintArchiveScreenHandler::new
    );
    public static final MenuType<ComponentBoxScreenHandler> COMPONENT_BOX = register(
        "component_box",
        ComponentBoxScreenHandler::new
    );
    public static final MenuType<EngineeringTableScreenHandler> ENGINEERING_TABLE = register(
        "engineering_table",
        EngineeringTableScreenHandler::new
    );
    public static final MenuType<SurgeryScreenHandler> SURGERY = register(
        "surgery",
        SurgeryScreenHandler::new
    );
}
