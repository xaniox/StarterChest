package com.matzefratze123.starterchest.event;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.matzefratze123.starterchest.Permissions;
import com.matzefratze123.starterchest.system.StarterChestManager;

public class BlockBreakListener implements Listener {

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		if (e.isCancelled())
			return;
		
		Block block = e.getBlock();
		
		if (block.getType() != Material.CHEST)
			return;
		if (block.getState() == null)
			return;
		Chest chest = (Chest) block.getState();
		if (!StarterChestManager.isStarterChest(chest))
			return;
		e.setCancelled(true);
		if (!e.getPlayer().hasPermission(Permissions.HAS_ADMIN_PERMISSIONS.getPerm()))
			e.getPlayer().sendMessage(ChatColor.DARK_RED + "You are not permitted to destroy starterchests!");
		else
			e.getPlayer().sendMessage(ChatColor.RED + "You are going to destroy a starterchest. If you really wan't to do that, then look at the chest and type: /sc remove");
	}
	
}
