package com.matzefratze123.starterchest.database;

public interface IDatabase {

	/**
	 * Loads the contents of the Database into the HashMap's of the System
	 */
	public void load();
	
	/**
	 * Saves the contents of the HashMap's into the Database
	 */
	public void save();
	
}
