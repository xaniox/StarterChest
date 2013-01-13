package com.matzefratze123.starterchest.event;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.matzefratze123.starterchest.Permissions;
import com.matzefratze123.starterchest.system.StarterChestManager;

public class PlayerInteractListener implements Listener {
	
	public static HashMap<String, String> isViewing = new HashMap<String, String>();
	public PlayerInteractListener() {} 
	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerInteract(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		if (e.getClickedBlock() == null || e.getClickedBlock().getState() == null)
			return;
		Block block = e.getClickedBlock();
		BlockState state = block.getState();
		if (state.getType() != Material.CHEST)
			return;
		Chest chest = (Chest) state;
		if (e.getAction() != Action.RIGHT_CLICK_BLOCK) 
			return;
		if (!StarterChestManager.isStarterChest(chest))
			return;
		if (player.isOp() || player.hasPermission(Permissions.HAS_ADMIN_PERMISSIONS.getPerm())) {
			//Handle Admin
			e.setCancelled(true);
			Inventory inv = Bukkit.getServer().createInventory((HumanEntity)player, 45, "StarterChest Admin");
			ItemStack[] is = StarterChestManager.getStorageByChest(chest).getOriginalItemStacks();
			if (is != null)
				inv.setContents(is);
			player.openInventory(inv);
			isViewing.put(player.getName(), StarterChestManager.getIdFromChest(chest));
			player.getWorld().playSound(player.getLocation(), Sound.PORTAL, 4.0F, player.getLocation().getPitch());
			for (int i = 0; i <= 4; i++) {
				player.getWorld().playEffect(block.getLocation(), Effect.ENDER_SIGNAL, 4);
			}
		} else if (player.hasPermission(Permissions.CAN_USE_STARTERCHEST.getPerm())) {
			//Handle User
			e.setCancelled(true);
			Inventory inv = Bukkit.getServer().createInventory((HumanEntity)player, 45, "StarterChest");
			ItemStack[] is = new ItemStack[StarterChestManager.getStorageByChest(chest).getOriginalItemStacks().length];
			if (!StarterChestManager.getStorageByChest(chest).currentItems.containsKey(player.getName()))
				is = StarterChestManager.getStorageByChest(chest).getOriginalItemStacks().clone();
			else
				is = StarterChestManager.getStorageByChest(chest).currentItems.get(player.getName());
			inv.setContents(is);
			player.openInventory(inv);
			isViewing.put(player.getName(), StarterChestManager.getIdFromChest(chest));
			player.getWorld().playSound(player.getLocation(), Sound.PORTAL, 4.0F, player.getLocation().getPitch());
			for (int i = 0; i <= 4; i++) {
				player.getWorld().playEffect(block.getLocation(), Effect.ENDER_SIGNAL, 4);
			}
		} else {
			//Handle no permission
			player.sendMessage(ChatColor.RED + "You are not allowed to use starterchests!");
			e.setCancelled(true);
		}
		return;
	}
}
