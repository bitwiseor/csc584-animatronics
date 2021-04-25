package edu.ncsu.csc584.animatronics.entity.ai.goal;

import java.util.EnumSet;

import edu.ncsu.csc584.animatronics.entity.ai.util.Behavior;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;

/**
 * Runs the decision tree implementation used for the Bonnie entity
 * 
 * @author Brenden Lech
 * @author Bansi Chhatrala
 */
public class BonnieGoal extends Goal {
	
	/** The speed this mob moves at when fleeing the player */
	private final double FLEE_SPEED = 0.7;
	/** The speed this mob moves at when moving to attack the player */
	private final double ATTACK_SPEED = 0.6;
	/** The speed this mob moves at when wandering around */
	private final double WANDER_SPEED = 0.4;
	/** The cooldown time between this mob's attacks in game ticks */
	private final int COOLDOWN_TIME = 20;
	
	/** How far away players can be before this entity is no longer aware of them */
	private final int AWARENESS_DISTANCE_XZ = 50;
	/** How far away players can be (up/down) before this entity is no longer aware of them */
	private final int AWARENESS_DISTANCE_Y = 20;
	
	/** How far this entity can pathfind away during wandering */
	private final int WANDER_DISTANCE_XZ = 10;
	/** How far this entity can pathfind away during wandering (up/down) */
	private final int WANDER_DISTANCE_Y = 3;
	/** How far offset the wander box will be when a player is nearby,
	 * so the mob wanders towards the player */
	private final int WANDER_OFFSET = 5;
	/** The chance of this mob choosing a new wander path after it finished its previous
	 * (in percentage chance per tick) */
	private final float NEW_WANDER_PATH_CHANCE = 0.02f;
	
	/** The percentage this entity's health must be below to be considered at low health */
	private final double LOW_HEALTH_PERCENTAGE = 0.3;
	/** The percentage a player's health must be below to be considered at low health */
	private final double LOW_PLAYER_HEALTH_PERCENTAGE = 0.3;
	
	/** The entity that is running this goal */
	private final MobEntity entity;
	
	/** Whether the player was just attacked */
	private boolean justAttackedPlayer;
	
	/** The box which a player must be inside for this entity to be aware of the player */
	private AxisAlignedBB awarenessBox;
	/** The box that the mob wanders around in */
	private AxisAlignedBB wanderBox;
	
	/** The object that runs this mob's behaviors */
	private Behavior behavior;
	
	/**
	 * Creates a new BonnieGoal, used for the Bonnie entity's AI
	 * 
	 * @param entityIn the entity this goal controls
	 */
	public BonnieGoal(MobEntity entityIn) {
		
		entity = entityIn;
		justAttackedPlayer = false;
		awarenessBox = new AxisAlignedBB(-AWARENESS_DISTANCE_XZ, -AWARENESS_DISTANCE_Y,
				-AWARENESS_DISTANCE_XZ, AWARENESS_DISTANCE_XZ, AWARENESS_DISTANCE_Y,
				AWARENESS_DISTANCE_XZ);
		wanderBox = new AxisAlignedBB(-WANDER_DISTANCE_XZ, -WANDER_DISTANCE_Y,
				-WANDER_DISTANCE_XZ, WANDER_DISTANCE_XZ, WANDER_DISTANCE_Y, WANDER_DISTANCE_XZ);
		behavior = new Behavior(entity);
		
		this.setMutexFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE, Goal.Flag.LOOK));
		
	}
	
	/**
	 * Returns whether this goal should execute this tick
	 * 
	 * @return whether this goal should execute this tick
	 */
	@Override
	public boolean shouldExecute() {
		return true;
	}
	
	/**
	 * Runs the decision tree every game tick
	 */
	public void tick() {
		
		LivingEntity nearestPlayer = nearbyPlayer();
		
		if (nearestPlayer != null) {
			// A player is within the awareness box
			
			LivingEntity targetedPlayer = entity.getAttackTarget();
			if (targetedPlayer != null) {
				// A player is visible to this entity
				
				if (entity.getHealth() <= entity.getMaxHealth() * LOW_HEALTH_PERCENTAGE) {
					// This entity is at low health
					if (behavior.flee(targetedPlayer, FLEE_SPEED)) {
						justAttackedPlayer = false;
					}
					
				} else {
					// This entity is not at low health
					
					if (justAttackedPlayer) {
						// This entity just attacked the player
						
						if (targetedPlayer.getHealth() <=
								targetedPlayer.getMaxHealth() * LOW_PLAYER_HEALTH_PERCENTAGE) {
							// The player's health is low
							if (behavior.flee(targetedPlayer, FLEE_SPEED)) {
								justAttackedPlayer = false;
							}
							
						} else {
							// The player's health is not low
							if (behavior.attack(targetedPlayer, ATTACK_SPEED, COOLDOWN_TIME)) {
								justAttackedPlayer = true;
							}
							
						}
						
					} else {
						// This entity did not just attack the player
						if (behavior.attack(targetedPlayer, ATTACK_SPEED, COOLDOWN_TIME)) {
							justAttackedPlayer = true;
						}
						
					}
					
				}
				
			} else {
				// No player is visible to this entity
				behavior.wanderTowardsPlayer(wanderBox, WANDER_SPEED, NEW_WANDER_PATH_CHANCE,
						nearestPlayer, WANDER_OFFSET);
				
			}
			
		} else {
			// No players are within the awareness box
			behavior.wander(wanderBox, WANDER_SPEED, NEW_WANDER_PATH_CHANCE);
			
		}
		
		behavior.decrementAttackCooldown();
		
		behavior.regenerateHealth();
		
	}
	
	/**
	 * Returns the nearest player within the awarenessBox, or null if there is no player
	 * 
	 * @return the nearest player within the awarenessBox, or null if there is no player
	 */
	private LivingEntity nearbyPlayer() {
		
		// Returns the targeted player if there is one
		if (entity.getAttackTarget() != null) {
			return entity.getAttackTarget();
		}
		
		// Finds the nearest survival or adventure mode player within the awarenessBox
		AxisAlignedBB offsetAwarenessBox = awarenessBox.offset(entity.getPosition());
		PlayerEntity nearestPlayer = null;
		double shortestDistance = Double.MAX_VALUE;
		
		for (PlayerEntity player : entity.world.getPlayers()) {
			
			if (!player.isCreative() && !player.isSpectator() &&
					offsetAwarenessBox.contains(player.getPositionVec())) {
				
				double distance = entity.getDistanceSq(player);
				if (distance < shortestDistance) {
					nearestPlayer = player;
					shortestDistance = distance;
				}
				
			}
			
		}
		
		// Returns the nearest player within the awarenessBox, or null if there are none
		return nearestPlayer;
		
	}
	
}
















