package com.matzefratze123.starterchest.command.commands;

import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;

import com.matzefratze123.starterchest.Permissions;
import com.matzefratze123.starterchest.command.AbstractCommand;
import com.matzefratze123.starterchest.command.CommandHandler;
import com.matzefratze123.starterchest.system.ChestStorage;
import com.matzefratze123.starterchest.system.StarterChestManager;

public class ChestInfoCommand extends AbstractCommand {

	@Override
	public boolean execute(Player player, String[] args) {
		if (!player.hasPermission(Permissions.CAN_GET_INFO_ABOUT_STARTERCHEST.getPerm())) {
			player.sendMessage(CommandHandler.getNoPermissionsMessage());
			return true;
		}
		if (args.length == 1) { 
			if (!StarterChestManager.hasChestWithId(args[0])) {
				player.sendMessage(ChatColor.RED + "There is no chest with this id!");
				return true;
			}
			
			ChestStorage storage = StarterChestManager.getStorage(args[0]);
			this.sendInformation(storage, player);
			return true;
		} else if (args.length == 0) {
			Block block = player.getTargetBlock(null, 5);
			if (block == null || block.getState() == null) {
				player.sendMessage(ChatColor.RED + "Please look on a starter-chest!");
				return true;
			}
			if (block.getState().getType() != Material.CHEST) {
				player.sendMessage(ChatColor.RED + "Please look on a starter-chest!");
				return true;
			}
			Chest chest = (Chest)block.getState();
			if (!StarterChestManager.isStarterChest(chest)) {
				player.sendMessage(ChatColor.RED + "This chest isn't a starter chest!");
				return true;
			}
			String id = StarterChestManager.getIdFromChest(chest);
			ChestStorage storage = StarterChestManager.getStorage(id);
			this.sendInformation(storage, player);
		} else return false;
		return true;
	}

	@Override
	public String getUsage() {
		return "Usage: /sc info [ID] or /sc info";
	}
	
	private void sendInformation(ChestStorage storage, Player player) {
		player.sendMessage(ChatColor.DARK_AQUA + "ID: " + storage.getId());
		player.sendMessage(ChatColor.DARK_AQUA + "Location: x: " + storage.getChestLoc().getBlockX() + " y: " + storage.getChestLoc().getBlockY() + " z: " + storage.getChestLoc().getBlockZ());
		StringBuilder userBuilder = new StringBuilder();
		Set<String> usedPlayers = storage.currentItems.keySet();
		for (String playerName : usedPlayers) {
			userBuilder.append(playerName).append(" ");
		}
		player.sendMessage(ChatColor.DARK_AQUA + "Players who had used this chest: " + userBuilder.toString());
	}

}
