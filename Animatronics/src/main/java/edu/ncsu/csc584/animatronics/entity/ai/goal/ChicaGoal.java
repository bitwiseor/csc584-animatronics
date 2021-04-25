package edu.ncsu.csc584.animatronics.entity.ai.goal;

import java.util.List;

import edu.ncsu.csc584.animatronics.entity.ai.util.Action;
import edu.ncsu.csc584.animatronics.entity.ai.util.Communicatable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.AxisAlignedBB;

/**
 * Runs the decision tree implementation used for the Chica entity
 * 
 * @author Brenden Lech
 * @author Bansi Chhatrala
 */
public class ChicaGoal extends Goal {

	/** The speed this mob moves at when fleeing the player */
	private final double FLEE_SPEED = 0.7;
	/** The speed this mob moves at when wandering around */
	private final double WANDER_SPEED = 0.4;
	/** The speed this mob moves at when panicking */
	private final double PANIC_SPEED = 0.8;
	/** The speed this mob moves at when moving to attack the player */
	private final double ATTACK_SPEED = 0.6;
	/** The cooldown time between this mob's attacks in game ticks */
	private final int COOLDOWN_TIME = 20;
	
	/** How far this entity can pathfind away during wandering */
	private final int WANDER_DISTANCE_XZ = 10;
	/** How far this entity can pathfind away during wandering (up/down) */
	private final int WANDER_DISTANCE_Y = 3;
	/** The chance of this mob choosing a new wander path after it finished its previous
	 * (in percentage chance per tick) */
	private final float NEW_WANDER_PATH_CHANCE = 0.02f;

	/** How far this entity can receive communications from other entities from */
	private final int COMMUNICATION_DISTANCE_XZ = 40;
	/** How far this entity can receive communications from other entities from (up//down) */
	private final int COMMUNICATION_DISTANCE_Y = 20;
	
	/** The time that this mob will panic for after being attacked, in ticks */
	private final int PANIC_TIME = 30;
	
	/** The percentage this entity's health must be below to be considered at low health */
	private final double LOW_HEALTH_PERCENTAGE = 0.3;
	
	/** The entity running this goal */
	private final MobEntity entity;
	/** The object that runs this mob's actions */
	Action action;
	
	/** The box that the mob wanders around in */
	private AxisAlignedBB wanderBox;
	/** The box that other communicating mobs must be inside for this mob to be aware of them */
	private AxisAlignedBB communicationBox;
	
	/** Times how long this mob will continue panicking for */
	private int panicTimer;
	/** Whether this mob is currently attacking an entity */
	private boolean isAttacking;
	
    /**
     * Creates a new ChicaGoal, used for the Chica entity's AI
     * 
     * @param entityIn the entity this goal controls
     */
    public ChicaGoal(MobEntity entityIn) {
    	
        entity = entityIn;
        action = new Action(entity);

		wanderBox = new AxisAlignedBB(-WANDER_DISTANCE_XZ, -WANDER_DISTANCE_Y,
				-WANDER_DISTANCE_XZ, WANDER_DISTANCE_XZ, WANDER_DISTANCE_Y, WANDER_DISTANCE_XZ);
		communicationBox = new AxisAlignedBB(-COMMUNICATION_DISTANCE_XZ, -COMMUNICATION_DISTANCE_Y,
				-COMMUNICATION_DISTANCE_XZ, COMMUNICATION_DISTANCE_XZ, COMMUNICATION_DISTANCE_Y,
				COMMUNICATION_DISTANCE_XZ);
        
		panicTimer = 0;
		isAttacking = false;
		
    }
    
    @Override
    public boolean shouldExecute() {
    	return true;
    }
    
    /**
     * Runs the decision tree every game tick
     */
    public void tick() {
    	
    	LivingEntity targetedPlayer = entity.getAttackTarget();
    	if (targetedPlayer != null) {
    		// A player is visible to this entity
    		
    		if (entity.getHealth() <= entity.getMaxHealth() * LOW_HEALTH_PERCENTAGE) {
    			// This entity is at low health
    			
    			if (hasHelpAttacking()) {
    				// Other entities are also attacking the player
    				action.attack(targetedPlayer, ATTACK_SPEED, COOLDOWN_TIME);
    				isAttacking = true;
    				
    			} else {
    				// No other communicatable entities are also attacking the player
    				action.flee(targetedPlayer, FLEE_SPEED);
    				isAttacking = false;
    			}
    			
    		} else {
    			// This entity is not at low health
    			
    			if (entity.hurtTime > 0) {
    				panicTimer = PANIC_TIME;
    			}
    			
    			if (panicTimer > 0) {
    				// This entity has been hurt recently
    				action.wander(wanderBox, PANIC_SPEED, 1.0f);
    				isAttacking = false;
    				
    			} else {
    				// This entity has not been hurt recently
    				action.attack(targetedPlayer, ATTACK_SPEED, COOLDOWN_TIME);
    				isAttacking = true;
    				
    			}
    			
    		}
    		
    	} else {
    		// No players are visible to this entity
    		action.wander(wanderBox, WANDER_SPEED, NEW_WANDER_PATH_CHANCE);
    		isAttacking = false;
    		
    	}
    	
    	action.decrementAttackCooldown();
    	panicTimer--;
    	
    	action.regenerateHealth();
    	
    }
	
	/**
	 * Returns whether this mob is currently attacking an entity
	 * 
	 * @return whether this mob is currently attacking an entity
	 */
	public boolean isAttacking() {
		return isAttacking;
	}
    
    /**
     * Returns whether another animatronic is currently attacking this mob's target
     * 
     * @return whether another animatronic is currently attacking this mob's target
     */
    private boolean hasHelpAttacking() {
    	
    	if (entity.getAttackTarget() != null) {
    		
    		List<MobEntity> nearbyEntities =
    				entity.world.getEntitiesWithinAABB(MobEntity.class,
    						communicationBox.offset(entity.getPosition()));
    		
    		// Identifies any nearby animatronic mobs that can communicate
    		for (MobEntity nearbyEntity : nearbyEntities) {
    			if (nearbyEntity != entity && nearbyEntity instanceof Communicatable) {
    				Communicatable nearbyCommunicatable = (Communicatable)nearbyEntity;
    				
    				// Returns true if the nearby mob has the same attack target as this mob
    				if (nearbyCommunicatable.getEntityBeingAttacked() ==
    						entity.getAttackTarget()) {
    					return true;
    				}
    				
    			}
    		}
    		
    	}
    	
    	return false;
    	
    }
    
}
