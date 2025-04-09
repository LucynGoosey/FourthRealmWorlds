package me.lucyn.fourthrealm.listeners;

import me.lucyn.fourthrealm.FourthRealmCore;
import me.lucyn.fourthrealm.FourthRealmWorlds;
import me.lucyn.fourthrealm.RealmPlayer;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import javax.naming.Name;

import static me.lucyn.fourthrealm.FourthRealmWorlds.fourthRealmCore;

public class DeathListener implements Listener {


    //TODO: handle players disconnecting while in purgatory
    //add




    private final FourthRealmWorlds fourthRealmWorlds;

    private int seconds = 120;

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
                i--;

                bar.setProgress((double) i / seconds);

                if(i >= 60) {
                    bar.setTitle(String.valueOf(i / 60) + ":" + String.valueOf(i % 60));
                }


                bar.setTitle(String.valueOf(i));

                if(i == 0) {
                    rotateWorld(event);
                    bar.removePlayer(event.getPlayer());
                    fourthRealmWorlds.getServer().removeBossBar(key);
                    cancel();

                }


            }
        }.runTaskTimer(fourthRealmWorlds, 0, 20);





        //purgatory code goes here



        //rotateWorld(event);// eventually this will run after the purgatory timer finishes


    }
    //this will have to be reworked to not use the respawn event, as they'll be respawning in purgatory and then teleported to the correct spot.

    public void rotateWorld(PlayerRespawnEvent event) {
        RealmPlayer realmPlayer = fourthRealmCore.getPlayerData(event.getPlayer());

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

            event.getPlayer().teleport(realmPlayer.beds.get(fourthRealmWorlds.worlds.get(index)));

        } else {
            event.getPlayer().teleport(fourthRealmWorlds.worlds.get(index).getSpawnLocation());
        }
        //fourthRealmWorlds.getLogger().info("respawning player in next world: " + fourthRealmWorlds.worlds.indexOf(event.getRespawnLocation().getWorld()));

        realmPlayer.currentLivingWorld = event.getPlayer().getWorld();





    }

}
