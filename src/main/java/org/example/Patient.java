package org.example;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import java.util.Map.Entry;

interface PatientDetails {
	static LinkedHashMap<Integer, LinkedHashMap<String, String>> patientsHm = new LinkedHashMap<Integer, LinkedHashMap<String, String>>();
	
	void patientHome();
	void writePatientDetails(String username, String name, int age, ArrayList<String> history, ArrayList<String> currentDisease, String docName, String billed);
	void readPatientDetails();
}

public class Patient extends Authentication implements PatientDetails {
	Scanner sc = new Scanner(System.in);
	static int id = 1;
	static int pdid = 1;
	
	ArrayList<String> patientHistory = new ArrayList<String>();
	ArrayList<String> currentDisease = new ArrayList<String>();
	
	
	public void writePatientDetails(String username, String name, int age, ArrayList<String> history, ArrayList<String> currentDisease, String docName, String billed) {
		try {
			FileReader fr = new FileReader("G:\\All Study Materials\\hms\\src\\main\\resources\\dbCredentials.properties");
			Properties prop = new Properties();
			prop.load(fr);
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/"+prop.getProperty("dbName"), prop.getProperty("root"), prop.getProperty("password"));

			Statement statement = connection.createStatement();
			
			try {
				statement.executeUpdate("CREATE TABLE PatientDetails (ID int PRIMARY KEY AUTO_INCREMENT, Username varchar(255), Name varchar(255), Age int, DiseaseHistory varchar(1000), CurrentDisease varchar(1000), DoctorName varchar(255), Billed varchar(50))");
			}
			catch (Exception e) {
				System.out.println("Table already created!");
			}
			
			try {
				statement.executeUpdate("INSERT INTO PatientDetails (Username, Name, Age, DiseaseHistory, CurrentDisease, DoctorName, Billed) VALUES (\"" + username + "\""+ ", "+ "\"" + name +  "\"" + ", "+ "\"" + age +  "\"" + ", "+ "\"" + history.toString() +  "\"" + ", "+ "\"" + currentDisease.toString() +  "\"" + ", "+ docName + ", "+ "\"" + billed +  "\"" + ")");
				pdid++;
			}
			catch (Exception e) {
				
			}
			
//			ResultSet res = statement.executeQuery("SELECT * FROM PatientDetails");
//			
//			ResultSetMetaData metaData = res.getMetaData();
//			
//			for (int i=1; i<=metaData.getColumnCount(); i++) {
//				System.out.print(metaData.getColumnName(i) + " ");
//			}
//			System.out.println();
//			while (res.next()) {
//				System.out.println(res.getInt(1) + " " + res.getString(2) + " " + res.getString(3) + " " + res.getInt(4) + " " + res.getString(5) + " " + res.getString(6) + " " + res.getString(7) + " " + res.getString(8));
//			}
			connection.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void readPatientDetails() {
		try {
			FileReader fr = new FileReader("G:\\All Study Materials\\hms\\src\\main\\resources\\dbCredentials.properties");
			Properties prop = new Properties();
			prop.load(fr);
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/"+prop.getProperty("dbName"), prop.getProperty("root"), prop.getProperty("password"));

			Statement statement = connection.createStatement();
			
			ResultSet res = statement.executeQuery("SELECT * FROM PatientDetails");
			
//			ResultSetMetaData metaData = res.getMetaData();
//			
//			for (int i=1; i<=metaData.getColumnCount(); i++) {
//				System.out.print(metaData.getColumnName(i) + " ");
//			}
//			System.out.println();
			
			while (res.next()) {
				LinkedHashMap<String, String> patientDetails = new LinkedHashMap<String, String>();
				String h = res.getString(5);
				h = h.replace("[", "");
				h = h.replace("]", "");
				for (String i : h.split(",")) {
					patientHistory.add(i);
				}
				
				String c = res.getString(6);
				c = c.replace("[", "");
				c = c.replace("]", "");
				for (String i : c.split(",")) {
					currentDisease.add(i);
				}
				
				patientDetails.put("Username", res.getString(2));
				patientDetails.put("Name", res.getString(3));
				patientDetails.put("Age", String.valueOf(res.getInt(4)));
				patientDetails.put("History", res.getString(5));
				patientDetails.put("Current Disease", res.getString(6));
				patientDetails.put("Doctor Name", res.getString(7));
				patientDetails.put("Billed", res.getString(8));
				
				patientsHm.put(res.getInt(1), patientDetails);
			}
			writePatientDetails(res.getString(2), res.getString(3), res.getInt(4), patientHistory, currentDisease, res.getString(7), res.getString(8));
			System.out.println(patientsHm);
			connection.close();
		}
		catch (Exception e) {
			//e.printStackTrace();
		}
	}
	
	
	public void patientHome() {
		System.out.println("Welcome to Patient Home!");
		
		boolean quit = false;
		
		while (!quit) {
			System.out.println("\nHere are the commands: \n1.Details form\n2.View Details\n3.Bill View\n0.Logout\n");
			System.out.print("Enter the command: ");
			
			int cmd = sc.nextInt();
			switch (cmd) {
			case 1:
				
				boolean isExists = false;
				
				for (LinkedHashMap<String, String> pd : patientsHm.values()) {
					if (pd.get("Username").equals(getCurrentPatient())) {
						isExists = true;
					}
				}
				
				if (!isExists) {
					patientHistory.clear();
					currentDisease.clear();
					
					System.out.print("Enter the name: ");
					String name = sc.next();
					sc.nextLine();
					
					int age = 0;
					do {
						try {
							System.out.print("Enter the age: ");
							age = sc.nextInt();
						}
						catch (Exception e) {
							System.out.println("Age must be an integer! Please try again...");
							sc.next();
						}
					} while (age<1);
					sc.nextLine();
					
					System.out.print("Enter the Disease History: ");
					String diseaseHistory = sc.nextLine();
					if (diseaseHistory.contains(",")) {
						String[] arr = diseaseHistory.split(",");
						for (String i : arr) {
							patientHistory.add(i.trim());
						}
					} else {
						patientHistory.add(diseaseHistory);
					}
					
					System.out.print("Enter the current disease: ");
					String disease = sc.nextLine();
					if (disease.contains(",")) {
						String[] arr = disease.split(",");
						for (String i : arr) {
							currentDisease.add(i.trim());
						}
					} else {
						currentDisease.add(disease);
					}
					
					ArrayList<String> username = new ArrayList<String>(); 
					username.addAll(Authenticate.patients.keySet());
					
					if (username.contains(getCurrentPatient())) {
						writePatientDetails(getCurrentPatient(), name, age, patientHistory, currentDisease, "null", "false");
					} else {
						System.out.println("Invalid!");
					}
				} else {
					System.out.println("Form already filled!");
				}
				//System.out.println(patientsHm);
				//readPatientDetails();
				break;
				
			case 2:
				readPatientDetails();
				//System.out.println(patientsHm);
				boolean detail = false;
				for (LinkedHashMap<String, String> i : patientsHm.values()) {
					if (i.get("Username").equals(getCurrentPatient())) {
						System.out.println("Patient Details:-");
						for (Entry<String, String> details : i.entrySet()) {
							System.out.println(details.getKey() + " - " + details.getValue());
						}
						detail = true;
						break;
					}
				}
				if (!(detail)) {
					System.out.println("Please fill the form!");
				}
				break;
				
			case 3:
				if (patientsHm.size()>0) {
					for (LinkedHashMap<String, String> p : patientsHm.values()) {
						if (p.get("Username").equals(getCurrentPatient())) {
							if (p.get("Billed").equals("true")) {
								int totalAmount = 0;
								String temp = p.get("Current Disease");
								temp = temp.replace("[", "");
								temp = temp.replace("]", "");
								if (temp.contains(",")) {
									ArrayList<String> cDisease = new ArrayList<String>(Arrays.asList(temp.split(",")));
									for (String i : cDisease) {
										try {
											String code = DoctorDetails.diseaseDesc().get(i.trim()).keySet().toString();
											code = code.replace("[", "");
											code = code.replace("]", "");
											System.out.println("Disease : " + i.trim() + ", Code: " + code);
											totalAmount += DoctorDetails.diseaseDesc().get(i.trim()).get(code);
										}
										catch (Exception e) {
											e.printStackTrace();
										}
									}
								} else {
									try {
										String code = DoctorDetails.diseaseDesc().get(temp).keySet().toString();
										code = code.replace("[", "");
										code = code.replace("]", "");
										System.out.println("Disease : " + temp + ", Code: " + code);
										totalAmount += DoctorDetails.diseaseDesc().get(temp).get(code);
									}
									catch (Exception e) {
										e.printStackTrace();
									}
								}
								
								System.out.println("Doctor Name : " + p.get("Doctor Name"));
								System.out.println("Total amount : " + totalAmount);
							} else {
								System.out.println("Your form doesn't get billed by the doctor!");
							}
						} 
					} 
				} else {
					System.out.println("Please fill the form!");
				}	
				break;
				
			case 0:
				quit=true;
				logout();
				break;
				
			default:
				System.out.println("Invalid command!");
				break;
			}
		}
	}
}
