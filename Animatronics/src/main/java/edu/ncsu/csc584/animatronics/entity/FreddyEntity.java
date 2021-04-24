package edu.ncsu.csc584.animatronics.entity;

import edu.ncsu.csc584.animatronics.lists.EntityList;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

/**
 * Creates the Freddy entity
 * 
 * @author Brenden Lech
 * @author Bansi Chhatrala
 */
public class FreddyEntity extends MonsterEntity {
	
	/**
	 * Creates a new FreddyEntity
	 * 
	 * @param type currently unused
	 * @param worldIn a reference passed to the superconstructor
	 */
    public FreddyEntity(EntityType<? extends MonsterEntity> type, World worldIn) {
        super((EntityType<? extends MonsterEntity>) EntityList.freddy, worldIn);
    }
    
    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new SwimGoal(this));
        goalSelector.addGoal(1, new MeleeAttackGoal(this, 0.9d, false));
        goalSelector.addGoal(2, new RandomWalkingGoal(this, 0.6d));
        goalSelector.addGoal(3, new LookRandomlyGoal(this));
        
        targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
    }
    
    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0d);
        getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.6d);
        getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0d);
    }
}
