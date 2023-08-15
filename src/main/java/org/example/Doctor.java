package org.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;
import java.util.*;
import java.util.Map.Entry;

interface DoctorDetails {

	LinkedHashMap<Integer, LinkedHashMap<String, String>> doctorsHm = new LinkedHashMap<Integer, LinkedHashMap<String, String>>();
	LinkedHashMap<Integer, LinkedHashMap<String, String>> patientBillsHm = new LinkedHashMap<Integer, LinkedHashMap<String, String>>();
	
	static LinkedHashMap<String, LinkedHashMap<String, Integer>> diseaseDesc = new LinkedHashMap<String, LinkedHashMap<String, Integer>>();
	
	void doctorDetails(String username, String name, int age, String domain, String speciality);
	void updatePatientDetails(int id, String doctorName, String isBilled);
	void readPatientBills();
	void patientBills(String name, int age, String diseaseHistory, String currentDisease, String doctorName, int totalAmount);
	
	void docHome() throws Exception;
	static LinkedHashMap<String, LinkedHashMap<String, Integer>> diseaseDesc() throws Exception {

		FileReader fr1 = new FileReader("G:\\All Study Materials\\hms\\src\\main\\resources\\dbCredentials.properties");
		Properties prop = new Properties();
		prop.load(fr1);

		File file = new File(prop.getProperty("dieasesDesc"));
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String line;
		int count = 0;
		
		while ((line=br.readLine()) != null) {
			LinkedHashMap<String, Integer> codePrice = new LinkedHashMap<String, Integer>();
			count++;
			if (count > 1) {
				String[] arr = line.split("\\t");
				codePrice.put(arr[1], Integer.parseInt(arr[2]));
				diseaseDesc.put(arr[0], codePrice);
			}
		}
		br.close();
		return (diseaseDesc);
	}
}

public class Doctor extends Authentication implements DoctorDetails {

	Scanner sc = new Scanner(System.in);
	static int id = 1;
	private LinkedHashMap<String, String> doctorDetails = new LinkedHashMap<String, String>();


	// After the Doctor Billed the detials from the patients
	public void updatePatientDetails(int id, String doctorName, String isBilled) {
		try {
			FileReader fr = new FileReader("G:\\All Study Materials\\hms\\src\\main\\resources\\dbCredentials.properties");
			Properties prop = new Properties();
			prop.load(fr);

			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/"+prop.getProperty("dbName"), prop.getProperty("root"), prop.getProperty("password"));

			Statement statement = connection.createStatement();

			statement.executeUpdate("UPDATE PatientDetails SET DoctorName = " + "\"" + doctorName +  "\"" +", Billed = " + "\"" + isBilled +  "\"" +" WHERE ID ="+ id);

			ResultSet res = statement.executeQuery("SELECT * FROM PatientDetails");

			ResultSetMetaData metaData = res.getMetaData();

			for (int i=1; i<=metaData.getColumnCount(); i++) {
				System.out.print(metaData.getColumnName(i) + " ");
			}
			System.out.println();
			while (res.next()) {
				System.out.println(res.getInt(1) + " " + res.getString(2) + " " + res.getString(3) + " " + res.getInt(4) + " " + res.getString(5) + " " + res.getString(6) + " " + res.getString(7) + " " + res.getString(8));
			}
			connection.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void readPatientBills() {
		try {
			FileReader fr = new FileReader("G:\\All Study Materials\\hms\\src\\main\\resources\\dbCredentials.properties");
			Properties prop = new Properties();
			prop.load(fr);
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/"+prop.getProperty("dbName"), prop.getProperty("root"), prop.getProperty("password"));

			Statement statement = connection.createStatement();
			
			try {
				statement.executeUpdate("CREATE TABLE PatientBills (ID int PRIMARY KEY AUTO_INCREMENT, Name varchar(255), Age int, DiseaseHistory varchar(1000), CurrentDisease varchar(1000), DoctorName varchar(255), TotalPrice int)");
			}
			catch (Exception e) {
				System.out.println("Table already created!");
			}
			
			ResultSet res = statement.executeQuery("SELECT * FROM patientbills");
			
			while (res.next()) {
				LinkedHashMap<String, String> patientBillsTemp = new LinkedHashMap<String, String>();
				patientBillsTemp.put("Name", res.getString(2));
				patientBillsTemp.put("Age", String.valueOf(res.getInt(3)));
				patientBillsTemp.put("History", res.getString(4));
				patientBillsTemp.put("Current Disease", res.getString(5));
				patientBillsTemp.put("Doctor Name", res.getString(6));
				patientBillsTemp.put("TotalPrice", String.valueOf(res.getInt(7)));
					
				patientBillsHm.put(res.getInt(1), patientBillsTemp);
			}
		
			connection.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void patientBills(String name, int age, String diseaseHistory, String currentDisease, String doctorName, int totalAmount) {
		
		try {
			FileReader fr = new FileReader("G:\\All Study Materials\\hms\\src\\main\\resources\\dbCredentials.properties");
			Properties prop = new Properties();
			prop.load(fr);
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/"+prop.getProperty("dbName"), prop.getProperty("root"), prop.getProperty("password"));

			Statement statement = connection.createStatement();
			
			try {
				statement.executeUpdate("CREATE TABLE PatientBills (ID int PRIMARY KEY AUTO_INCREMENT, Name varchar(255), Age int, DiseaseHistory varchar(1000), CurrentDisease varchar(1000), DoctorName varchar(255), TotalPrice int)");
			}
			catch (Exception e) {
				System.out.println("Table already created!");
			}
			System.out.println();
			
			statement.executeUpdate("INSERT INTO PatientBills (Name, Age, DiseaseHistory, CurrentDisease, DoctorName, TotalPrice) VALUES (\"" + name + "\", " + age + ", \"" + diseaseHistory + "\", \"" + currentDisease + "\", \"" + doctorName + "\", " + totalAmount + ")");
				
			connection.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void doctorDetails(String username, String name, int age, String domain, String speciality) {
		doctorDetails.put("Username", username);
		doctorDetails.put("Name", name);
		doctorDetails.put("Age", String.valueOf(age));
		doctorDetails.put("Domain", domain);
		doctorDetails.put("Speciality", speciality);
		
		doctorsHm.put(id, doctorDetails);
		++id;
		
		System.out.println("Form filled successfully!!!");
	}

	public void docHome() throws Exception {
		new Patient().readPatientDetails();
		readPatientBills();
		System.out.println("Welcome to Doctor Home!");
		
		boolean quit = false;
		
		while (!quit) {
			System.out.println("\nHere are the commands: \n1.Details form\n2.View My Details\n3.View Patient Details\n4.Make Bill\n0.Logout\n");
			System.out.print("Enter the command: ");
			
			int cmd = sc.nextInt();
			switch (cmd) {
			case 1:
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
				
				System.out.print("Enter the domain: ");
				String domain = sc.next();
				sc.nextLine();
				
				System.out.print("Enter the speciality: ");
				String speciality = sc.next();
				sc.nextLine();
				
				ArrayList<String> username = new ArrayList<String>(); 
				username.addAll(Authenticate.doctors.keySet());
				
				if (username.contains(getCurrentDoctor())) {
					doctorDetails(getCurrentDoctor(), name, age, domain, speciality);
				} else {
					System.out.println("Invalid!");
				}
				break;
				
			case 2:
				for (LinkedHashMap<String, String> i : doctorsHm.values()) {
					if (i.get("Username").equals(getCurrentDoctor())) {
						System.out.println(i);
					}
				}
				break;
				
			case 3:
				boolean detail1 = false;
				for (LinkedHashMap<String, String> i : PatientDetails.patientsHm.values()) {
					for (Entry<String, String> details : i.entrySet()) {
						System.out.println(details.getKey() + " - " + details.getValue());
					}
					System.out.println();
					detail1 = true;
				}
				if (!(detail1)) {
					System.out.println("There are no patients right now!");
				}
				break;

			case 4:
				boolean detail2 = false;

				for (Entry<Integer, LinkedHashMap<String, String>> details : PatientDetails.patientsHm.entrySet()) {
					System.out.println("Patient's ID" + " - " + details.getKey());
					for (Entry<String, String> details2 : details.getValue().entrySet()) {

						System.out.println(details2.getKey() + " - " + details2.getValue());
					}
					System.out.println();
					detail2 = true;
				}

				if (!(detail2)) {
					System.out.println("There are no patients right now!");
				} else {

					System.out.print("Enter the patient id: ");
					int id = sc.nextInt();
					if (!PatientDetails.patientsHm.get(id).get("Billed").equals("false")) {
						System.out.println("Patient has already got billed!");
					} else {
						if (PatientDetails.patientsHm.containsKey(id)) {
							int totalAmount = 0;
							String temp = PatientDetails.patientsHm.get(id).get("Current Disease");
							temp = temp.replace("[", "");
							temp = temp.replace("]", "");
							if (temp.contains(",")) {
								ArrayList<String> cDisease = new ArrayList<String>(Arrays.asList(temp.split(",")));
								for (String i : cDisease) {
									String code = DoctorDetails.diseaseDesc().get(i.trim()).keySet().toString();
									code = code.replace("[", "");
									code = code.replace("]", "");
									System.out.println("Disease : " + i.trim() + ", Code: " + code);
									totalAmount += DoctorDetails.diseaseDesc().get(i.trim()).get(code);
								}
							} else {
								String code = DoctorDetails.diseaseDesc().get(temp).keySet().toString();
								code = code.replace("[", "");
								code = code.replace("]", "");
								System.out.println("Disease : " + temp + ", Code: " + code);
								totalAmount += DoctorDetails.diseaseDesc().get(temp).get(code);
							}
							updatePatientDetails(id,   getCurrentDoctor(), "true");

							new Patient().readPatientDetails();
							System.out.println(patientBillsHm);

							for (Entry<Integer, LinkedHashMap<String, String>> i : PatientDetails.patientsHm.entrySet()) {
								if (i.getValue().get("Billed").equals("true") && !patientBillsHm.containsKey(i.getKey())) {
									patientBills(i.getValue().get("Name"), Integer.parseInt(i.getValue().get("Age")), i.getValue().get("History"), i.getValue().get("Current Disease"), i.getValue().get("Doctor Name"), totalAmount);
								}
							}

							readPatientBills();

							System.out.println("Total amount: " + totalAmount);
						} else {
							System.out.println("Patient is not there...!\nSelect different patient to continue...");
							System.out.print("Enter the patient id: ");
							id = sc.nextInt();
						}
					}
				}
				break;
			
			case 0:
				quit=true;
				logout();	
				break;
			}
		}
	}

}
