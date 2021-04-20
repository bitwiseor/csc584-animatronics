package edu.ncsu.csc584.animatronics.config;

import java.io.File;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;

import edu.ncsu.csc584.animatronics.AnimatronicsMod;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class Config 
{
    private static final ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SERVER;
    
    private static final ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec CLIENT;
    
    static
    {
        SERVER = SERVER_BUILDER.build();
        CLIENT = CLIENT_BUILDER.build();
    }
    
    public static void loadConfig(ForgeConfigSpec config, String path)
    {
        AnimatronicsMod.logger.info("Loading config: " + path);
        final CommentedFileConfig file = CommentedFileConfig.builder(new File(path)).sync().autosave().writingMode(WritingMode.REPLACE).build();
        AnimatronicsMod.logger.info("Built config: " + path);
        file.load();
        AnimatronicsMod.logger.info("Loaded config: " + path);
        config.setConfig(file);
    }
}