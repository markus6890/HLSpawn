package com.gmail.markushygedombrowski.commands;

import com.gmail.markushygedombrowski.HLSpawn;
import com.gmail.markushygedombrowski.combat.CombatList;

import com.gmail.markushygedombrowski.cooldown.SpawnCooldown;
import com.gmail.markushygedombrowski.warp.WarpManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class SpawnCommand implements CommandExecutor {
    private WarpManager warpManager;
    private HLSpawn plugin;
    private CombatList combatList;
    private HashMap<Player, String> spawnwait = new HashMap<>();
    private HashMap<String,Integer> spawnprice = new HashMap<>();

    public SpawnCommand(WarpManager warpManager, HLSpawn plugin, CombatList combatList) {
        this.warpManager = warpManager;
        this.plugin = plugin;
        this.combatList = combatList;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        String spawn = "c";


        if (!(sender instanceof Player)) {
            System.out.println("Kan kun bruges af Players");
            return true;
        }
        Player p = (Player) sender;
        if (!p.hasPermission("fange.spawn")) {
            p.sendMessage("§cDet har du ikke permission til!");
            return true;

        }
        if(SpawnCooldown.isCooling(p, "spawn")) {
            p.sendMessage("§8§l[§6§lHublolland§8§l] §cDu kan bruge /spawn om §a" + SpawnCooldown.getRemaining(p, "spawn") + " §cminuter!");
            return true;
        }
        if (combatList.isPlayerInCombat(p)) {
            p.sendMessage(" §8§l[§6§lHublolland§8§l] §cDu kan ikke komme til spawn mens du er i combat!");
            return true;
        }

        if (p.hasPermission("spawn.c")) {
            spawn = "c";
        } else if (p.hasPermission("spawn.b")) {
            spawn = "b";
        } else if (p.hasPermission("spawn.a")) {
            spawn = "a";
        }
        spawnprice.put("c",1000);
        spawnprice.put("b",5000);
        spawnprice.put("a",10000);

        if (p.hasMetadata("spawn")) {
            spawnwait.put(p, spawn);
            if (plugin.econ.getBalance(p) < spawnprice.get(spawnwait.get(p))) {
                spawnwait.remove(p);
                p.sendMessage("§cDu har ikke råd til at komme til spawn!");
                return true;
            }
            for (int i = 0; i < 10; i++) {
                int finalI = i;
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        if (spawnwait.containsKey(p)) {
                            if (p.hasMetadata("spawn")) {
                                p.sendMessage("§8§l[§6§lHublolland§8§l] §bDu er ved at blive teleporteret til spawn om §a" + (10 - finalI) + " §bsekunder!");
                                if(combatList.isPlayerInCombat(p)) {
                                    p.sendMessage("§cDu kan ikke komme til spawn mens du er i combat!");
                                    p.removeMetadata("spawn", plugin);
                                    spawnwait.remove(p);
                                    return;
                                }
                                if (finalI == 9) {
                                    p.teleport(warpManager.getWarpInfo(spawnwait.get(p)).getLocation());
                                    plugin.econ.withdrawPlayer(p, spawnprice.get(spawnwait.get(p)));
                                    p.sendMessage("§8§l[§6§lHublolland§8§l] §bDu er nu kommet til spawn!");
                                    p.removeMetadata("spawn", plugin);
                                    spawnwait.remove(p);
                                    SpawnCooldown.add(p, "spawn", 600, System.currentTimeMillis());
                                }
                            }
                        }
                    }
                }, i * 20);
            }


            return true;
        }
        p.sendMessage("§8§l[§6§lHublolland§8§l] §bDet koster §a" + spawnprice.get(spawn) + "$ §bfor at komme til spawn. Skriv §6/spawn §bigen for at komme til spawn!");
        p.sendMessage("§8§l[§6§lHublolland§8§l] §bBevæg dig ikke! Ellers bliver det cancelled!");
        p.setMetadata("spawn", new org.bukkit.metadata.FixedMetadataValue(plugin, true));

        return true;
    }


}
