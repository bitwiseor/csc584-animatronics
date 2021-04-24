package edu.ncsu.csc584.animatronics.entity.ai.goal;

import java.util.EnumSet;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

/**
 * Runs the decision tree implementation used for the Bonnie entity
 * 
 * @author Brenden Lech
 * @author Bansi Chhatrala
 */
public class BonnieGoal extends Goal {
	
	/** How long this mob looks for the target after last seeing it, in ticks */
	private final int SEARCH_TIME = 400;
	/** The speed this mob moves at when fleeing the player */
	private final double FLEE_SPEED = 0.6;
	/** The speed this mob moves at when moving to attack the player */
	private final double ATTACK_SPEED = 0.6;
	/** The cooldown time between this mob's attacks in game ticks */
	private final int COOLDOWN_TIME = 20;
	
	/** The entity that is running this goal */
	private final MobEntity entity;
	/** How long since this mob has seen the player */
	private int recentlySeenTarget;
	/** The path this mob is fleeing along to get away from the player */
	private Path fleePath;
	/** The location this mob is fleeing to to get away from the player */
	private Vec3d fleeLocation;
	
	/** The path this mob is moving along to get to the player to attack them */
	private Path attackPath;
	/** The current cooldown between this mob's attacks in game ticks */
	private int attackCooldown;
	
	public BonnieGoal(MobEntity entityIn) {
		
		entity = entityIn;
		recentlySeenTarget = 0;
		
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
		
		// The player this mob is targeting
		LivingEntity player = entity.getAttackTarget();
		
		// Sets the timer for whether the target has been seen recently
		if (player != null) {
			// We can see the player now
			
			recentlySeenTarget = SEARCH_TIME;
			
			if (entity.getHealth() <= entity.getMaxHealth() * 0.3) {
				// We are at low health; flee the player
				flee(player);
			} else {
				// We are not at low health; attack the player
				attack(player);
			}
			
		} else if (recentlySeenTarget > 0) {
			recentlySeenTarget--;
		}
		
		// Decrements the attack cooldown timer
		attackCooldown--;
		
	}
	
	/**
	 * Makes this mob flee the given entity
	 * 
	 * @param player the entity to flee
	 */
	private void flee(LivingEntity player) {
		
		// Removes any attack path the mob was using
		attackPath = null;
		
		// Finds a path to flee along if it's not already fleeing
		if (fleePath == null) {
			
			// Finds a possible location to flee to
			Vec3d playerLocation = new Vec3d(player.posX, player.posY, player.posZ);
			fleeLocation = RandomPositionGenerator.findRandomTargetBlockAwayFrom((CreatureEntity)entity, 20, 8, playerLocation);
			
			// If the flee location exists and is farther from the player than this entity currently is
			if (fleeLocation != null && player.getDistanceSq(fleeLocation) > player.getDistanceSq(entity)) {
				
				// Finds a path to the chosen flee location
				fleePath = entity.getNavigator().func_225466_a(fleeLocation.x, fleeLocation.y, fleeLocation.z, 1);
				
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
			}
			
		}
		
	}
	
	/**
	 * Makes this mob move to and attack the given entity
	 * 
	 * @param player the entity to attack
	 */
	private void attack(LivingEntity player) {
		
		// Removes any flee path the mob was using
		fleePath = null;
		
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
		}
		
	}
	
}
















