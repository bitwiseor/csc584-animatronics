package edu.ncsu.csc584.animatronics.client.models;

import edu.ncsu.csc584.animatronics.entities.FreddyEntity;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


@OnlyIn(Dist.CLIENT)
public class FreddyModel extends PlayerModel<FreddyEntity> {

	public FreddyModel() {
		//TODO change this number probably
		super(1.0f, false);
	}
	
	
	
}
