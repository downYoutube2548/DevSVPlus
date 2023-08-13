package com.downyoutube.devsvplus.devsvplus.ui;

import com.downyoutube.devsvplus.devsvplus.DevSVPlus;
import com.downyoutube.devsvplus.devsvplus.Utils;
import de.tr7zw.nbtapi.NBTItem;
import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmocore.api.player.profess.PlayerClass;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class CharacterInteraction implements Listener {

    @EventHandler
    public void onInvClick(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof CharacterHolder) {
            event.setCancelled(true);

            if (event.getCurrentItem() == null || event.getCurrentItem().getType().equals(Material.AIR)) return;
            // check clicked button
            Player player = (Player) event.getWhoClicked();
            NBTItem clicked_item_nbt = new NBTItem(event.getCurrentItem());
            String button_id = clicked_item_nbt.getString("BUTTON_ID");

            if (button_id.startsWith("CHARACTER_")) {
                String selected_character = clicked_item_nbt.getString("SELECTED_CHARACTER");
                String clicked_character = button_id.replace("CHARACTER_", "");
                if (!clicked_character.equals(selected_character)) {
                    PlayerClass character = MMOCore.plugin.classManager.get(clicked_character);
                    Utils.setClass(player, character);
                    if (DevSVPlus.main.getConfig().contains("message.change-character")) player.sendMessage(Utils.colorize(DevSVPlus.main.getConfig().getString("message.change-character")).replace("%character%", character.getName()));
                    player.sendTitle(
                            ((DevSVPlus.main.getConfig().contains("message.change-character-title")) ? Utils.colorize(DevSVPlus.main.getConfig().getString("message.change-character-title")) : "").replace("%character%", character.getName()),
                            (DevSVPlus.main.getConfig().contains("message.change-character-sub-title")) ? Utils.colorize(DevSVPlus.main.getConfig().getString("message.change-character-sub-title")) : ""
                             );
                    player.closeInventory();
                }

            } else {
                switch (button_id) {
                    case "PREVIOUS" -> {
                        int page = clicked_item_nbt.getInteger("PAGE");
                        String selected_character = clicked_item_nbt.getString("SELECTED_CHARACTER");
                        CharacterUI.openCharacterUI(player, page-1, selected_character);
                    }
                    case "NEXT" -> {
                        int page = clicked_item_nbt.getInteger("PAGE");
                        String selected_character = clicked_item_nbt.getString("SELECTED_CHARACTER");
                        CharacterUI.openCharacterUI(player, page+1, selected_character);
                    }
                }
            }
        }
    }
}
