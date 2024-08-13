package com.sockmit2007.omniaetnihil;

import java.util.Optional;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.sockmit2007.omniaetnihil.block.CorruptStorage;
import com.sockmit2007.omniaetnihil.block.CrudeJawCrusher;
import com.sockmit2007.omniaetnihil.block.CrudePreprocessor;
import com.sockmit2007.omniaetnihil.block.ExampleCrafter;
import com.sockmit2007.omniaetnihil.block.HurtBlock;
import com.sockmit2007.omniaetnihil.block.PrecisionCraftingTable;
import com.sockmit2007.omniaetnihil.block.SpreadBlock;
import com.sockmit2007.omniaetnihil.block.TieredBlock;
import com.sockmit2007.omniaetnihil.block.entity.CorruptStorageBlockEntity;
import com.sockmit2007.omniaetnihil.block.entity.CrudeJawCrusherBlockEntity;
import com.sockmit2007.omniaetnihil.block.entity.CrudePreprocessorBlockEntity;
import com.sockmit2007.omniaetnihil.block.entity.ExampleCrafterBlockEntity;
import com.sockmit2007.omniaetnihil.block.entity.PrecisionCraftingTableBlockEntity;
import com.sockmit2007.omniaetnihil.capabilities.OmniaEtNihilCapabilities;
import com.sockmit2007.omniaetnihil.client.particle.ArcaneParticle;
import com.sockmit2007.omniaetnihil.client.renderer.block.CorruptStorageRenderer;
import com.sockmit2007.omniaetnihil.client.renderer.block.ExampleCrafterRenderer;
import com.sockmit2007.omniaetnihil.client.renderer.entity.ExampleEntityRenderer;
import com.sockmit2007.omniaetnihil.client.renderer.entity.LichEntityRenderer;
import com.sockmit2007.omniaetnihil.entity.ExampleEntity;
import com.sockmit2007.omniaetnihil.entity.LichEntity;
import com.sockmit2007.omniaetnihil.item.GrabberJar;
import com.sockmit2007.omniaetnihil.recipe.ExampleCraftingRecipe;
import com.sockmit2007.omniaetnihil.recipe.JawCrushingRecipe;
import com.sockmit2007.omniaetnihil.recipe.PrecisionCraftingRecipe;
import com.sockmit2007.omniaetnihil.recipe.PreprocessingRecipe;
import com.sockmit2007.omniaetnihil.screen.CorruptStorageMenu;
import com.sockmit2007.omniaetnihil.screen.CorruptStorageScreen;
import com.sockmit2007.omniaetnihil.screen.CrudeJawCrusherMenu;
import com.sockmit2007.omniaetnihil.screen.CrudeJawCrusherScreen;
import com.sockmit2007.omniaetnihil.screen.CrudePreprocessorMenu;
import com.sockmit2007.omniaetnihil.screen.CrudePreprocessorScreen;
import com.sockmit2007.omniaetnihil.screen.ExampleCrafterMenu;
import com.sockmit2007.omniaetnihil.screen.ExampleCrafterScreen;
import com.sockmit2007.omniaetnihil.screen.PrecisionCraftingTableMenu;
import com.sockmit2007.omniaetnihil.screen.PrecisionCraftingTableScreen;
import com.sockmit2007.omniaetnihil.datagen.ModItemModelProvider;

import net.minecraft.DetectedVersion;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.metadata.PackMetadataGenerator;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.InclusiveRange;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

@Mod(OmniaEtNihil.MODID)
public class OmniaEtNihil {
	public static final String MODID = "omniaetnihil";
	public static final Logger LOGGER = LogUtils.getLogger();

	// #region Registers
	public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
	public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
	private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MODID);
	public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(MODID);
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, MODID);
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MODID);
	public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Registries.MENU, MODID);
	public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, MODID);
	public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, MODID);
	public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(Registries.PARTICLE_TYPE, MODID);
	public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Registries.FLUID, MODID);
	public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, MODID);
	// #endregion

	// #region Particles
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> ARCANE_PARTICLE = PARTICLE_TYPES.register("arcane_particle", () -> new SimpleParticleType(false));
	// #endregion

	// #region Utility Methods for Registers
	private static <T extends AbstractContainerMenu> DeferredHolder<MenuType<?>, MenuType<T>> register(String name, MenuType.MenuSupplier<T> menu) {
		return MENU_TYPES.register(name, () -> new MenuType<>(menu, FeatureFlags.VANILLA_SET));
	}
	// #endregion

	// #region Data Components
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> MANA_DATA_COMPONENT = DATA_COMPONENTS.registerComponentType("mana", builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT));
	// #endregion

	// #region Capabilities

	// #endregion

	// #region Menus
	public static final DeferredHolder<MenuType<?>, MenuType<CorruptStorageMenu>> CORRUPT_STORAGE_MENU = register("corrupt_storage", CorruptStorageMenu::new);
	public static final DeferredHolder<MenuType<?>, MenuType<ExampleCrafterMenu>> EXAMPLE_CRAFTER_MENU = register("example_crafter", ExampleCrafterMenu::new);
	public static final DeferredHolder<MenuType<?>, MenuType<CrudePreprocessorMenu>> CRUDE_PREPROCESSOR_MENU = register("crude_preprocessor", CrudePreprocessorMenu::new);
	public static final DeferredHolder<MenuType<?>, MenuType<CrudeJawCrusherMenu>> CRUDE_JAW_CRUSHER_MENU = register("crude_jaw_crusher", CrudeJawCrusherMenu::new);
	public static final DeferredHolder<MenuType<?>, MenuType<PrecisionCraftingTableMenu>> PRECISION_CRAFTING_TABLE_MENU = register("precision_crafting_table", PrecisionCraftingTableMenu::new);
	// #endregion

	// #region Tags
	public static final TagKey<Block> unspreadableBlocksTag = BlockTags.create(ResourceLocation.fromNamespaceAndPath(MODID, "unspreadable"));
	// #endregion

	// #region Blocks

	public static final DeferredBlock<Block> CORRUPT_STORAGE = BLOCKS.register("corrupt_storage", () -> new CorruptStorage(BlockBehaviour.Properties.of().noOcclusion().mapColor(MapColor.STONE)));
	public static final DeferredItem<BlockItem> CORRUPT_STORAGE_ITEM = ITEMS.registerSimpleBlockItem("corrupt_storage", CORRUPT_STORAGE);

	public static final DeferredBlock<Block> EXAMPLE_CRAFTER = BLOCKS.register("example_crafter", () -> new ExampleCrafter(BlockBehaviour.Properties.of().noOcclusion().mapColor(MapColor.STONE)));
	public static final DeferredItem<BlockItem> EXAMPLE_CRAFTER_ITEM = ITEMS.registerSimpleBlockItem("example_crafter", EXAMPLE_CRAFTER);

	public static final DeferredBlock<Block> CRUDE_PREPROCESSOR = BLOCKS.register("crude_preprocessor", () -> new CrudePreprocessor(BlockBehaviour.Properties.of().noOcclusion().mapColor(MapColor.STONE)));
	public static final DeferredItem<BlockItem> CRUDE_PREPROCESSOR_ITEM = ITEMS.registerSimpleBlockItem("crude_preprocessor", CRUDE_PREPROCESSOR);

	public static final DeferredBlock<Block> CRUDE_JAW_CRUSHER = BLOCKS.register("crude_jaw_crusher", () -> new CrudeJawCrusher(BlockBehaviour.Properties.of().noOcclusion().mapColor(MapColor.STONE)));
	public static final DeferredItem<BlockItem> CRUDE_JAW_CRUSHER_ITEM = ITEMS.registerSimpleBlockItem("crude_jaw_crusher", CRUDE_JAW_CRUSHER);

	public static final DeferredBlock<Block> PRECISION_CRAFTING_TABLE = BLOCKS.register("precision_crafting_table", () -> new PrecisionCraftingTable(BlockBehaviour.Properties.of().noOcclusion().mapColor(MapColor.STONE)));
	public static final DeferredItem<BlockItem> PRECISION_CRAFTING_TABLE_ITEM = ITEMS.registerSimpleBlockItem("precision_crafting_table", PRECISION_CRAFTING_TABLE);

	public static final DeferredBlock<Block> EXAMPLE_BLOCK = BLOCKS.registerSimpleBlock("example_block", BlockBehaviour.Properties.of().mapColor(MapColor.STONE));
	public static final DeferredItem<BlockItem> EXAMPLE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("example_block", EXAMPLE_BLOCK);

	public static final DeferredBlock<Block> HURT_BLOCK = BLOCKS.register("hurt_block", () -> new HurtBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE)));
	public static final DeferredItem<BlockItem> HURT_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("hurt_block", HURT_BLOCK);

	public static final DeferredBlock<Block> SPREAD_BLOCK = BLOCKS.register("spread_block", () -> new SpreadBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE)));
	public static final DeferredItem<BlockItem> SPREAD_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("spread_block", SPREAD_BLOCK);

	public static final DeferredBlock<Block> TIERED_BLOCK = BLOCKS.register("tiered_block", () -> new TieredBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE)));
	public static final DeferredItem<BlockItem> TIERED_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("tiered_block", TIERED_BLOCK);

	// #endregion

	// #region Block Entities
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CorruptStorageBlockEntity>> CORRUPT_STORAGE_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("corrupt_storage", () -> BlockEntityType.Builder.of(CorruptStorageBlockEntity::new, CORRUPT_STORAGE.get()).build(null));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CrudePreprocessorBlockEntity>> CRUDE_PREPROCESSOR_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("crude_preprocessor", () -> BlockEntityType.Builder.of(CrudePreprocessorBlockEntity::new, CRUDE_PREPROCESSOR.get()).build(null));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CrudeJawCrusherBlockEntity>> CRUDE_JAW_CRUSHER_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("crude_jaw_crusher", () -> BlockEntityType.Builder.of(CrudeJawCrusherBlockEntity::new, CRUDE_JAW_CRUSHER.get()).build(null));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ExampleCrafterBlockEntity>> EXAMPLE_CRAFTER_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("example_crafter", () -> BlockEntityType.Builder.of(ExampleCrafterBlockEntity::new, EXAMPLE_CRAFTER.get()).build(null));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PrecisionCraftingTableBlockEntity>> PRECISION_CRAFTING_TABLE_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("precision_crafting_table", () -> BlockEntityType.Builder.of(PrecisionCraftingTableBlockEntity::new, PRECISION_CRAFTING_TABLE.get()).build(null));
	// #endregion

	// #region Entities
	public static final DeferredHolder<EntityType<?>, EntityType<ExampleEntity>> EXAMPLE_ENTITY = ENTITY_TYPES.register("example_entity", () -> EntityType.Builder.of(ExampleEntity::new, MobCategory.MONSTER).sized(0.9F, 0.9F).clientTrackingRange(10).build("example_entity"));
	public static final DeferredItem<SpawnEggItem> EXAMPLE_ENTITY_SPAWN_EGG = ITEMS.register("example_entity_spawn_egg", () -> new DeferredSpawnEggItem(EXAMPLE_ENTITY, 0xDFDFDF, 0x99CFE8, new Item.Properties()));

	public static final DeferredHolder<EntityType<?>, EntityType<LichEntity>> LICH = ENTITY_TYPES.register("lich", () -> EntityType.Builder.of(LichEntity::new, MobCategory.MONSTER).sized(0.9F, 0.9F).clientTrackingRange(10).build("lich"));
	public static final DeferredItem<SpawnEggItem> LICH_ENTITY_SPAWN_EGG = ITEMS.register("lich_entity_spawn_egg", () -> new DeferredSpawnEggItem(LICH, 0xDFDFDF, 0x99CFE8, new Item.Properties()));
	// #endregion

	// #region Items
	public static final DeferredItem<Item> EXAMPLE_ITEM = ITEMS.registerSimpleItem("example_item", new Item.Properties().food(new FoodProperties.Builder().alwaysEdible().nutrition(1).saturationModifier(2f).build()));

	public static final DeferredItem<Item> LARGE_ROCK = ITEMS.registerSimpleItem("large_rock", new Item.Properties());

	public static final DeferredItem<Item> GRABBER_JAR = ITEMS.register("grabber_jar", () -> new GrabberJar(new Item.Properties().component(MANA_DATA_COMPONENT.value(), 0)));

	// #region Grinding Rolls
	public static final DeferredItem<Item> WOODEN_GRINDING_ROLL = ITEMS.registerSimpleItem("wooden_grinding_roll", new Item.Properties());
	public static final DeferredItem<Item> STONE_GRINDING_ROLL = ITEMS.registerSimpleItem("stone_grinding_roll", new Item.Properties());
	public static final DeferredItem<Item> COPPER_GRINDING_ROLL = ITEMS.registerSimpleItem("copper_grinding_roll", new Item.Properties());
	public static final DeferredItem<Item> IRON_GRINDING_ROLL = ITEMS.registerSimpleItem("iron_grinding_roll", new Item.Properties());
	public static final DeferredItem<Item> GOLD_GRINDING_ROLL = ITEMS.registerSimpleItem("gold_grinding_roll", new Item.Properties());
	public static final DeferredItem<Item> DIAMOND_GRINDING_ROLL = ITEMS.registerSimpleItem("diamond_grinding_roll", new Item.Properties());
	public static final DeferredItem<Item> NETHERITE_GRINDING_ROLL = ITEMS.registerSimpleItem("netherite_grinding_roll", new Item.Properties());
	// #endregion

	// #region Impactors
	public static final DeferredItem<Item> WOODEN_IMPACTOR = ITEMS.registerSimpleItem("wooden_impactor", new Item.Properties());
	public static final DeferredItem<Item> STONE_IMPACTOR = ITEMS.registerSimpleItem("stone_impactor", new Item.Properties());
	public static final DeferredItem<Item> COPPER_IMPACTOR = ITEMS.registerSimpleItem("copper_impactor", new Item.Properties());
	public static final DeferredItem<Item> IRON_IMPACTOR = ITEMS.registerSimpleItem("iron_impactor", new Item.Properties());
	public static final DeferredItem<Item> GOLD_IMPACTOR = ITEMS.registerSimpleItem("gold_impactor", new Item.Properties());
	public static final DeferredItem<Item> DIAMOND_IMPACTOR = ITEMS.registerSimpleItem("diamond_impactor", new Item.Properties());
	public static final DeferredItem<Item> NETHERITE_IMPACTOR = ITEMS.registerSimpleItem("netherite_impactor", new Item.Properties());
	// #endregion

	// #region Impure Dust
	public static final DeferredItem<Item> IMPURE_IRON_DUST = ITEMS.registerSimpleItem("impure_iron_dust", new Item.Properties());
	// #endregion

	// #region Preprocessed Ore
	public static final DeferredItem<Item> PREPROCESSED_IRON_ORE = ITEMS.registerSimpleItem("preprocessed_iron_ore", new Item.Properties());
	// #endregion

	// #region Crushed Ore

	// #region Large Crushed Ore
	public static final DeferredItem<Item> LARGE_CRUSHED_IRON_ORE = ITEMS.registerSimpleItem("large_crushed_iron_ore", new Item.Properties());
	// #endregion

	// #region Medium Crushed Ore
	public static final DeferredItem<Item> MEDIUM_CRUSHED_IRON_ORE = ITEMS.registerSimpleItem("medium_crushed_iron_ore", new Item.Properties());
	// #endregion

	// #region Small Crushed Ore
	public static final DeferredItem<Item> SMALL_CRUSHED_IRON_ORE = ITEMS.registerSimpleItem("small_crushed_iron_ore", new Item.Properties());
	// #endregion

	// #region Fine Crushed Ore
	public static final DeferredItem<Item> FINE_CRUSHED_IRON_ORE = ITEMS.registerSimpleItem("fine_crushed_iron_ore", new Item.Properties());
	// #endregion

	// #region Ultra-Fine Crushed Ore
	public static final DeferredItem<Item> ULTRA_FINE_CRUSHED_IRON_ORE = ITEMS.registerSimpleItem("ultra_fine_crushed_iron_ore", new Item.Properties());
	// #endregion

	// #endregion

	// #region Ground Ore
	public static final DeferredItem<Item> GROUND_IRON_ORE = ITEMS.registerSimpleItem("ground_iron_ore", new Item.Properties());
	// #endregion

	// #region Washed Ore
	public static final DeferredItem<Item> WASHED_IRON_ORE = ITEMS.registerSimpleItem("washed_iron_ore", new Item.Properties());
	// #endregion

	// #region Scrubbed Ore
	public static final DeferredItem<Item> SCRUBBED_IRON_ORE = ITEMS.registerSimpleItem("scrubbed_iron_ore", new Item.Properties());
	// #endregion

	// #region Purified Ore
	public static final DeferredItem<Item> PURIFIED_IRON_ORE = ITEMS.registerSimpleItem("purified_iron_ore", new Item.Properties());
	// #endregion

	// #region Refined Dust
	public static final DeferredItem<Item> REFINED_IRON_DUST = ITEMS.registerSimpleItem("refined_iron_dust", new Item.Properties());
	// #endregion

	// #region Flotation Concentrate
	public static final DeferredItem<Item> IRON_FLOTATION_CONCENTRATE = ITEMS.registerSimpleItem("iron_flotation_concentrate", new Item.Properties());
	// #endregion

	// #region Leachate
	public static final DeferredItem<Item> IRON_LEACHATE = ITEMS.registerSimpleItem("iron_leachate", new Item.Properties());
	// #endregion

	// #region Roasted Leachate
	public static final DeferredItem<Item> ROASTED_IRON_LEACHATE = ITEMS.registerSimpleItem("roasted_iron_leachate", new Item.Properties());
	// #endregion

	// #region Refined Slurry
	public static final DeferredItem<Item> REFINED_IRON_SLURRY = ITEMS.registerSimpleItem("refined_iron_slurry", new Item.Properties());
	// #endregion

	// #region High-Purity Slurry
	public static final DeferredItem<Item> HIGH_PURITY_IRON_SLURRY = ITEMS.registerSimpleItem("high_purity_iron_slurry", new Item.Properties());
	// #endregion

	// #region Drops
	public static final DeferredItem<Item> ABYSSAL_FLESH = ITEMS.registerSimpleItem("abyssal_flesh", new Item.Properties());
	public static final DeferredItem<Item> ABYSSAL_BONE = ITEMS.registerSimpleItem("abyssal_bone", new Item.Properties());
	public static final DeferredItem<Item> AQUATIC_FLESH = ITEMS.registerSimpleItem("aquatic_flesh", new Item.Properties());
	public static final DeferredItem<Item> AQUATIC_BONE = ITEMS.registerSimpleItem("aquatic_bone", new Item.Properties());
	public static final DeferredItem<Item> ARCANE_FLESH = ITEMS.registerSimpleItem("arcane_flesh", new Item.Properties());
	public static final DeferredItem<Item> ARCANE_BONE = ITEMS.registerSimpleItem("arcane_bone", new Item.Properties());
	public static final DeferredItem<Item> DIVINE_FLESH = ITEMS.registerSimpleItem("divine_flesh", new Item.Properties());
	public static final DeferredItem<Item> DIVINE_BONE = ITEMS.registerSimpleItem("divine_bone", new Item.Properties());
	public static final DeferredItem<Item> ETHEREAL_FLESH = ITEMS.registerSimpleItem("ethereal_flesh", new Item.Properties());
	public static final DeferredItem<Item> ETHEREAL_BONE = ITEMS.registerSimpleItem("ethereal_bone", new Item.Properties());
	public static final DeferredItem<Item> INFERNAL_FLESH = ITEMS.registerSimpleItem("infernal_flesh", new Item.Properties());
	public static final DeferredItem<Item> INFERNAL_BONE = ITEMS.registerSimpleItem("infernal_bone", new Item.Properties());
	public static final DeferredItem<Item> PETRIFIED_FLESH = ITEMS.registerSimpleItem("petrified_flesh", new Item.Properties());
	public static final DeferredItem<Item> PETRIFIED_BONE = ITEMS.registerSimpleItem("petrified_bone", new Item.Properties());
	public static final DeferredItem<Item> VERDANT_FLESH = ITEMS.registerSimpleItem("verdant_flesh", new Item.Properties());
	public static final DeferredItem<Item> VERDANT_BONE = ITEMS.registerSimpleItem("verdant_bone", new Item.Properties());
	public static final DeferredItem<Item> RADIANT_FLESH = ITEMS.registerSimpleItem("radiant_flesh", new Item.Properties());
	public static final DeferredItem<Item> RADIANT_BONE = ITEMS.registerSimpleItem("radiant_bone", new Item.Properties());
	// #endregion
	// #endregion

	// #region Tabs
	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("omniaetnihil_tab", () -> CreativeModeTab.builder().title(Component.translatable("itemGroup.omniaetnihil.omniaetnihil_tab")).withTabsBefore(CreativeModeTabs.COMBAT).icon(() -> EXAMPLE_ITEM.get().getDefaultInstance()).displayItems((parameters, output) -> {
		output.accept(EXAMPLE_ITEM.get());
		output.accept(EXAMPLE_BLOCK_ITEM.get());
		output.accept(GRABBER_JAR.get());
		output.accept(HURT_BLOCK_ITEM.get());
		output.accept(EXAMPLE_ENTITY_SPAWN_EGG.get());
		output.accept(LICH_ENTITY_SPAWN_EGG.get());
		output.accept(SPREAD_BLOCK_ITEM.get());
		output.accept(TIERED_BLOCK_ITEM.get());
		output.accept(CORRUPT_STORAGE_ITEM.get());
		output.accept(EXAMPLE_CRAFTER_ITEM.get());
	}).build());

	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MECHANICAL_TAB = CREATIVE_MODE_TABS.register("mechanical_tab", () -> CreativeModeTab.builder().title(Component.translatable("itemGroup.omniaetnihil.mechanical_tab")).withTabsBefore(CreativeModeTabs.COMBAT).icon(() -> CRUDE_PREPROCESSOR_ITEM.get().getDefaultInstance()).displayItems((parameters, output) -> {

		output.accept(CRUDE_PREPROCESSOR_ITEM.get());
		output.accept(LARGE_ROCK.get());
		output.accept(PREPROCESSED_IRON_ORE.get());

		output.accept(CRUDE_JAW_CRUSHER_ITEM.get());
		output.accept(IMPURE_IRON_DUST.get());
		output.accept(LARGE_CRUSHED_IRON_ORE.get());

		output.accept(MEDIUM_CRUSHED_IRON_ORE.get());

		output.accept(SMALL_CRUSHED_IRON_ORE.get());

		output.accept(WOODEN_GRINDING_ROLL.get());
		output.accept(STONE_GRINDING_ROLL.get());
		output.accept(COPPER_GRINDING_ROLL.get());
		output.accept(IRON_GRINDING_ROLL.get());
		output.accept(GOLD_GRINDING_ROLL.get());
		output.accept(DIAMOND_GRINDING_ROLL.get());
		output.accept(NETHERITE_GRINDING_ROLL.get());
		output.accept(FINE_CRUSHED_IRON_ORE.get());

		output.accept(WOODEN_IMPACTOR.get());
		output.accept(STONE_IMPACTOR.get());
		output.accept(COPPER_IMPACTOR.get());//
		output.accept(IRON_IMPACTOR.get());
		output.accept(GOLD_IMPACTOR.get());
		output.accept(DIAMOND_IMPACTOR.get());
		output.accept(NETHERITE_IMPACTOR.get());
		output.accept(ULTRA_FINE_CRUSHED_IRON_ORE.get());

		output.accept(GROUND_IRON_ORE.get());

		output.accept(WASHED_IRON_ORE.get());

		output.accept(SCRUBBED_IRON_ORE.get());

		output.accept(PURIFIED_IRON_ORE.get());

		output.accept(REFINED_IRON_DUST.get());

		output.accept(IRON_FLOTATION_CONCENTRATE.get());

		output.accept(IRON_LEACHATE.get());

		output.accept(ROASTED_IRON_LEACHATE.get());

		output.accept(REFINED_IRON_SLURRY.get());

		output.accept(HIGH_PURITY_IRON_SLURRY.get());
	}).build());

	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> DROPS_TAB = CREATIVE_MODE_TABS.register("drops_tab", () -> CreativeModeTab.builder().title(Component.translatable("itemGroup.omniaetnihil.drops_tab")).withTabsBefore(CreativeModeTabs.COMBAT).icon(() -> ABYSSAL_FLESH.get().getDefaultInstance()).displayItems((parameters, output) -> {
		output.accept(ABYSSAL_BONE.get());
		output.accept(ABYSSAL_FLESH.get());
		output.accept(AQUATIC_BONE.get());
		output.accept(AQUATIC_FLESH.get());
		output.accept(ARCANE_BONE.get());
		output.accept(ARCANE_FLESH.get());
		output.accept(DIVINE_BONE.get());
		output.accept(DIVINE_FLESH.get());
		output.accept(ETHEREAL_BONE.get());
		output.accept(ETHEREAL_FLESH.get());
		output.accept(INFERNAL_BONE.get());
		output.accept(INFERNAL_FLESH.get());
		output.accept(PETRIFIED_BONE.get());
		output.accept(PETRIFIED_FLESH.get());
		output.accept(VERDANT_BONE.get());
		output.accept(VERDANT_FLESH.get());
		output.accept(RADIANT_BONE.get());
		output.accept(RADIANT_FLESH.get());
	}).build());
	// #endregion

	// #region Recipes
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<ExampleCraftingRecipe>> EXAMPLE_CRAFTING_SERIALIZER = RECIPE_SERIALIZERS.register("example_crafting", ExampleCraftingRecipe.Serializer::new);
	public static final DeferredHolder<RecipeType<?>, RecipeType<ExampleCraftingRecipe>> EXAMPLE_CRAFTING_TYPE = RECIPE_TYPES.register("example_crafting", () -> RecipeType.simple(ResourceLocation.fromNamespaceAndPath(MODID, "example_crafting")));

	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<PreprocessingRecipe>> PREPROCESSING_SERIALIZER = RECIPE_SERIALIZERS.register("preprocessing", PreprocessingRecipe.Serializer::new);
	public static final DeferredHolder<RecipeType<?>, RecipeType<PreprocessingRecipe>> PREPROCESSING_TYPE = RECIPE_TYPES.register("preprocessing", () -> RecipeType.simple(ResourceLocation.fromNamespaceAndPath(MODID, "preprocessing")));

	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<JawCrushingRecipe>> JAW_CRUSHING_SERIALIZER = RECIPE_SERIALIZERS.register("jaw_crushing", JawCrushingRecipe.Serializer::new);
	public static final DeferredHolder<RecipeType<?>, RecipeType<JawCrushingRecipe>> JAW_CRUSHING_TYPE = RECIPE_TYPES.register("jaw_crushing", () -> RecipeType.simple(ResourceLocation.fromNamespaceAndPath(MODID, "jaw_crushing")));

	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<PrecisionCraftingRecipe>> PRECISION_CRAFTING_SERIALIZER = RECIPE_SERIALIZERS.register("precision_crafting", PrecisionCraftingRecipe.Serializer::new);
	public static final DeferredHolder<RecipeType<?>, RecipeType<PrecisionCraftingRecipe>> PRECISION_CRAFTING_TYPE = RECIPE_TYPES.register("precision_crafting", () -> RecipeType.simple(ResourceLocation.fromNamespaceAndPath(MODID, "precision_crafting")));
	// #endregion

	// #region Fluids
	// public static final DeferredHolder<FluidType, ?> T_FLUID_TYPE = FLUID_TYPES.register("dirty_water", () -> new FluidType(OmniaEtNihil.T_PROPS));
	// public static final DeferredHolder<Fluid, FlowingFluid> T = FLUIDS.register("dirty_water", () -> new BaseFlowingFluid.Source(OmniaEtNihil.T_PROPS));
	// public static final DeferredHolder<Fluid, FlowingFluid> FLOWING_T = FLUIDS.register("flowing_dirty_water", () -> new BaseFlowingFluid.Flowing(OmniaEtNihil.T_PROPS));
	// public static final DeferredBlock<LiquidBlock> T_BLOCK = BLOCKS.register("dirty_water", () -> new LiquidBlock(T.get(), BlockBehaviour.Properties.ofFullCopy(Blocks.WATER)));
	// public static final DeferredItem<BucketItem> T_BUCKET_ITEM = ITEMS.register("dirty_water_bucket", () -> new BucketItem(T.get(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
	// private static final BaseFlowingFluid.Properties T_PROPS = new BaseFlowingFluid.Properties(T_FLUID_TYPE, T, FLOWING_T).explosionResistance(100.f).block(T_BLOCK).bucket(T_BUCKET_ITEM);
	// #endregion

	public OmniaEtNihil(IEventBus modEventBus, ModContainer modContainer, Dist dist) {
		modEventBus.addListener(this::dataSetup);
		modEventBus.addListener(this::commonSetup);

		BLOCKS.register(modEventBus);
		ITEMS.register(modEventBus);
		ENTITY_TYPES.register(modEventBus);
		BLOCK_ENTITY_TYPES.register(modEventBus);
		CREATIVE_MODE_TABS.register(modEventBus);
		MENU_TYPES.register(modEventBus);
		PARTICLE_TYPES.register(modEventBus);
		DATA_COMPONENTS.register(modEventBus);
		ATTACHMENT_TYPES.register(modEventBus);
		RECIPE_SERIALIZERS.register(modEventBus);
		RECIPE_TYPES.register(modEventBus);
		FLUIDS.register(modEventBus);
		FLUID_TYPES.register(modEventBus);

		NeoForge.EVENT_BUS.register(this);

		modEventBus.addListener(this::addCreative);
		modEventBus.addListener(this::registerCapabilities);
		modEventBus.addListener(this::registerEntityAttributes);

		modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

		if (dist == Dist.CLIENT) {
			modEventBus.addListener(this::registerMenuScreens);
			modEventBus.addListener(this::registerEntityRenderers);
			modEventBus.addListener(this::registerParticleProviders);
		}
	}

	public void registerMenuScreens(RegisterMenuScreensEvent event) {
		event.register(CORRUPT_STORAGE_MENU.get(), CorruptStorageScreen::new);
		event.register(EXAMPLE_CRAFTER_MENU.get(), ExampleCrafterScreen::new);
		event.register(CRUDE_PREPROCESSOR_MENU.get(), CrudePreprocessorScreen::new);
		event.register(CRUDE_JAW_CRUSHER_MENU.get(), CrudeJawCrusherScreen::new);
		event.register(PRECISION_CRAFTING_TABLE_MENU.get(), PrecisionCraftingTableScreen::new);
	}

	public void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(EXAMPLE_ENTITY.get(), ExampleEntityRenderer::new);
		event.registerEntityRenderer(LICH.get(), LichEntityRenderer::new);
		event.registerBlockEntityRenderer(CORRUPT_STORAGE_BLOCK_ENTITY.get(), context -> new CorruptStorageRenderer());
		event.registerBlockEntityRenderer(EXAMPLE_CRAFTER_BLOCK_ENTITY.get(), context -> new ExampleCrafterRenderer());
	}

	private void commonSetup(final FMLCommonSetupEvent event) {
		LOGGER.info("Common Setup");
	}

	private void addCreative(BuildCreativeModeTabContentsEvent event) {
		if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS)
			event.accept(EXAMPLE_BLOCK_ITEM);
	}

	private void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, CRUDE_PREPROCESSOR_BLOCK_ENTITY.get(), CrudePreprocessorBlockEntity::getFluidHandler);
		event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, CRUDE_PREPROCESSOR_BLOCK_ENTITY.get(), CrudePreprocessorBlockEntity::getEnergyStorage);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, CRUDE_PREPROCESSOR_BLOCK_ENTITY.get(), CrudePreprocessorBlockEntity::getInventory);

		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, CRUDE_JAW_CRUSHER_BLOCK_ENTITY.get(), CrudeJawCrusherBlockEntity::getInventory);
		event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, CRUDE_JAW_CRUSHER_BLOCK_ENTITY.get(), CrudeJawCrusherBlockEntity::getEnergyStorage);

		event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, PRECISION_CRAFTING_TABLE_BLOCK_ENTITY.get(), PrecisionCraftingTableBlockEntity::getEnergyStorage);
		event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, PRECISION_CRAFTING_TABLE_BLOCK_ENTITY.get(), PrecisionCraftingTableBlockEntity::getFluidHandler);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, PRECISION_CRAFTING_TABLE_BLOCK_ENTITY.get(), PrecisionCraftingTableBlockEntity::getInventory);
		event.registerBlockEntity(OmniaEtNihilCapabilities.QuantumHandler.BLOCK, PRECISION_CRAFTING_TABLE_BLOCK_ENTITY.get(), PrecisionCraftingTableBlockEntity::getQuantumHandler);

	}

	public void registerEntityAttributes(EntityAttributeCreationEvent event) {
		event.put(EXAMPLE_ENTITY.get(), ExampleEntity.createMobAttributes().build());
		event.put(LICH.get(), LichEntity.createMobAttributes().build());
	}

	public void registerParticleProviders(RegisterParticleProvidersEvent event) {
		event.registerSpriteSet(ARCANE_PARTICLE.get(), ArcaneParticle.Factory::new);
	}

	public void dataSetup(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		ExistingFileHelper fileHelper = event.getExistingFileHelper();
		PackOutput packOutput = generator.getPackOutput();
		generator.addProvider(event.includeClient(), new ModItemModelProvider(packOutput, fileHelper));

		// generator.addProvider(event.includeClient(), new ModLanguageProvider(packOutput, MODID));

		// pack.mcmeta
		generator.addProvider(true, new PackMetadataGenerator(packOutput).add(PackMetadataSection.TYPE, new PackMetadataSection(Component.translatable("pack.omniaetnihil.mod.description"), DetectedVersion.BUILT_IN.getPackVersion(PackType.SERVER_DATA), Optional.of(new InclusiveRange<>(0, Integer.MAX_VALUE)))));
	}

	@SubscribeEvent
	public void onServerStarting(ServerStartingEvent event) {
		LOGGER.info("Server starting");
		LOGGER.info("This is the recipe type: " + EXAMPLE_CRAFTING_TYPE.get().toString());
		LOGGER.info("This is the recipe serializer: " + EXAMPLE_CRAFTING_SERIALIZER.get().toString());
	}

	@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
	public static class ClientModEvents {
		@SubscribeEvent
		public static void onClientSetup(FMLClientSetupEvent event) {
			LOGGER.info("Client Setup");
		}
	}
}
