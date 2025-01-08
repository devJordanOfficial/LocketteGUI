package com.infamousgc.lockette.Event;

import com.infamousgc.lockette.UI.CreateLockUI;
import com.infamousgc.lockette.UI.SettingsUI;
import com.infamousgc.lockette.UI.UserInterface;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class InventoryClick implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        if (!(inv.getHolder(false) instanceof UserInterface userInterface)) return;

        event.setCancelled(true);

        int slot = event.getRawSlot();
        if (slot >= 0 && slot < userInterface.getInventory().getSize())
            userInterface.handleClick(slot);
    }
}
