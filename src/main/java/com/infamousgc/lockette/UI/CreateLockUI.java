package com.infamousgc.lockette.UI;

import com.infamousgc.lockette.LockManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class CreateLockUI extends UserInterface{
    private final Location chestLocation;
    private final LockManager lockManager = LockManager.getInstance();

    public CreateLockUI(Player player, Location chestLocation) {
        super(player, title(), 27);
        this.chestLocation = chestLocation;
        initializeItems();
        openInventory();
    }

    @Override
    protected void initializeItems() {
        ItemStack item = new ItemStack(Material.CHAIN);
        ItemMeta meta = item.getItemMeta();

        meta.displayName(Component.text("Lock Chest")
                .decoration(TextDecoration.BOLD, true)
                .decoration(TextDecoration.ITALIC, false)
                .color(NamedTextColor.YELLOW));

        meta.lore(Arrays.asList(
                Component.empty(),
                Component.text("Click to secure your chest").color(NamedTextColor.GRAY),
                Component.text("Prevents unauthorized access").color(NamedTextColor.GRAY),
                Component.empty(),
                keybindComponent("Left-Click", "Lock")
        ));

        fillSpacer(Material.BLACK_STAINED_GLASS_PANE);

        item.setItemMeta(meta);
        inv.setItem(13, item);
    }

    @Override
    public void handleClick(int slot) {
        if (slot == 13) {
            lockManager.lockChest(chestLocation, player);
            player.closeInventory();
        }
    }

    public Location getChestLocation() {
        return chestLocation;
    }
}
