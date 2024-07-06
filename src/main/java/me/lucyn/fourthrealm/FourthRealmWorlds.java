package me.lucyn.fourthrealm;

import org.bukkit.plugin.java.JavaPlugin;

public final class FourthRealmWorlds extends JavaPlugin {

    public static FourthRealmCore fourthRealmCore;

    @Override
    public void onEnable() {
        fourthRealmCore = (FourthRealmCore) this.getServer().getPluginManager().getPlugin("FourthRealmCore");





        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
