package edu.ncsu.csc584.animatronics;

import org.apache.logging.log4j.Logger;

import edu.ncsu.csc584.animatronics.init.TutorialEntities;
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
public class AnimatronicsModRegistries 
{
    public static final Logger LOGGER = AnimatronicsMod.logger;
    public static final String MODID = AnimatronicsMod.MOD_ID;
    
    @SubscribeEvent
    public static void registerItems(final RegistryEvent.Register<Item> event)
    {
        event.getRegistry().registerAll
        (
            

        );
        
        TutorialEntities.registerEntitySpawnEggs(event);
        
        LOGGER.info("Items registered.");
    }
    
    @SubscribeEvent
    public static void registerBlocks(final RegistryEvent.Register<Block> event)
    {
        event.getRegistry().registerAll
        (
            
        );
        
        LOGGER.info("Blocks registered.");
    }
    
    @SubscribeEvent
    public static void registerEntities(final RegistryEvent.Register<EntityType<?>> event)
    {
        event.getRegistry().registerAll
        (
            TutorialEntities.tutorial_entity
        );
        
        TutorialEntities.registerEntityWorldSpawns();
    }
    
    public static ResourceLocation location(String name)
    {
        return new ResourceLocation(MODID, name);
    }
}   









