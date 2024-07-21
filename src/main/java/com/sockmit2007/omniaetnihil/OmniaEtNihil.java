package com.sockmit2007.omniaetnihil;

import java.util.Optional;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.sockmit2007.omniaetnihil.block.CorruptStorage;
import com.sockmit2007.omniaetnihil.block.HurtBlock;
import com.sockmit2007.omniaetnihil.block.SpreadBlock;
import com.sockmit2007.omniaetnihil.block.TieredBlock;
import com.sockmit2007.omniaetnihil.block.entity.CorruptStorageBlockEntity;
import com.sockmit2007.omniaetnihil.client.renderer.block.CorruptStorageRenderer;
import com.sockmit2007.omniaetnihil.client.renderer.entity.ExampleEntityRenderer;
import com.sockmit2007.omniaetnihil.client.renderer.entity.LichEntityRenderer;
import com.sockmit2007.omniaetnihil.entity.ExampleEntity;
import com.sockmit2007.omniaetnihil.entity.LichEntity;
import com.sockmit2007.omniaetnihil.item.GrabberJar;
import com.sockmit2007.omniaetnihil.screen.CorruptStorageMenu;
import com.sockmit2007.omniaetnihil.screen.CorruptStorageScreen;
import com.sockmit2007.omniaetnihil.datagen.ModItemModelProvider;

import net.minecraft.DetectedVersion;
import net.minecraft.core.component.DataComponentType;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
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
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
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
	public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister
			.create(Registries.CREATIVE_MODE_TAB, MODID);
	private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister
			.create(NeoForgeRegistries.ATTACHMENT_TYPES, MODID);
	public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(MODID);
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE,
			MODID);
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister
			.create(Registries.BLOCK_ENTITY_TYPE, MODID);
	public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Registries.MENU,
			MODID);

	// #endregion

	private static <T extends AbstractContainerMenu> DeferredHolder<MenuType<?>, MenuType<T>> register(String name,
			MenuType.MenuSupplier<T> menu) {
		return MENU_TYPES.register(name, () -> new MenuType<>(menu, FeatureFlags.VANILLA_SET));
	}

	public static final DeferredHolder<MenuType<?>, MenuType<CorruptStorageMenu>> CORRUPT_STORAGE_MENU = register(
			"corrupt_storage",
			CorruptStorageMenu::new);

	public static final TagKey<Block> unspreadableBlocksTag = BlockTags
			.create(ResourceLocation.fromNamespaceAndPath(MODID, "unspreadable"));

	public static final DeferredBlock<Block> CORRUPT_STORAGE = BLOCKS.register("corrupt_storage",
			() -> new CorruptStorage(BlockBehaviour.Properties.of().noOcclusion().mapColor(MapColor.STONE)));
	public static final DeferredItem<BlockItem> CORRUPT_STORAGE_ITEM = ITEMS.registerSimpleBlockItem(
			"corrupt_storage",
			CORRUPT_STORAGE);

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CorruptStorageBlockEntity>> CORRUPT_STORAGE_BLOCK_ENTITY = BLOCK_ENTITY_TYPES
			.register("corrupt_storage",
					() -> BlockEntityType.Builder.of(CorruptStorageBlockEntity::new, CORRUPT_STORAGE.get())
							.build(null));

	public static final DeferredBlock<Block> EXAMPLE_BLOCK = BLOCKS.registerSimpleBlock("example_block",
			BlockBehaviour.Properties.of().mapColor(MapColor.STONE));
	public static final DeferredItem<BlockItem> EXAMPLE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("example_block",
			EXAMPLE_BLOCK);

	public static final DeferredBlock<Block> HURT_BLOCK = BLOCKS.register("hurt_block",
			() -> new HurtBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE)));
	public static final DeferredItem<BlockItem> HURT_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("hurt_block",
			HURT_BLOCK);

	public static final DeferredBlock<Block> SPREAD_BLOCK = BLOCKS.register("spread_block",
			() -> new SpreadBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE)));
	public static final DeferredItem<BlockItem> SPREAD_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("spread_block",
			SPREAD_BLOCK);

	public static final DeferredBlock<Block> TIERED_BLOCK = BLOCKS.register("tiered_block",
			() -> new TieredBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE)));
	public static final DeferredItem<BlockItem> TIERED_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("tiered_block",
			TIERED_BLOCK);

	public static final DeferredHolder<EntityType<?>, EntityType<ExampleEntity>> EXAMPLE_ENTITY = ENTITY_TYPES
			.register("example_entity",
					() -> EntityType.Builder.of(ExampleEntity::new, MobCategory.MONSTER).sized(0.9F, 0.9F)
							.clientTrackingRange(10).build("example_entity"));
	public static final DeferredHolder<EntityType<?>, EntityType<LichEntity>> LICH = ENTITY_TYPES
			.register("lich",
					() -> EntityType.Builder.of(LichEntity::new, MobCategory.MONSTER).sized(0.9F, 0.9F)
							.clientTrackingRange(10).build("lich"));

	public static final DeferredItem<SpawnEggItem> EXAMPLE_ENTITY_SPAWN_EGG = ITEMS.register("example_entity_spawn_egg",
			() -> new DeferredSpawnEggItem(EXAMPLE_ENTITY, 0xDFDFDF, 0x99CFE8, new Item.Properties()));
	public static final DeferredItem<SpawnEggItem> LICH_ENTITY_SPAWN_EGG = ITEMS.register("lich_entity_spawn_egg",
			() -> new DeferredSpawnEggItem(LICH, 0xDFDFDF, 0x99CFE8, new Item.Properties()));

	public static final DeferredItem<Item> EXAMPLE_ITEM = ITEMS.registerSimpleItem("example_item",
			new Item.Properties().food(new FoodProperties.Builder()
					.alwaysEdible().nutrition(1).saturationModifier(2f).build()));

	// #region Drops
	public static final DeferredItem<Item> ABYSSAL_FLESH = ITEMS.registerSimpleItem("abyssal_flesh",
			new Item.Properties());
	public static final DeferredItem<Item> ABYSSAL_BONE = ITEMS.registerSimpleItem("abyssal_bone",
			new Item.Properties());
	public static final DeferredItem<Item> AQUATIC_FLESH = ITEMS.registerSimpleItem("aquatic_flesh",
			new Item.Properties());
	public static final DeferredItem<Item> AQUATIC_BONE = ITEMS.registerSimpleItem("aquatic_bone",
			new Item.Properties());
	public static final DeferredItem<Item> ARCANE_FLESH = ITEMS.registerSimpleItem("arcane_flesh",
			new Item.Properties());
	public static final DeferredItem<Item> ARCANE_BONE = ITEMS.registerSimpleItem("arcane_bone",
			new Item.Properties());
	public static final DeferredItem<Item> DIVINE_FLESH = ITEMS.registerSimpleItem("divine_flesh",
			new Item.Properties());
	public static final DeferredItem<Item> DIVINE_BONE = ITEMS.registerSimpleItem("divine_bone",
			new Item.Properties());
	public static final DeferredItem<Item> ETHEREAL_FLESH = ITEMS.registerSimpleItem("ethereal_flesh",
			new Item.Properties());
	public static final DeferredItem<Item> ETHEREAL_BONE = ITEMS.registerSimpleItem("ethereal_bone",
			new Item.Properties());
	public static final DeferredItem<Item> INFERNAL_FLESH = ITEMS.registerSimpleItem("infernal_flesh",
			new Item.Properties());
	public static final DeferredItem<Item> INFERNAL_BONE = ITEMS.registerSimpleItem("infernal_bone",
			new Item.Properties());
	public static final DeferredItem<Item> PETRIFIED_FLESH = ITEMS.registerSimpleItem("petrified_flesh",
			new Item.Properties());
	public static final DeferredItem<Item> PETRIFIED_BONE = ITEMS.registerSimpleItem("petrified_bone",
			new Item.Properties());
	public static final DeferredItem<Item> VERDANT_FLESH = ITEMS.registerSimpleItem("verdant_flesh",
			new Item.Properties());
	public static final DeferredItem<Item> VERDANT_BONE = ITEMS.registerSimpleItem("verdant_bone",
			new Item.Properties());
	public static final DeferredItem<Item> YELLOW_FLESH = ITEMS.registerSimpleItem("yellow_flesh",
			new Item.Properties());
	public static final DeferredItem<Item> YELLOW_BONE = ITEMS.registerSimpleItem("yellow_bone",
			new Item.Properties());
	// #endregion

	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> MANA_DATA_COMPONENT = DATA_COMPONENTS
			.registerComponentType(
					"mana",
					builder -> builder
							.persistent(Codec.INT)
							.networkSynchronized(ByteBufCodecs.INT));

	public static final DeferredItem<Item> GRABBER_JAR = ITEMS.register("grabber_jar", () -> new GrabberJar(
			new Item.Properties().component(MANA_DATA_COMPONENT.value(), 0)));

	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS
			.register("omniaetnihil_tab", () -> CreativeModeTab.builder()
					.title(Component.translatable("itemGroup.omniaetnihil.omniaetnihil_tab"))
					.withTabsBefore(CreativeModeTabs.COMBAT)
					.icon(() -> EXAMPLE_ITEM.get().getDefaultInstance())
					.displayItems((parameters, output) -> {
						output.accept(EXAMPLE_ITEM.get());
						output.accept(EXAMPLE_BLOCK_ITEM.get());
						output.accept(GRABBER_JAR.get());
						output.accept(HURT_BLOCK_ITEM.get());
						output.accept(EXAMPLE_ENTITY_SPAWN_EGG.get());
						output.accept(LICH_ENTITY_SPAWN_EGG.get());
						output.accept(SPREAD_BLOCK_ITEM.get());
						output.accept(TIERED_BLOCK_ITEM.get());
						output.accept(CORRUPT_STORAGE_ITEM.get());
					}).build());

	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> DROPS_TAB = CREATIVE_MODE_TABS
			.register("drops_tab", () -> CreativeModeTab.builder()
					.title(Component.translatable("itemGroup.omniaetnihil.drops_tab"))
					.withTabsBefore(CreativeModeTabs.COMBAT)
					.icon(() -> ABYSSAL_FLESH.get().getDefaultInstance())
					.displayItems((parameters, output) -> {
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
						output.accept(YELLOW_BONE.get());
						output.accept(YELLOW_FLESH.get());
					}).build());

	public OmniaEtNihil(IEventBus modEventBus, ModContainer modContainer, Dist dist) {
		modEventBus.addListener(this::dataSetup);
		modEventBus.addListener(this::commonSetup);

		BLOCKS.register(modEventBus);
		ITEMS.register(modEventBus);
		ENTITY_TYPES.register(modEventBus);
		BLOCK_ENTITY_TYPES.register(modEventBus);
		CREATIVE_MODE_TABS.register(modEventBus);
		MENU_TYPES.register(modEventBus);

		DATA_COMPONENTS.register(modEventBus);
		ATTACHMENT_TYPES.register(modEventBus);

		NeoForge.EVENT_BUS.register(this);

		modEventBus.addListener(this::addCreative);
		modEventBus.addListener(this::registerCapabilities);
		modEventBus.addListener(this::registerEntityAttributes);

		modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

		if (dist == Dist.CLIENT) {
			modEventBus.addListener(this::registerMenuScreens);
			modEventBus.addListener(this::registerEntityRenderers);
		}
	}

	public void registerMenuScreens(RegisterMenuScreensEvent event) {
		event.register(CORRUPT_STORAGE_MENU.get(), CorruptStorageScreen::new);
	}

	public void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(EXAMPLE_ENTITY.get(), ExampleEntityRenderer::new);
		event.registerEntityRenderer(LICH.get(), LichEntityRenderer::new);
		event.registerBlockEntityRenderer(CORRUPT_STORAGE_BLOCK_ENTITY.get(), context -> new CorruptStorageRenderer());
	}

	private void commonSetup(final FMLCommonSetupEvent event) {
		LOGGER.info("Common Setup");
	}

	private void addCreative(BuildCreativeModeTabContentsEvent event) {
		if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS)
			event.accept(EXAMPLE_BLOCK_ITEM);
	}

	private void registerCapabilities(RegisterCapabilitiesEvent event) {
	}

	public void registerEntityAttributes(EntityAttributeCreationEvent event) {
		event.put(EXAMPLE_ENTITY.get(), ExampleEntity.createMobAttributes().build());
		event.put(LICH.get(), LichEntity.createMobAttributes().build());
	}

	public void dataSetup(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		ExistingFileHelper fileHelper = event.getExistingFileHelper();
		PackOutput packOutput = generator.getPackOutput();
		generator.addProvider(event.includeClient(), new ModItemModelProvider(packOutput, fileHelper));

		// generator.addProvider(event.includeClient(), new
		// ModLanguageProvider(packOutput, MODID));

		// pack.mcmeta
		generator.addProvider(true,
				new PackMetadataGenerator(packOutput).add(PackMetadataSection.TYPE, new PackMetadataSection(
						Component.translatable("pack.omniaetnihil.mod.description"),
						DetectedVersion.BUILT_IN.getPackVersion(PackType.SERVER_DATA),
						Optional.of(new InclusiveRange<>(0, Integer.MAX_VALUE)))));
	}

	@SubscribeEvent
	public void onServerStarting(ServerStartingEvent event) {
		LOGGER.info("Server starting");
	}

	@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
	public static class ClientModEvents {
		@SubscribeEvent
		public static void onClientSetup(FMLClientSetupEvent event) {
			LOGGER.info("Client Setup");
		}
	}
}
