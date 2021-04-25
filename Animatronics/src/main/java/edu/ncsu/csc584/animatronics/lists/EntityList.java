package edu.ncsu.csc584.animatronics.lists;

import edu.ncsu.csc584.animatronics.AnimatronicsMod;
import edu.ncsu.csc584.animatronics.AnimatronicsModRegistries;
import edu.ncsu.csc584.animatronics.entity.BonnieEntity;
import edu.ncsu.csc584.animatronics.entity.ChicaEntity;
import edu.ncsu.csc584.animatronics.entity.FoxyEntity;
import edu.ncsu.csc584.animatronics.entity.FreddyEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;

/**
 * Contains a list of references to the entities this mod adds
 * 
 * @author Brenden Lech
 * @author Bansi Chhatrala
 */
public class EntityList {
	
	/** The Freddy entity */
    public static EntityType<FreddyEntity> freddy = createFreddyEntity();
	
	/** The Bonnie entity */
    public static EntityType<BonnieEntity> bonnie = createBonnieEntity();
    
    /** The Chica entity */
    public static EntityType<ChicaEntity> chica = createChicaEntity();
    
    /** The Foxy entity */
    public static EntityType<FoxyEntity> foxy = createFoxyEntity();
    
    /**
     * Creates and returns the Freddy entity
     * Note: The item registry event is run before the entity registry event.
     * Therefore, this code cannot go inside the entity registry event because
     * the entity must be created before its spawn egg is registered.
     * 
     * @return the Freddy entity
     */
    private static EntityType<FreddyEntity> createFreddyEntity() {
    	
    	EntityType.Builder<FreddyEntity> freddyBuilder =
    			EntityType.Builder.create(FreddyEntity::new, EntityClassification.MONSTER);
    	
    	EntityType<FreddyEntity> freddy = freddyBuilder.build(AnimatronicsMod.MOD_ID + ":freddy");
    	freddy.setRegistryName(AnimatronicsModRegistries.getResourceLocation("freddy"));
    	
    	return freddy;
    }
    
    /**
     * Creates and returns the Bonnie entity
     * Note: The item registry event is run before the entity registry event.
     * Therefore, this code cannot go inside the entity registry event because
     * the entity must be created before its spawn egg is registered.
     * 
     * @return the Bonnie entity
     */
    private static EntityType<BonnieEntity> createBonnieEntity() {
    	
    	EntityType.Builder<BonnieEntity> bonnieBuilder =
    			EntityType.Builder.create(BonnieEntity::new, EntityClassification.MONSTER);
    	
    	EntityType<BonnieEntity> bonnie = bonnieBuilder.build(AnimatronicsMod.MOD_ID + ":bonnie");
    	bonnie.setRegistryName(AnimatronicsModRegistries.getResourceLocation("bonnie"));
    	
    	return bonnie;
    }
    
    /**
     * Creates and returns the Chica entity
     * Note: The item registry event is run before the entity registry event.
     * Therefore, this code cannot go inside the entity registry event because
     * the entity must be created before its spawn egg is registered.
     * 
     * @return the Chica entity
     */
    private static EntityType<ChicaEntity> createChicaEntity() {
        
        EntityType.Builder<ChicaEntity> chicaBuilder =
                EntityType.Builder.create(ChicaEntity::new, EntityClassification.MONSTER);
        
        EntityType<ChicaEntity> chica = chicaBuilder.build(AnimatronicsMod.MOD_ID + ":chica");
        chica.setRegistryName(AnimatronicsModRegistries.getResourceLocation("chica"));
        
        return chica;
    }
    
    /**
     * Creates and returns the Foxy entity
     * Note: The item registry event is run before the entity registry event.
     * Therefore, this code cannot go inside the entity registry event because
     * the entity must be created before its spawn egg is registered.
     * 
     * @return the Foxy entity
     */
    private static EntityType<FoxyEntity> createFoxyEntity() {
        
        EntityType.Builder<FoxyEntity> foxyBuilder =
                EntityType.Builder.create(FoxyEntity::new, EntityClassification.MONSTER);
        
        EntityType<FoxyEntity> foxy = foxyBuilder.build(AnimatronicsMod.MOD_ID + ":foxy");
        foxy.setRegistryName(AnimatronicsModRegistries.getResourceLocation("foxy"));
        
        return foxy;
    }
    
}
