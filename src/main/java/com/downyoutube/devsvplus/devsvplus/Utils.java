package com.downyoutube.devsvplus.devsvplus;

import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmocore.api.event.PlayerChangeClassEvent;
import net.Indyuce.mmocore.api.player.PlayerData;
import net.Indyuce.mmocore.api.player.profess.PlayerClass;
import net.Indyuce.mmocore.api.player.profess.SavedClassInformation;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static String colorize(String s) {
        if (s == null || s.equals(""))
            return "";
        Pattern pattern = Pattern.compile("&#[a-fA-F0-9]{6}");
        Matcher match = pattern.matcher(s);
        while (match.find()) {
            String hexColor = s.substring(match.start(), match.end());
            s = s.replace(hexColor, net.md_5.bungee.api.ChatColor.of(hexColor).toString());
            match = pattern.matcher(s);
        }
        return net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', s);
    }

    public static List<String> colorize(List<String> l) {
        List<String> output = new ArrayList<>();
        for (String s : l) {
            output.add(colorize(s));
        }
        return output;
    }

    public static List<String> placeholder(List<String> input, String[] target, String[] replacement) {
        List<String> output = new ArrayList<>();
        for (String s : input) {
            if (s == null) {
                continue;
            }

            for (int i = 1; i <= target.length; i++) {
                if (replacement[i - 1] != null) {
                    s = s.replaceAll(target[i - 1], replacement[i - 1]);
                }
            }
            output.add(colorize(s));
        }

        return output;
    }

    public static String placeholder(String input, String[] target, String[] replacement) {
        String output = input;
        for (int i = 1; i <= target.length; i++) {
            if (replacement[i - 1] != null) {
                output = output.replaceAll(target[i - 1], replacement[i - 1]);
            }
        }
        return colorize(output);
    }

    public static String Format(Double input) {
        DecimalFormat df = new DecimalFormat("#,###.####");
        return df.format(input);
    }

    public static Boolean isPageValid(int size, int page, int spaces) {
        if (page <= 0) {
            return false;
        }

        int upperBound = page * spaces;
        int lowerBound = upperBound - spaces;

        return size > lowerBound;
    }

    public static void setClass(Player player, PlayerClass new_character) {
        PlayerData data = PlayerData.get(player);

        // set class
        PlayerChangeClassEvent called = new PlayerChangeClassEvent(data, new_character);
        Bukkit.getPluginManager().callEvent(called);

        (Objects.requireNonNull(data.hasSavedClass(new_character) ? data.getClassInfo(new_character) : new SavedClassInformation(MMOCore.plugin.dataProvider.getDataManager().getDefaultData()))).load(new_character, data);



        while (data.getBoundSkills().size() > 0) {
            data.unbindSkill(0);
        }


        if (DevSVPlus.main.getConfig().contains("skill-bound." + new_character.getId())) {
            int i = 1;
            for (String s : Objects.requireNonNull(DevSVPlus.main.getConfig().getString("skill-bound." + new_character.getId())).split(";")) {
                if (new_character.getSkill(s) == null) continue;
                data.bindSkill(i-1, new_character.getSkill(s));
                i++;
            }
        }
    }

    public static List<String> tabComplete(String a, List<String> arg) {
        List<String> matches = new ArrayList<>();
        String search = a.toLowerCase(Locale.ROOT);
        for (String s : arg) {
            if (s.toLowerCase(Locale.ROOT).startsWith(search)) {
                matches.add(s);
            }
        }
        return matches;
    }
}
