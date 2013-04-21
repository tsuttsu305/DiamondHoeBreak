package net.tsuttsu305.DiamondHoeBreak;

import java.util.logging.Logger;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class DiamondHoeBreak extends JavaPlugin {
    public static DiamondHoeBreak plugin;
    Logger logger = Logger.getLogger("Minecraft");
    
    @Override
    public void onEnable(){
        
        //WorldGuardチェック
        if (getServer().getPluginManager().isPluginEnabled("WorldGuard")){
            logger.info("DiamondHoeBreak hooked to WorldGuard");
        }else{
            getServer().getPluginManager().disablePlugin(this);
        }

        //HawkEyeチェック
        if (getServer().getPluginManager().isPluginEnabled("HawkEye")){
            logger.info("DiamondHoeBreak hooked to Hawkeye");
            getServer().getPluginManager().registerEvents(new HoeEvent(this), this);
        }else{
            getServer().getPluginManager().disablePlugin(this);
        }

    }

    //WorldGuard使用時
    private WorldGuardPlugin getWorldGuard() {
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");
        // WorldGuard may not be loaded
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            getServer().getPluginManager().disablePlugin(this);
            return null; // Maybe you want throw an exception instead
        }
        return (WorldGuardPlugin) plugin;
    }
    
    public WorldGuardPlugin wg(){
        return getWorldGuard();

    }

}