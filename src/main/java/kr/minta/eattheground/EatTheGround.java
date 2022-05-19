package kr.minta.eattheground;

import kr.minta.eattheground.handler.Game;
import kr.minta.eattheground.thread.GameReady;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class EatTheGround extends JavaPlugin {
    public static Plugin plugin;
    public static int readyID;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        getServer().getPluginManager().registerEvents(Game.getInstance(), this);
        startCountdown();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void startCountdown() {
        GameReady.timeUntilStart = 10;
        readyID = getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new GameReady(this), 0L, 20L);
    }

    public void stopCountdown() {
        getServer().getScheduler().cancelTask(readyID);
    }

    public void restartCountDown() {
        stopCountdown();
        startCountdown();
    }
}
