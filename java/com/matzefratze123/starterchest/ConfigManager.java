package com.matzefratze123.starterchest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

public class ConfigManager {

	private StarterChest t;
	public ConfigManager(StarterChest instance) {
		t = instance;
		final File dataFolder = t.getDataFolder();
		dataFolder.mkdirs();
		final File config = new File(dataFolder, "config.yml");
		if (!config.exists()) {
			this.t.getLogger().info("Can't find config.yml! Creating a new file...");
			this.createDefaultConfig(config);
		}
	}
	
	private void createDefaultConfig(File configFile) {
		try {
            configFile.createNewFile();
            final InputStream configIn = StarterChest.class.getResourceAsStream("/configdefault.yml");
            final FileOutputStream configOut = new FileOutputStream(configFile);
            final byte[] buffer = new byte[1024];
            int read;
            while ((read = configIn.read(buffer)) > 0) {
                configOut.write(buffer, 0, read);
            }
            configIn.close();
            configOut.close();
            this.t.getLogger().log(Level.INFO, "Config file successfully created!");
        } catch (final IOException ioe) {
            this.t.getLogger().log(Level.WARNING, "Can't create config file! IOException!", ioe);
            return;
        } catch (final NullPointerException npe) { // getResourceAsStream returns null under certain reload conditions (reloaded updated plugin)
            this.t.getLogger().log(Level.SEVERE, "Can't write config.yml! NullPointerException! (Bukkit bug?) Deleting file...");
            configFile.delete();
        }
	}
	
	public String getDatabaseType() {
		return t.getConfig().getString("database.type");
	}
	
	public String getMySQLHost() {
		return t.getConfig().getString("database.host");
	}
	
	public String getMySQLPort() {
		return t.getConfig().getString("database.port");
	}
	
	public String getDatabase() {
		return t.getConfig().getString("database.databasename");
	}
	
	public String getUsername() {
		return t.getConfig().getString("database.username");
	}
	
	public String getPassword() {
		return t.getConfig().getString("database.password");
	}
}
