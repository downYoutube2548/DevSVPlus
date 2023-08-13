package com.downyoutube.devsvplus.devsvplus;

import com.downyoutube.devsvplus.devsvplus.YamlLoader.LoadFile;
import com.downyoutube.devsvplus.devsvplus.YamlLoader.SaveFile;
import com.downyoutube.devsvplus.devsvplus.event.LoadData;
import com.downyoutube.devsvplus.devsvplus.event.SkillMode;
import com.downyoutube.devsvplus.devsvplus.ui.CharacterInteraction;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public final class DevSVPlus extends JavaPlugin {

    public static DevSVPlus main;
    public static HashMap<UUID, YamlConfiguration> data = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        main = this;

        // loadResource(this, "config.yml");
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        for (Player player : Bukkit.getOnlinePlayers()) {
            LoadFile.loadData(player, data);
        }

        getServer().getPluginManager().registerEvents(new LoadData(), this);
        getServer().getPluginManager().registerEvents(new CharacterInteraction(), this);
        getServer().getPluginManager().registerEvents(new SkillMode(), this);
        Objects.requireNonNull(getCommand("character")).setExecutor(new Character());
    }

    @Override
    public void onDisable() {
        try {
            for (Player player : Bukkit.getOnlinePlayers()) {
                SaveFile.saveData(player, data);
            }
        } catch (Exception ignored) {}
    }

    /*
    private static File loadResource(Plugin plugin, String resource) {
        File folder = plugin.getDataFolder();
        if (!folder.exists())
            folder.mkdir();
        File resourceFile = new File(folder, resource);
        try {
            //if (!resourceFile.exists()) {
            resourceFile.createNewFile();
            try (InputStream in = plugin.getResource(resource);
                 OutputStream out = new FileOutputStream(resourceFile)) {
                ByteStreams.copy(in, out);
            }
            //}
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resourceFile;
    }

     */
}
