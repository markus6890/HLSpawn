package com.gmail.markushygedombrowski;

import com.gmail.markushygedombrowski.combat.CombatList;
import com.gmail.markushygedombrowski.commands.SpawnCommand;
import com.gmail.markushygedombrowski.cooldown.SpawnCooldown;
import com.gmail.markushygedombrowski.listener.MoveListener;
import com.gmail.markushygedombrowski.warp.WarpManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class HLSpawn extends JavaPlugin {
    public Economy econ = null;
    @Override
    public void onEnable() {
        HLWarp warp = HLWarp.getInstance();
        WarpManager warpManager = warp.getWarpManager();

        CombatList combatList = CombatMain.getInstance().getCombatList();
        getCommand("spawn").setExecutor(new SpawnCommand(warpManager, this, combatList));
        System.out.println("HLSpawn enabled");
        MoveListener moveListener = new MoveListener(this);
        Bukkit.getPluginManager().registerEvents(moveListener, this);
        if (!setupEconomy()) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {

            @Override
            public void run() {
                SpawnCooldown.handleCooldowns();
            }
        }, 1L, 1L);
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
    @Override
    public void onDisable() {
        System.out.println("HLSpawn disabled");
    }
}
