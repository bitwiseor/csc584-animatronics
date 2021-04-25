package edu.ncsu.csc584.animatronics.client.renders;

import edu.ncsu.csc584.animatronics.AnimatronicsModRegistries;
import edu.ncsu.csc584.animatronics.client.models.FoxyModel;
import edu.ncsu.csc584.animatronics.entity.FoxyEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.IRenderFactory;

@OnlyIn(Dist.CLIENT)
public class FoxyRender extends MobRenderer<FoxyEntity, FoxyModel> {
	
	/**
	 * Creates a new FoxyRender
	 * 
	 * @param manager passed to the superconstructor
	 */
	public FoxyRender(EntityRendererManager manager) {
		// The third argument is the shadow size
		super(manager, new FoxyModel(), 0f);
	}
	
	/**
	 * Returns the location of this entity's texture
	 * 
	 * @param entity the entity used
	 */
	@Override
	protected ResourceLocation getEntityTexture(FoxyEntity entity) {
		return AnimatronicsModRegistries.getResourceLocation("textures/entity/foxy.png");
	}
	
	/**
	 * Provides EntityRenderers
	 * 
	 * @author Brenden Lech
	 * @author Bansi Chhatrala
	 */
	public static class RenderFactory implements IRenderFactory<FoxyEntity> {
		
		@Override
		public EntityRenderer<? super FoxyEntity> createRenderFor(EntityRendererManager manager) {
			return new FoxyRender(manager);
		}
		
	}
	
}
