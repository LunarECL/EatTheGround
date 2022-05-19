package kr.minta.eattheground.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ChatUtilities {
    public static void broadCast(String content) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(content);
        }
    }
}
