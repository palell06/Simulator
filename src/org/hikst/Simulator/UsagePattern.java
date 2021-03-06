package org.hikst.Simulator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

import javax.swing.text.DateFormatter;

public class UsagePattern {
	private int id;
	private String name;
	private int[] pattern = new int[24];
	private boolean actual;
	private Calendar calendar;
	
	public UsagePattern(int id) throws UsagePatternNotFoundException {
		
		calendar = new GregorianCalendar();
		
		String query = "SELECT * FROM Usage_Pattern WHERE ID=?; ";
		PreparedStatement statement;
		try {
			statement = Settings.getDBC().prepareStatement(query);
			statement.setInt(1, id);
			ResultSet set = statement.executeQuery();

			DecimalFormat nft = new DecimalFormat("#00.###");
			nft.setDecimalSeparatorAlwaysShown(false);

			if (set.next()) {
				this.id = set.getInt("id");
				this.name = set.getString("name");
				// Fill the array with the pattern
				for (int i = 0; i <= 23; i++) {
					this.pattern[i] = set.getInt("c" + nft.format(i));
				}
				this.actual = set.getBoolean("actual");

				return;
			} else {
				throw new UsagePatternNotFoundException();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getProbability(Date time) {
		
		calendar.setTime(time);
		int hour  = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		
		int thisPattern = pattern[hour];

		int nextPattern;
		if (hour < 23)
			nextPattern = pattern[hour + 1];
		else
			nextPattern = pattern[0];

		// Modifying thisPattern to take account for minutes into the hour
		double percentageOfNextPattern = (double) minute / 60.0;
		thisPattern = thisPattern + (int) ((double) (nextPattern - thisPattern) * (double) percentageOfNextPattern);

		if (actual) {
			// If the pattern is an actual use percentage
			return thisPattern;
		} else {
			// If the pattern is a possibility of an device being on
			Random r = new Random();
			if (thisPattern >= r.nextInt(101)) // 101 since 100 is a possibility
				// The device is on
				return 100;
			else
				// The device is off
				return 0;
			
		}
	}

}
