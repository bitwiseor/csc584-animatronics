package edu.ncsu.csc584.animatronics.entity.ai.util;

import net.minecraft.entity.LivingEntity;

/**
 * Allows mobs to communicate information to each other
 * 
 * @author Brenden Lech
 * @author Bansi Chhatrala
 */
public interface Communicatable {
	
	/**
	 * Returns the entity this entity is attacking, or null if not currently attacking
	 * 
	 * @return the entity this entity is attacking, or null if not currently attacking
	 */
	public LivingEntity getEntityBeingAttacked();
	
}