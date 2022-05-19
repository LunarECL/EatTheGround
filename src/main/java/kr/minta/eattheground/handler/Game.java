package kr.minta.eattheground.handler;

import kr.minta.eattheground.EatTheGround;
import kr.minta.eattheground.utils.ChatUtilities;
import kr.minta.eattheground.utils.TeleportUtilities;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;

public class Game implements Listener {
    static Game instance = new Game();
    private boolean canStart;
    private boolean hasStart;
    public int minPlayer = 7;
    private HashMap<Player, Material> gamers;
    private ArrayList<Player> beginers = new ArrayList<>();
    private ArrayList<Material> wools = new ArrayList<>();
    private int radius = 20;
    private Location centre;

    private Game() {
        canStart = true;
        hasStart = false;
        gamers = new HashMap<>();
        wools.add(Material.BLACK_WOOL);
        wools.add(Material.GRAY_WOOL);
        wools.add(Material.RED_WOOL);
        wools.add(Material.GREEN_WOOL);
        wools.add(Material.PURPLE_WOOL);
        wools.add(Material.BROWN_WOOL);
        wools.add(Material.ORANGE_WOOL);
        wools.add(Material.BLUE_WOOL);
        centre = new Location(Bukkit.getWorlds().get(0), -127, 95, 67);
    }

    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

    public boolean isCanStart() {
        return Bukkit.getOnlinePlayers().size() >= minPlayer;
    }

    public void endGame() {
        hasStart = false;
        int beginX = centre.getBlockX()-radius;
        int beginZ = centre.getBlockZ()-radius;
        int endX = centre.getBlockX()+radius;
        int endZ = centre.getBlockZ()+radius;

        int y = centre.getBlockY();

        HashMap<Player, Integer> score = new HashMap<>();
        for (Player player : gamers.keySet()){
            score.put(player, 0);
        }
        for (int i=beginX; i<=endX; i++){
            for (int j=beginZ; j<=endZ; j++){
                double hypothesis = Math.sqrt(Math.pow(i- centre.getBlockX(), 2) + Math.pow(j- centre.getBlockZ(), 2));
                if(hypothesis<radius){
                    Block block = new Location(centre.getWorld(), i, y, j).getBlock();
                    for (Player player : gamers.keySet()){
                        if(gamers.get(player).equals(block.getType())){
                            int s = score.get(player) + 1;
                            score.put(player, s);
                            block.setType(Material.WHITE_WOOL);
                            break;
                        }
                    }
                }
            }
        }

        String result = "";

        for (Player player : score.keySet()){
            result += String.format("%s : %d\n", player, score.get(player));
        }

        ChatUtilities.broadCast(result);
    }

    public void start() {
        Bukkit.getLogger().info("Start Game");
        canStart = false;
        hasStart = true;
        ArrayList<Player> onlines = new ArrayList<>();
        onlines.addAll(Bukkit.getOnlinePlayers());
        Bukkit.getLogger().info("Add Onlines");
        for (int i = 0; i < minPlayer; i++) {
            Player player = onlines.get(i);
            Bukkit.getLogger().info(player.getName() + " got");
            gamers.put(player, wools.get(i));
            Bukkit.getLogger().info(player.getName() + " gamers Putted");
            beginers.add(player);
            Bukkit.getLogger().info(player.getName() + " beginers Putted");
            TeleportUtilities.randomTeleportCircle(radius, centre, player);
            Bukkit.getLogger().info(player.getName() + "Teleported Putted");
            player.setGameMode(GameMode.ADVENTURE);
            player.getInventory().clear();
            player.getInventory().addItem(new ItemStack(Material.DIAMOND_HOE));
            Bukkit.getLogger().info(player.getName() + "check");
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(EatTheGround.plugin, new Runnable() {
            @Override
            public void run() {
                endGame();

            }
        }, 1200L);
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!hasStart) {
            return;
        }
        event.setCancelled(true);
        if (event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            return;
        }
        Block block = event.getClickedBlock();
        Material wool = gamers.get(player);
        if(block.getType().equals(Material.WHITE_WOOL)) {
            if(beginers.contains(player)) {
                block.setType(wool);
                beginers.remove(player);
            }
            else{
                if (block.getRelative(BlockFace.NORTH).getType().equals(wool) || block.getRelative(BlockFace.SOUTH).getType().equals(wool) || block.getRelative(BlockFace.EAST).getType().equals(wool) || block.getRelative(BlockFace.WEST).getType().equals(wool)) {
                    block.setType(wool);
                }
            }
        }
    }
}
