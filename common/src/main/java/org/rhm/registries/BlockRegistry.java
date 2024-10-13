package org.rhm.registries;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Identifier;
import org.rhm.CyberRewaredMod;
import org.rhm.block.*;
import org.rhm.item.ModBlockItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlockRegistry {
    public static List<Block> blocks = new ArrayList<>();

    public static final Block BLUEPRINT_ARCHIVE = register(
        "blueprint_archive",
        new BlueprintArchiveBlock()
    );
    public static final Block COMPONENT_BOX = register(
        "component_box",
        new ComponentBoxBlock()
    );
    public static final Block ROBOSURGEON = register(
        "robosurgeon",
        new RobosurgeonBlock()
    );
    public static final Block SURGERY_CHAMBER = register(
        "surgery_chamber",
        new SurgeryChamberBlock()
    );
    public static final Block CHARGER = register(
        "charger",
        new ChargerBlock()
    );
    public static final Block ENGINEERING_TABLE = register(
        "engineering_table",
        new EngineeringTableBlock()
    );
    public static final Block SCANNER = register(
        "scanner",
        new ScannerBlock()
    );
    public static final Block RADIO_KIT = register(
        "radio_kit",
        new RadioKitBlock()
    );
    public static final Block RADIO_POST = register(
        "radio_post",
        new RadioPostBlock()
    );


    public static Block register(String path, Block entry) {
        return register(path, entry, new ModBlockItem(entry));
    }

    public static Block register(String path, Block entry, BlockItem item) {
        ItemRegistry.register(path, item);
        CyberRewaredMod.blockRegisterFunc.accept(Identifier.of(CyberRewaredMod.MOD_ID, path), entry);
        blocks.add(entry);
        return entry;
    }

    public static void initialize() {
        blocks = Collections.unmodifiableList(blocks);
    }
}
