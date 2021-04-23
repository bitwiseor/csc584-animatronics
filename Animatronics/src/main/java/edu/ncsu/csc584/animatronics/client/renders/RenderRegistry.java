package edu.ncsu.csc584.animatronics.client.renders;

import edu.ncsu.csc584.animatronics.entities.FreddyEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

/**
 * Registers renderers for the client (used in AnimatronicsMod.clientRegistries)
 * 
 * @author Brenden Lech
 * @author Bansi Chhatrala
 */
@OnlyIn(Dist.CLIENT)
public class RenderRegistry {
	
	/**
	 * Registers the renderers for entities
	 */
	public static void registryEntityRenderers() {
		
		// Registers the renderer for FreddyEntity
		RenderingRegistry.registerEntityRenderingHandler(FreddyEntity.class,
				new FreddyRender.RenderFactory());
		
	}
	
}