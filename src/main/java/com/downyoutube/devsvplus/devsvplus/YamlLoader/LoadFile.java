package com.downyoutube.devsvplus.devsvplus.YamlLoader;

import com.downyoutube.devsvplus.devsvplus.DevSVPlus;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class LoadFile {
    public static void loadData(Player player, HashMap<UUID, YamlConfiguration> data) {
        if (!data.containsKey(player.getUniqueId())) {
            File folder = new File(DevSVPlus.main.getDataFolder() + "/PlayerData");
            if (!folder.exists())
                folder.mkdir();
            File file = new File(DevSVPlus.main.getDataFolder() + "/PlayerData", player.getUniqueId()+".yml");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("§cError! Can't load data of "+player.getName());
                    return;
                }
            }

            data.put(player.getUniqueId(), YamlConfiguration.loadConfiguration(file));
        }
    }
}
