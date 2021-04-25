package edu.ncsu.csc584.animatronics.entity.ai.util;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

/**
 * Provides implementations for behaviors common across different mobs
 * 
 * @author Brenden Lech
 * @author Bansi Chhatrala
 */
public class Action {
	
	/** The entity running these behaviors */
	private MobEntity entity;
	
	/** The path this mob is fleeing along to get away from the player */
	private Path fleePath;
	/** The path this mob is moving along to get to the player to attack them */
	private Path attackPath;
	/** The path this mob is wandering along */
	private Path wanderPath;
	
	/** The current cooldown between this mob's attacks in game ticks */
	private int attackCooldown;
	
	/**
	 * Creates a new Behavior object, which stores parameters
	 * used in behaviors and runs behaviors for entities
	 * 
	 * @param entity the entity running these behaviors
	 */
	public Action(MobEntity entity) {
		
		this.entity = entity;
		
		fleePath = null;
		attackPath = null;
		wanderPath = null;
		attackCooldown = 0;
		
	}
	
	/**
	 * Makes this mob flee the given entity
	 * 
	 * @param target the entity to flee
	 * @param fleeSpeed the speed this mob moves at while fleeing
	 * @return whether this mob successfully fleed to a new chosen target location
	 */
	public boolean flee(LivingEntity target, double fleeSpeed) {

		// Removes any other paths this mob was using
		attackPath = null;
		wanderPath = null;
		
		// Finds a path to flee along if it's not already fleeing
		if (fleePath == null) {
			
			// Finds a possible location to flee to
			Vec3d playerLocation = new Vec3d(target.posX, target.posY, target.posZ);
			Vec3d fleeLocation = RandomPositionGenerator.findRandomTargetBlockAwayFrom(
					(CreatureEntity)entity, 20, 8, playerLocation);
			
			// If the flee location exists and is farther from the player than this entity currently is
			if (fleeLocation != null && target.getDistanceSq(fleeLocation) > target.getDistanceSq(entity)) {
				
				// Finds a path to the chosen flee location
				fleePath = entity.getNavigator().func_225466_a(fleeLocation.x, fleeLocation.y,
						fleeLocation.z, 1);
				
			}
			
		// Given that there is a path to flee along, this mob flees
		} else {
			
			// Sets this mob to flee along the fleePath
			if (fleePath != entity.getNavigator().getPath()) {
				entity.getNavigator().setPath(fleePath, fleeSpeed);
			}
			
			// Removes the flee path if this mob reached its destination
			if (fleePath.isFinished()) {
				fleePath = null;
				return true;
			}
			
		}
		
		return false;
		
	}
	
	/**
	 * Makes this mob wander around the given area
	 * 
	 * @param area the area to choose from when selecting a new location to wander to
	 * @param wanderSpeed the speed the mob moves at when wandering
	 * @param newWanderPathChance the chance that a new location to wander to will be
	 * selected, in percent chance per tick
	 */
	public void wander(AxisAlignedBB area, double wanderSpeed, float newWanderPathChance) {
		
		// Removes any other paths this mob was using
		attackPath = null;
		fleePath = null;
		
		if (wanderPath == null) {
			
			// There is a chance this entity will not choose a new path yet
			if (entity.getRNG().nextFloat() <= newWanderPathChance) {
				
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
				entity.getNavigator().setPath(wanderPath, wanderSpeed);
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
	 * @param area the area to choose from when selecting a new location to wander to
	 * @param wanderSpeed the speed the mob moves at when wandering
	 * @param newWanderPathChance the chance that a new location to wander to will be
	 * selected, in percent chance per tick
	 * @param target the entity to wander towards
	 * @param wanderOffset how much to offset the wander area towards the target
	 */
	public void wanderTowardsEntity(AxisAlignedBB area, double wanderSpeed,
			float newWanderPathChance, LivingEntity target, int wanderOffset) {
		
		int offsetX = wanderOffset;
		if (entity.posX > target.posX) {
			offsetX *= -1;
		}
		
		int offsetZ = wanderOffset;
		if (entity.posZ > target.posZ) {
			offsetZ *= -1;
		}
		
		wander(area.offset(offsetX, 0, offsetZ), wanderSpeed, newWanderPathChance);
		
	}
	
	/**
	 * Decrements the attack cooldown
	 */
	public void decrementAttackCooldown() {
		if (attackCooldown > 0) {
			attackCooldown--;
		}
	}
	
	/**
	 * Makes this mob move to and attack the given entity
	 * 
	 * @param target the entity to attack
	 * @param attackMovementSpeed how fast this mob should move when attacking
	 * @param cooldownTime how long this mob's cooldown period between attacks is
	 * @return whether the target entity was attacked
	 */
	public boolean attack(LivingEntity target, double attackMovementSpeed, int cooldownTime) {

		// Removes any other paths this mob was using
		fleePath = null;
		wanderPath = null;
		
		attackPath = entity.getNavigator().getPathToEntityLiving(target, 1);
		entity.getNavigator().setPath(attackPath, attackMovementSpeed);
		entity.getLookController().func_220679_a(target.posX,
				target.posY + target.getEyeHeight(), target.posZ);
		
		if (attemptAttack(target, attackCooldown)) {
			attackCooldown = cooldownTime;
			return true;
		}
		
		return false;
		
	}
	
	/**
	 * Makes this mob attack the given entity if close enough
	 * Adapted from Minecraft's code in net.minecraft.entity.ai.goal.MeleeAttackGoal
	 * 
	 * @param entity the entity running this behavior
	 * @param target the entity to attack
	 * @param cooldown the entity's current attack cooldown
	 * @return whether the other entity was attacked
	 */
	public boolean attemptAttack(LivingEntity target, int cooldown) {
		
		double reach = Math.pow(entity.getWidth() * 2, 2) + target.getWidth();
		double distanceToPlayer = entity.getDistanceSq(target);
		
		if (distanceToPlayer <= reach && cooldown <= 0) {
			entity.swingArm(Hand.MAIN_HAND);
			entity.attackEntityAsMob(target);
			return true;
		}
		
		return false;
		
	}
	
	/**
	 * Makes this mob regenerate one heart every five seconds
	 * if this method is called once per tick
	 * 
	 * @param entity the entity running this behavior
	 */
	public void regenerateHealth() {
		entity.setHealth(entity.getHealth() + 0.02f);
	}
	
}
