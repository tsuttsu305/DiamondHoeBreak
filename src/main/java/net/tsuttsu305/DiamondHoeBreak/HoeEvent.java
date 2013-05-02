package net.tsuttsu305.DiamondHoeBreak;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

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
        List<HawkEyeData> data = new ArrayList<>();
        
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
                                data.add(new HawkEyeData(dbrk, new BlockEntry(player, DataType.BLOCK_BREAK, center.getRelative(x, 0, z))));
                                
                                
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
        
        HawkINSERT in = new HawkINSERT(data);
        in.start();
    }

}

class HawkEyeData{

    private JavaPlugin plugin;
    private BlockEntry entry;
    public HawkEyeData(JavaPlugin plugin, BlockEntry entry) {
        this.plugin = plugin;
        this.entry = entry;
    }
    
    public JavaPlugin getPlugin() {
        return plugin;
    }
    public BlockEntry getEntry() {
        return entry;
    }
}

class HawkINSERT extends Thread{
    List<HawkEyeData> data;
    
    
    public HawkINSERT(List<HawkEyeData> data) {
        this.data = data;
    }
    @Override
    public void run() {
        for (HawkEyeData d : data) {
            HawkEyeAPI.addEntry(d.getPlugin(), d.getEntry());
        }

    }
}