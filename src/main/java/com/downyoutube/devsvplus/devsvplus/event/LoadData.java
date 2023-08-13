package com.downyoutube.devsvplus.devsvplus.event;

import com.downyoutube.devsvplus.devsvplus.DevSVPlus;
import com.downyoutube.devsvplus.devsvplus.YamlLoader.LoadFile;
import com.downyoutube.devsvplus.devsvplus.YamlLoader.SaveFile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class LoadData implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        LoadFile.loadData(player, DevSVPlus.data);
        DevSVPlus.data.get(player.getUniqueId()).set("name", player.getName());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        SaveFile.saveData(player, DevSVPlus.data);
    }
}
