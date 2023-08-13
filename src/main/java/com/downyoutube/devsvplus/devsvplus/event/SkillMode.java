package com.downyoutube.devsvplus.devsvplus.event;

import net.Indyuce.mmocore.api.event.PlayerKeyPressEvent;
import net.Indyuce.mmocore.api.player.PlayerData;
import net.Indyuce.mmocore.skill.cast.PlayerKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class SkillMode implements Listener {

    HashMap<Player, Integer> hash = new HashMap<>();

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onCastSkill(PlayerKeyPressEvent event) {
        if (event.getPressed() != PlayerKey.SWAP_HANDS) return;
        Player player = event.getPlayer();
        PlayerData data = PlayerData.get(player);
        if (!data.isCasting()) {
            if (data.getBoundSkills().size() == 0) return;
            if (player.getInventory().getHeldItemSlot() != 8) {
                ItemStack item1 = player.getInventory().getItemInMainHand();
                ItemStack item2 = player.getInventory().getItem(8);

                player.getInventory().setItemInMainHand(item2);
                player.getInventory().setItem(8, item1);

                hash.put(player, player.getInventory().getHeldItemSlot());

                player.getInventory().setHeldItemSlot(8);
            }
        } else {
            if (hash.containsKey(player)) {
                ItemStack item1 = player.getInventory().getItemInMainHand();
                ItemStack item2 = player.getInventory().getItem(hash.get(player));

                player.getInventory().setItemInMainHand(item2);
                player.getInventory().setItem(hash.get(player), item1);

                player.getInventory().setHeldItemSlot(hash.get(player));

                hash.remove(player);
            }
        }
    }
}
