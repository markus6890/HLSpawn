package com.gmail.markushygedombrowski.listener;

import com.gmail.markushygedombrowski.HLSpawn;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import  org.bukkit.event.Listener;
public class MoveListener implements Listener{

    private HLSpawn plugin;

    public MoveListener(HLSpawn plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        if(p.hasMetadata("spawn")) {
            p.sendMessage("§8§l[§6§lHublolland§8§l] §cSpawn er cancelled fordi du bevæge dig!");
            p.removeMetadata("spawn", plugin);
        }
    }
}
