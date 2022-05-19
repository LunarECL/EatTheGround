package kr.minta.eattheground.thread;

import kr.minta.eattheground.EatTheGround;
import kr.minta.eattheground.handler.Game;
import kr.minta.eattheground.utils.ChatUtilities;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class GameReady implements Runnable{
    public static int timeUntilStart;

    EatTheGround plugin;

    public GameReady(EatTheGround plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        if (timeUntilStart <= 0) {
            if (!Game.getInstance().isCanStart()) {
                ChatUtilities.broadCast("게임 시작 불가 필요 인원: " + Bukkit.getOnlinePlayers().size() + "/" + Game.getInstance().minPlayer);
                plugin.restartCountDown();
                return;
            }
            Game.getInstance().start();
            plugin.stopCountdown();
        }
        timeUntilStart--;
        ChatUtilities.broadCast(String.valueOf(timeUntilStart));

    }
}
