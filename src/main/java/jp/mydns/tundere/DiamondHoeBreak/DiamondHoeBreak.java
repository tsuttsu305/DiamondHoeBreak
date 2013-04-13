package jp.mydns.tundere.DiamondHoeBreak;

import java.util.logging.Logger;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class DiamondHoeBreak extends JavaPlugin {
	public static DiamondHoeBreak plugin;
	Logger logger = Logger.getLogger("Minecraft");
	public boolean wgs = false;
	public boolean hawkeyeFlag = false;

	public void onEnable(){
		getServer().getPluginManager().registerEvents(new HoeEvent(this), this);

		//WorldGuardチェック
		if (getServer().getPluginManager().isPluginEnabled("WorldGuard")){
			//WorldGuard使用フラグ
			wgs = true;
			logger.info("DiamondHoeBreak hooked to WorldGuard");
		}else{
			wgs = false;
		}

		//HawkEyeチェック
		if (getServer().getPluginManager().isPluginEnabled("HawkEye")){
			//HawkEye Flag
			hawkeyeFlag = true;
			logger.info("DiamondHoeBreak hooked to Hawkeye");
		}else{
			hawkeyeFlag = false;
		}
		if(hawkeyeFlag){
			getServer().getPluginManager().registerEvents(new HoeEvent(this), this);
		}else{
			getServer().getPluginManager().registerEvents(new HoeEventNoneHawkEye(this), this);
		}
	}

	//WorldGuard使用時
	private WorldGuardPlugin getWorldGuard() {
		Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");
		// WorldGuard may not be loaded
		if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
			return null; // Maybe you want throw an exception instead
		}
		return (WorldGuardPlugin) plugin;
	}
	public WorldGuardPlugin wg(){
		return getWorldGuard();

	}

}
