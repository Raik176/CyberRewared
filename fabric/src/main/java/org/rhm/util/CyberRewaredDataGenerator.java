package org.rhm.util;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import org.rhm.CyberRewaredMod;
import org.rhm.item.ComponentItem;
import org.rhm.item.CyberItem;
import org.rhm.item.CyberLimbItem;
import org.rhm.item.LimbItem;
import org.rhm.registries.BlockRegistry;
import org.rhm.registries.ComponentRegistry;
import org.rhm.registries.EntityRegistry;
import org.rhm.registries.ItemRegistry;
import org.rhm.registries.PotionRegistry;
import org.rhm.util.config.Config;
import org.rhm.util.config.ConfigTranslation;
import org.rhm.util.config.ConfigTranslations;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class CyberRewaredDataGenerator implements DataGeneratorEntrypoint {
    public static final String IS_SCAVENGED = Objects.requireNonNull(BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(ComponentRegistry.SCAVENGED)).toString();
    ;
    public static final String IS_RIGHT = Objects.requireNonNull(BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(ComponentRegistry.IS_RIGHT)).toString();

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(BlockLootTableProvider::new);
        pack.addProvider(ItemTagProvider::new);
        pack.addProvider(BlockTagProvider::new);
        pack.addProvider(RecipeProvider::new);
        pack.addProvider(ModelProvider::new);

        pack.addProvider((dataOutput, registryLookup) -> new LanguageProvider(dataOutput, "en_us", registryLookup) {
            @Override
            public void generateTranslations(HolderLookup.Provider wrapperLookup, TranslationBuilder builder) {
                builder.add(CyberRewaredMod.MOD_ID + ".itemGroup", "Cyber Rewared");

                // Config
                builder.add("config." + CyberRewaredMod.MOD_ID + ".title", "Cyber Rewared Config");
                for (Field field : Config.class.getDeclaredFields()) {
                    ConfigTranslations translations = field.getAnnotation(ConfigTranslations.class);
                    ConfigTranslation translationAnnotation = field.getAnnotation(ConfigTranslation.class);
                    ConfigTranslation[] translationsArray;
                    if (translations != null) {
                        translationsArray = translations.value();
                    } else if (translationAnnotation != null) {
                        translationsArray = new ConfigTranslation[]{translationAnnotation};
                    } else {
                        continue;
                    }
                    boolean foundCurrentLocale = false;
                    for (ConfigTranslation translation : translationsArray) {
                        if (foundCurrentLocale) {
                            throw new IllegalStateException("Multiple translations found for field: " + field.getName());
                        }
                        if (Objects.equals(translation.locale(), locale)) {
                            foundCurrentLocale = true;
                            String fieldName;
                            try {
                                fieldName = (String) field.get(null);
                            } catch (IllegalAccessException e) {
                                throw new RuntimeException("Access failed for field: " + field.getName(), e);
                            }
                            builder.add("config." + CyberRewaredMod.MOD_ID + ".option." + fieldName, translation.name());
                            builder.add("config." + CyberRewaredMod.MOD_ID + ".option." + fieldName + ".ttp", String.join("\n", translation.description()));
                        }
                    }
                }

                // JEI
                builder.add(CyberRewaredMod.MOD_ID + ".jei.smash", "Engineering (Smashing)");
                builder.add(CyberRewaredMod.MOD_ID + ".jei.craft", "Engineering (Crafting)");
                builder.add(CyberRewaredMod.MOD_ID + ".jei.scan", "Scanning");
                builder.add(CyberRewaredMod.MOD_ID + ".jei.paper_ttp", "Add Paper for chance of obtaining Blueprint.");

                builder.add(CyberItem.SCAVENGED_KEY, "Scavenged");
                builder.add(CyberItem.MANUFACTURED_KEY, "Manufactured");
                builder.add(CyberItem.SLOT_KEY, "%s slot");
                builder.add(CyberItem.TOLERANCE_KEY, "Tolerance cost: %s");
                builder.add(CyberItem.POWER_KEY, "Requires %s pow/s when in use");
                builder.add(CyberItem.REQUIRED_KEY, "Requires %s");
                builder.add(CyberItem.INCOMPATIBLE_KEY, "Incompatible with %s");

                builder.add(CyberRewaredMod.COMPONENT_TAG, "Cyber Component");

                builder.add(ItemRegistry.CYBEREYES, "Cybereyes");
                builder.add(ItemRegistry.CYBEREYES.getDescriptionId() + ".ttp", "Immunity to Blindness\nEnables Cybereye upgrades.");

                builder.add(ItemRegistry.CYBERARM, "%s Cyberarm");
                builder.add(ItemRegistry.CYBERARM.getDescriptionId() + ".ttp", "Enables Cyberarm upgrades.");

                builder.add(ItemRegistry.CYBERLEG, "%s Cyberleg");
                builder.add(ItemRegistry.CYBERLEG.getDescriptionId() + ".ttp", "Enables Cyberleg upgrades.");

                if (ItemRegistry.KATANA.isEmpty()) {
                    throw new IllegalStateException("Running datagen without having all item's enabled. Please reset the config.");
                }
                builder.add(ItemRegistry.KATANA.get(), "Katana");

                builder.add(ItemRegistry.BLUEPRINT, "%s Blueprint");
                builder.add("item." + CyberRewaredMod.MOD_ID + ".blueprint.empty", "Blank");
                builder.add(ItemRegistry.BLUEPRINT.getDescriptionId() + ".empty_ttp", "Craft with any Cyberware to create valid Blueprint.");
                builder.add("item." + CyberRewaredMod.MOD_ID + ".limb.left", "Left");
                builder.add("item." + CyberRewaredMod.MOD_ID + ".limb.right", "Right");
                builder.add(ItemRegistry.HUMAN_ARM, "Human %s Arm");
                builder.add(ItemRegistry.HUMAN_LEG, "Human %s Leg");
                builder.add(ItemRegistry.XP_CAPSULE, "Experience Capsule");
                builder.add(ItemRegistry.XP_CAPSULE.getDescriptionId() + ".ttp", "%s recorded experience stored.");
                builder.add(ItemRegistry.HUMAN_BONES, "Human Bones");
                builder.add(ItemRegistry.HUMAN_BRAIN, "Human Brain");
                builder.add(ItemRegistry.HUMAN_EYES, "Human Eyes");
                builder.add(ItemRegistry.HUMAN_HEART, "Human Heart");
                builder.add(ItemRegistry.HUMAN_LUNGS, "Human Lungs");
                builder.add(ItemRegistry.HUMAN_MUSCLES, "Human Muscles");
                builder.add(ItemRegistry.HUMAN_SKIN, "Human Skin");
                builder.add(ItemRegistry.HUMAN_STOMACH, "Human Stomach");
                builder.add(ItemRegistry.HUMAN_LIVER, "Human Liver");

                builder.add(EntityRegistry.CYBERZOMBIE, "Cyberzombie");
                builder.add(ItemRegistry.getSpawnEgg(EntityRegistry.CYBERZOMBIE), "Cyberzombie Spawn Egg");

                builder.add(BlockRegistry.SCANNER, "Scanner");
                builder.add(BlockRegistry.SCANNER.getDescriptionId() + ".ttp", "Analyzes Cyberware to create Blueprints.");
                builder.add(CyberRewaredMod.MOD_ID + ".gui.percent", "%1$s%% Chance");
                builder.add(CyberRewaredMod.MOD_ID + ".gui.insertPaper", "Insert Paper");
                builder.add(CyberRewaredMod.MOD_ID + ".gui.toScan", "Insert Cyberware to Scan");
                builder.add(CyberRewaredMod.MOD_ID + ".gui.scanProgress", "%s Remaining (%s/%s Ticks)");
                builder.add(CyberRewaredMod.MOD_ID + ".gui.notScanning", "Waiting for input.");
                builder.add(CyberRewaredMod.MOD_ID + ".gui.scannerSayings", String.join("\n", new String[]{
                    "Reticulating Splines", "Resolving GUID Conflict", "Perturbing Matrices",
                    "Obfuscating Quigley Matrix", "Integrating Curves", "Burning SINs",
                    "Graphing Whale Migration", "Asserting Packed Exemplars", "Simulating Thermonuclear War",
                    "Updating to Windows 10", "Predicting Lottery Numbers", "Uprooting Data Trees",
                    "Attempting POST Operations", "Subscribing Event Handlers", "Installing Ask Toolbar",
                    "Suppressing Propaganda", "Seizing Means of Production", "Rebalancing Other Mods",
                    "Refreshing Reddit Buffer", "Deciphering Zodiac Letters", "Paging Through Rulebook",
                    "Rolling 4d6", "Unwithering Passive Flowers", "Merging Pull Requests",
                    "Repeating Seen Sayings", "Converting Binary to Octal", "Dicing Models",
                    "Gesticulating Mimes", "Splatting Transforms", "Sequencing Particles",
                    "Searching for Llamas", "Retracting Phong Shader", "Reimplementing Quick play",
                    "Removing Microtransactions", "Lecturing Errant Subsystems", "Iterating Cellular Automata",
                    "Applying Feng Shui Shaders", "Bypassing DRM Measures", "Generating Stupid Messages",
                    "Gathering Particle Sources", "Splicing Genetic Sources", "Unveiling Herobrine",
                    "Flashing Customized BIOS", "Recommending Roots", "Enforcing EULA Stipulations",
                    "Quelling Uprising", "Calculating Screw Direction", "Modeling Cyberware Housing",
                    "Polishing Scanner Lens", "Extracting Firmware", "Cracking License Key",
                    "Subdividing Strands", "Constructing Simulation", "Routing Neural Networks",
                    "Monitoring Electron Travel", "Zeroing Instrument Error", "Recalculating Route",
                    "Partitioning Singularities", "Compressing Internal Clock", "Resetting Water Indicators",
                    "Analyzing Branding Details", "Performing Yulife Calculation", "Abstracting Noise Layers",
                    "Sifting Neural Particles", "Solving Differential Equations", "Commencing Spatial Simulation",
                    "Instantiating Child Elements", "Wiping System32", "Remembering Lycaon",
                    // These are not from the original Cyberware
                    "Recycling Bad Jokes", "Debugging Life Choices", "Installing Windows on a Cat", "404 Page not found",
                    "Updating JRE", "Compiling new messages", "Connecting to Matrix", "Downloading Cat Videos",
                    "Syncing Timezones with Mars", "KERNEL PANIC! Restarting", "Debugging Debugger", "Compiling Regrets",
                    "Fixing recursive loop", "Catching Exceptions like Pokémon",
                    "Forking Reality", "Blaming the Compiler for Bugs",
                    "Compiling Code at 3AM", "Translating Coffee into Code", "Segmentation Fault",
                    "Applying Hotfix for Cold Coffee"
                }));

                builder.add(BlockRegistry.BLUEPRINT_ARCHIVE, "Blueprint Archive");
                builder.add(BlockRegistry.BLUEPRINT_ARCHIVE.getDescriptionId() + ".ttp", "Stores Cyberware Blueprints.\nAccessible by Engineering Table if adjacent.");

                builder.add(BlockRegistry.COMPONENT_BOX, "Component Box");
                builder.add(BlockRegistry.COMPONENT_BOX.getDescriptionId() + ".ttp", "Stores Components.\nAccessible by Engineering Table if adjacent.");

                builder.add(BlockRegistry.ENGINEERING_TABLE, "Engineering Table");
                builder.add(BlockRegistry.ENGINEERING_TABLE.getDescriptionId() + ".ttp", "Used to dismantle and create Cyberware.");
                builder.add(CyberRewaredMod.MOD_ID + ".gui.smash", "Void Warranty");
                builder.add(CyberRewaredMod.MOD_ID + ".gui.smash_chance", "%1$s%% Chance for Blueprint");
                // this is "to Destroy" in the original but i think salvage sounds better
                builder.add(CyberRewaredMod.MOD_ID + ".gui.insertSalvage", "Insert Cyberware to Salvage");
                builder.add(CyberRewaredMod.MOD_ID + ".gui.insertBlueprint", "Insert Blueprint");

                builder.add(BlockRegistry.SURGERY_CHAMBER, "Surgery Chamber");
                builder.add(BlockRegistry.SURGERY_CHAMBER.getDescriptionId() + ".ttp", "Performs Operations for the Robosurgeon.\nRight-click while inside to activate .");

                builder.add(BlockRegistry.CHARGER, "Charger");
                builder.add(BlockRegistry.CHARGER.getDescriptionId() + ".ttp", "Charges entities with Cyberware.\nAccepts TR Energy.");

                builder.add(BlockRegistry.ROBOSURGEON, "Robosurgeon");
                builder.add(BlockRegistry.ROBOSURGEON.getDescriptionId() + ".ttp", "Used to set what Cyberware to install or remove.\nPlace on top of a Surgery Chamber to use.");

                builder.add(BlockRegistry.RADIO_KIT, "Radio Kit");
                builder.add(BlockRegistry.RADIO_KIT.getDescriptionId() + ".ttp", "Slightly increases Cyberzombie spawn rate");

                builder.add(BlockRegistry.RADIO_POST, "Radio Tower Component");
                builder.add(BlockRegistry.RADIO_POST.getDescriptionId() + ".ttp", "Constructs a large Radio.\nTo form:\n  - Place 4 layers of 3x3 components. (3x4x3)\n  - 6 layers of a single component, centered\n    over the base.");

                builder.add(ItemRegistry.ACTUATOR, "Actuator");
                builder.add(ItemRegistry.BIOREACTOR, "Bioreactor");
                builder.add(ItemRegistry.TITANIUM_MESH, "Titanium Mesh");
                builder.add(ItemRegistry.SOLID_STATE_CIRCUITRY, "Solid-State Circuitry");
                builder.add(ItemRegistry.CHROME_PLATING, "Chrome Plating");
                builder.add(ItemRegistry.FIBER_OPTICS, "Fiber Optics");
                builder.add(ItemRegistry.FULLERENE_MICROSTRUCTURES, "Fullerene Microstructures");
                builder.add(ItemRegistry.SYNTHETIC_NERVES, "Synthetic Nerves");
                builder.add(ItemRegistry.STORAGE_CELL, "Storage Cell");
                builder.add(ItemRegistry.MICROELECTRIC_CELLS, "Microelectric Cells");

                builder.add(ItemRegistry.NEURO_SYRINGE, "Neuropozyne");
                builder.add(ItemRegistry.NEURO_SYRINGE.getDescriptionId() + ".ttp", "Anti-Rejection (20:00)");
                // This is so cursed but i don't wanna make another variable
                builder.add(PotionRegistry.NEURO_POTION.value().getEffects().getFirst().getEffect().value(), "Anti-Rejection");
            }
        });
    }

    public static class ModelProvider extends FabricModelProvider {

        public ModelProvider(FabricDataOutput output) {
            super(output);
        }

        @Override
        public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {

        }

        @Override
        public void generateItemModels(ItemModelGenerators itemModelGenerator) {
            for (Item modItem : ItemRegistry.modItems) {
                if (!(modItem instanceof LimbItem) && modItem instanceof IGeneratedModel) {
                    itemModelGenerator.generateFlatItem(modItem, ModelTemplates.FLAT_ITEM);
                } else if (modItem instanceof LimbItem item) {
                    itemModelGenerator.generateFlatItem(item, new LimbModelTemplate(false));
                    itemModelGenerator.generateFlatItem(item, new LimbModelTemplate(true));
                } else if (modItem instanceof CyberLimbItem item) {
                    itemModelGenerator.generateFlatItem(item, new CyberLimbModelTemplate(false, false));
                    itemModelGenerator.generateFlatItem(item, new CyberLimbModelTemplate(true, false));
                    itemModelGenerator.generateFlatItem(item, new CyberLimbModelTemplate(true, true));
                    itemModelGenerator.generateFlatItem(item, new CyberLimbModelTemplate(false, true));
                } else if (modItem instanceof CyberItem item) {
                    itemModelGenerator.generateFlatItem(item, new CyberModelTemplate(false));
                    itemModelGenerator.generateFlatItem(item, new CyberModelTemplate(true));
                } else if (modItem instanceof TieredItem item) {
                    itemModelGenerator.generateFlatItem(item, ModelTemplates.FLAT_HANDHELD_ITEM);
                } else if (modItem instanceof SpawnEggItem item) {
                    itemModelGenerator.generateFlatItem(item, new ModelTemplate(
                        Optional.of(ResourceLocation.withDefaultNamespace("item/template_spawn_egg")),
                        Optional.empty()
                    ));
                }
            }
        }
    }

    public static class RecipeProvider extends FabricRecipeProvider {
        public RecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        public void buildRecipes(RecipeOutput exporter) {
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.BLUEPRINT_ARCHIVE)
                .pattern("AAA").pattern("BBB").pattern("AAA")
                .define('A', Items.IRON_INGOT).define('B', Items.PAPER)
                .unlockedBy(FabricRecipeProvider.getHasName(Items.IRON_INGOT),
                    FabricRecipeProvider.has(Items.IRON_INGOT))
                .save(exporter);
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.SURGERY_CHAMBER)
                .pattern("AAA").pattern("ABA").pattern("ACA")
                .define('A', Items.IRON_INGOT).define('B', Items.IRON_BLOCK).define('C', Items.IRON_DOOR)
                .unlockedBy(FabricRecipeProvider.getHasName(Items.IRON_INGOT),
                    FabricRecipeProvider.has(Items.IRON_INGOT))
                .save(exporter);
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.CHARGER)
                .pattern("ABA").pattern("ACA").pattern("AAA")
                .define('A', Items.IRON_INGOT).define('B', Items.IRON_BARS).define('C', Items.REDSTONE_BLOCK)
                .unlockedBy(FabricRecipeProvider.getHasName(Items.IRON_INGOT),
                    FabricRecipeProvider.has(Items.IRON_INGOT))
                .save(exporter);
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.ENGINEERING_TABLE)
                .pattern(" BA").pattern("AAA").pattern("ACA")
                .define('A', Items.IRON_INGOT).define('B', Items.PISTON).define('C', Items.CRAFTING_TABLE)
                .unlockedBy(FabricRecipeProvider.getHasName(Items.IRON_INGOT),
                    FabricRecipeProvider.has(Items.IRON_INGOT))
                .save(exporter);
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.COMPONENT_BOX)
                .pattern(" A ").pattern("BCB").pattern(" B ")
                .define('A', CyberRewaredMod.COMPONENT_TAG)
                .define('B', Items.IRON_INGOT).define('C', Items.CHEST)
                .unlockedBy(FabricRecipeProvider.getHasName(Items.IRON_INGOT),
                    FabricRecipeProvider.has(Items.IRON_INGOT))
                .save(exporter);
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.RADIO_KIT)
                .pattern("A  ").pattern("BBB").pattern("BCB")
                .define('A', ItemRegistry.FIBER_OPTICS)
                .define('B', Items.IRON_INGOT).define('C', ItemRegistry.SOLID_STATE_CIRCUITRY)
                .unlockedBy(FabricRecipeProvider.getHasName(Items.IRON_INGOT),
                    FabricRecipeProvider.has(Items.IRON_INGOT))
                .save(exporter);
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.RADIO_POST, 6)
                .pattern("A A").pattern("ABA").pattern("ACA")
                .define('A', Items.IRON_BARS).define('B', ItemRegistry.FIBER_OPTICS)
                .define('C', ItemRegistry.CHROME_PLATING)
                .unlockedBy(FabricRecipeProvider.getHasName(Items.IRON_INGOT),
                    FabricRecipeProvider.has(Items.IRON_INGOT))
                .save(exporter);
        }
    }

    public static class ItemTagProvider extends FabricTagProvider<Item> {
        private static final ResourceKey<Registry<Item>> ITEM_REGISTRY = ResourceKey.createRegistryKey(ResourceLocation.parse("item"));

        public ItemTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
            super(output, ITEM_REGISTRY, registriesFuture);
        }

        @Override
        protected void addTags(HolderLookup.Provider wrapperLookup) {
            FabricTagProvider<Item>.FabricTagBuilder builder = getOrCreateTagBuilder(CyberRewaredMod.COMPONENT_TAG).setReplace(false);
            for (Item modItem : ItemRegistry.modItems) {
                if (modItem instanceof ComponentItem) builder.add(modItem);
            }
        }
    }
    public static class BlockTagProvider extends FabricTagProvider<Block> {
        private static final ResourceKey<Registry<Block>> BLOCK_REGISTRY = ResourceKey.createRegistryKey(ResourceLocation.parse("block"));

        public BlockTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
            super(output, BLOCK_REGISTRY, registriesFuture);
        }

        @SuppressWarnings("unused")
        protected static TagKey<Block> createTag(String name) {
            return TagKey.create(BLOCK_REGISTRY, ResourceLocation.fromNamespaceAndPath(CyberRewaredMod.MOD_ID, name));
        }

        @Override
        protected void addTags(HolderLookup.Provider wrapperLookup) {
            FabricTagProvider<Block>.FabricTagBuilder pickaxeMineableBuilder = getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_PICKAXE).setReplace(false);
            FabricTagProvider<Block>.FabricTagBuilder needsStoneBuilder = getOrCreateTagBuilder(BlockTags.NEEDS_STONE_TOOL).setReplace(false);

            for (Block block : BlockRegistry.blocks) {
                pickaxeMineableBuilder.add(block);
                needsStoneBuilder.add(block);
            }
        }
    }

    private static class BlockLootTableProvider extends FabricBlockLootTableProvider {
        protected BlockLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
            super(dataOutput, registryLookup);
        }

        @Override
        public void generate() {
            // This looks really cursed but it works
            for (Block block : BlockRegistry.blocks) {
                add(block, createSelfDropDispatchTable(block, MatchTool.toolMatches(ItemPredicate.Builder
                    .item()
                    .of(
                        Items.STONE_PICKAXE,
                        Items.IRON_PICKAXE,
                        Items.DIAMOND_PICKAXE,
                        Items.NETHERITE_PICKAXE
                    )), LootItem.lootTableItem(Blocks.AIR)));
            }
        }
    }

    private abstract static class LanguageProvider extends FabricLanguageProvider {
        public final String locale;

        protected LanguageProvider(FabricDataOutput dataOutput, String language, CompletableFuture<HolderLookup.Provider> registryLookup) {
            super(dataOutput, language, registryLookup);
            locale = language;
        }

        @Override
        public void generateTranslations(HolderLookup.Provider wrapperLookup, TranslationBuilder builder) {
            Optional<Path> path = dataOutput.getModContainer().findPath("assets/" + CyberRewaredMod.MOD_ID + "/lang/" + locale + ".existing.json");
            if (path.isPresent()) {
                try {
                    builder.add(path.get());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
