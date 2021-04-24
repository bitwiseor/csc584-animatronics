package edu.ncsu.csc584.animatronics.client.renders;

import edu.ncsu.csc584.animatronics.AnimatronicsModRegistries;
import edu.ncsu.csc584.animatronics.client.models.BonnieModel;
import edu.ncsu.csc584.animatronics.entity.BonnieEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.IRenderFactory;

@OnlyIn(Dist.CLIENT)
public class BonnieRender extends LivingRenderer<BonnieEntity, BonnieModel> {
	
	/**
	 * Creates a new BonnieRender
	 * 
	 * @param manager passed to the superconstructor
	 */
	public BonnieRender(EntityRendererManager manager) {
		// The third argument is the shadow size
		super(manager, new BonnieModel(), 0f);
	}
	
	/**
	 * Returns the location of this entity's texture
	 * 
	 * @param entity the entity used
	 */
	@Override
	protected ResourceLocation getEntityTexture(BonnieEntity entity) {
		return AnimatronicsModRegistries.getResourceLocation("textures/entity/bonnie.png");
	}
	
	/**
	 * Provides EntityRenderers
	 * 
	 * @author Brenden Lech
	 * @author Bansi Chhatrala
	 */
	public static class RenderFactory implements IRenderFactory<BonnieEntity> {
		
		@Override
		public EntityRenderer<? super BonnieEntity> createRenderFor(EntityRendererManager manager) {
			return new BonnieRender(manager);
		}
		
	}
	
}
