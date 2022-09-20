package project.system;

import java.sql.*;
import java.util.*;
import java.io.*;

// Class for all admin functionality level
class Admin extends main {

	// Column names of reading tables
	static String[] tempCols = { "Temperature", "Precipitation", "Humidity", "Wind" };
	static String[] snowCols = { "Density", "Depth", "Melting temperature" };
	static String[] gasCols = { "Methane, CarbonDioxide, GreenHouseGas, Hydrofluorocarbons, NitrousOxide" };
	static String[] aqiCols = { "Index_Value", "AQI_Level", "Main_Pollutant" };
	boolean exit = false;
	static main mainClass = new main();

	// Method for admin functions to be perform
	public void adminFunctionality() {
		try {
			exit = false;
			while (!exit) {
				System.out.println("\nWhat would you like to do?\n");
				System.out.println("1. Readings related operation\n2. Station related operation\n3. Exit");
				System.out.print("Select from above operation: ");
				int operation;
				operation = Integer.parseInt(main.keyboard.readLine());
				switch (operation) {
					case 1:
						mainClass.readingsFunctionality(); // Calling method related to readings
						exit = false;
						break;
					case 2:
						mainClass.stationsFunctionality(); // Calling method related to stations
						exit = false;
						exit = false;
						break;
					case 3:
						exit = true;
						break;
					default:
						System.out.println("Please select valid operation. (Only chars 0-9 are allowed).\n");
						break;
				}
			}
		} catch (Exception e) {
			System.out.println("Caught Exception: \n     " + e);
			return;
		}

	}

}

// Class for user functionality level
class User extends Admin {

	boolean exit = false;

	// Method for user functions to be perform
	public void UserFunctionality() {
		try {
			exit = false;
			while (!exit) {
				System.out.println("\nWhat would you like to do?\n");
				System.out.println("1. Readings related operation\n2. Station related operation\n3. Exit");
				System.out.print("Select from above operation: ");
				int operation;
				operation = Integer.parseInt(main.keyboard.readLine());
				switch (operation) {
					case 1:
						readingsFunctionality(); // Calling method related to readings
						exit = false;
						break;
					case 2:
						stationsFunctionality(); // Calling method related to stations
						exit = false;
						exit = false;
						break;
					case 3:
						exit = true;
						break;
					default:
						System.out.println("Please select valid operation. (Only chars 0-9 are allowed).\n");
						break;
				}
			}
		} catch (Exception e) {
			System.out.println("Caught Exception: \n     " + e);
			return;
		}

	}

}

class main {

	static BufferedReader keyboard;
	static Connection conn;
	static Statement stmt;
	static String endUser;
	static int access_level = -1;
	static Admin admin = new Admin();
	static User user = new User();
	boolean exit = false;

	// Set the End-user
	private static void endUser() {
		try {
			while (true) {
				// Reading End-user from user
				System.out.println("As who you want to access database?");
				System.out.println(
						"- Danny: Admin Level Grant\n- Thomas: User Level Grants\n- Exit: To exit from program");
				System.out.print("Enter your username: ");
				endUser = keyboard.readLine();
				if (endUser.toLowerCase().equals("danny")) {
					access_level = 0;
					System.out.println("\n---------Your logged in as an admin---------\n");
					admin.adminFunctionality();
					break;

				} else if (endUser.toLowerCase().equals("thomas")) {
					access_level = 1;
					System.out.println("\n---------Your logged in as a normal user---------\n");
					user.UserFunctionality();
					break;
				} else if (endUser.toLowerCase().equals("exit")) {
					conn.close();
					break;
				} else {
					System.out.println("Invalid username. Try again.");
				}
			}
			return;
		}

		catch (Exception e) {
			System.out.println("Caught Exception: \n     " + e);
			return;
		}
	}

	// -------------------------Reading Functionality------------------------- //

	// Method for reading functionality
	void readingsFunctionality() {
		try {
			exit = false;
			int reading_id;
			switch (access_level) {

				// Admin Level
				case 0:
					while (!exit) {
						System.out.println("\nWhat would you like to do in Readings?");
						System.out.println(
								"1. Add Readings\n2. Update Readings\n3. View Readings\n4. Delete Reading\n5. Exit");
						System.out.print("Select from above operation (1-5): ");
						int operation;
						operation = Integer.parseInt(keyboard.readLine());

						// Mapping values of readings to associated readings
						Map<String, Map<String, Object>> readings = new HashMap<String, Map<String, Object>>();
						String location = "";
						int reading = 0;
						switch (operation) {

							// Insert
							case 1:
								System.out.println("Enter reading values ->");
								System.out.println("Enter Location: ");
								location = keyboard.readLine();

								// Calling Temperature readings method reading method to take values from user
								System.out.println("\nTemperature readings: ");
								readings.put("temp", TemperatureReading());

								// Calling snow readings method reading method to take values from user
								System.out.println("\nSnow readings: ");
								readings.put("snow", snowReading());

								// Calling gasemission readings method reading method to take values from user
								System.out.println("\nGasEmission readings: ");
								readings.put("gas", gasReading());

								// Calling aqi readings method reading method to take values from user
								System.out.println("\nAQI readings: ");
								readings.put("aqi", aqiReading());

								// Inserting data into tables
								insertReading(location, readings);
								break;

							// Update
							case 2:
								System.out.println("\nWhich reading you want change?");
								System.out.println("\t1. Temperature\n\t2. Snow\n\t3. GasEmission\n\t4. AQI\n\t5. Exit");
								System.out.print("Select reading (1-5): ");
								reading = Integer.parseInt(keyboard.readLine());

								// Displaying columns of table
								printUpdateReadingsColumns(reading);
								System.out.print("\tSelect reading value (From printed list number): ");
								int reading_val = Integer.parseInt(keyboard.readLine());
								String[] updateData = getUpdateData(reading, reading_val);
								System.out.print("\tEnter new Value (associated value type from above given list): ");
								String temp = keyboard.readLine();
								int updated_value = 0;
								System.out.print("\tEnter reading Id (1-10): ");
								reading_id = Integer.parseInt(keyboard.readLine());
								if (Integer.class.isInstance(temp)) {
									updated_value = Integer.parseInt(temp);
									temp = "";
								}

								// Updating the value of particular reading with new value
								updateReading(updateData, ((updated_value !=0) ? updated_value : temp ), reading_id);
								break;

							// Select
							case 3:
								reading = 0;
								System.out.println("\nWhich reading you want view?");
								System.out
										.println("\t1. Temperature\n\t2. Snow\n\t3. GasEmission\n\t4. AQI\n\t5. Exit");
								System.out.print("\n\tSelect reading (1-5): ");
								reading = Integer.parseInt(keyboard.readLine());

								if (reading == 5) {
									exit = true;
									return;
								}

								System.out.println("\nChoose filter for reading you want view?");
								System.out.println("\n\t1. By city\n\t2. By Date\n\t3. Exit");
								System.out.print("\n\tChoose filter(1-3): ");
								int filter = Integer.parseInt(keyboard.readLine());
								switch (filter) {
									case 1:
										System.out.print("\n\tSelect location (ex. Chicago, San Jose or New York): ");
										location = keyboard.readLine();
										break;
									case 2:
										System.out.print("\n\tSelect date (ex. 10): ");
										location = keyboard.readLine();
										break;
								}

								// Selecting readings related to reading name
								selectReading(reading, filter, location);
								break;
							case 4:
								System.out.println("\nWhich reading you want delete?");
								System.out.println("\t1. Temperature\n\t2. Snow\n\t3. GasEmission\n\t4. AQI");
								System.out.print("Select reading (1-4): ");
								reading_id = Integer.parseInt(keyboard.readLine());
								System.out.print("Enter reading Id (1-10): ");
								int delete_id = Integer.parseInt(keyboard.readLine());
								deleteReading(reading_id, delete_id);
								break;
							case 5:
								conn.close();
								exit = true;
								break;
							default:
								System.out.println("Select valid reading.");
								break;

						}
					}
					break;
				
				// User level
					case 1:
					while (!exit) {
						System.out.println("What would you like to do in Readings?");
						System.out.println("1. View Readings\n2. Exit");
						System.out.print("Select from above operation (1-2): ");
						int operation;
						operation = Integer.parseInt(keyboard.readLine());

						String location = "";
						int reading = 0;
						switch (operation) {

							// Select readings
							case 1:
								reading = 0;
								System.out.println("\nWhich reading you want view?");
								System.out.println("\t1. Temperature\n\t2. Snow\n\t3. GasEmission\n\t4. AQI\n5. Exit");
								System.out.print("Select reading (1-5): ");
								reading = Integer.parseInt(keyboard.readLine());
								System.out.print("Select location (ex. Chicago, San Jose or New York): ");
								location = keyboard.readLine();
								if (reading == 5) {
									exit = true;
									return;
								}
								// Selecting readings related to reading name
								// selectReading(reading, location);
								break;
							case 2:
								conn.close();
								exit = true;
								break;
							default:
								System.out.println("Select valid reading.");
								break;

						}
					}

					break;
				default:
					System.out.println("Something went wrong. Please try again.");
					endUser();
					break;
			}
		} catch (Exception e) {
			System.out.println("Caught Exception: \n     " + e);
		}
	}

	// Insert reading data
	void insertReading(String loc, Map<String, Map<String, Object>> readings) {
		try {
			stmt = conn.createStatement();
			int rowId = 0;
			String get_last_id_query = "SELECT READING# FROM READINGS WHERE ROWNUM = 1 ORDER BY READING# DESC";
			ResultSet rset_id = stmt.executeQuery(get_last_id_query);
			while (rset_id.next()) {
				rowId = Integer.parseInt(rset_id.getString(1)) + 1;
			}
			rset_id.close();
			String query = "DECLARE " +
					"CURR_TIME TIMESTAMP := SYSDATE;" +
					" BEGIN " +

					// First Inserting timestamp
					"INSERT INTO DIMDATE VALUES (CURR_TIME); " +

					// Inserting data in readings table
					"INSERT INTO READINGS VALUES (" + rowId + "," + "1, CURR_TIME); " +
					
					// Inserting data into temperature table
					"INSERT INTO temperature VALUES (" + rowId + "," + readings.get("temp").get("_temp") + "," +
					readings.get("temp").get("precip") + "," + readings.get("temp").get("humidity") + ","
					+ readings.get("temp").get("wind") + "); " +
					
					// Inserting data into snow table
					"INSERT INTO snow VALUES (" + rowId + "," + readings.get("snow").get("density") + "," +
					readings.get("snow").get("depth") + "," + readings.get("snow").get("melt_temp") + "); " +
					
					// Inserting data into gasemission table
					"INSERT INTO gasemission VALUES (" + rowId + "," + readings.get("gas").get("methane") + "," +
					readings.get("gas").get("co2") + "," + readings.get("gas").get("green") + ","
					+ readings.get("gas").get("hydro") + "," + readings.get("gas").get("no") + "); " +
					
					// Inserting data into aqi
					"INSERT INTO aqi VALUES (" + rowId + "," + readings.get("aqi").get("index") + ",'" +
					readings.get("aqi").get("level") + "','" + readings.get("aqi").get("pollutant") + "'); " +
					"END;";

			// Buidling the statement
			stmt = conn.createStatement();

			// Executing query
			int rset = stmt.executeUpdate(query);
			conn.commit();
			if (rset > 0) {
				System.out.println("Data inserted successfully!");
			} else {
				System.out.println("Something went wrong.");
			}

		}
		// Handling the exception on entering wrong column name
		catch (SQLSyntaxErrorException e) {
			System.out.println("Column name is not correct. Please try again.");
			return;
		}

		catch (Exception e) {
			System.out.println("Caught Exception: \n     " + e);
			return;
		}

	}

	// Delete reading data
	void deleteReading(int reading_id, int delete_id) {
		try {
			int rset = 0;
			String query;
			switch (reading_id) {
				case 1:
					stmt = conn.createStatement();
					query = "DELETE FROM TEMPERATURE WHERE READING#=" + delete_id;
					rset = stmt.executeUpdate(query);
					conn.commit();
					if (rset > 0) {
						System.out.println("Data deleted successfully!");
					} else {
						System.out.println("Something went wrong.");
					}
					break;
				case 2:
					stmt = conn.createStatement();
					query = "DELETE FROM SNOW WHERE READING#=" + delete_id;
					rset = stmt.executeUpdate(query);
					conn.commit();
					if (rset > 0) {
						System.out.println("Data deleted successfully!");
					} else {
						System.out.println("Something went wrong.");
					}
					break;
				case 3:
					stmt = conn.createStatement();
					query = "DELETE FROM GASEMISSION WHERE READING#=" + delete_id;
					rset = stmt.executeUpdate(query);
					conn.commit();
					if (rset > 0) {
						System.out.println("Data deleted successfully!");
					} else {
						System.out.println("Something went wrong.");
					}
					break;
				case 4:
					stmt = conn.createStatement();
					query = "DELETE FROM AQI WHERE READING#=" + delete_id;
					rset = stmt.executeUpdate(query);
					conn.commit();
					if (rset > 0) {
						System.out.println("Data deleted successfully!");
					} else {
						System.out.println("Something went wrong.");
					}
					break;
				default:
					System.out.println("Select valid reading.");
					break;
			}
		} catch (Exception e) {
			System.out.println("Caught Exception: \n     " + e);
		}
	}

	// Select reading data
	void selectReading(int reading_id, int filter, String filter_data) {
		try {
			String resultAlignFormat;

			switch (reading_id) {

				// Temprature data
				case 1:
					resultAlignFormat = "| %-12s | %-12s | %-12s | %-12s | %-12s | %n";
					if (filter == 1) {

						// Based on city name
						String sql = "SELECT * FROM temperature_by_cities WHERE city='" + filter_data.toUpperCase()
								+ "'";
						stmt = conn.createStatement();
						ResultSet rset = stmt.executeQuery(sql);
						System.out.format(
								"+--------------+--------------+--------------+--------------+--------------+%n");
						System.out.format(
								"| Temprature   | Precipition  | Humidity     | Wind         | City         |%n");
						System.out.format(
								"+--------------+--------------+--------------+--------------+--------------+%n");
						while (rset.next()) {
							System.out.format(resultAlignFormat, rset.getInt(2), rset.getInt(3), rset.getInt(4),
									rset.getInt(5), rset.getString(6));
						}
						rset.close();
						System.out.format(
								"+--------------+--------------+--------------+--------------+--------------+%n");
						System.out.println();
						System.out.format(
								"----------------------------------------------------------------------------%n");
						System.out.println();
					} else {

						// Based on date
						String sql = "SELECT * FROM temperature_by_timings";
						stmt = conn.createStatement();
						ResultSet rset = stmt.executeQuery(sql);
						System.out.format(
								"+--------------+--------------+--------------+--------------+--------------+%n");
						System.out.format(
								"| Temprature   | Precipition  | Humidity     | Wind         | Date         |%n");
						System.out.format(
								"+--------------+--------------+--------------+--------------+--------------+%n");
						while (rset.next()) {
							if (rset.getString(6).split(" ")[0].contains(filter_data)) {
								System.out.format(resultAlignFormat, rset.getInt(2), rset.getInt(3), rset.getInt(4),
										rset.getInt(5),
										rset.getString(6).split(" ")[0]);
							}
						}
						rset.close();
						System.out.format(
								"+--------------+--------------+--------------+--------------+--------------+%n");
						System.out.println();
						System.out.format(
								"----------------------------------------------------------------------------%n");
						System.out.println();
					}

					break;

				// Snow Readings
				case 2:
					resultAlignFormat = "| %-12s | %-12s | %-12s | %-12s | %n";
					if (filter == 1) {

						// Based on city name
						String sql = "SELECT * FROM SNOW_BY_CITIES WHERE city='" + filter_data.toUpperCase() + "'";
						stmt = conn.createStatement();
						ResultSet rset = stmt.executeQuery(sql);
						System.out.format("+--------------+--------------+---------------------+--------------+%n");
						System.out.format("| Density      | Depth        | Melting Temperature | City         |%n");
						System.out.format("+--------------+--------------+---------------------+--------------+%n");
						while (rset.next()) {
							System.out.format(resultAlignFormat, rset.getFloat(2), rset.getFloat(3), rset.getInt(4),
									rset.getString(5));
						}
						rset.close();
						System.out.format("+--------------+--------------+---------------------+--------------+%n");
						System.out.println();
						System.out.format("--------------------------------------------------------------------%n");
						System.out.println();
					} else {

						// Based on date
						String sql = "SELECT * FROM SNOW_BY_timings";
						stmt = conn.createStatement();
						ResultSet rset = stmt.executeQuery(sql);
						System.out.format("+--------------+--------------+---------------------+--------------+%n");
						System.out.format("| Density      | Depth        | Melting Temperature | Time         |%n");
						System.out.format("+--------------+--------------+---------------------+--------------+%n");
						while (rset.next()) {
							if (rset.getString(5).split(" ")[0].contains(filter_data)) {
								System.out.format(resultAlignFormat, rset.getFloat(2), rset.getFloat(3), rset.getInt(4),
										rset.getString(5).split(" ")[0]);
							}
						}
						rset.close();
						System.out.format("+--------------+--------------+---------------------+--------------+%n");
						System.out.println();
						System.out.format("--------------------------------------------------------------------%n");
						System.out.println();
					}
					break;

				// GasEmission Readings
				case 3:
					resultAlignFormat = "| %-12s | %-12s | %-12s | %-12s | %-12s | %-12s | %n";
					if (filter == 1) {

						// Based on city name
						String sql = "SELECT * FROM GASEMISSION_BY_CITIES WHERE city='" + filter_data.toUpperCase()
								+ "'";
						stmt = conn.createStatement();
						ResultSet rset = stmt.executeQuery(sql);
						System.out.format(
								"+--------------+--------------+---------------------+--------------------+-----------------+------------+%n");
						System.out.format(
								"| Methane      | CarbonDioxide| Green House Gas     | HydroFluoro Carbons| Nitrous Oxide   | City       |%n");
						System.out.format(
								"+--------------+--------------+---------------------+--------------------+-----------------+------------+%n");
						while (rset.next()) {
							System.out.format(resultAlignFormat, rset.getFloat(2), rset.getFloat(3), rset.getFloat(4),
									rset.getFloat(5), rset.getFloat(6), rset.getString(7));
						}
						rset.close();
						System.out.format(
								"+--------------+--------------+---------------------+--------------------+-----------------+------------+%n");
						System.out.println();
						System.out.format(
								"---------------------------------------------------------------------------------------------------------%n");
						System.out.println();
					} else {

						// Based on date
						String sql = "SELECT * FROM SNOW_BY_timings";
						stmt = conn.createStatement();
						ResultSet rset = stmt.executeQuery(sql);
						System.out.format("+--------------+--------------+---------------------+--------------+%n");
						System.out.format("| Density      | Depth        | Melting Temperature | City         |%n");
						System.out.format("+--------------+--------------+---------------------+--------------+%n");
						while (rset.next()) {
							if (rset.getString(7).split(" ")[0].contains(filter_data)) {
								System.out.format(resultAlignFormat, rset.getFloat(2), rset.getFloat(3), rset.getFloat(4),
									rset.getFloat(5), rset.getFloat(6), rset.getString(7).split(" ")[0]);
							}
						}
						rset.close();
						System.out.format(
								"+--------------+--------------+---------------------+--------------------+-----------------+------------+%n");
						System.out.println();
						System.out.format(
								"---------------------------------------------------------------------------------------------------------%n");
						System.out.println();
					}
					break;

				// AQI Reading
				case 4:
					resultAlignFormat = "| %-12s | %-12s | %-12s | %-12s | %n";
					if (filter == 1) {

						// Based on city name
						String sql = "SELECT * FROM aqi_BY_CITIES WHERE city='" + filter_data.toUpperCase() + "'";
						stmt = conn.createStatement();
						ResultSet rset = stmt.executeQuery(sql);
						System.out.format("+--------------+--------------+----------------+--------------+%n");
						System.out.format("| Index Level  | AQI Level    | Main Pollutant | City         |%n");
						System.out.format("+--------------+--------------+----------------+--------------+%n");
						while (rset.next()) {
							System.out.format(resultAlignFormat, rset.getInt(2), rset.getString(3), rset.getString(4),
									rset.getString(5));
						}
						rset.close();
						System.out.format("+--------------+--------------+----------------+--------------+%n");
						System.out.println();
						System.out.format("---------------------------------------------------------------%n");
						System.out.println();
					} else {

						// Based on date
						String sql = "SELECT * FROM SNOW_BY_timings";
						stmt = conn.createStatement();
						ResultSet rset = stmt.executeQuery(sql);
						System.out.format("+--------------+--------------+----------------+--------------+%n");
						System.out.format("| Index Level  | AQI Level    | Main Pollutant | City         |%n");
						System.out.format("+--------------+--------------+----------------+--------------+%n");
						while (rset.next()) {
							if (rset.getString(5).split(" ")[0].contains(filter_data)) {
										System.out.format(resultAlignFormat, rset.getInt(2), rset.getString(3), rset.getString(4),
									rset.getString(5).split(" ")[0]);
							}
						}
						rset.close();
						System.out.format("+--------------+--------------+----------------+--------------+%n");
						System.out.println();
						System.out.format("---------------------------------------------------------------%n");
						System.out.println();
					}
					break;
				default:
					System.out.println("Select valid reading.");
					break;
			}
		} catch (Exception e) {
			System.out.println("Caught Exception: \n     " + e);
		}
	}

	// Update reading data
	void updateReading(String[] tableData, Object new_value, int reading_id){
		try{
			int rset = 0;
			Object temp = new_value;
			String query = "UPDATE "+ tableData[1] + " SET "+tableData[0]+" = " + temp + " WHERE READING# = " + reading_id;
			if(Integer.class.isInstance(temp)){
				temp = Integer.parseInt(temp.toString());
				query = "UPDATE "+ tableData[1] + " SET "+tableData[0]+" = " + temp + " WHERE READING# = " + reading_id;
			}
			if(Float.class.isInstance(temp)){
				temp = Float.parseFloat(temp.toString());
				query = "UPDATE "+ tableData[1] + " SET "+tableData[0]+" = " + temp + " WHERE READING# = " + reading_id;
			}
			if(String.class.isInstance(temp)){
				temp = temp.toString();
				query = "UPDATE "+ tableData[1] + " SET "+tableData[0]+" = '" + temp + "'" + " WHERE READING# = " + reading_id;
			}
			stmt = conn.createStatement();
			rset = stmt.executeUpdate(query);
			conn.commit();
			if(rset > 0){
				System.out.println("Data updated successfully!");
			}
			else{
				System.out.println("Something went wrong!");
			}
		}
		catch (Exception e) {
			System.out.println("Caught Exception: \n     " + e);
			return;
		}

	}

	// Beutifying columns to display for update
	void printUpdateReadingsColumns(int reading) {
		while (true) {
			try {
				if (reading == 1) {
					System.out.println(
							"\t1. Temperature (°F)\t2. Precipitation (Rain or Sunny)\t3. Humidity (%) \t4. Wind(mph)");
					break;

				} else if (reading == 2) {
					System.out.println("\t1. Density (g/cm3)\t2. Depth (cm)\t3. Melting Temperature (°F)");
					break;

				} else if (reading == 3) {
					System.out.println(
							"\t1. Methane (%)\t2. CarbonDioxide (%)\t3. GreenHouseGas (%)\t4. Hydrofluorocarbons (ppm)\t5. NitrousOxide (%)");
					break;

				} else if (reading == 4) {
					System.out.println(
							"\t1. Index Value (0-999)\t2. AQI Level (Green or Red or Yellow)\t3. Main Pollutant (In words)");
					break;

				} else if (reading == 5) {
					break;
				} else {
					System.out.println("Please select valid reading.");
				}
			} catch (Exception e) {
				System.out.println("Caught Exception: \n     " + e);
			}
		}

	}

	// Method holds to Temperature reading
	private Map<String, Object> TemperatureReading() {

		while (true) {
			try {
				System.out.println(
							"\t- Temperature (°F)\t- Precipitation (Rain or Sunny)\t- Humidity (%) \t- Wind(mph)");
				Map<String, Object> temp = new HashMap<String, Object>();
				System.out.print("\t- Temperature (ex. 35): ");
				int _temp = Integer.parseInt(keyboard.readLine());
				temp.put("_temp", _temp);
				System.out.print("\n\t- Precipitation (45): ");
				int precip = Integer.parseInt(keyboard.readLine());
				temp.put("precip", precip);
				System.out.print("\n\t- Humidity (65): ");
				int hum = Integer.parseInt(keyboard.readLine());
				temp.put("humidity", hum);
				System.out.print("\n\t- Wind (15): ");
				int wind = Integer.parseInt(keyboard.readLine());
				temp.put("wind", wind);
				return temp;
			} catch (Exception e) {
				System.out.println("Caught Exception: \n     " + e);
			}
			break;
		}
		return null;

	}

	// Method holds to snow reading
	private Map<String, Object> snowReading() {
		while (true) {
			try {
				System.out.println("\t1. Density (g/cm3)\t2. Depth (cm)\t3. Melting Temperature (°F)");
				Map<String, Object> snow = new HashMap<String, Object>();
				System.out.print("\t- Density (ex. 0.5): ");
				Float den = Float.parseFloat(keyboard.readLine());
				snow.put("density", den);
				System.out.print("\n\t- Depth (ex. 1): ");
				int d = Integer.parseInt(keyboard.readLine());
				snow.put("depth", d);
				System.out.print("\n\t- Melting Temperature (ex. 35): ");
				int melt = Integer.parseInt(keyboard.readLine());
				snow.put("melt_temp", melt);
				return snow;
			} catch (IOException e) {
				System.out.println("Enter valid readings.");
			} catch (Exception e) {
				System.out.println("Caught Exception: \n     " + e);
			}
			break;
		}
		return null;
	}

	// Method holds to gas reading
	private Map<String, Object> gasReading() {
		while (true) {
			try {
				System.out.println(
							"\t- Methane (%)\t- CarbonDioxide (%)\t- GreenHouseGas (%)\t- Hydrofluorocarbons (ppm)\t- NitrousOxide (%)");
				Map<String, Object> gas = new HashMap<String, Object>();
				System.out.print("\t- Methane (ex. 5): ");
				int m = Integer.parseInt(keyboard.readLine());
				gas.put("methane", m);
				System.out.print("\n\t- CarbonDioxide (ex. 40): ");
				int co2 = Integer.parseInt(keyboard.readLine());
				gas.put("co2", co2);
				System.out.print("\n\t- GreenHouseGas (ex. 35): ");
				int green = Integer.parseInt(keyboard.readLine());
				gas.put("green", green);
				System.out.print("\n\t- Hydrofluorocarbons (ex. 0.5): ");
				Float hydro = Float.parseFloat(keyboard.readLine());
				gas.put("hydro", hydro);
				System.out.print("\n\t- NitrousOxide (ex. 5): ");
				int no = Integer.parseInt(keyboard.readLine());
				gas.put("no", no);
				return gas;
			} catch (IOException e) {
				System.out.println("Enter valid readings.");
			} catch (Exception e) {
				System.out.println("Caught Exception: \n     " + e);
			}
			break;
		}
		return null;
	}

	// Method holds to aqi reading
	private Map<String, Object> aqiReading() {
		while (true) {
			try {
				System.out.println(
							"\t- Index Value (0-999)\t- AQI Level (Green or Red or Yellow)\t- Main Pollutant (In words)");
				Map<String, Object> aqi = new HashMap<String, Object>();
				System.out.print("\t- Index Value (ex. 30): ");
				int index = Integer.parseInt(keyboard.readLine());
				aqi.put("index", index);
				System.out.print("\n\t- AQI Level (ex. Green or Red or Yellow): ");
				String lvl = keyboard.readLine();
				aqi.put("level", lvl);
				System.out.print("\n\t- Main Pollutant (ex. CO2, Oxygen, Nitrogen, etc.): ");
				String p = keyboard.readLine();
				aqi.put("pollutant", p);
				return aqi;
			} catch (IOException e) {
				System.out.println("Enter valid readings.");
			} catch (Exception e) {
				System.out.println("Caught Exception: \n     " + e);
			}
			break;
		}
		return null;
	}

	// Get Column name to update
	String[] getUpdateData(int reading, int reading_col){
		String[] data = new String[2];
		switch(reading){
			case 1:
				data[0] = admin.tempCols[reading_col-1];
				data[1] = "temperature";
				break;
			case 2:
				data[0] = admin.snowCols[reading_col-1];
				data[1] = "snow";
				break;
			case 3:
				data[0] = admin.gasCols[reading_col-1];
				data[1] = "gasemission";
				break;
			case 4:
				data[0] = admin.aqiCols[reading_col-1];
				data[1] = "aqi";
				break;
			default:
				System.out.println("Select valid reading column.");
				break;
		}
		return data;
	}
	
	// -------------------------Station Functionality------------------------- //

	// Method for stations functionality
	void stationsFunctionality() {
		try {
			exit = false;
			switch (access_level) {
				case 0:
					while (!exit) {
						System.out.println("What would you like to do in Stations?");
						System.out.println("\t1. View Stations\n\t2. Update Station\n\t3. Exit");
						System.out.print("Select from above operation: ");
						int operation;
						operation = Integer.parseInt(keyboard.readLine());
						switch (operation) {

							// Select stations
							case 1:

								String location = "";
								System.out.println("Enter Location (Optional or ex. Chicago, San Jose or New York): ");
								location = keyboard.readLine();

								// Calling staion view method reading method to take values from user
								selectStations(location);
								break;

							// Update station
							case 2:
								while (!exit) {
								System.out.println("\nWhat you want to change in station data? ");
								System.out.print(
										"\t1. Station Name\n\t2. Area\n\t3. Exit");
								System.out.print("\nSelect column (1-3): ");
								int station_col = Integer.parseInt(keyboard.readLine());
								System.out.print("\nSelect station id (ex. 4): ");
								int staion_id = Integer.parseInt(keyboard.readLine());
									System.out.print("Enter new value: ");
									if (station_col == 1) {
										String new_name = keyboard.readLine();

										// method to update station value
										updateStation("Station_Name", new_name, staion_id);
										break;
									} else if (station_col == 2) {
										String new_area = keyboard.readLine();

										// method to update station value
										updateStation("Coverage", new_area,staion_id);
										break;

									}
									else if(station_col == 3){exit = true;}
									else{
										System.out.println("Please select valid column.");
									}
								}
								break;

							case 3:
								exit = true;
								break;
							default:
								System.out.println("Select valid option.");
								break;

						}
					}
					break;
				case 1:
					while (!exit) {
						System.out.println("What would you like to do in Stations?");
						System.out.println("\t1. View Stations\n\t2. Update Station\n\t3. Exit");
						System.out.print("Select from above operation: ");
						int operation;
						operation = Integer.parseInt(keyboard.readLine());
						switch (operation) {

							// Select stations
							case 1:

								String location = "";
								System.out.println("Enter Location (Optional or ex. Chicago, San Jose or New York): ");
								location = keyboard.readLine();

								// Calling staion view method reading method to take values from user
								selectStations(location);
								break;
							case 2:
								exit = true;
								break;
							default:
								System.out.println("Select valid option.");
								break;

						}
					}
					break;
				default:
					System.out.println("Something went wrong. Please try again.");
					endUser();
					break;
			}
		} catch (Exception e) {
			System.out.println("Caught Exception: \n     " + e);
		}
	}

	// Method to update station data
	void updateStation(String col_name, String new_value, int staion_id){
		try{
			String query =  "UPDATE STATION " +
							"SET station."+col_name+"='"+new_value+"'"+
							" WHERE station.Station#="+staion_id;
			stmt = conn.createStatement();
			int rset = stmt.executeUpdate(query);
			conn.commit();
			if(rset > 0){
				System.out.println("Data updated successfully!");
			}
			else{
				System.out.println("Something went wrong!");
			}
		}
		catch (Exception e) {
			System.out.println("Caught Exception: \n     " + e);
		}
	}

	// Method to view station data
	void selectStations(String location){
		try{
			String resultAlignFormat = "| %-12s | %-12s | %-12s | %-12s | %n";
			String sql ="SELECT Station.Station_Name,Station.Coverage, Location.City, State.State_Name "+
						"FROM Station INNER JOIN Location ON Station.Station# = Location.Station# "+
						"INNER JOIN State ON Location.State# = State.State# ";
						if(!location.isEmpty())
							sql = sql +"WHERE Location.City='" + location.toUpperCase() + "'";
						stmt = conn.createStatement();
						ResultSet rset = stmt.executeQuery(sql);
						System.out.format(
								"+--------------+--------------+--------------+--------------+%n");
						System.out.format(
								"| Station Name | Coverage     | City         | State        |%n");
						System.out.format(
								"+--------------+--------------+--------------+--------------+%n");
						while (rset.next()) {
							System.out.format(resultAlignFormat, rset.getString(1), rset.getInt(2), rset.getString(3),
									rset.getString(4));
						}
						rset.close();
						System.out.format(
							"+--------------+--------------+--------------+--------------+%n");
						System.out.println();
						System.out.format(
								"-------------------------------------------------------------%n");
						System.out.println();
		}catch (Exception e) {
			System.out.println("Caught Exception: \n     " + e);
		}
	}
	

	//Main method
	public static void main(String args[]) {
		String username = "", password = "";

		keyboard = new BufferedReader(new InputStreamReader(System.in));

		try {

			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());

			System.out.println("Registered the driver...");

			conn = DriverManager.getConnection("", username, password);

			System.out.println("logged into oracle as " + username);

			conn.setAutoCommit(false);

			stmt = conn.createStatement();

			// Who is enduser?
			endUser();

		}

		catch (SQLException e) {
			System.out.println("Caught SQL Exception: \n     " + e);
		}

		catch (NumberFormatException e) {
			System.out.println("Please enter valid choice. Only numbers are allowed.");
		}

		catch (Exception e) {
			System.out.println("Caught Exception: \n     " + e);
		}
	}
}
// End of program
