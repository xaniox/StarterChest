package com.matzefratze123.starterchest.command.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;

import com.matzefratze123.starterchest.Permissions;
import com.matzefratze123.starterchest.command.AbstractCommand;
import com.matzefratze123.starterchest.command.CommandHandler;
import com.matzefratze123.starterchest.system.StarterChestManager;

public class ChestRemoveCommand extends AbstractCommand {

	@Override
	public boolean execute(Player player, String[] args) {
		if (!player.hasPermission(Permissions.CAN_REMOVE_STARTERCHEST.getPerm())) {
			player.sendMessage(CommandHandler.getNoPermissionsMessage());
			return true;
		}
		if (args.length == 1) { 
			if (!StarterChestManager.hasChestWithId(args[0])) {
				player.sendMessage(ChatColor.RED + "There is no chest with this id!");
				return true;
			}
			
			StarterChestManager.getStorage(args[0]).getChestLoc().getBlock().setType(Material.AIR);
			StarterChestManager.removeStorage(args[0]);
			player.sendMessage(ChatColor.DARK_AQUA + "The starter-chest with the id \"" + args[0].toLowerCase() + "\" has been removed!");
			return true;
		} else if (args.length == 0) {
			Block block = player.getTargetBlock(null, 5);
			if (block == null || block.getState() == null) {
				player.sendMessage(ChatColor.RED + "Please look on a chest to remove a starter-chest!");
				return true;
			}
			if (block.getState().getType() != Material.CHEST) {
				player.sendMessage(ChatColor.RED + "Please look on a chest to remove a starter-chest!");
				return true;
			}
			Chest chest = (Chest)block.getState();
			if (!StarterChestManager.isStarterChest(chest)) {
				player.sendMessage(ChatColor.RED + "This chest isn't a starter chest!");
				return true;
			}
			String id = StarterChestManager.getIdFromChest(chest);
			StarterChestManager.getStorage(id).getChestLoc().getBlock().setType(Material.AIR);
			StarterChestManager.removeStorage(id);
			player.sendMessage(ChatColor.DARK_AQUA + "The chest with the id \"" + id + "\" has been removed!");
		} else return false;
		return true;
	}

	@Override
	public String getUsage() {
		return "Usage: /sc remove (ID)";
	}

}