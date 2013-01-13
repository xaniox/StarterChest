package com.matzefratze123.starterchest.system;

import java.util.Collection;
import java.util.HashMap;
import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

public class StarterChestManager {

	public static HashMap<String, ChestStorage> chestSaveMap = new HashMap<String, ChestStorage>();
	
	public static ChestStorage getStorage(String id) {
		if (!hasChestWithId(id))
			return null;
		return chestSaveMap.get(id);
	}
	
	public static ChestStorage getStorageByChest(Chest chest) {
		for (ChestStorage storage : getStorages()) {
			if (isSameBlockLocation(storage.getChestLoc(), chest.getLocation()))
				return storage;
		}
		return null;
	}
	
	public static ChestStorage[] getStorages() {
		Collection<ChestStorage> storages = chestSaveMap.values();
		return storages.toArray(new ChestStorage[storages.size()]);
	}
	
	public static void addStorage(String id, ItemStack[] originalItemStacks, Location chestLoc) {
		chestSaveMap.put(id, new ChestStorage(originalItemStacks, chestLoc, id));
	}
	
	public static void addStorage(String id, Location chestLoc) {
		chestSaveMap.put(id, new ChestStorage(chestLoc, id));
	}
	
	public static void removeStorage(String id) {
		if (!hasChestWithId(id))
			return;
		chestSaveMap.remove(id);
	}
	
	public static boolean hasChestWithId(String id) {
		return chestSaveMap.containsKey(id);
	}
	
	public static boolean isStarterChest(Chest chest) {
		for (ChestStorage storage : getStorages()) {
			if (isSameBlockLocation(storage.getChestLoc(), chest.getLocation()))
				return true;
		}
		return false;
	}
	
	public static String getIdFromChest(Chest chest) {
		for (ChestStorage storage : getStorages()) {
			if (isSameBlockLocation(storage.getChestLoc(), chest.getLocation()))
				return storage.getId();
		}
		return null;
	}
	
	public static boolean isSameBlockLocation(Location loc1, Location loc2) {
		if (loc1.getBlockX() == loc2.getBlockX() &&
				loc1.getBlockY() == loc2.getBlockY() &&
				loc1.getBlockZ() == loc2.getBlockZ())
			return true;
		return false;
	}
	
}
