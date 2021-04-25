package edu.ncsu.csc584.animatronics.client.renders;

import edu.ncsu.csc584.animatronics.AnimatronicsModRegistries;
import edu.ncsu.csc584.animatronics.client.models.FreddyModel;
import edu.ncsu.csc584.animatronics.entity.FreddyEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.IRenderFactory;

@OnlyIn(Dist.CLIENT)
public class FreddyRender extends MobRenderer<FreddyEntity, FreddyModel> {
	
	/**
	 * Creates a new FreddyRender
	 * 
	 * @param manager passed to the superconstructor
	 */
	public FreddyRender(EntityRendererManager manager) {
		// The third argument is the shadow size
		super(manager, new FreddyModel(), 0f);
	}
	
	/**
	 * Returns the location of this entity's texture
	 * 
	 * @param entity the entity used
	 */
	@Override
	protected ResourceLocation getEntityTexture(FreddyEntity entity) {
		return AnimatronicsModRegistries.getResourceLocation("textures/entity/freddy.png");
	}
	
	/**
	 * Provides EntityRenderers
	 * 
	 * @author Brenden Lech
	 * @author Bansi Chhatrala
	 */
	public static class RenderFactory implements IRenderFactory<FreddyEntity> {
		
		@Override
		public EntityRenderer<? super FreddyEntity> createRenderFor(EntityRendererManager manager) {
			return new FreddyRender(manager);
		}
		
	}
	
}
