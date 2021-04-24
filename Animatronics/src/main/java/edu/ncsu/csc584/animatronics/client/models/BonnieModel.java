package edu.ncsu.csc584.animatronics.client.models;

import edu.ncsu.csc584.animatronics.entity.BonnieEntity;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


@OnlyIn(Dist.CLIENT)
public class BonnieModel extends PlayerModel<BonnieEntity> {
	
	public BonnieModel() {
		super(1.0f, false);
	}
	
}
