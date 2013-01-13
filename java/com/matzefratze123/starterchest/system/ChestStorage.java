package com.matzefratze123.starterchest.system;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ChestStorage {

	//String = playerName | ItemStack[] The itemstacks in the chest of player!
	public HashMap<String, ItemStack[]> currentItems = new HashMap<String, ItemStack[]>();
	public ItemStack[] originalItemStacks;
	private String id;
	private Location chestLoc;

	public ChestStorage(ItemStack[] originalItemStack, Location chestLoc, String id) {
		this.originalItemStacks = originalItemStack;
		this.chestLoc = chestLoc;
		this.id = id;
	}
	
	public ChestStorage(Location chestLoc, String id) {
		this.chestLoc = chestLoc;
		this.id = id;
	}

	public ItemStack[] getItems(Player player) {
 		if (!currentItems.containsKey(player.getName()))
 			return null;
 		return currentItems.get(player.getName());
	}
	
	public void setItems(Player player, ItemStack[] is) {
		currentItems.put(player.getName(), is);
	}
	
	public ItemStack[] getOriginalItemStacks() {
		return originalItemStacks;
	}

	public Location getChestLoc() {
		return chestLoc;
	}

	public void setChestLoc(Location chestLoc) {
		this.chestLoc = chestLoc;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}