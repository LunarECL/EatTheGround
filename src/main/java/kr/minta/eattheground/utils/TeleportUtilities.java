package kr.minta.eattheground.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Random;

public class TeleportUtilities {
    public static void randomTeleportCircle(int radius, Location centre, Player player) {
        int beginX = centre.getBlockX() - radius;
        int beginZ = centre.getBlockZ() - radius;
        int endX = centre.getBlockX() + radius;
        int endZ = centre.getBlockZ() + radius;
        int randX;
        int randZ;
        while (true) {
            randX = (int) (Math.random() * (endX - beginX + 1) + beginX);
            randZ = (int) (Math.random() * (endZ - beginZ + 1) + beginZ);
            double hypothesis = Math.sqrt(Math.pow(randX- centre.getBlockX(), 2) + Math.pow(randZ- centre.getBlockZ(), 2));
            if (hypothesis < radius) {
                break;
            }

        }
        Location location = new Location(centre.getWorld(), randX, centre.getBlockY()+1, randZ);
        player.teleport(location);
    }
}
