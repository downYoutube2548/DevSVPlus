package com.downyoutube.devsvplus.devsvplus.YamlLoader;

import com.downyoutube.devsvplus.devsvplus.DevSVPlus;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class SaveFile {
    public static void saveData(Player player, HashMap<UUID, YamlConfiguration> data) {
        try {
            File file = new File(DevSVPlus.main.getDataFolder()+"/PlayerData", player.getUniqueId()+".yml");
            data.get(player.getUniqueId()).save(file);
        } catch (IOException e) {
            System.out.println("§cError! Can't save file.");
            return;
        }
        data.remove(player.getUniqueId());
    }
}
