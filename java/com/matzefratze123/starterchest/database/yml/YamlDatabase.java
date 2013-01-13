package com.matzefratze123.starterchest.database.yml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import com.matzefratze123.starterchest.StarterChest;
import com.matzefratze123.starterchest.database.IDatabase;
import com.matzefratze123.starterchest.database.ItemStackParser;
import com.matzefratze123.starterchest.database.LocationParser;
import com.matzefratze123.starterchest.system.ChestStorage;
import com.matzefratze123.starterchest.system.StarterChestManager;

public class YamlDatabase implements IDatabase {

	protected File[] databases = new File("plugins/StarterChest/Database").listFiles();
	
	public YamlDatabase() {
		File databaseDirectory = new File("plugins/StarterChest/Database");
		databaseDirectory.mkdirs();
	}
	
	@Override
	public void load() {
		if (databases.length == 0)
			return;
		for (File database : this.databases) {
			if (!database.getName().endsWith(".yml"))
				return;
			FileConfiguration yamldb = YamlConfiguration.loadConfiguration(database);
			//Important
			Location location = LocationParser.convertStringtoLocation(yamldb.getString("location"));
			String id = database.getName().replace(".yml", "");
			//End Important
			List<String> originalStacks = yamldb.getStringList("originalItems.items");
			List<ItemStack> listOfOriginalItemStacks = new ArrayList<ItemStack>();
			
			//For every Original ItemStack
			for (String s : originalStacks) {
				if (s.equalsIgnoreCase("empty"))
					listOfOriginalItemStacks.add(null);
				else
					listOfOriginalItemStacks.add(ItemStackParser.convertStringtoItemStack(s));
			}
			HashMap<String, ItemStack[]> currentItems = new HashMap<String, ItemStack[]>();//Important
			ConfigurationSection section = yamldb.getConfigurationSection("players");
			if (section == null) {
				StarterChestManager.addStorage(id, listOfOriginalItemStacks.toArray(new ItemStack[listOfOriginalItemStacks.size()]), location);
				continue;
			}
			Set<String> playerSectionKeys = section.getKeys(Boolean.valueOf(false));
			//For every PlayerString
			for (String currentKey : playerSectionKeys) {
				List<String> itemstacksAsString = yamldb.getStringList("players." + currentKey + ".items");
				List<ItemStack> itemstacklist = new ArrayList<ItemStack>();
				//For every ItemStack of Player
				for (String itemstack : itemstacksAsString)
					if (itemstack.equalsIgnoreCase("empty")) {
						itemstacklist.add(null);
					} else {
						itemstacklist.add(ItemStackParser.convertStringtoItemStack(itemstack));
					}
				currentItems.put(currentKey, itemstacklist.toArray(new ItemStack[itemstacklist.size()]));
			}
			
			StarterChestManager.addStorage(id, listOfOriginalItemStacks.toArray(new ItemStack[listOfOriginalItemStacks.size()]), location);
			StarterChestManager.getStorage(id).currentItems.putAll(currentItems);
			
		}
	}

	@Override
	public void save() {
		Set<String> keySet = StarterChestManager.chestSaveMap.keySet();
		ChestStorage currentStorage;
		
		for (String id : keySet) {
			currentStorage = StarterChestManager.chestSaveMap.get(id);
			ItemStack[] originalStacks = currentStorage.getOriginalItemStacks();
			Set<String> keySetPlayerItemStacks = currentStorage.currentItems.keySet();
			
			File databaseFile = new File("plugins/StarterChest/Database/" + id + ".yml");
			if (!databaseFile.exists())
				try {
					databaseFile.createNewFile();
					createDatabaseTemplate(databaseFile);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			//Define Database of type FileConfiguration
			FileConfiguration yamldb = YamlConfiguration.loadConfiguration(databaseFile);
			//Set Original ItemStacks as ArrayList
			ArrayList<String> isList = new ArrayList<String>();
			for (ItemStack originalIs : originalStacks)
				if (originalIs == null)
					isList.add("empty");
				else
					isList.add(ItemStackParser.convertItemStacktoString(originalIs));
			yamldb.set("originalItems.items", isList);
			//Finish setting original itemstacks
			
			for (String playerSection : keySetPlayerItemStacks) {
				ArrayList<String> isListPlayer = new ArrayList<String>();
				for (ItemStack playerItemStack : currentStorage.currentItems.get(playerSection))
					if (playerItemStack == null) {
						isListPlayer.add("empty");
					} else {
						isListPlayer.add(ItemStackParser.convertItemStacktoString(playerItemStack));
					}
				yamldb.set("players." + playerSection + ".items", isListPlayer);
			}
			yamldb.set("location", LocationParser.convertLocationtoString(currentStorage.getChestLoc()));
			
			try {
				yamldb.save(databaseFile);
			} catch (IOException e) {
				Bukkit.getLogger().severe("[StarterChest] ERROR! Failed to save stats for StarterChest! IOExcpetion occured?");
				e.printStackTrace();
			}
			
		}
		
		//Finished saving itemstacks to database
		Bukkit.getLogger().info("StarterChest stats successfully saved to YAML database!");
	}

	public void createDatabaseTemplate(File file) {
		try {
			if (!file.exists())
				file.createNewFile();
			final InputStream templateIn = StarterChest.class.getResourceAsStream("/databasetemplate.yml");
            final FileOutputStream templateOut = new FileOutputStream(file);
            final byte[] buffer = new byte[1024];
            int read;
            while ((read = templateIn.read(buffer)) > 0) {
                templateOut.write(buffer, 0, read);
            }
            templateIn.close();
            templateOut.close();
		} catch (IOException e) {
			Bukkit.getLogger().info("An IOException occured while creating a database file!");
			e.printStackTrace();
		}
	}

}
