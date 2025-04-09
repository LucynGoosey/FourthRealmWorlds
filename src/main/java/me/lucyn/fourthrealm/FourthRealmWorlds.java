package me.lucyn.fourthrealm;

import me.lucyn.fourthrealm.listeners.BedListener;
import me.lucyn.fourthrealm.listeners.DeathListener;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class FourthRealmWorlds extends JavaPlugin {

    public static FourthRealmCore fourthRealmCore;
    public List<World> worlds;

    @Override
    public void onEnable() {
        fourthRealmCore = (FourthRealmCore) this.getServer().getPluginManager().getPlugin("FourthRealmCore");
        worlds = new ArrayList<>();

        List<World> allWorlds = getServer().getWorlds();

        for(World world : allWorlds) {
            if(world.getEnvironment().equals(World.Environment.NORMAL)) {
                worlds.add(world);

            }
        }



        for(int i = 0; i < 4; i++ ) {
            getLogger().info(i + " - " + worlds.get(i).getName());
        }


        getLogger().info(worlds.size() + " worlds loaded");

        getServer().getPluginManager().registerEvents(new BedListener(fourthRealmCore), this);
        getServer().getPluginManager().registerEvents(new DeathListener(this), this);




        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
