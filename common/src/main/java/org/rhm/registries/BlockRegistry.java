package org.rhm.registries;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.rhm.CyberRewaredMod;
import org.rhm.block.BlueprintArchiveBlock;
import org.rhm.block.ComponentBoxBlock;
import org.rhm.block.EngineeringTableBlock;
import org.rhm.block.ScannerBlock;

public class BlockRegistry {
    public static final Block SCANNER = register(
        "scanner",
        new ScannerBlock(),
        true
    );
    public static final Block BLUEPRINT_ARCHIVE = register(
        "blueprint_archive",
        new BlueprintArchiveBlock(),
        true
    );
    public static final Block COMPONENT_BOX = register(
        "component_box",
        new ComponentBoxBlock(),
        true
    );
    public static final Block ENGINEERING_TABLE = register(
        "engineering_table",
        new EngineeringTableBlock(),
        true
    );


    public static Block register(String path, Block entry, boolean registerItem) {
        if (registerItem) ItemRegistry.register(path, new BlockItem(entry, new Item.Settings()));

        return Registry.register(Registries.BLOCK, Identifier.of(CyberRewaredMod.MOD_ID, path), entry);
    }

    public static void initialize() {
    }
}
