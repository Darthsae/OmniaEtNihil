package com.sockmit2007.omniaetnihil;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.sockmit2007.omniaetnihil.block.HurtBlock;
import com.sockmit2007.omniaetnihil.item.GrabberJar;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
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
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
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

	public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
	public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister
			.create(Registries.CREATIVE_MODE_TAB, MODID);
	private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister
			.create(NeoForgeRegistries.ATTACHMENT_TYPES, MODID);
	public static final DeferredRegister.DataComponents REGISTRAR = DeferredRegister.createDataComponents(MODID);

	public static final DeferredBlock<Block> EXAMPLE_BLOCK = BLOCKS.registerSimpleBlock("example_block",
			BlockBehaviour.Properties.of().mapColor(MapColor.STONE));
	public static final DeferredItem<BlockItem> EXAMPLE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("example_block",
			EXAMPLE_BLOCK);

	public static final DeferredBlock<Block> HURT_BLOCK = BLOCKS.register("hurt_block",
			() -> new HurtBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE)));
	public static final DeferredItem<BlockItem> HURT_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("hurt_block",
			HURT_BLOCK);

	public static final DeferredItem<Item> EXAMPLE_ITEM = ITEMS.registerSimpleItem("example_item",
			new Item.Properties().food(new FoodProperties.Builder()
					.alwaysEdible().nutrition(1).saturationModifier(2f).build()));

	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> MANA_DATA_COMPONENT = REGISTRAR
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
					}).build());

	public OmniaEtNihil(IEventBus modEventBus, ModContainer modContainer) {
		modEventBus.addListener(this::commonSetup);

		BLOCKS.register(modEventBus);
		ITEMS.register(modEventBus);
		CREATIVE_MODE_TABS.register(modEventBus);

		REGISTRAR.register(modEventBus);
		ATTACHMENT_TYPES.register(modEventBus);

		NeoForge.EVENT_BUS.register(this);

		modEventBus.addListener(this::addCreative);
		modEventBus.addListener(this::registerCapabilities);

		modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
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
