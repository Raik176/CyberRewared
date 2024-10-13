package org.rhm.registries;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.rhm.CyberRewaredMod;
import org.rhm.block.entity.BlueprintArchiveBlockEntity;
import org.rhm.block.entity.ChargerBlockEntity;
import org.rhm.block.entity.ComponentBoxBlockEntity;
import org.rhm.block.entity.EngineeringTableBlockEntity;
import org.rhm.block.entity.RadioPostBlockEntity;
import org.rhm.block.entity.ScannerBlockEntity;
import org.rhm.util.IEnergyStorage;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

// TODO: refactor code
// apparently this has to be loader specific...
@SuppressWarnings("unchecked")
public class BlockEntityRegistry {
    public static Map<BlockEntityType<?>, Block> modBlockEntities = new HashMap<>();

    public static <T extends BlockEntity> BlockEntityType<T> register(String path, BlockEntityFactory<T> be, Block block) {
        BlockEntityType<?> type = CyberRewaredMod.blockEntityRegisterFunc.apply(Identifier.of(CyberRewaredMod.MOD_ID, path), be, block);
        modBlockEntities.put(type, block);
        return (BlockEntityType<T>) type;
    }

    public static void initialize() {
        modBlockEntities = Collections.unmodifiableMap(modBlockEntities);
        modBlockEntities.forEach((bet, b) -> {
            if (bet.instantiate(new BlockPos(0, 0, 0), b.getDefaultState()) instanceof IEnergyStorage) {
                CyberRewaredMod.energyStorageRegisterFunc.accept((BlockEntityType<? extends IEnergyStorage>) bet);
            }
        });
    }

    // straight from mc lol
    @FunctionalInterface
    public interface BlockEntityFactory<T extends BlockEntity> {
        T create(BlockPos pos, BlockState state);
    }

    public static final BlockEntityType<ScannerBlockEntity> SCANNER = register(
        "scanner",
        ScannerBlockEntity::new,
        BlockRegistry.SCANNER
    );
    public static final BlockEntityType<BlueprintArchiveBlockEntity> BLUEPRINT_ARCHIVE = register(
        "blueprint_archive",
        BlueprintArchiveBlockEntity::new,
        BlockRegistry.BLUEPRINT_ARCHIVE
    );
    public static final BlockEntityType<ComponentBoxBlockEntity> COMPONENT_BOX = register(
        "component_box",
        ComponentBoxBlockEntity::new,
        BlockRegistry.COMPONENT_BOX
    );
    public static final BlockEntityType<EngineeringTableBlockEntity> ENGINEERING_TABLE = register(
        "engineering_table",
        EngineeringTableBlockEntity::new,
        BlockRegistry.ENGINEERING_TABLE
    );
    public static final BlockEntityType<ChargerBlockEntity> CHARGER = register(
        "charger",
        ChargerBlockEntity::new,
        BlockRegistry.CHARGER
    );
    public static final BlockEntityType<RadioPostBlockEntity> RADIO_POST = register(
        "radio_post",
        RadioPostBlockEntity::new,
        BlockRegistry.RADIO_POST
    );
}
