package com.downyoutube.devsvplus.devsvplus;

import com.downyoutube.devsvplus.devsvplus.ui.CharacterUI;
import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmocore.api.player.PlayerData;
import net.Indyuce.mmocore.api.player.profess.PlayerClass;
import net.Indyuce.mmocore.api.player.profess.SavedClassInformation;
import net.Indyuce.mmocore.skill.ClassSkill;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Character implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player player) {

            if (args.length >= 1) {
                if (player.hasPermission("character.admin")) {
                    if (args.length >= 3) {
                        if (Bukkit.getPlayer(args[1]) != null) {
                            if (MMOCore.plugin.classManager.get(args[2]) != null && !args[2].equals(".DS_S")) {
                                Player p = Bukkit.getPlayer(args[1]);
                                List<String> characters = new ArrayList<>(DevSVPlus.data.get(p.getUniqueId()).getStringList("characters"));
                                switch (args[0]) {
                                    case "give":
                                        if (!characters.contains(args[2].toUpperCase())) {
                                            characters.add(args[2].toUpperCase());
                                            DevSVPlus.data.get(p.getUniqueId()).set("characters", characters);
                                            PlayerData data = PlayerData.get(p.getUniqueId());
                                            PlayerClass character = MMOCore.plugin.classManager.get(args[2]);
                                            if (!data.hasSavedClass(character))
                                                new SavedClassInformation(MMOCore.plugin.dataProvider.getDataManager().getDefaultData());
                                            sender.sendMessage("§aGive " + args[2] + " to " + args[1] + "!");
                                        }
                                        break;
                                    case "remove":
                                        if (characters.contains(args[2].toUpperCase())) {
                                            while (characters.contains(args[2].toUpperCase())) {
                                                characters.remove(args[2].toUpperCase());
                                            }
                                            DevSVPlus.data.get(p.getUniqueId()).set("characters", characters);
                                            sender.sendMessage("§aRemove " + args[2] + " from " + args[1] + "!");
                                        }
                                        break;
                                    case "set":
                                        Utils.setClass(p, MMOCore.plugin.classManager.get(args[2]));
                                        sender.sendMessage("§aSet " + args[1] + "'s character to " + args[2] + "!");
                                        break;
                                }
                            } else {

                            }
                        } else {

                        }
                    } else if (args.length == 1) {
                        if (args[0].equals("reload")) {
                            DevSVPlus.main.reloadConfig();
                        } /* else if (args[0].equals("debug")) {
                            Bukkit.broadcastMessage(DevSVPlus.main.getConfig().getString("skill-bound.MARKSMAN.active"));
                        } */
                    } else {

                    }
                }
            } else {
                PlayerClass current_character = PlayerData.get(player.getUniqueId()).getProfess();
                CharacterUI.openCharacterUI(player, 1, current_character.getId());
            }

        } else {
            sender.sendMessage(ChatColor.RED+"You are not player!");
        }

        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> output = new ArrayList<>();
        if (args.length == 1) {
            List<String> list = new ArrayList<>();
            if (sender.hasPermission("characters.admin")) {
                list.addAll(List.of("give", "remove", "set", "reload"));
            }
            output = Utils.tabComplete(args[0], list);
        }
        if (args.length == 2) {
            if (args[0].equals("give") || args[0].equals("remove") || args[0].equals("set")) {
                if (sender.hasPermission("characters.admin")) {
                    List<String> list = new ArrayList<>();
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        list.add(p.getName());
                    }
                    output = Utils.tabComplete(args[1], list);
                }
            }
        }
        if (args.length == 3) {
            if (sender.hasPermission("characters.admin")) {
                if (args[0].equals("give") || args[0].equals("remove") || args[0].equals("set")) {
                    List<String> list = new ArrayList<>();

                    for (PlayerClass character : MMOCore.plugin.classManager.getAll()) {
                        if (character.getId().equals(".DS_S")) continue;
                        list.add(character.getId().toUpperCase());
                    }

                    output = Utils.tabComplete(args[2], list);
                }
            }
        }

        return output;
    }
}
