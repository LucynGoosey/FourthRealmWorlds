package me.lucyn.fourthrealm;

import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class FourthRealmWorlds extends JavaPlugin {

    public static FourthRealmCore fourthRealmCore;
    public static List<World> worlds;

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





        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
