package org.rhm.registries;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.Block;
import org.rhm.CyberRewaredMod;
import org.rhm.item.*;
import org.rhm.item.cyber.CyberArm;
import org.rhm.item.cyber.CyberEyes;
import org.rhm.item.cyber.CyberLeg;
import org.rhm.util.config.Config;

import java.util.*;

@SuppressWarnings("unused")
public class ItemRegistry {
    public static final Optional<Item> KATANA;
    // these are technically useless at runtime but i use them for datagen
    private static final HashMap<Block, BlockItem> blockItems = new HashMap<>();
    private static final HashMap<EntityType<?>, SpawnEggItem> spawnEggItems = new HashMap<>();
    public static List<Item> modItems = new ArrayList<>();
    public static final Item CYBEREYES = register(
        "cybereyes",
        new CyberEyes()
    );
    public static final Item CYBERARM = register(
        "cyberarm",
        new CyberArm()
    );
    public static final Item CYBERLEG = register(
        "cyberleg",
        new CyberLeg()
    );
    public static final Item HUMAN_ARM = register(
        "human_arm",
        new LimbItem(new Item.Properties())
    );
    public static final Item HUMAN_LEG = register(
        "human_leg",
        new LimbItem(new Item.Properties())
    );
    public static final Item HUMAN_BONES = register(
        "human_bones",
        new OrganItem(new Item.Properties())
    );
    public static final Item HUMAN_BRAIN = register(
        "human_brain",
        new OrganItem(new Item.Properties())
    );
    public static final Item HUMAN_EYES = register(
        "human_eyes",
        new OrganItem(new Item.Properties())
    );
    public static final Item HUMAN_HEART = register(
        "human_heart",
        new OrganItem(new Item.Properties())
    );
    public static final Item HUMAN_LUNGS = register(
        "human_lungs",
        new OrganItem(new Item.Properties())
    );
    public static final Item HUMAN_MUSCLES = register(
        "human_muscles",
        new OrganItem(new Item.Properties())
    );
    public static final Item HUMAN_SKIN = register(
        "human_skin",
        new OrganItem(new Item.Properties())
    );
    public static final Item HUMAN_STOMACH = register(
        "human_stomach",
        new OrganItem(new Item.Properties())
    );
    public static final Item HUMAN_LIVER = register(
        "human_liver",
        new OrganItem(new Item.Properties())
    );
    public static final Item BLUEPRINT = register(
        "blueprint",
        new BlueprintItem()
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

    static {
        if (Config.getCast(Config.KATANA_ENABLED, Boolean.class)) {
            KATANA = Optional.of(register(
                "katana",
                new KatanaItem()
            ));
        } else {
            KATANA = Optional.empty();
        }
    }


    public static Item register(String path, Item entry) {
        modItems.add(entry);
        if (entry instanceof BlockItem bi) {
            blockItems.put(bi.getBlock(), bi);
        }
        if (entry instanceof SpawnEggItem sei) {
            spawnEggItems.put(sei.getType(sei.getDefaultInstance()), sei);
        }
        CyberRewaredMod.itemRegisterFunc.accept(ResourceLocation.fromNamespaceAndPath(CyberRewaredMod.MOD_ID, path), entry);
        return entry;
    }

    public static BlockItem getBlockItem(Block block) {
        return blockItems.get(block);
    }

    public static SpawnEggItem getSpawnEgg(EntityType<?> entity) {
        return spawnEggItems.get(entity);
    }

    public static void initialize() {
        // For the creative tab
        modItems.sort(new Comparator<>() {
            @Override
            public int compare(Item o1, Item o2) {
                return getItemPriority(o1) - getItemPriority(o2);
            }

            private int getItemPriority(Item item) {
                return switch (item) {
                    case BlockItem blockItem -> 1;
                    case CyberItem cyberItem -> 2;
                    case LimbItem limbItem -> 3;
                    case OrganItem organItem -> 4;
                    case ComponentItem componentItem -> 5;
                    case null, default -> 6;
                };
            }
        });

        modItems = Collections.unmodifiableList(modItems);
    }

    public static ItemStack[] getAllSubTypes(Item item) {
        switch (item) {
            case LimbItem li -> {
                ItemStack stack = new ItemStack(li);
                stack.set(ComponentRegistry.IS_RIGHT, true);
                return new ItemStack[]{new ItemStack(li), stack};
            }
            case CyberLimbItem cli -> {
                ItemStack stack1 = new ItemStack(cli);
                stack1.set(ComponentRegistry.IS_RIGHT, true);
                ItemStack stack2 = new ItemStack(cli);
                stack2.set(ComponentRegistry.IS_RIGHT, true);
                stack2.set(ComponentRegistry.SCAVENGED, true);
                ItemStack stack3 = new ItemStack(cli);
                stack3.set(ComponentRegistry.SCAVENGED, true);
                return new ItemStack[]{new ItemStack(cli), stack1, stack3, stack2};
            }
            case CyberItem ci -> {
                ItemStack stack = new ItemStack(ci);
                stack.set(ComponentRegistry.SCAVENGED, true);
                return new ItemStack[]{new ItemStack(ci), stack};
            }
            default -> {
                return new ItemStack[]{new ItemStack(item)};
            }
        }
    }

    public static List<Object> getCreativeEntries() {
        List<Object> entries = new ArrayList<>();

        for (Item i : modItems) {
            entries.addAll(Arrays.stream(getAllSubTypes(i)).toList());
        }

        return entries;
    }
}
