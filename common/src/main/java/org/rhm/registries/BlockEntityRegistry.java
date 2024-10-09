package org.rhm.registries;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.rhm.CyberRewaredMod;
import org.rhm.block.entity.BlueprintArchiveBlockEntity;
import org.rhm.block.entity.ComponentBoxBlockEntity;
import org.rhm.block.entity.EngineeringTableBlockEntity;
import org.rhm.block.entity.ScannerBlockEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

// TODO: refactor code
// apparently this has to be loader specific...
@SuppressWarnings("unchecked")
public class BlockEntityRegistry {
    // time for some shitty code lololol
    public static final Map<Identifier, BlockEntityType<? extends BlockEntity>> BLOCK_ENTITY_TYPES = new HashMap<>();
    // each mod loader will loop this and then register the block entity
    public static final Map<Identifier, Map.Entry<BlockEntityFactory<? extends BlockEntity>, Block>> BLOCK_ENTITY_RAW = new HashMap<>();

    public static <T extends BlockEntity> Supplier<BlockEntityType<T>> register(String path, BlockEntityFactory<T> be, Block block) {
        Identifier id = Identifier.of(CyberRewaredMod.MOD_ID, path);
        BLOCK_ENTITY_RAW.put(id, Map.entry(be, block));
        return () -> (BlockEntityType<T>) BLOCK_ENTITY_TYPES.get(id);
    }

    public static void initialize() {
    }

    // straight from mc lol
    @FunctionalInterface
    public interface BlockEntityFactory<T extends BlockEntity> {
        T create(BlockPos pos, BlockState state);
    }

    public static final Supplier<BlockEntityType<ScannerBlockEntity>> SCANNER = register(
        "scanner",
        ScannerBlockEntity::new,
        BlockRegistry.SCANNER
    );
    public static final Supplier<BlockEntityType<BlueprintArchiveBlockEntity>> BLUEPRINT_ARCHIVE = register(
        "blueprint_archive",
        BlueprintArchiveBlockEntity::new,
        BlockRegistry.BLUEPRINT_ARCHIVE
    );
    public static final Supplier<BlockEntityType<ComponentBoxBlockEntity>> COMPONENT_BOX = register(
        "component_box",
        ComponentBoxBlockEntity::new,
        BlockRegistry.COMPONENT_BOX
    );
    public static final Supplier<BlockEntityType<EngineeringTableBlockEntity>> ENGINEERING_TABLE = register(
        "engineering_table",
        EngineeringTableBlockEntity::new,
        BlockRegistry.ENGINEERING_TABLE
    );
}
