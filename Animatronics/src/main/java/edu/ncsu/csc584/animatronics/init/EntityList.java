package edu.ncsu.csc584.animatronics.init;

import edu.ncsu.csc584.animatronics.AnimatronicsMod;
import edu.ncsu.csc584.animatronics.AnimatronicsModRegistries;
import edu.ncsu.csc584.animatronics.entities.FreddyEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraftforge.event.RegistryEvent;

public class EntityList {
	
	// Minecraft/Forge runs the item registry event before the entity registry event. This code
	// can't go inside the entity registry event, because this entity needs to be created before
	// the item registry code runs (so the spawn egg is created)
    public static EntityType<?> freddy = EntityType.Builder.create(FreddyEntity::new, EntityClassification.CREATURE).build(AnimatronicsMod.MOD_ID + ":freddy").setRegistryName(AnimatronicsMod.RegistryEvents.resourceLocation("freddy"));
    
    /**
     * Registers all entity spawn eggs
     * 
     * @param event the event used for registration
     */
    public static void registerEntitySpawnEggs(final RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
            TutorialItems.tutorial_entity_egg = registerEntitySpawnEgg(freddy, 0x7E4C12, 0xB6660B, "freddy_spawn_egg")
        );
    }

    /**
     * Registers an entity's spawn egg
     * 
     * @param type the entity's type
     * @param baseColor the base color of the spawn egg
     * @param secondaryColor the secondary color of the spawn egg
     * @param name the name of the entity's spawn egg item (i.e. "cow_spawn_egg")
     */
    public static Item registerEntitySpawnEgg(EntityType<?> type, int baseColor, int secondaryColor, String name) {
        SpawnEggItem item = new SpawnEggItem(type, baseColor, secondaryColor, new Item.Properties().group(ItemGroup.MISC));
        item.setRegistryName(AnimatronicsModRegistries.location(name));
        return item;
    }
    
}
