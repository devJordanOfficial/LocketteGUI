package com.infamousgc.lockette;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class LockManager {
    private static LockManager instance;
    private final Map<Location, ChestLock> lockedChests;

    private LockManager() {
        lockedChests = new HashMap<>();
    }

    public static LockManager getInstance() {
        if (instance == null)
            instance = new LockManager();
        return instance;
    }

    public void lockChest(Location location, Player owner) {
        lockedChests.put(location, new ChestLock(owner.getUniqueId()));
    }

    public boolean isChestLocked(Location location) {
        return lockedChests.containsKey(location);
    }

    public Privacy getPrivacy(Location location) {
        return lockedChests.get(location).getPrivacy();
    }

    public void setPrivacy(Location location, Privacy privacy) {
        ChestLock lock = lockedChests.get(location);
        if (lock != null)
            lock.setPrivacy(privacy);
    }

    public boolean canAccessChest(Location location, Player player) {
        ChestLock lock = lockedChests.get(location);
        return lock != null && (lock.getOwner().equals(player.getUniqueId()) || lock.hasAccess(player.getUniqueId()));
    }

    public void addAccess(Location location, Player player) {
        ChestLock lock = lockedChests.get(location);
        if (lock != null)
            lock.addAccess(player.getUniqueId());
    }

    public void removeAccess(Location location, Player player) {
        ChestLock lock = lockedChests.get(location);
        if (lock != null)
            lock.removeAccess(player.getUniqueId());
    }

    private static class ChestLock {
        private final UUID owner;
        private final Set<UUID> accessList = new HashSet<>();
        private Privacy privacy = Privacy.RESTRICTED;

        private ChestLock(UUID owner) {
            this.owner = owner;
        }

        public UUID getOwner() {
            return owner;
        }

        public void setPrivacy(Privacy privacy) {
            this.privacy = privacy;
        }

        public Privacy getPrivacy() {
            return privacy;
        }

        public void addAccess(UUID player) {
            accessList.add(player);
        }

        public void removeAccess(UUID player) {
            accessList.remove(player);
        }

        public boolean hasAccess(UUID player) {
            return accessList.contains(player);
        }
    }

    public enum Privacy {
        PUBLIC,
        RESTRICTED,
        PRIVATE
    }
}
