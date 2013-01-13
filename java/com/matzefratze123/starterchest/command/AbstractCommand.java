package com.matzefratze123.starterchest.command;

import org.bukkit.entity.Player;

public abstract class AbstractCommand {

	public abstract boolean execute(Player player, String[] args);

	public abstract String getUsage();
}
