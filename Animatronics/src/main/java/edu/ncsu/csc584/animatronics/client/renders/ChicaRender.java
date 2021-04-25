package edu.ncsu.csc584.animatronics.client.renders;

import edu.ncsu.csc584.animatronics.AnimatronicsModRegistries;
import edu.ncsu.csc584.animatronics.client.models.ChicaModel;
import edu.ncsu.csc584.animatronics.entity.ChicaEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.IRenderFactory;

@OnlyIn(Dist.CLIENT)
public class ChicaRender extends MobRenderer<ChicaEntity, ChicaModel> {
	
	/**
	 * Creates a new ChicaRender
	 * 
	 * @param manager passed to the superconstructor
	 */
	public ChicaRender(EntityRendererManager manager) {
		// The third argument is the shadow size
		super(manager, new ChicaModel(), 0f);
	}
	
	/**
	 * Returns the location of this entity's texture
	 * 
	 * @param entity the entity used
	 */
	@Override
	protected ResourceLocation getEntityTexture(ChicaEntity entity) {
		return AnimatronicsModRegistries.getResourceLocation("textures/entity/chica.png");
	}
	
	/**
	 * Provides EntityRenderers
	 * 
	 * @author Brenden Lech
	 * @author Bansi Chhatrala
	 */
	public static class RenderFactory implements IRenderFactory<ChicaEntity> {
		
		@Override
		public EntityRenderer<? super ChicaEntity> createRenderFor(EntityRendererManager manager) {
			return new ChicaRender(manager);
		}
		
	}
	
}
