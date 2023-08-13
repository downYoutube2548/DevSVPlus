package com.downyoutube.devsvplus.devsvplus.ui;

import com.downyoutube.devsvplus.devsvplus.DevSVPlus;
import com.downyoutube.devsvplus.devsvplus.Utils;
import de.tr7zw.nbtapi.NBTItem;
import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmocore.api.player.PlayerData;
import net.Indyuce.mmocore.api.player.profess.ClassOption;
import net.Indyuce.mmocore.api.player.profess.PlayerClass;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CharacterUI {

    public static void openCharacterUI(Player player, int page, String selected_character) {

        Inventory inv = Bukkit.createInventory(new CharacterHolder(), 54, Objects.requireNonNull(DevSVPlus.main.getConfig().getString("gui.title")));

        // character button
        List<String> data_characters = DevSVPlus.data.get(player.getUniqueId()).getStringList("characters");
        List<String> characters = new ArrayList<>();
        for (String c : data_characters) {
            if (MMOCore.plugin.classManager.get(c) != null && !c.equals(".DS_S")) {
                if (characters.contains(c)) continue;
                characters.add(c);
            }
        }

        int[] character_slot = new int[]{11,12,13,14,15,20,21,22,23,24,29,30,31,32,33};
        int l = (page * character_slot.length) - character_slot.length;
        for (int i = 0; i < character_slot.length && i < characters.size() && l < characters.size(); i++) {
            String character = characters.get(l);
            PlayerClass playerClass = MMOCore.plugin.classManager.get(character);
            PlayerData playerData = PlayerData.get(player.getUniqueId());
            ItemStack character_icon = playerClass.getIcon();
            ItemMeta character_icon_meta = character_icon.getItemMeta();
            assert character_icon_meta != null;
            character_icon_meta.setDisplayName(playerClass.getName());
            List<String> character_icon_lore = new ArrayList<>();
            List<String> lore = (character.equals(selected_character)) ? Utils.placeholder(Utils.colorize(DevSVPlus.main.getConfig().getStringList("gui.character-list-button.activated-lore")), new String[]{"%level%", "%max-level%"}, new String[]{Utils.Format((character.equals(selected_character)) ? playerData.getLevel() : playerData.hasSavedClass(playerClass) ? (double) playerData.getClassInfo(playerClass).getLevel() : 0), Utils.Format((double)playerClass.getMaxLevel())}) : Utils.placeholder(Utils.colorize(DevSVPlus.main.getConfig().getStringList("gui.character-list-button.lore")), new String[]{"%level%", "%max-level%"}, new String[]{Utils.Format((character.equals(selected_character)) ? playerData.getLevel() : playerData.hasSavedClass(playerClass) ? (double) playerData.getClassInfo(playerClass).getLevel() : 0), Utils.Format((double)playerClass.getMaxLevel())});
            for (String s : lore) {
                if (s.equals("%description%")) {
                    character_icon_lore.addAll(Utils.colorize(playerClass.getDescription()));
                } else {
                    character_icon_lore.add(s);
                }
            }
            character_icon_meta.setLore(character_icon_lore);
            if (character.equals(selected_character)) {
                character_icon_meta.addEnchant(Enchantment.DURABILITY, 0, true);
                character_icon_meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            character_icon.setItemMeta(character_icon_meta);
            NBTItem character_button_nbt = new NBTItem(character_icon);
            character_button_nbt.setString("BUTTON_ID", "CHARACTER_" + character);
            character_button_nbt.setString("SELECTED_CHARACTER", selected_character);
            inv.setItem(character_slot[i], character_button_nbt.getItem());
            l++;
        }

        // previous button
        if (Utils.isPageValid(characters.size(), page-1, character_slot.length)) {
            ItemStack previous_button = new ItemStack(Material.valueOf(DevSVPlus.main.getConfig().getString("gui.previous-button.material")));
            ItemMeta previous_button_meta = previous_button.getItemMeta();
            assert previous_button_meta != null;
            previous_button_meta.setDisplayName(Utils.colorize(DevSVPlus.main.getConfig().getString("gui.previous-button.name")));
            previous_button_meta.setLore(Utils.colorize(DevSVPlus.main.getConfig().getStringList("gui.previous-button.lore")));
            previous_button_meta.setCustomModelData(DevSVPlus.main.getConfig().getInt("gui.previous-button.model"));
            previous_button.setItemMeta(previous_button_meta);
            NBTItem previous_button_nbt = new NBTItem(previous_button);
            previous_button_nbt.setString("BUTTON_ID", "PREVIOUS");
            previous_button_nbt.setInteger("PAGE", page);
            previous_button_nbt.setString("SELECTED_CHARACTER", selected_character);
            inv.setItem(47, previous_button_nbt.getItem());
        }

        // next button
        if (Utils.isPageValid(characters.size(), page+1, character_slot.length)) {
            ItemStack next_button = new ItemStack(Material.valueOf(DevSVPlus.main.getConfig().getString("gui.next-button.material")));
            ItemMeta next_button_meta = next_button.getItemMeta();
            assert next_button_meta != null;
            next_button_meta.setDisplayName(Utils.colorize(DevSVPlus.main.getConfig().getString("gui.next-button.name")));
            next_button_meta.setLore(Utils.colorize(DevSVPlus.main.getConfig().getStringList("gui.next-button.lore")));
            next_button_meta.setCustomModelData(DevSVPlus.main.getConfig().getInt("gui.next-button.model"));
            next_button.setItemMeta(next_button_meta);
            NBTItem next_button_nbt = new NBTItem(next_button);
            next_button_nbt.setString("BUTTON_ID", "NEXT");
            next_button_nbt.setInteger("PAGE", page);
            next_button_nbt.setString("SELECTED_CHARACTER", selected_character);
            inv.setItem(51, next_button_nbt.getItem());
        }

        // selected character
        PlayerClass playerClass = MMOCore.plugin.classManager.get(selected_character);
        ItemStack selected_character_button = playerClass.getIcon();
        ItemMeta selected_character_button_meta = selected_character_button.getItemMeta();
        assert selected_character_button_meta != null;
        selected_character_button_meta.setDisplayName(playerClass.getName());
        selected_character_button_meta.setLore(playerClass.getDescription());
        selected_character_button.setItemMeta(selected_character_button_meta);
        inv.setItem(49, selected_character_button);
        player.openInventory(inv);
    }
}
