package me.lucyn.fourthrealm.listeners;

import me.lucyn.fourthrealm.FourthRealmCore;
import me.lucyn.fourthrealm.FourthRealmWorlds;
import me.lucyn.fourthrealm.RealmPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import static me.lucyn.fourthrealm.FourthRealmWorlds.fourthRealmCore;

public class DeathListener implements Listener {

    private static FourthRealmWorlds fourthRealmWorlds;

    public DeathListener(FourthRealmWorlds fourthRealmWorlds) {
        this.fourthRealmWorlds = fourthRealmWorlds;
    }


    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {

        if(!event.getRespawnReason().equals(PlayerRespawnEvent.RespawnReason.END_PORTAL)) {

            RealmPlayer realmPlayer = fourthRealmCore.getPlayerData(event.getPlayer());

            int index = fourthRealmWorlds.worlds.indexOf(realmPlayer.currentLivingWorld);

            if(index > fourthRealmWorlds.worlds.size()) {
                index = 0;
            } else {
                index++;
            }

            if(realmPlayer.beds.containsKey(fourthRealmWorlds.worlds.get(index))) {
                event.setRespawnLocation(realmPlayer.beds.get(fourthRealmWorlds.worlds.get(index)));
            } else {
                event.setRespawnLocation(fourthRealmWorlds.worlds.get(index).getSpawnLocation());
            }

        }

    }


}
