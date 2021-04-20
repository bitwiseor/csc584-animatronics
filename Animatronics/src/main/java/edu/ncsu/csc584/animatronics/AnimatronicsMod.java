package edu.ncsu.csc584.animatronics;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.ncsu.csc584.animatronics.lists.BlockList;
import edu.ncsu.csc584.animatronics.lists.ItemList;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * The main class for the Animatronics Mod
 * 
 * @author Brenden Lech
 * @author Bansi Chhatrala
 */
@Mod("animatronics")
public class AnimatronicsMod {
	
	/** Used to reference this main mod class outside of this class */
	public static AnimatronicsMod instance;
	/** The id name of this mod */
	public static final String MOD_ID = "animatronics";
	/** Used to print debug information to the console */
	public static Logger LOGGER = LogManager.getLogger(MOD_ID);
	
	/**
	 * Begins the initialization code for the mod
	 */
	public AnimatronicsMod() {
		
		// Required forge registration code so forge runs the setup methods
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientRegistries);
		MinecraftForge.EVENT_BUS.register(this);
		
		instance = this;
		
	}
	
	private void setup(FMLCommonSetupEvent event) {
		logger.info("setup method run successfully");
	}
	
	private void clientRegistries(FMLClientSetupEvent event) {
		logger.info("clientRegistries method run successfully");
	}
	
	/**
	 * A class containing the functions that register new items and blocks
	 * 
	 * @author Brenden Lech
	 * @author Bansi Chhatrala
	 */
	@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
	public static class RegistryEvents {
		
		/**
		 * Registers items
		 * 
		 * @param event used to register items
		 */
		@SubscribeEvent
		public static void registerItems(RegistryEvent.Register<Item> event) {
			
			// Prepares the tutorial item for registration
			Item.Properties tutorial_item_properties = new Item.Properties();
			tutorial_item_properties.group(ItemGroup.MISC);
			ItemList.tutorial_item = new Item(tutorial_item_properties);
			ItemList.tutorial_item.setRegistryName(resourceLocation("tutorial_item"));
			
			// Prepares the tutorial item block for registration
			Item.Properties tutorial_item_block_properties = new Item.Properties();
			tutorial_item_block_properties.group(ItemGroup.BUILDING_BLOCKS);
			ItemList.tutorial_block = new BlockItem(BlockList.tutorial_block,
					tutorial_item_block_properties);
			ItemList.tutorial_block.setRegistryName(BlockList.tutorial_block.getRegistryName());
			
			// Registers all the items
			event.getRegistry().registerAll(
					ItemList.tutorial_item,
					ItemList.tutorial_block
			);
			
			logger.info("Items registered!");
			
		}
		
		/**
		 * Registers blocks
		 * 
		 * @param event used to register blocks
		 */
		@SubscribeEvent
		public static void registerBlocks(RegistryEvent.Register<Block> event) {
			
			// Prepares the tutorial block for registration
			Block.Properties tutorial_block_properties = Block.Properties.create(Material.IRON);
			tutorial_block_properties.hardnessAndResistance(2.0f, 3.0f);
			tutorial_block_properties.lightValue(1);
			tutorial_block_properties.sound(SoundType.METAL);
			BlockList.tutorial_block = new Block(tutorial_block_properties);
			BlockList.tutorial_block.setRegistryName(resourceLocation("tutorial_block"));
			
			// Registers all the blocks
			event.getRegistry().registerAll(BlockList.tutorial_block);
			
			logger.info("Blocks registered!");
			
		}
		
		/**
		 * Returns the location of the resource with the given name
		 * @param resourceName the name of the resource whose location to get
		 * @return the location of the resource with the given name
		 */
		private static ResourceLocation resourceLocation(String resourceName) {
			return new ResourceLocation(MOD_ID, resourceName);
		}
		
	}
	
}
