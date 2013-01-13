package com.matzefratze123.starterchest.command;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.matzefratze123.starterchest.Permissions;
import com.matzefratze123.starterchest.StarterChest;
import com.matzefratze123.starterchest.command.commands.ChestInfoCommand;
import com.matzefratze123.starterchest.command.commands.ChestListCommand;
import com.matzefratze123.starterchest.command.commands.ChestRemoveCommand;
import com.matzefratze123.starterchest.command.commands.ChestSetCommand;

public class CommandHandler implements CommandExecutor {

	public StarterChest t;
	public static HashMap<String, AbstractCommand> commands = new HashMap<String, AbstractCommand>();
	
	public CommandHandler(StarterChest instance) {
		t = instance;
		setupCommands();
	}
	
	private void setupCommands() {
		commands.put("set", new ChestSetCommand());
		commands.put("remove", new ChestRemoveCommand());
		commands.put("list", new ChestListCommand());
		commands.put("info", new ChestInfoCommand());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("[StarterChest] You must be a player to execute this command!");
			return true;
		}
		Player player = (Player) sender;
		
		if (args.length == 0) {
			player.sendMessage(ChatColor.GRAY + this.t.getDescription().getName() + " version " + ChatColor.DARK_GRAY + this.t.getDescription().getVersion() + ChatColor.GRAY + " enabled.");
			player.sendMessage(ChatColor.GRAY + this.t.getDescription().getDescription());
			return true;
		}
		
		if (args.length >= 1) {
			if (CommandHandler.commands.containsKey(args[0].toLowerCase())) {
				Vector<String> cutArgs = new Vector<String>();
				cutArgs.addAll(Arrays.asList(args));
				cutArgs.remove(0);
				
				AbstractCommand gsCommand = (AbstractCommand) CommandHandler.commands.get(args[0].toLowerCase());
				
				if (!gsCommand.execute(player, (String[]) cutArgs.toArray(new String[0]))) {
					player.sendMessage(ChatColor.RED + gsCommand.getUsage());
					return true;
				}
				return true;
			} else {
				if (!player.hasPermission(Permissions.CAN_SHOW_HELP.getPerm())) {
					player.sendMessage(getNoPermissionsMessage());
					return true;
				}
				showHelp(player);
				return true;
			}
		}
		return true;
	}
	
	private void showHelp(Player player) {
		player.sendMessage(ChatColor.DARK_AQUA + "/sc set <ID> --> Defines a new starterchest on a chest you are looking");
		player.sendMessage(ChatColor.DARK_AQUA + "/sc remove [ID] --> Removes a starterchest with the id. If there is no given id you are removing the starterchest you are looking.");
		player.sendMessage(ChatColor.DARK_AQUA + "/sc list --> List all starterchests.");
		player.sendMessage(ChatColor.DARK_AQUA + "/sc info --> Displays information about the starterchest you are looking.");
		player.sendMessage(ChatColor.DARK_AQUA + "/sc help --> Displays this help.");
	}
	
	public static String getNoPermissionsMessage() {
		return ChatColor.DARK_RED + "You don't have permission!";
	}

	public static HashMap<String, AbstractCommand> getGSMap() {
		return commands;
	}

}
