package com.matzefratze123.starterchest.command.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;

import com.matzefratze123.starterchest.Permissions;
import com.matzefratze123.starterchest.command.AbstractCommand;
import com.matzefratze123.starterchest.command.CommandHandler;
import com.matzefratze123.starterchest.system.StarterChestManager;

public class ChestSetCommand extends AbstractCommand {

	@Override
	public boolean execute(Player player, String[] args) {
		if (!player.hasPermission(Permissions.CAN_SET_STARTERCHEST.getPerm())) {
			player.sendMessage(CommandHandler.getNoPermissionsMessage());
			return true;
		}
		if (args.length != 1)
			return false;
		Block block = player.getTargetBlock(null, 5);
		if (block == null || block.getState() == null) {
			player.sendMessage(ChatColor.RED + "Please look on a chest to define a starter-chest!");
			return true;
		}
		BlockState state = block.getState();
		if (state.getType() != Material.CHEST) {
			player.sendMessage(ChatColor.RED + "Please look on a chest to define a starter-chest!");
			return true;
		}
		if (StarterChestManager.hasChestWithId(args[0])) {
			player.sendMessage(ChatColor.RED + "There is already a chest with the given id!");
			return true;
		}
		Chest chest = (Chest) state;
		if (StarterChestManager.isStarterChest(chest)) {
			player.sendMessage(ChatColor.RED + "This is already a starterchest!");
			return true;
		}
		StarterChestManager.addStorage(args[0], state.getLocation());
		player.sendMessage(ChatColor.DARK_AQUA + "Chest successfully created! You can now fill the chest to set the starter contents for new users.");
		return true; 
	}

	@Override
	public String getUsage() {
		return "Usage: /sc set <ID>";
	}

}
