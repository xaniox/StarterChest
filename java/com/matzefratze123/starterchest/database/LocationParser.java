package com.matzefratze123.starterchest.database;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * Methods to parse Locations to Strings and back
 * 
 * @author matzefratze123
 */
public class LocationParser {

	public static String convertLocationtoString(Location location) {
		int x = location.getBlockX();
		int y = location.getBlockY();
		int z = location.getBlockZ();
		String world = location.getWorld().getName();
		
		String s = world + "," + x + "," + y + "," + z;
		return s;
	}
	
	public static Location convertStringtoLocation(String s) {
		String[] split = s.split(",", -3);
		
		int x = Integer.parseInt(split[1]);
		int y = Integer.parseInt(split[2]);
		int z = Integer.parseInt(split[3]);
		World world = Bukkit.getWorld(split[0]);
		
		return new Location(world, x, y, z);
	}
	
}
