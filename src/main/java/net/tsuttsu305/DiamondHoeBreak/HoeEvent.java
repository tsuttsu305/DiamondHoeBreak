package net.tsuttsu305.DiamondHoeBreak;

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
        if (event.isCancelled()) return;
        
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
                            //エリアフラグ判定
                            if (dbrk.wg().canBuild(player, event.getClickedBlock())){
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

    /**
     * 破壊用メソッド
     * クリックした中心のブロックを渡す。 WorldGuard権限判定用にPlayerも
     * @param center 中心
     * @param player Player
     */
    public void blockBreak(Block center, Player player){
        for (int x = -3;x<=3;x++){
            for (int z = -3;z<=3;z++){
                switch(center.getRelative(x, 0, z).getType()){
                    case CROPS:
                    case POTATO:
                    case CARROT:
                    case SUGAR_CANE_BLOCK:
                            //エリアフラグ判定
                            if (dbrk.wg().canBuild(player, center.getRelative(x, 0, z))){
                                //同一作物だけに発動
                                //HawkEye用にBlockStateを保存
                                HawkINSERT h = new HawkINSERT(dbrk, new BlockEntry(player, DataType.BLOCK_BREAK, center.getRelative(x, 0, z)));
                                h.start();
                                center.getRelative(x, 0, z).breakNaturally();
                                short du = player.getItemInHand().getDurability();
                                player.getItemInHand().setDurability((short) (du+1));
                            }else{

                            }
                        break;
                        
                    default:
                        break;

                }
            }
        }
    }

}

class HawkINSERT extends Thread{
    DiamondHoeBreak plugin;
    BlockEntry entry;
    
    public HawkINSERT(DiamondHoeBreak plugin, BlockEntry entry) {
        this.plugin = plugin;
        this.entry = entry;
    }
    @Override
    public void run() {
        HawkEyeAPI.addEntry(plugin, entry);
    }
}