package me.lucyn.fourthrealm.listeners;

import me.lucyn.fourthrealm.FourthRealmCore;
import me.lucyn.fourthrealm.RealmPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;

public class BedListener implements Listener {

    private FourthRealmCore fourthRealmCore;

    public BedListener(FourthRealmCore fourthRealmCore) {
        this.fourthRealmCore = fourthRealmCore;
    }

    @EventHandler
    public void onSleep(PlayerBedEnterEvent event) {
        if(event.getBedEnterResult().equals(PlayerBedEnterEvent.BedEnterResult.OK) || event.getBedEnterResult().equals(PlayerBedEnterEvent.BedEnterResult.NOT_POSSIBLE_NOW)) {
            RealmPlayer player = fourthRealmCore.getPlayerData(event.getPlayer());
            player.beds.put(event.getPlayer().getWorld(), event.getBed().getLocation());
            fourthRealmCore.setPlayerData(player);



        }

    }


}
