package com.infamousgc.lockette.UI;

import com.infamousgc.lockette.LockManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class SettingsUI extends UserInterface {
    private final Location chestLocation;
    private final LockManager lockManager = LockManager.getInstance();

    private final static int PRIVACY_SLOT = 12;
    private final static int ACCESS_SLOT = 14;

    public SettingsUI(Player player, Location chestLocation) {
        super(player, title(), 27);
        this.chestLocation = chestLocation;
        initializeItems();
        openInventory();
    }

    @Override
    protected void initializeItems() {

        // Privacy Settings Button
        ItemStack privacyItem = new ItemStack(Material.REDSTONE_TORCH);
        ItemMeta privacyMeta = privacyItem.getItemMeta();
        privacyMeta.displayName(Component.text("Change Privacy")
                .decoration(TextDecoration.BOLD, true)
                .decoration(TextDecoration.ITALIC, false)
                .color(NamedTextColor.YELLOW));

        privacyMeta.lore(Arrays.asList(
                Component.empty(),
                Component.text("Click to cycle privacy state", NamedTextColor.GRAY),
                Component.text("Public - Anyone can access this chest as if it was not locked", NamedTextColor.GRAY),
                Component.text("Restricted - Only people you allow can access this chest", NamedTextColor.GRAY),
                Component.text("Private - Only you can access this chest", NamedTextColor.GRAY),
                Component.empty(),
                Component.text("» ", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false)
                        .append(Component.text("Public")
                                .decoration(TextDecoration.ITALIC, false)
                                .color(getCurrentPrivacy().equals(LockManager.Privacy.PUBLIC) ? NamedTextColor.YELLOW : NamedTextColor.GRAY)),
                Component.text("» ", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false)
                        .append(Component.text("Restricted")
                                .decoration(TextDecoration.ITALIC, false)
                                .color(getCurrentPrivacy().equals(LockManager.Privacy.RESTRICTED) ? NamedTextColor.YELLOW : NamedTextColor.GRAY)),
                Component.text("» ", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false)
                        .append(Component.text("Private")
                                .decoration(TextDecoration.ITALIC, false)
                                .color(getCurrentPrivacy().equals(LockManager.Privacy.PRIVATE) ? NamedTextColor.YELLOW : NamedTextColor.GRAY)),
                Component.empty(),
                keybindComponent("Left-Click", "Cycle")
        ));
        privacyItem.setItemMeta(privacyMeta);
        inv.setItem(PRIVACY_SLOT, privacyItem);

        // Access Control Button
        ItemStack accessItem = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta accessMeta = accessItem.getItemMeta();
        accessMeta.displayName(Component.text("Change Access")
                .decoration(TextDecoration.BOLD, true)
                .decoration(TextDecoration.ITALIC, false)
                .color(NamedTextColor.YELLOW));

        accessMeta.lore(Arrays.asList(
                Component.empty(),
                Component.text("Modify who can access your chest").color(NamedTextColor.GRAY),
                Component.text("Only applies when privacy is set to 'Restricted'").color(NamedTextColor.GRAY),
                Component.empty(),
                keybindComponent("Left-Click", "Modify Access")
        ));
        accessItem.setItemMeta(accessMeta);
        inv.setItem(ACCESS_SLOT, accessItem);

        fillSpacer(Material.BLACK_STAINED_GLASS_PANE);
    }

    @Override
    public void handleClick(int slot) {
        switch (slot) {
            case PRIVACY_SLOT -> {
                LockManager.Privacy currentPrivacy = getCurrentPrivacy();
                LockManager.Privacy newPrivacy = LockManager.Privacy.values()[(currentPrivacy.ordinal() + 1) % LockManager.Privacy.values().length];
                lockManager.setPrivacy(chestLocation, newPrivacy);
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                updateInventory();
            }
            case ACCESS_SLOT -> {} // TODO: More robust update method, probably create item in its own method and call it when needed
            // TODO: Log click types and such to see triple click for 2 rapid click bug
        }
    }

    private void updateInventory() {
        inv.clear();
        initializeItems();
        player.updateInventory();
    }

    private LockManager.Privacy getCurrentPrivacy() {
        return lockManager.getPrivacy(chestLocation);
    }
}
