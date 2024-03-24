package com.gmail.markushygedombrowski.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SuicideCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player)) {
            System.out.println("Kan kun bruges af Players");
            return true;
        }
        Player p = (Player) commandSender;
        if(p.hasPermission("vagt")) {
            p.sendMessage("§cDu kan ikke begå selvmord som vagt!");
            return true;
        }
        p.sendMessage("§cDu begik selvmord!");
        p.setHealth(0);
        return true;
    }
}
