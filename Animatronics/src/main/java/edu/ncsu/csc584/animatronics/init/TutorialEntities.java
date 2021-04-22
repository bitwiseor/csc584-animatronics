package edu.ncsu.csc584.animatronics.init;

import edu.ncsu.csc584.animatronics.AnimatronicsModRegistries;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraftforge.event.RegistryEvent;

public class TutorialEntities {
    public static EntityType<?> tutorial_entity;
    
    public static void registerEntitySpawnEggs(final RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
            TutorialItems.tutorial_entity_egg = registerEntitySpawnEgg(tutorial_entity, 0xF2E9E9, 0xFAA8F5, "tutorial_entity_egg")
        );
    }
    
    public static Item registerEntitySpawnEgg(EntityType<?> type, int color1, int color2, String name) {
        SpawnEggItem item = new SpawnEggItem(type, color1, color2, new Item.Properties().group(ItemGroup.MISC));
        item.setRegistryName(AnimatronicsModRegistries.location(name));
        return item;
    }
    
    public static void registerEntityWorldSpawns() {
        registerEntityWorldSpawn(tutorial_entity, EntityClassification.CREATURE, Biomes.PLAINS, Biomes.BEACH, Biomes.JUNGLE);
    }
    
    public static void registerEntityWorldSpawn(EntityType<?> entity, EntityClassification classification, Biome ... biomes) {
        for(Biome biome : biomes) {
            if(biome != null) {
                biome.getSpawns(classification).add(new SpawnListEntry(entity, 10, 1, 10));
            }
        }
    }
}
