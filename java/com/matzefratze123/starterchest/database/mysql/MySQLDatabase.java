package com.matzefratze123.starterchest.database.mysql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import com.matzefratze123.starterchest.StarterChest;
import com.matzefratze123.starterchest.database.IDatabase;
import com.matzefratze123.starterchest.database.ItemStackParser;
import com.matzefratze123.starterchest.database.LocationParser;
import com.matzefratze123.starterchest.system.ChestStorage;
import com.matzefratze123.starterchest.system.StarterChestManager;

public class MySQLDatabase implements IDatabase {

	private Connection conn;
	private StarterChest t;
	
	public MySQLDatabase(StarterChest instance) {
		this.t = instance;
        createConnection();
    }
	
	private void createConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
	        
	        conn = DriverManager.getConnection("jdbc:mysql://" + StarterChest.dbHost + ":"
	                + StarterChest.dbPort + "/" + StarterChest.databaseName + "?" + "user="
	        		+ StarterChest.dbUser + "&" + "password=" + StarterChest.dbPassword);
	    } catch (ClassNotFoundException e) {
	        Bukkit.getLogger().severe("No drivers found for MySQL database! Changing to YAML!");
	        t.setYamlDatabase();
	        t.getPluginDatabase().load();
	    } catch (SQLException e) {
	        Bukkit.getLogger().severe("Could not connect to MySQL database! Bad username or password?");
	        Bukkit.getLogger().severe("Using YAML Database!");
	        t.setYamlDatabase();
	    }
	}
	
	private Connection getInstance() {
		try {
			if (conn == null || conn.isClosed()) {
				createConnection();
				System.out.println("Reopening connection...");
			}
			return conn;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	
	@Override
	public void load() {
		conn = getInstance();
		List<String> tables = new ArrayList<String>();
		
		try {
			//Indicate all table names...
			DatabaseMetaData metadata = conn.getMetaData();
			ResultSet result = metadata.getTables(null, null, null, new String[] {"TABLE"});
			
			while (result.next()) {
				String table = result.getString("TABLE_NAME");
				tables.add(table);
			}
			
			for (String table : tables) {
				if (!table.endsWith("_starterchest"))
					continue;
				//Important
				Location chestLoc = null;
				ItemStack[] originalStacks = null;
				HashMap<String, ItemStack[]> playerStacks = new HashMap<String, ItemStack[]>();
				
				Statement query = conn.createStatement();
				String sql = "SELECT * FROM " + table;
				ResultSet rs = query.executeQuery(sql);
				
				int counter = 0;
				while (rs.next()) {
					if (counter == 0) {
						String locationString = rs.getString("stacks_0");
						Location loc = LocationParser.convertStringtoLocation(locationString);
						chestLoc = loc;
					} else if (counter == 1) {
						if (!rs.getString("name").equalsIgnoreCase("originalStacks")) {
							Bukkit.getLogger().warning("No originalitems for the chest " + table + " found! ");
						}
						originalStacks = this.createItemStackArray(rs);
					} else {
						String player = rs.getString("name");
						ItemStack[] playerStack = this.createItemStackArray(rs);
						playerStacks.put(player, playerStack);
					}
					
					counter++;
				}
				StarterChestManager.addStorage(table.replace("_starterchest", ""), originalStacks, chestLoc);
				StarterChestManager.getStorage(table.replace("_starterchest", "")).currentItems.putAll(playerStacks);
			}
			
			Bukkit.getLogger().info("[StarterChest] Loaded " + tables.size() + " chests from database!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ItemStack[] createItemStackArray(ResultSet rs) throws SQLException {
		List<ItemStack> isList = new ArrayList<ItemStack>();
		for (int i = 0; i < 45; i++) {
			String currentIs = rs.getString("stacks_" + i);
			if (currentIs.equalsIgnoreCase("empty"))
				isList.add(null);
			else
				isList.add(ItemStackParser.convertStringtoItemStack(currentIs));
		}
		return isList.toArray(new ItemStack[isList.size()]);
	}
	
	@Override
	public void save() {
		conn = getInstance();
		Set<String> keySet = StarterChestManager.chestSaveMap.keySet();
		ChestStorage currentStorage;
		
		for (String id : keySet) {
			currentStorage = StarterChestManager.getStorage(id);
			ItemStack[] originalStacks = currentStorage.getOriginalItemStacks();
			Set<String> keySetPlayerMap = currentStorage.currentItems.keySet();
			
			String columnsCreate = this.buildColumnString(" TEXT");
			String columnsInsert = this.buildColumnString("");
			
			try {
				//Drop the table if it exists...
				Statement dropUpdate = this.createStatement();
				String dropTableSQL = "DROP TABLE IF EXISTS " + id + "_starterchest";
				dropUpdate.executeUpdate(dropTableSQL);
				
				//Create a new table...
				Statement createUpdate = this.createStatement();
				String createTableSQL = "CREATE TABLE " + id + "_starterchest" + " (name TEXT, " + columnsCreate + ");";
				createUpdate.executeUpdate(createTableSQL);
				
				//Insert the location of the chest...
				Statement locationInsert = this.createStatement();
				String locationString = LocationParser.convertLocationtoString(currentStorage.getChestLoc());
				String insertLocationSQL = "INSERT INTO " + id + "_starterchest" + " (stacks_0) VALUES ('" + locationString + "')";
				locationInsert.executeUpdate(insertLocationSQL);
				
				//Insert the originalStacks of the chest...
				Statement originalStacksInsert = this.createStatement();
				String originalStacksString = this.buildItemStackString(originalStacks);
				String insertoriginalStacksSQL = "INSERT INTO " + id + "_starterchest" + " (name, " + columnsInsert + ") VALUES ('originalStacks', " + originalStacksString + ")";
				originalStacksInsert.executeUpdate(insertoriginalStacksSQL);
				
				//Insert itemstacks for every registered player of the chest...
				for (String playerName : keySetPlayerMap) {
					//Insert playerstacks
					Statement playerStacksInsert = this.createStatement();
					String playerStacksString = this.buildItemStackString(currentStorage.currentItems.get(playerName));
					String insertplayerStackSQL = "INSERT INTO " + id + "_starterchest" + " (name, " + columnsInsert + ") VALUES ('" + playerName + "', " + playerStacksString + ")";
					playerStacksInsert.executeUpdate(insertplayerStackSQL);
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private String buildColumnString(String append) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < 45; i++) {
			String column = i == 0 ? "stacks_" + i + " " + append : ", stacks_" + i + " " + append;
			builder.append(column);
		}
		return builder.toString();
			
	}
	
	public Statement createStatement() throws SQLException {
		if (this.conn == null)
			new MySQLDatabase(t);
		return conn.createStatement();
	}
	
	private String buildItemStackString(ItemStack[] itemstacks) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < itemstacks.length; i++) {
			if (itemstacks[i] == null) {
				String isString = i == 0 ? "'empty'": ", 'empty'";
				builder.append(isString);
			} else {
				String rawisString = ItemStackParser.convertItemStacktoString(itemstacks[i]);
				String isString = i == 0 ? "'" + rawisString + "'": ", '" + rawisString + "'";
				builder.append(isString);
			}
		}
		return builder.toString();
	}

}
