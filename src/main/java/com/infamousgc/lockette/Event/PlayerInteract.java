package com.infamousgc.lockette.Event;

import com.infamousgc.lockette.LockManager;
import com.infamousgc.lockette.Main;
import com.infamousgc.lockette.UI.CreateLockUI;
import com.infamousgc.lockette.UI.SettingsUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerInteract implements Listener {
    private final Main plugin;
    private static final Map<UUID, Long> clickDelay = new HashMap<>();

    public PlayerInteract(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();

        if (block == null || block.getType() != Material.CHEST) return;
        if (!player.isSneaking()) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        // TODO: Make it so you cannot open the interface for a chest you cannot open
        // TODO: Maybe make it less easy to open the interface by accident - Test

        event.setCancelled(true);

        long currentTime = System.currentTimeMillis();
        UUID uuid = player.getUniqueId();

        if (clickDelay.containsKey(uuid)) {
            long lastClick = clickDelay.get(uuid);
            if (currentTime - lastClick <= 500) {
                Bukkit.getScheduler().cancelTasks(plugin);
                clickDelay.remove(uuid);

                if (!LockManager.getInstance().isChestLocked(block.getLocation()))
                    new CreateLockUI(player, block.getLocation());
                else if (LockManager.getInstance().canAccessChest(block.getLocation(), player))
                    new SettingsUI(player, block.getLocation());
                else
                    player.sendMessage("You do not have permission to access this chest");
            }
        }

        clickDelay.put(uuid, currentTime);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            clickDelay.remove(uuid);
            event.useInteractedBlock();
        }, 10L);
    }

}
