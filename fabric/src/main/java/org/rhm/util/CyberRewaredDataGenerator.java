package org.rhm.util;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.loot.condition.MatchToolLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.rhm.CyberRewaredMod;
import org.rhm.item.ComponentItem;
import org.rhm.item.LimbItem;
import org.rhm.item.NeuroItem;
import org.rhm.item.OrganItem;
import org.rhm.registries.BlockRegistry;
import org.rhm.registries.EntityRegistry;
import org.rhm.registries.ItemRegistry;
import org.rhm.registries.PotionRegistry;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class CyberRewaredDataGenerator implements DataGeneratorEntrypoint {
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
            public void generateTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder builder) {
                builder.add(CyberRewaredMod.MOD_ID + ".itemGroup", "Cyber Rewared");
                builder.add(CyberRewaredMod.COMPONENT_TAG, "Cyber Component");

                builder.add(CyberRewaredMod.MOD_ID + ".jei.smash", "Engineering (Smashing)");
                builder.add(CyberRewaredMod.MOD_ID + ".jei.craft", "Engineering (Crafting)");
                builder.add(CyberRewaredMod.MOD_ID + ".jei.scan", "Scanning");
                builder.add(CyberRewaredMod.MOD_ID + ".jei.paper_ttp", "Add Paper for chance of obtaining Blueprint.");

                builder.add("item." + CyberRewaredMod.MOD_ID + ".limb.left", "Left");
                builder.add("item." + CyberRewaredMod.MOD_ID + ".limb.right", "Right");
                builder.add(ItemRegistry.HUMAN_ARM, "Human %s Arm");
                builder.add(ItemRegistry.HUMAN_LEG, "Human %s Leg");
                builder.add(ItemRegistry.XP_CAPSULE, "Experience Capsule");
                builder.add(ItemRegistry.XP_CAPSULE.getTranslationKey() + ".ttp", "%s recorded experience stored.");
                builder.add(ItemRegistry.HUMAN_BONES, "Human Bones");
                builder.add(ItemRegistry.HUMAN_BRAIN, "Human Brain");
                builder.add(ItemRegistry.HUMAN_EYES, "Human Eyes");
                builder.add(ItemRegistry.HUMAN_HEART, "Human Heart");
                builder.add(ItemRegistry.HUMAN_LUNGS, "Human Lungs");
                builder.add(ItemRegistry.HUMAN_MUSCLES, "Human Muscles");
                builder.add(ItemRegistry.HUMAN_SKIN, "Human Skin");
                builder.add(ItemRegistry.HUMAN_STOMACH, "Human Stomach");


                builder.add(EntityRegistry.CYBERZOMBIE, "Cyberzombie");
                builder.add(ItemRegistry.getSpawnEgg(EntityRegistry.CYBERZOMBIE), "Cyberzombie Spawn Egg");

                builder.add(BlockRegistry.SCANNER, "Scanner");
                builder.add(BlockRegistry.SCANNER.getTranslationKey() + ".ttp", "Analyzes Cyberware to create Blueprints.");
                builder.add(CyberRewaredMod.MOD_ID + ".gui.percent", "%1$s%% Chance");
                builder.add(CyberRewaredMod.MOD_ID + ".gui.insertPaper", "Insert Paper");
                builder.add(CyberRewaredMod.MOD_ID + ".gui.toScan", "Insert Cyberware to Scan");
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
                    "Instantiating Child Elements", "Wiping System32", "Remembering Lycaon"
                }));

                builder.add(BlockRegistry.BLUEPRINT_ARCHIVE, "Blueprint Archive");
                builder.add(BlockRegistry.BLUEPRINT_ARCHIVE.getTranslationKey() + ".ttp", "Stores Cyberware Blueprints.\nAccessible by Engineering Table if adjacent.");

                builder.add(BlockRegistry.COMPONENT_BOX, "Component Box");
                builder.add(BlockRegistry.COMPONENT_BOX.getTranslationKey() + ".ttp", "Stores Components.\nAccessible by Engineering Table if adjacent.");

                builder.add(BlockRegistry.ENGINEERING_TABLE, "Engineering Table");
                builder.add(BlockRegistry.ENGINEERING_TABLE.getTranslationKey() + ".ttp", "Used to dismantle and create Cyberware.");
                builder.add(CyberRewaredMod.MOD_ID + ".gui.smash", "Void Warranty");
                // this is "to Destroy" in the original but i think salvage sounds better
                builder.add(CyberRewaredMod.MOD_ID + ".gui.insertSalvage", "Insert Cyberware to Salvage");
                builder.add(CyberRewaredMod.MOD_ID + ".gui.insertBlueprint", "Insert Blueprint");

                builder.add(BlockRegistry.SURGERY_CHAMBER, "Surgery Chamber");
                builder.add(BlockRegistry.SURGERY_CHAMBER.getTranslationKey() + ".ttp", "Performs Operations for the Robosurgeon.\nRight-click while inside to activate .");

                builder.add(BlockRegistry.CHARGER, "Charger");
                builder.add(BlockRegistry.CHARGER.getTranslationKey() + ".ttp", "Charges entities with Cyberware.\nAccepts TR Energy.");

                builder.add(BlockRegistry.ROBOSURGEON, "Robosurgeon");
                builder.add(BlockRegistry.ROBOSURGEON.getTranslationKey() + ".ttp", "Used to set what Cyberware to install or remove.\nPlace on top of a Surgery Chamber to use.");

                builder.add(BlockRegistry.RADIO_KIT, "Radio Kit");
                builder.add(BlockRegistry.RADIO_KIT.getTranslationKey() + ".ttp", "Slightly increases Cyberzombie spawn rate");

                builder.add(BlockRegistry.RADIO_POST, "Radio Tower Component");
                builder.add(BlockRegistry.RADIO_POST.getTranslationKey() + ".ttp", "Constructs a large Radio.\nTo form:\n  - Place 4 layers of 3x3 components. (3x4x3)\n  - 6 layers of a single component, centered\n    over the base.");

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
                builder.add(ItemRegistry.NEURO_SYRINGE.getTranslationKey() + ".ttp", "Anti-Rejection (20:00)");
                // This is so cursed but i dont wanna make another variable
                builder.add(PotionRegistry.NEURO_POTION.value().getEffects().getFirst().getEffectType().value(), "Anti-Rejection");
            }
        });
    }

    public static class ModelProvider extends FabricModelProvider {

        public ModelProvider(FabricDataOutput output) {
            super(output);
        }

        @Override
        public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {

        }

        @Override
        public void generateItemModels(ItemModelGenerator itemModelGenerator) {
            for (Item modItem : ItemRegistry.modItems) {
                if ((modItem instanceof OrganItem && !(modItem instanceof LimbItem)) ||
                    modItem instanceof ComponentItem || modItem instanceof NeuroItem) {
                    itemModelGenerator.register(modItem, Models.GENERATED);
                }
            }
        }
    }

    public static class RecipeProvider extends FabricRecipeProvider {
        public RecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        public void generate(RecipeExporter exporter) {
            ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, BlockRegistry.BLUEPRINT_ARCHIVE)
                .pattern("AAA").pattern("BBB").pattern("AAA")
                .input('A', Items.IRON_INGOT).input('B', Items.PAPER)
                .criterion(FabricRecipeProvider.hasItem(Items.IRON_INGOT),
                    FabricRecipeProvider.conditionsFromItem(Items.IRON_INGOT))
                .offerTo(exporter);
            ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, BlockRegistry.SURGERY_CHAMBER)
                .pattern("AAA").pattern("ABA").pattern("ACA")
                .input('A', Items.IRON_INGOT).input('B', Items.IRON_BLOCK).input('C', Items.IRON_DOOR)
                .criterion(FabricRecipeProvider.hasItem(Items.IRON_INGOT),
                    FabricRecipeProvider.conditionsFromItem(Items.IRON_INGOT))
                .offerTo(exporter);
            ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, BlockRegistry.CHARGER)
                .pattern("ABA").pattern("ACA").pattern("AAA")
                .input('A', Items.IRON_INGOT).input('B', Items.IRON_BARS).input('C', Items.REDSTONE_BLOCK)
                .criterion(FabricRecipeProvider.hasItem(Items.IRON_INGOT),
                    FabricRecipeProvider.conditionsFromItem(Items.IRON_INGOT))
                .offerTo(exporter);
            ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, BlockRegistry.ENGINEERING_TABLE)
                .pattern(" BA").pattern("AAA").pattern("ACA")
                .input('A', Items.IRON_INGOT).input('B', Items.PISTON).input('C', Items.CRAFTING_TABLE)
                .criterion(FabricRecipeProvider.hasItem(Items.IRON_INGOT),
                    FabricRecipeProvider.conditionsFromItem(Items.IRON_INGOT))
                .offerTo(exporter);
            ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, BlockRegistry.COMPONENT_BOX)
                .pattern(" A ").pattern("BCB").pattern(" B ")
                .input('A', CyberRewaredMod.COMPONENT_TAG)
                .input('B', Items.IRON_INGOT).input('C', Items.CHEST)
                .criterion(FabricRecipeProvider.hasItem(Items.IRON_INGOT),
                    FabricRecipeProvider.conditionsFromItem(Items.IRON_INGOT))
                .offerTo(exporter);
            ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, BlockRegistry.RADIO_KIT)
                .pattern("A  ").pattern("BBB").pattern("BCB")
                .input('A', ItemRegistry.FIBER_OPTICS)
                .input('B', Items.IRON_INGOT).input('C', ItemRegistry.SOLID_STATE_CIRCUITRY)
                .criterion(FabricRecipeProvider.hasItem(Items.IRON_INGOT),
                    FabricRecipeProvider.conditionsFromItem(Items.IRON_INGOT))
                .offerTo(exporter);
            ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, BlockRegistry.RADIO_POST, 6)
                .pattern("A A").pattern("ABA").pattern("ACA")
                .input('A', Items.IRON_BARS).input('B', ItemRegistry.FIBER_OPTICS)
                .input('C', ItemRegistry.CHROME_PLATING)
                .criterion(FabricRecipeProvider.hasItem(Items.IRON_INGOT),
                    FabricRecipeProvider.conditionsFromItem(Items.IRON_INGOT))
                .offerTo(exporter);
            // large radio
            /*
            ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, BlockRegistry.RADIO_KIT)
                .pattern("ABA").pattern("CDC").pattern("AEA")
                .input('A', Items.IRON_INGOT).input('B', Items.ENDER_EYE)
                .input('C', ItemRegistry.TITANIUM_MESH).input('D', BlockRegistry.RADIO_KIT)
                .input('E', ItemRegistry.SOLID_STATE_CIRCUITRY)
                .criterion(FabricRecipeProvider.hasItem(Items.IRON_INGOT),
                    FabricRecipeProvider.conditionsFromItem(Items.IRON_INGOT))
                .offerTo(exporter);
             */
        }
    }

    public static class ItemTagProvider extends FabricTagProvider<Item> {
        private static final RegistryKey<Registry<Item>> ITEM_REGISTRY = RegistryKey.ofRegistry(Identifier.of("item"));

        public ItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, ITEM_REGISTRY, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
            FabricTagBuilder builder = getOrCreateTagBuilder(CyberRewaredMod.COMPONENT_TAG);
            for (Item modItem : ItemRegistry.modItems) {
                if (modItem instanceof ComponentItem) builder.add(modItem);
            }
        }
    }
    public static class BlockTagProvider extends FabricTagProvider<Block> {
        private static final RegistryKey<Registry<Block>> BLOCK_REGISTRY = RegistryKey.ofRegistry(Identifier.of("block"));

        public BlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, BLOCK_REGISTRY, registriesFuture);
        }

        @SuppressWarnings("unused")
        protected static TagKey<Block> createTag(String name) {
            return TagKey.of(BLOCK_REGISTRY, Identifier.of(CyberRewaredMod.MOD_ID, name));
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
            FabricTagBuilder pickaxeMineableBuilder = getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE).setReplace(false);
            FabricTagBuilder needsStoneBuilder = getOrCreateTagBuilder(BlockTags.NEEDS_STONE_TOOL).setReplace(false);

            for (Block block : BlockRegistry.blocks) {
                pickaxeMineableBuilder.add(block);
                needsStoneBuilder.add(block);
            }

            /*
            getOrCreateTagBuilder(BlockTags.FENCES)
                .add(BlockRegistry.RADIO_POST)
                .setReplace(false);
             */
        }
    }

    private static class BlockLootTableProvider extends FabricBlockLootTableProvider {
        protected BlockLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
            super(dataOutput, registryLookup);
        }

        @Override
        public void generate() {
            // This looks really cursed but it works
            for (Block block : BlockRegistry.blocks) {
                addDrop(block, drops(block, MatchToolLootCondition.builder(ItemPredicate.Builder
                    .create()
                    .items(
                        Items.STONE_PICKAXE,
                        Items.IRON_PICKAXE,
                        Items.DIAMOND_PICKAXE,
                        Items.NETHERITE_PICKAXE
                    )), ItemEntry.builder(Blocks.AIR)));
            }
        }
    }

    private abstract static class LanguageProvider extends FabricLanguageProvider {
        public final String LANGUAGE_CODE;

        protected LanguageProvider(FabricDataOutput dataOutput, String language, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
            super(dataOutput, language, registryLookup);
            LANGUAGE_CODE = language;
        }

        @Override
        public void generateTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder builder) {
            Optional<Path> path = dataOutput.getModContainer().findPath("assets/" + CyberRewaredMod.MOD_ID + "/lang/" + LANGUAGE_CODE + ".existing.json");
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
