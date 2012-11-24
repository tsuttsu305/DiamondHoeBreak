package jp.tsuttsu305.DiamondHoeBreak;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import uk.co.oliwali.HawkEye.DataType;
import uk.co.oliwali.HawkEye.entry.BlockEntry;
import uk.co.oliwali.HawkEye.util.HawkEyeAPI;

public class HoeEvent implements Listener{
	private DiamondHoeBreak dbrk = null;

	public HoeEvent(DiamondHoeBreak dbrk) {
		this.dbrk = dbrk;
	}

	@EventHandler
	public void onPlayerUseHoe(PlayerInteractEvent event){
		//クリック判定　左クリック
		if (event.getAction() == Action.LEFT_CLICK_BLOCK){
			Player player = event.getPlayer();
			//ダイヤくわ判定
			if(player.getItemInHand().getType() == Material.DIAMOND_HOE){
				//しゃがんでるか判定
				if (player.isSneaking()){
					//ブロック判定
					switch(event.getClickedBlock().getType()){
						case CROPS:
						case POTATO:
						case CARROT:
						case SUGAR_CANE_BLOCK:
							//WorldGuard使用Flag判定
							if(dbrk.wgs == true){
								//エリアフラグ判定
								if (dbrk.wg().canBuild(player, event.getClickedBlock())){
							blockBreak(event.getClickedBlock(), player);
								}
							}else{
								blockBreak(event.getClickedBlock(), player);
							}
							break;
						default:
							break;
					}
				}
			}
		}
	}

	//破壊用メソッド
	//クリックした中心のブロックを渡す。 WorldGuard権限判定用にPlayerも
	public void blockBreak(Block center, Player player){
		//サーチ
		for (int x = -3;x<=3;x++){
			for (int z = -3;z<=3;z++){
				switch(center.getRelative(x, 0, z).getType()){
					case CROPS:
					case POTATO:
					case CARROT:
					case SUGAR_CANE_BLOCK:
						//WorldGuard使用Flag判定
						if(dbrk.wgs == true){
							//エリアフラグ判定
							if (dbrk.wg().canBuild(player, center.getRelative(x, 0, z))){
								//HawkEye用にBlockStateを保存
								HawkEyeAPI.addEntry(dbrk, new BlockEntry(player, DataType.BLOCK_BREAK, center.getRelative(x, 0, z)));
								center.getRelative(x, 0, z).breakNaturally();
								short du = player.getItemInHand().getDurability();
								player.getItemInHand().setDurability((short) (du+1));
							}else{

							}
						}else{
							//HawkEye用にBlockStateを保存
							HawkEyeAPI.addEntry(dbrk, new BlockEntry(player, DataType.BLOCK_BREAK, center.getRelative(x, 0, z)));
							center.getRelative(x, 0, z).breakNaturally();
							short du = player.getItemInHand().getDurability();
							player.getItemInHand().setDurability((short) (du+1));
						}
						break;
					default:
						break;

				}
			}
		}
	}

}
