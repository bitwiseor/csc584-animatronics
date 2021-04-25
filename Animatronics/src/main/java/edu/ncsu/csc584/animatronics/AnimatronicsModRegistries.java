package edu.ncsu.csc584.animatronics;

import org.apache.logging.log4j.Logger;

import edu.ncsu.csc584.animatronics.lists.BlockList;
import edu.ncsu.csc584.animatronics.lists.EntityList;
import edu.ncsu.csc584.animatronics.lists.ItemList;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * A class containing the functions that register new items and blocks
 * 
 * @author Brenden Lech
 * @author Bansi Chhatrala
 */
@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class AnimatronicsModRegistries {
	
	/** Used to print debug information to the console */
    public static final Logger logger = AnimatronicsMod.logger;
	
	/**
	 * Registers items
	 * 
	 * @param event used to register items
	 */
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		
		// Creates the tutorial item so it can be registered
		Item.Properties tutorial_item_properties = new Item.Properties();
		tutorial_item_properties.group(ItemGroup.MISC);
		ItemList.tutorial_item = new Item(tutorial_item_properties);
		ItemList.tutorial_item.setRegistryName(getResourceLocation("tutorial_item"));

		// Creates the tutorial item block so it can be registered
		Item.Properties tutorial_item_block_properties = new Item.Properties();
		tutorial_item_block_properties.group(ItemGroup.BUILDING_BLOCKS);
		ItemList.tutorial_block = new BlockItem(BlockList.tutorial_block,
				tutorial_item_block_properties);
		ItemList.tutorial_block.setRegistryName(BlockList.tutorial_block.getRegistryName());

		// Creates the Freddy Spawn Egg so it can be registered
		ItemList.freddy_spawn_egg = createEntitySpawnEgg(EntityList.freddy,
				ItemList.FREDDY_SPAWN_EGG_BASE_COLOR, ItemList.FREDDY_SPAWN_EGG_SECONDARY_COLOR,
				"freddy_spawn_egg");

		// Creates the Bonnie Spawn Egg so it can be registered
		ItemList.bonnie_spawn_egg = createEntitySpawnEgg(EntityList.bonnie,
				ItemList.BONNIE_SPAWN_EGG_BASE_COLOR, ItemList.BONNIE_SPAWN_EGG_SECONDARY_COLOR,
				"bonnie_spawn_egg");
		
		// Creates the Chica Spawn Egg so it can be registered
        ItemList.chica_spawn_egg = createEntitySpawnEgg(EntityList.chica,
                ItemList.CHICA_SPAWN_EGG_BASE_COLOR, ItemList.CHICA_SPAWN_EGG_SECONDARY_COLOR,
                "chica_spawn_egg");
        
     // Creates the Foxy Spawn Egg so it can be registered
        ItemList.foxy_spawn_egg = createEntitySpawnEgg(EntityList.foxy,
                ItemList.FOXY_SPAWN_EGG_BASE_COLOR, ItemList.FOXY_SPAWN_EGG_SECONDARY_COLOR,
                "foxy_spawn_egg");
		
		// Registers all the items
		event.getRegistry().registerAll(
				ItemList.tutorial_item,
				ItemList.tutorial_block,
				ItemList.freddy_spawn_egg,
				ItemList.bonnie_spawn_egg,
				ItemList.chica_spawn_egg,
				ItemList.foxy_spawn_egg
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
		BlockList.tutorial_block.setRegistryName(getResourceLocation("tutorial_block"));
		
		// Registers all the blocks
		event.getRegistry().registerAll(BlockList.tutorial_block);
		
		logger.info("Blocks registered!");
		
	}
	
	/**
	 * Registers entities
	 * 
	 * @param event the event used to register entities
	 */
	@SubscribeEvent
	public static void registerEntities(RegistryEvent.Register<EntityType<?>> event) {
		
		event.getRegistry().registerAll(
				EntityList.freddy,
				EntityList.bonnie,
				EntityList.chica,
				EntityList.foxy
		);
		
	}
	
	/**
	 * Returns the location of the resource with the given name
	 * @param resourceName the name of the resource whose location to get
	 * @return the location of the resource with the given name
	 */
	public static ResourceLocation getResourceLocation(String resourceName) {
		return new ResourceLocation(AnimatronicsMod.MOD_ID, resourceName);
	}

    /**
     * Prepares an entity's spawn egg for registration
     * 
     * @param entityType the entity type of the entity this spawn egg spawns
     * @param baseColor the base color of the spawn egg
     * @param secondaryColor the secondary color of the spawn egg
     * @param itemName the name of the entity's spawn egg item (i.e. "cow_spawn_egg")
     */
    private static Item createEntitySpawnEgg(EntityType<?> entityType, int baseColor,
    		int secondaryColor, String itemName) {
    	
    	Item.Properties spawnEggProperties = new Item.Properties();
    	spawnEggProperties.group(ItemGroup.MISC);
    	
        SpawnEggItem spawnEgg =
        		new SpawnEggItem(entityType, baseColor, secondaryColor, spawnEggProperties);
        spawnEgg.setRegistryName(getResourceLocation(itemName));
        return spawnEgg;
        
    }
	
}