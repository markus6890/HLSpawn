package com.gmail.markushygedombrowski.cooldown;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class SpawnCooldown {


    private final UtilTime utilTime;
    public static HashMap<Player, AbilityCooldown> cooldownSpawn = new HashMap<>();

    public SpawnCooldown(UtilTime utilTime) {
        this.utilTime = utilTime;
    }


    public static void add(Player player, String ability, long seconds, long systime) {
        if (!cooldownSpawn.containsKey(player)) cooldownSpawn.put(player, new AbilityCooldown(player));
        if (isCooling(player, ability)) return;
        cooldownSpawn.get(player).cooldownMap.put(ability, new AbilityCooldown(player, seconds * 1000, System.currentTimeMillis()));
    }

    public static boolean isCooling(Player player, String ability) {
        if (!cooldownSpawn.containsKey(player)) return false;
        return cooldownSpawn.get(player).cooldownMap.containsKey(ability);
    }

    public static double getRemaining(Player player, String ability) {
        if (!cooldownSpawn.containsKey(player)) return 0.0;
        if (!cooldownSpawn.get(player).cooldownMap.containsKey(ability)) return 0.0;
        return UtilTime.convert((cooldownSpawn.get(player).cooldownMap.get(ability).seconds + cooldownSpawn.get(player).cooldownMap.get(ability).systime) - System.currentTimeMillis(), TimeUnit.BEST, 1);
    }



    public static void removeCooldown(Player player, String ability) {
        if (!cooldownSpawn.containsKey(player)) {
            return;
        }
        if (!cooldownSpawn.get(player).cooldownMap.containsKey(ability)) {
            return;
        }
        cooldownSpawn.get(player).cooldownMap.remove(ability);

    }


    public static void handleCooldowns() {
        if (cooldownSpawn.isEmpty()) {
            return;
        }
        cooldownSpawn.forEach((player, abilityCooldown) -> {
            abilityCooldown.cooldownMap.forEach((ability, cooldown) -> {
                if (getRemaining(player, ability) <= 0.0) {
                    removeCooldown(player, ability);
                }

            });
        });

    }
}
