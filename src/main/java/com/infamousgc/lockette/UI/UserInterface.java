package com.infamousgc.lockette.UI;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public abstract class UserInterface implements InventoryHolder {
    protected final Inventory inv;
    protected final Player player;
    protected final int size;

    public UserInterface(Player player, Component title, int size) {
        this.player = player;
        this.size = size;
        this.inv = createInventory(title, size);
    }

    private Inventory createInventory(Component title, int size) {
        return Bukkit.createInventory(this, size, title);
    }

    protected abstract void initializeItems();

    public abstract void handleClick(int slot);

    protected void fillSpacer(Material type) {
        ItemStack spacer = new ItemStack(type);
        ItemMeta meta = spacer.getItemMeta();
        meta.displayName(Component.empty());
        spacer.setItemMeta(meta);

        for (int i = 0; i < inv.getSize(); i ++) {
            if (inv.getItem(i) == null)
                inv.setItem(i, spacer);
        }
    }

    protected Component keybindComponent(String keybind, String action) {
        return Component.text()
                .decoration(TextDecoration.ITALIC, false)
                .append(Component.text("» ", NamedTextColor.DARK_GRAY))
                .append(Component.text(keybind, NamedTextColor.YELLOW))
                .append(Component.text(" to ", NamedTextColor.GRAY))
                .append(Component.text(action, NamedTextColor.YELLOW))
                .build();
    }

    protected static Component title() {
        return MiniMessage.miniMessage().deserialize("<gradient:gold:yellow><b>Lockette GUI</b></gradient>");
    }

    protected void openInventory() {
        player.openInventory(inv);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inv;
    }
}
