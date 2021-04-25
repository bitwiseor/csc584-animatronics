package edu.ncsu.csc584.animatronics.entity;

import edu.ncsu.csc584.animatronics.entity.ai.goal.FoxyGoal;
import edu.ncsu.csc584.animatronics.lists.EntityList;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

/**
 * Creates the Foxy entity
 * 
 * @author Brenden Lech
 * @author Bansi Chhatrala
 */
public class FoxyEntity extends MonsterEntity {
    
    /**
     * Creates a new FoxyEntity
     * 
     * @param type currently unused
     * @param worldIn a reference passed to the superconstructor
     */
    public FoxyEntity(EntityType<? extends MonsterEntity> type, World worldIn) {
        super((EntityType<? extends MonsterEntity>) EntityList.foxy, worldIn);
    }
    
    @Override
    protected void registerGoals() {
        
        // Adds the goals it uses as its AI
        goalSelector.addGoal(0, new SwimGoal(this));
        goalSelector.addGoal(1, new FoxyGoal(this));
        
        // Targets any players within its follow range
        targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, false));
        
    }
    
    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(30.0d);
        //TODO change
        getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0d);
        getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.6d);
        getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0d);
    }
}
