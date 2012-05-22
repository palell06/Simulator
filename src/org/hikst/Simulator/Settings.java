package org.hikst.Simulator;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;

public class Settings {
	private Properties configFile;
	private File file;
	private static String db_hostname, db_port, db_db, db_user, db_pw;
	private static Connection dbc;
	
	/*
	 * Constructors
	 */
	public Settings(){
		this("Simulator.properties");
	}
	
	public Settings(String $filename){
		configFile = new Properties();
		file = new File($filename);
		
		load();
	}
	
	private void writeDefaultConfig(){
		configFile.setProperty("DB_HOSTNAME", "");
		configFile.setProperty("DB_PORT", "");
		configFile.setProperty("DB_DB", "");
		configFile.setProperty("DB_USER", "");
		configFile.setProperty("DB_PW", "");
		
		try {
			configFile.store(new FileWriter(file),null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void load(){
		try {
			configFile.load(new FileReader(file));
			
			db_hostname = configFile.getProperty("DB_HOSTNAME");
			db_port = configFile.getProperty("DB_PORT");
			db_db = configFile.getProperty("DB_DB");
			db_user = configFile.getProperty("DB_USER");
			db_pw = configFile.getProperty("DB_PW");
		} catch (IOException e) {
			// TODO: �nsker vi � kreve brukerinput i denne klassen?
			System.err.println("Unable to open " + file.getAbsolutePath() + ".\nCreate default config at this location? [y/n]");
			Scanner scan = new Scanner(System.in);
			if(scan.nextLine().equalsIgnoreCase("y")){
				writeDefaultConfig();
			}
			System.err.println("Retry loading config? [y/n]");
			if(scan.nextLine().equalsIgnoreCase("y")){
				load();
			} else {
				System.exit(0);
			}
		}
	}
	
	/*
	 * Did anyone need a database connection???
	 * This lets the application use the database connection everywhere.
	 */
	public static Connection getDBC(){
		if (openDatabaseConnection()){
			return dbc;
		}
		return null;
	}
	
	private static boolean openDatabaseConnection(){
		try {
			if (!dbc.isClosed()){
				return true;
			}
		} catch (SQLException e1) {
			// This is not unexpected and will always fail before the dbc is created.
			//Please just continue!
		}
		
		try {
			dbc = DriverManager.getConnection("jdbc:postgresql://" + db_hostname + ":" + db_port + "/" + db_db,db_user,db_pw);
			return !dbc.isClosed();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/*
	 * SimulatorID is the ID given to the simulator instance by the database
	 * The setter will only be used by the AliveMessenger after the first reporting.
	 */
	public int getSimulatorID(){
		return Integer.parseInt(configFile.getProperty("simulator_id"));
	}
	
	public void setSimulatorID(int id){
		configFile.setProperty("simulator_id", Integer.toString(id));
		try {
			configFile.store(new FileWriter(file),null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
