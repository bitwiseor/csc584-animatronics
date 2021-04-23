package edu.ncsu.csc584.animatronics;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.ncsu.csc584.animatronics.client.renders.RenderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * The main class for the Animatronics Mod
 * 
 * @author Brenden Lech
 * @author Bansi Chhatrala
 */
@Mod("animatronics")
public class AnimatronicsMod {
	
	/** Used to reference this main mod class outside of this class */
	public static AnimatronicsMod instance;
	/** The id name of this mod */
	public static final String MOD_ID = "animatronics";
	/** Used to print debug information to the console */
	public static Logger logger = LogManager.getLogger(MOD_ID);
	
	/**
	 * Begins the initialization code for the mod
	 */
	public AnimatronicsMod() {
		
		// Required forge registration code so forge runs the setup methods
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientRegistries);
		MinecraftForge.EVENT_BUS.register(this);
		
		instance = this;
		
	}
	
	private void setup(FMLCommonSetupEvent event) {
		logger.info("setup method run successfully");
	}
	
	private void clientRegistries(FMLClientSetupEvent event) {
		
		// Registers renderers used by this mod
		RenderRegistry.registryEntityRenderers();
		
		logger.info("clientRegistries method run successfully");
		
	}
	
}
