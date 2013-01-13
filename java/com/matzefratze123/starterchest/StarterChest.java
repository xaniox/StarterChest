package com.matzefratze123.starterchest;

import com.matzefratze123.starterchest.command.CommandHandler;
import com.matzefratze123.starterchest.database.IDatabase;
import com.matzefratze123.starterchest.database.mysql.MySQLDatabase;
import com.matzefratze123.starterchest.database.yml.YamlDatabase;
import com.matzefratze123.starterchest.event.BlockBreakListener;
import com.matzefratze123.starterchest.event.EntityExplodeListener;
import com.matzefratze123.starterchest.event.InventoryCloseListener;
import com.matzefratze123.starterchest.event.PlayerInteractListener;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class StarterChest extends JavaPlugin {

	public IDatabase db;
	public String databaseType;
	public ConfigManager configm;
	public PluginDescriptionFile pdf;
	public PluginManager pm;
	
	//Used for handling MySQL database
	public static String dbHost;
	public static String dbPort;
	public static String databaseName;
	public static String dbUser;
	public static String dbPassword;
	
	@Override
	public void onEnable() {
		
		pm = this.getServer().getPluginManager();
		pdf = this.getDescription();
		configm = new ConfigManager(this);
		
		this.setupDatabase();
		
		db.load();
		
		pm.registerEvents(new PlayerInteractListener(), this);
		pm.registerEvents(new InventoryCloseListener(), this);
		pm.registerEvents(new EntityExplodeListener(), this);
		pm.registerEvents(new BlockBreakListener(), this);
		
		this.getCommand("starterchest").setExecutor(new CommandHandler(this));
		
		this.getLogger().info(pdf.getName() + " v" + pdf.getVersion() + " activated!");
	}
	
	@Override
	public void onDisable() {
		PluginDescriptionFile pdf = this.getDescription();
		
		//Save stats to database
		db.save();
		this.getLogger().info(pdf.getName() + " v" + pdf.getVersion() + " deactivated!");
	}
	
	public IDatabase getPluginDatabase() {
		return this.db;
	}
	
	public void setYamlDatabase() {
		this.db = new YamlDatabase();
		this.databaseType = "yaml";
	}
	
	public void setMySQLDatabase() {
		this.db = new MySQLDatabase(this);
		this.databaseType = "mysql";
	}
	
	private void setupDatabase() {
		String dbType = configm.getDatabaseType();
		
		if (dbType.equalsIgnoreCase("yaml")) {
			setYamlDatabase();
		} else if (dbType.equalsIgnoreCase("mysql")) {
			
			dbHost = configm.getMySQLHost();
			dbPort = configm.getMySQLPort();
			databaseName = configm.getDatabase();
			dbUser = configm.getUsername();
			dbPassword = configm.getPassword();
			
			setMySQLDatabase();
			
		} else {
			Bukkit.getLogger().warning("Warning! No allowed database typ was found! The type " + configm.getDatabaseType() + " is not allowed!");
			Bukkit.getLogger().warning("Using YAML database...");
			setYamlDatabase();
		}
	}
}
