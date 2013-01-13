package com.matzefratze123.starterchest.command.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.matzefratze123.starterchest.Permissions;
import com.matzefratze123.starterchest.command.AbstractCommand;
import com.matzefratze123.starterchest.command.CommandHandler;
import com.matzefratze123.starterchest.system.ChestStorage;
import com.matzefratze123.starterchest.system.StarterChestManager;

public class ChestListCommand extends AbstractCommand {

	@Override
	public boolean execute(Player player, String[] args) {
		if (!player.hasPermission(Permissions.CAN_LIST_STARTERCHESTS.getPerm())) {
			player.sendMessage(CommandHandler.getNoPermissionsMessage());
			return true;
		}
		if (args.length != 0)
			return false;
		
		ChestStorage[] storages = StarterChestManager.getStorages();
		for (ChestStorage storage : storages) {
			Location chestLoc = storage.getChestLoc();
			player.sendMessage(ChatColor.DARK_AQUA + "ID: " + storage.getId() + "  Location: x: " + chestLoc.getBlockX() + " y: " + chestLoc.getBlockY() + " z: " + chestLoc.getBlockZ());
		}
		return true;
	}

	@Override
	public String getUsage() {
		return "Usage: /sc list";
	}

}
