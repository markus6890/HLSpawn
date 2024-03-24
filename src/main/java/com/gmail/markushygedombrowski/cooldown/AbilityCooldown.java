package com.gmail.markushygedombrowski.cooldown;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class AbilityCooldown {
    public String ability = "";
    public Player player;
    public long seconds;
    public long systime;

    public AbilityCooldown(Player player, long seconds, long systime) {
        this.player = player;
        this.seconds = seconds;
        this.systime = systime;
    }
    public AbilityCooldown(Player player) {
        this.player = player;
    }

    public HashMap<String, AbilityCooldown> cooldownMap = new HashMap<String, AbilityCooldown>();
    public String getAbility() {
        return ability;
    }


}
