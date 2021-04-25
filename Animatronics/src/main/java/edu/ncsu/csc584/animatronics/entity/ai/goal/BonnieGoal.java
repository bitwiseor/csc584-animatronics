package edu.ncsu.csc584.animatronics.entity.ai.goal;

import java.util.EnumSet;
import java.util.List;

import edu.ncsu.csc584.animatronics.AnimatronicsMod;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameType;

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
	
	/** The path this mob is fleeing along to get away from the player */
	private Path fleePath;
	/** The location this mob is fleeing to to get away from the player */
	private Vec3d fleeLocation;
	
	/** The path this mob is moving along to get to the player to attack them */
	private Path attackPath;
	/** The current cooldown between this mob's attacks in game ticks */
	private int attackCooldown;
	/** Whether the player was just attacked */
	private boolean justAttackedPlayer;
	
	/** The path this mob is wandering along */
	private Path wanderPath;
	/** The box which a player must be inside for this entity to be aware of the player */
	private AxisAlignedBB awarenessBox;
	/** The box that the mob wanders around in */
	private AxisAlignedBB wanderBox;
	
	/**
	 * Creates a new BonnieGoal, used for the Bonnie entity's AI
	 * 
	 * @param entityIn the entity this goal controls
	 */
	public BonnieGoal(MobEntity entityIn) {
		
		entity = entityIn;
		awarenessBox = new AxisAlignedBB(-AWARENESS_DISTANCE_XZ, -AWARENESS_DISTANCE_Y,
				-AWARENESS_DISTANCE_XZ, AWARENESS_DISTANCE_XZ, AWARENESS_DISTANCE_Y,
				AWARENESS_DISTANCE_XZ);
		wanderBox = new AxisAlignedBB(-WANDER_DISTANCE_XZ, -WANDER_DISTANCE_Y,
				-WANDER_DISTANCE_XZ, WANDER_DISTANCE_XZ, WANDER_DISTANCE_Y, WANDER_DISTANCE_XZ);
		justAttackedPlayer = false;
		
		//TODO set the flags that this behaviour will use
		// Options are JUMP, LOOK, MOVE, and TARGET
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
					flee(targetedPlayer);
					
				} else {
					// This entity is not at low health
					
					if (justAttackedPlayer) {
						// This entity just attacked the player
						
						if (targetedPlayer.getHealth() <=
								targetedPlayer.getMaxHealth() * LOW_PLAYER_HEALTH_PERCENTAGE) {
							// The player's health is low
							flee(targetedPlayer);
							
						} else {
							// The player's health is not low
							attack(targetedPlayer);
							
						}
						
					} else {
						// This entity did not just attack the player
						attack(targetedPlayer);
						
					}
					
				}
				
			} else {
				// No player is visible to this entity
				wanderTowardsPlayer(nearestPlayer);
				
			}
			
		} else {
			// No players are within the awareness box
			wander(wanderBox);
			
		}
		
		attackCooldown--;
		
		regenerateHealth();
		
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
	
	/**
	 * Makes this mob wander around the given area
	 * 
	 * @param area the area to choose from when selecting a new location to wander to
	 */
	private void wander(AxisAlignedBB area) {
		
		// Removes any other paths this mob was using
		attackPath = null;
		fleePath = null;
		
		if (wanderPath == null) {
			
			// There is a chance this entity will not choose a new path yet
			if (entity.getRNG().nextFloat() <= NEW_WANDER_PATH_CHANCE) {
				
				// Finds a new location to wander to
				double minX = entity.posX + area.minX;
				double maxX = entity.posX + area.maxX;
				double minY = entity.posY + area.minY;
				double maxY = entity.posY + area.maxY;
				double minZ = entity.posZ + area.minZ;
				double maxZ = entity.posZ + area.maxZ;
				double wanderTargetX = minX + entity.getRNG().nextDouble() * (maxX - minX);
				double wanderTargetY = minY + entity.getRNG().nextDouble() * (maxY - minY);
				double wanderTargetZ = minZ + entity.getRNG().nextDouble() * (maxZ - minZ);
				
				wanderPath = entity.getNavigator().func_225466_a(wanderTargetX, wanderTargetY,
						wanderTargetZ, 1);
				
			}
			
		} else {
			
			// Sets this mob to move along the wander path
			if (wanderPath != entity.getNavigator().getPath()) {
				entity.getNavigator().setPath(wanderPath, WANDER_SPEED);
			}
			
			// Removes the wander path if this mob reached its destination
			if (wanderPath.isFinished()) {
				wanderPath = null;
			}
			
		}
		
	}
	
	/**
	 * Makes this mob wander towards the given entity
	 * 
	 * @param player the entity to wander towards
	 */
	private void wanderTowardsPlayer(LivingEntity player) {
		
		int offsetX = WANDER_OFFSET;
		if (entity.posX > player.posX) {
			offsetX *= -1;
		}
		
		int offsetZ = WANDER_OFFSET;
		if (entity.posZ > player.posZ) {
			offsetZ *= -1;
		}
		
		wander(wanderBox.offset(offsetX, 0, offsetZ));
		
	}
	
	/**
	 * Makes this mob flee the given entity
	 * 
	 * @param player the entity to flee
	 */
	private void flee(LivingEntity player) {

		// Removes any other paths this mob was using
		attackPath = null;
		wanderPath = null;
		
		// Finds a path to flee along if it's not already fleeing
		if (fleePath == null) {
			
			// Finds a possible location to flee to
			Vec3d playerLocation = new Vec3d(player.posX, player.posY, player.posZ);
			fleeLocation = RandomPositionGenerator.findRandomTargetBlockAwayFrom((CreatureEntity)entity, 20, 8, playerLocation);
			
			// If the flee location exists and is farther from the player than this entity currently is
			if (fleeLocation != null && player.getDistanceSq(fleeLocation) > player.getDistanceSq(entity)) {
				
				// Finds a path to the chosen flee location
				fleePath = entity.getNavigator().func_225466_a(fleeLocation.x, fleeLocation.y,
						fleeLocation.z, 1);
				
			}
			
		// Given that there is a path to flee along, this mob flees
		} else {
			
			// Sets this mob to flee along the fleePath
			if (fleePath != entity.getNavigator().getPath()) {
				entity.getNavigator().setPath(fleePath, FLEE_SPEED);
			}
			
			// Removes the flee path if this mob reached its destination
			if (fleePath.isFinished()) {
				fleePath = null;
				fleeLocation = null;
				justAttackedPlayer = false;
			}
			
		}
		
	}
	
	/**
	 * Makes this mob move to and attack the given entity
	 * 
	 * @param player the entity to attack
	 */
	private void attack(LivingEntity player) {

		// Removes any other paths this mob was using
		fleePath = null;
		wanderPath = null;
		
		attackPath = entity.getNavigator().getPathToEntityLiving(player, 1);
		entity.getNavigator().setPath(attackPath, ATTACK_SPEED);
		attemptAttack(player);
		
	}
	
	/**
	 * Makes this mob attack the given entity if close enough
	 * Adapted from Minecraft's code in net.minecraft.entity.ai.goal.MeleeAttackGoal
	 * 
	 * @param player the entity to attack
	 */
	private void attemptAttack(LivingEntity player) {
		
		double reach = Math.pow(entity.getWidth() * 2, 2) + player.getWidth();
		double distanceToPlayer = entity.getDistanceSq(player);
		
		if (distanceToPlayer <= reach && attackCooldown <= 0) {
			attackCooldown = COOLDOWN_TIME;
			entity.swingArm(Hand.MAIN_HAND);
			entity.attackEntityAsMob(player);
			justAttackedPlayer = true;
		}
		
	}
	
	/**
	 * Makes this mob regenerate one heart every five seconds
	 * if this method is called once per tick
	 */
	private void regenerateHealth() {
		entity.setHealth(entity.getHealth() + 0.02f);
	}
	
}
















