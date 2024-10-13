package org.rhm.registries;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;
import org.rhm.CyberRewaredMod;
import org.rhm.item.ComponentItem;
import org.rhm.item.LimbItem;
import org.rhm.item.NeuroItem;
import org.rhm.item.OrganItem;
import org.rhm.item.XPCapsuleItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("unused")
public class ItemRegistry {
    // these are technically useless at runtime but i use them for datagen
    private static final HashMap<Block, BlockItem> blockItems = new HashMap<>();
    private static final HashMap<EntityType<?>, SpawnEggItem> spawnEggItems = new HashMap<>();
    public static List<Item> modItems = new ArrayList<>();
    public static final Item HUMAN_ARM = register(
        "human_arm",
        new LimbItem(new Item.Settings())
    );
    public static final Item HUMAN_LEG = register(
        "human_leg",
        new LimbItem(new Item.Settings())
    );
    public static final Item HUMAN_BONES = register(
        "human_bones",
        new OrganItem(new Item.Settings())
    );
    public static final Item HUMAN_BRAIN = register(
        "human_brain",
        new OrganItem(new Item.Settings())
    );
    public static final Item HUMAN_EYES = register(
        "human_eyes",
        new OrganItem(new Item.Settings())
    );
    public static final Item HUMAN_HEART = register(
        "human_heart",
        new OrganItem(new Item.Settings())
    );
    public static final Item HUMAN_LUNGS = register(
        "human_lungs",
        new OrganItem(new Item.Settings())
    );
    public static final Item HUMAN_MUSCLES = register(
        "human_muscles",
        new OrganItem(new Item.Settings())
    );
    public static final Item HUMAN_SKIN = register(
        "human_skin",
        new OrganItem(new Item.Settings())
    );
    public static final Item HUMAN_STOMACH = register(
        "human_stomach",
        new OrganItem(new Item.Settings())
    );

    public static final Item NEURO_SYRINGE = register(
        "neuropozyne",
        new NeuroItem()
    );

    public static final Item ACTUATOR = register(
        "actuator",
        new ComponentItem()
    );
    public static final Item BIOREACTOR = register(
        "bioreactor",
        new ComponentItem()
    );
    public static final Item TITANIUM_MESH = register(
        "titanium_mesh",
        new ComponentItem()
    );
    public static final Item SOLID_STATE_CIRCUITRY = register(
        "solid_state_circuitry",
        new ComponentItem()
    );
    public static final Item CHROME_PLATING = register(
        "chrome_plating",
        new ComponentItem()
    );
    public static final Item FIBER_OPTICS = register(
        "fiber_optics",
        new ComponentItem()
    );
    public static final Item FULLERENE_MICROSTRUCTURES = register(
        "fullerene_microstructures",
        new ComponentItem()
    );
    public static final Item SYNTHETIC_NERVES = register(
        "synthetic_nerves",
        new ComponentItem()
    );
    public static final Item STORAGE_CELL = register(
        "storage_cell",
        new ComponentItem()
    );
    public static final Item MICROELECTRIC_CELLS = register(
        "microelectric_cells",
        new ComponentItem()
    );

    public static final Item XP_CAPSULE = register(
        "xp_capsule",
        new XPCapsuleItem()
    );


    public static Item register(String path, Item entry) {
        modItems.add(entry);
        if (entry instanceof BlockItem bi) {
            blockItems.put(bi.getBlock(), bi);
        }
        if (entry instanceof SpawnEggItem sei) {
            spawnEggItems.put(sei.getEntityType(sei.getDefaultStack()), sei);
        }
        CyberRewaredMod.itemRegisterFunc.accept(Identifier.of(CyberRewaredMod.MOD_ID, path), entry);
        return entry;
    }

    public static BlockItem getBlockItem(Block block) {
        return blockItems.get(block);
    }

    public static SpawnEggItem getSpawnEgg(EntityType<?> entity) {
        return spawnEggItems.get(entity);
    }

    public static void initialize() {
        //For the creative tab
        modItems.sort(new Comparator<>() {
            @Override
            public int compare(Item o1, Item o2) {
                return getItemPriority(o1) - getItemPriority(o2);
            }

            private int getItemPriority(Item item) {
                return switch (item) {
                    case BlockItem blockItem -> 1;
                    case LimbItem limbItem -> 2;
                    case OrganItem organItem -> 3;
                    case ComponentItem componentItem -> 4;
                    case null, default -> 5;
                };
            }
        });

        modItems = Collections.unmodifiableList(modItems);
    }
}
