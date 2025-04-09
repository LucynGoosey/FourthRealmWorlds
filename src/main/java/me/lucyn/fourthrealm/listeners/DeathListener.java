package me.lucyn.fourthrealm.listeners;

import me.lucyn.fourthrealm.FourthRealmCore;
import me.lucyn.fourthrealm.FourthRealmWorlds;
import me.lucyn.fourthrealm.PlayerDataHandler;
import me.lucyn.fourthrealm.RealmPlayer;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;


import static me.lucyn.fourthrealm.FourthRealmWorlds.fourthRealmCore;

public class DeathListener implements Listener {

    //TODO: handle players disconnecting while in purgatory

    private final FourthRealmWorlds fourthRealmWorlds;

    private int seconds = 15;

    public DeathListener(FourthRealmWorlds fourthRealmWorlds) {
        this.fourthRealmWorlds = fourthRealmWorlds;
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {

        if(event.getRespawnReason().equals(PlayerRespawnEvent.RespawnReason.END_PORTAL)) return; //prevents players from rotating worlds when going through end portal.

        NamespacedKey key = new NamespacedKey(fourthRealmWorlds, "bar" + event.getPlayer().getUniqueId());

        World purgatory = fourthRealmWorlds.getServer().getWorld("purgatory");
        BossBar bar = fourthRealmWorlds.getServer().createBossBar(key,"", BarColor.PURPLE, BarStyle.SOLID);

        bar.addPlayer(event.getPlayer());

        fourthRealmWorlds.getLogger().info("respawning player in purgatory");

        event.setRespawnLocation(purgatory.getSpawnLocation());
        new BukkitRunnable() {
            int i = seconds;
            @Override
            public void run() {

                if(i <= 0) {

                    if(!event.getPlayer().isOnline()) {
                        PlayerDataHandler playerDataHandler = fourthRealmCore.getPlayerDataHandler();
                        playerDataHandler.loadPlayerData(event.getPlayer());
                        ((FourthRealmCore) Bukkit.getPluginManager().getPlugin("FourthRealmCore")).setPlayerData(fourthRealmCore.getPlayerData(event.getPlayer()).setPurgatoryRespawn(true));
                        playerDataHandler.savePlayerData(FourthRealmCore.playerData.get(event.getPlayer()));
                        FourthRealmCore.playerData.remove(event.getPlayer());
                    } else {

                        rotateWorld(event.getPlayer());
                    }
                    bar.removePlayer(event.getPlayer());
                    Bukkit.removeBossBar(key);
                    cancel();

                }
                i--;

                if(!(((double) i / seconds) < 0.0)) {
                    bar.setProgress((double) i / seconds);
                }


                if(i >= 60) {
                    String ss;
                    int s = i % 60;
                    if(s < 10) {
                        ss = "0" + s;
                    } else {
                        ss = String.valueOf(s);

                    }
                    bar.setTitle(i / 60 + ":" + ss);
                } else {
                    bar.setTitle(String.valueOf(i));
                }
            }
        }.runTaskTimer(fourthRealmWorlds, 0, 20);

    }

    public void rotateWorld(Player player) {
        RealmPlayer realmPlayer = fourthRealmCore.getPlayerData(player);

        int index = fourthRealmWorlds.worlds.indexOf(realmPlayer.currentLivingWorld);
        //fourthRealmWorlds.getLogger().info("current world: " + index); //debugging stuff
        //fourthRealmWorlds.getLogger().info("index size: 0-" + (fourthRealmWorlds.worlds.size() - 1));
        if(index < fourthRealmWorlds.worlds.size() - 1) {
           // fourthRealmWorlds.getLogger().info("index is " + index + ". Changing to " + (index + 1));
            index++;
        } else {

            index = 0;
            fourthRealmWorlds.getLogger().info("2 index greater than or equal to max size: " + index);
        }
        if(realmPlayer.beds.containsKey(fourthRealmWorlds.worlds.get(index)) && realmPlayer.beds.get(fourthRealmWorlds.worlds.get(index)).getBlock().getBlockData() instanceof org.bukkit.block.data.type.Bed) {

            player.teleport(realmPlayer.beds.get(fourthRealmWorlds.worlds.get(index)));

        } else {
            player.teleport(fourthRealmWorlds.worlds.get(index).getSpawnLocation());
        }
        //fourthRealmWorlds.getLogger().info("respawning player in next world: " + fourthRealmWorlds.worlds.indexOf(event.getRespawnLocation().getWorld()));

        realmPlayer.currentLivingWorld = player.getWorld();
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent event) {

        new BukkitRunnable() {

            @Override
            public void run() {
                RealmPlayer realmPlayer = fourthRealmCore.getPlayerData(event.getPlayer());
                if(realmPlayer.purgatoryRespawn) {
                    rotateWorld(event.getPlayer());
                    realmPlayer.purgatoryRespawn = false;
                }
            }
        }.runTaskLater(fourthRealmWorlds, 20);

    }

}
