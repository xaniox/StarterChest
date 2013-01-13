package com.matzefratze123.starterchest.event;

import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.matzefratze123.starterchest.Permissions;
import com.matzefratze123.starterchest.system.StarterChestManager;

public class InventoryCloseListener implements Listener {
	
	public InventoryCloseListener() {}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryClose(InventoryCloseEvent e) {
		HumanEntity humanEntity = e.getPlayer();
		Inventory inv = e.getInventory();
		if (!(humanEntity instanceof Player))
			return;
		Player player = (Player) humanEntity;
		handleInventoryClose(inv, player, false);
		return;
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		if (player.getOpenInventory() == null)
			return;
		Inventory inv = player.getOpenInventory().getTopInventory();
		handleInventoryClose(inv, player, true);
	}
	
	private void handleInventoryClose(Inventory inv, Player player, boolean quit) {
		if (inv.getName().equalsIgnoreCase("StarterChest Admin") && player.hasPermission(Permissions.HAS_ADMIN_PERMISSIONS.getPerm())) {
			ItemStack[] is = inv.getContents();
			if (!PlayerInteractListener.isViewing.containsKey(player.getName())) {
				if (!quit)
					player.sendMessage(ChatColor.RED + "An error occurred while modifying starterchest! Has your name been changed?\n" + 
												   	   "Please try again!");
				return;
			}
			String id = PlayerInteractListener.isViewing.get(player.getName());
			StarterChestManager.getStorage(id).originalItemStacks = is;
			PlayerInteractListener.isViewing.remove(player.getName());
			
		} else if (inv.getName().equalsIgnoreCase("StarterChest")) {
			ItemStack[] is = inv.getContents();
			String id = PlayerInteractListener.isViewing.get(player.getName());
			StarterChestManager.getStorage(id).setItems(player, is);
			PlayerInteractListener.isViewing.remove(player.getName());
		} else return;
	}

}
