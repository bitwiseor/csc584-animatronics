package edu.ncsu.csc584.animatronics;

import org.apache.logging.log4j.Logger;

import edu.ncsu.csc584.animatronics.init.EntityList;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.SwordItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class AnimatronicsModRegistries {

	/** Used to print debug information to the console */
    public static final Logger logger = AnimatronicsMod.logger;
    
    @SubscribeEvent
    public static void registerItems(final RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll
        (
            
        );
        
        EntityList.registerEntitySpawnEggs(event);
        
        logger.info("Items registered.");
    }
    
    @SubscribeEvent
    public static void registerBlocks(final RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll
        (
            
        );
        
        logger.info("Blocks registered.");
    }
    
    public static ResourceLocation location(String name) {
        return new ResourceLocation(AnimatronicsMod.MOD_ID, name);
    }
}   









