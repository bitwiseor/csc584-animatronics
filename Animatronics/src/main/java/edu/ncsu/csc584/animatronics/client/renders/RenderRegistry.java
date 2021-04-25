package edu.ncsu.csc584.animatronics.client.renders;

import edu.ncsu.csc584.animatronics.entity.BonnieEntity;
import edu.ncsu.csc584.animatronics.entity.ChicaEntity;
import edu.ncsu.csc584.animatronics.entity.FoxyEntity;
import edu.ncsu.csc584.animatronics.entity.FreddyEntity;
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
		
		// Registers the renderer for Freddy
		RenderingRegistry.registerEntityRenderingHandler(FreddyEntity.class,
				new FreddyRender.RenderFactory());
		
		// Registers the renderer for Bonnie
		RenderingRegistry.registerEntityRenderingHandler(BonnieEntity.class,
				new BonnieRender.RenderFactory());
		
		// Registers the renderer for Chica
        RenderingRegistry.registerEntityRenderingHandler(ChicaEntity.class,
                new ChicaRender.RenderFactory());
        
     // Registers the renderer for Foxy
        RenderingRegistry.registerEntityRenderingHandler(FoxyEntity.class,
                new FoxyRender.RenderFactory());
		
	}
	
}
