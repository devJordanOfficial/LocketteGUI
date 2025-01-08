package com.infamousgc.lockette;

import com.infamousgc.lockette.Event.InventoryClick;
import com.infamousgc.lockette.Event.PlayerInteract;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        registerEvents();
    }

    @Override
    public void onDisable() {
    }

    private void registerEvents() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new InventoryClick(), this);
        pm.registerEvents(new PlayerInteract(), this);
    }
}
