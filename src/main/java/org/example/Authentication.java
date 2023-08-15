package org.example;

import java.io.FileReader;
import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Properties;
import java.util.Scanner;

interface Authenticate {
	LinkedHashMap<String, String> doctors = new LinkedHashMap<String, String>();
	LinkedHashMap<String, String> patients = new LinkedHashMap<String, String>();
	
	void setPatientName(String newName);
	String getPatientName();
	String getCurrentPatient();
	
	void setDoctorName(String newName);
	String getDoctorName();
	String getCurrentDoctor();
	
	void writePatients(String username, String password);
	void writeDoctors(String username, String password);
	
	void readPatients();
	void readDoctors();
	
	void doctorRegister();
	void patientRegister();
	void doctorLogin();
	void patientLogin();
	void logout();
	void start();
}

public class Authentication implements Authenticate {
	Scanner sc = new Scanner(System.in);
	static String patientName="";
	static String patientPassword="";
	
	static String doctorName="";
	static String doctorPassword="";
	
	static int pid = 1;
	static int did = 1;
	
	
	public void setPatientName(String newName) {
		patientName += newName;
	}
	
	
	public String getPatientName() {
		return patientName;
	}
	
	
	public String getCurrentPatient() {
		String currentPatient = getPatientName();
		return currentPatient;
	}
	
	
	public void setDoctorName(String newName) {
		doctorName += newName;
	}
	
	
	public String getDoctorName() {
		return doctorName;
	}
	
	
	public String getCurrentDoctor() {
		String currentDoctor = getDoctorName();
		return currentDoctor;
	}
	
	
	public void writePatients(String username, String password) {
		try {
			FileReader fr = new FileReader("G:\\All Study Materials\\hms\\src\\main\\resources\\dbCredentials.properties");
			Properties prop = new Properties();
			prop.load(fr);
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/"+prop.getProperty("dbName"), prop.getProperty("root"), prop.getProperty("password"));

			Statement statement = connection.createStatement();
			
			try {
				statement.executeUpdate("CREATE TABLE Patients (ID int PRIMARY KEY AUTO_INCREMENT, Username varchar(255), Password varchar(20))");
			}
			catch (Exception e) {
				System.out.println("Table already created!");
			}
			
			statement.executeUpdate("INSERT INTO Patients (Username, Password) VALUES (\"" + username + "\""+ ", "+ "\"" + password +  "\"" + ")");
			pid++;
			ResultSet res = statement.executeQuery("SELECT * FROM Patients");
			
			ResultSetMetaData metaData = res.getMetaData();
			
			for (int i=1; i<=metaData.getColumnCount(); i++) {
				System.out.print(metaData.getColumnName(i) + " ");
			}
			System.out.println();
			while (res.next()) {
				System.out.println(res.getInt(1) + " " + res.getString(2) + " " + res.getString(3));
			}
			connection.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void writeDoctors(String username, String password) {
		try {
			FileReader fr = new FileReader("G:\\All Study Materials\\hms\\src\\main\\resources\\dbCredentials.properties");
			Properties prop = new Properties();
			prop.load(fr);
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/"+prop.getProperty("dbName"), prop.getProperty("root"), prop.getProperty("password"));

			Statement statement = connection.createStatement();
			
			try {
				statement.executeUpdate("CREATE TABLE Doctors (ID int PRIMARY KEY AUTO_INCREMENT, Username varchar(255), Password varchar(20))");
			}
			catch (Exception e) {
				System.out.println("Table already created!");
			}
			
			statement.executeUpdate("INSERT INTO Doctors (Username, Password) VALUES (\"" + username + "\""+ ", "+ "\"" + password +  "\"" + ")");
			did++;
			
			ResultSet res = statement.executeQuery("SELECT * FROM Doctors");
			
			ResultSetMetaData metaData = res.getMetaData();
			
			for (int i=1; i<=metaData.getColumnCount(); i++) {
				System.out.print(metaData.getColumnName(i) + " ");
			}
			System.out.println();
			while (res.next()) {
				System.out.println(res.getInt(1) + " " + res.getString(2) + " " + res.getString(3));
			}
			connection.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void readPatients() {
		try {
			FileReader fr = new FileReader("G:\\All Study Materials\\hms\\src\\main\\resources\\dbCredentials.properties");
			Properties prop = new Properties();
			prop.load(fr);
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/"+prop.getProperty("dbName"), prop.getProperty("root"), prop.getProperty("password"));

			Statement statement = connection.createStatement();
			
			try {
				ResultSet res = statement.executeQuery("SELECT * FROM Patients");
				while (res.next()) {
					patients.put(res.getString(2), res.getString(3));
				}
			}
			catch (Exception e) {
				
			}
			
			connection.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void readDoctors() {
		try {
			FileReader fr = new FileReader("G:\\All Study Materials\\hms\\src\\main\\resources\\dbCredentials.properties");
			Properties prop = new Properties();
			prop.load(fr);
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/"+prop.getProperty("dbName"), prop.getProperty("root"), prop.getProperty("password"));

			Statement statement = connection.createStatement();
			
			try {
				ResultSet res = statement.executeQuery("SELECT * FROM Doctors");
				while (res.next()) {
					doctors.put(res.getString(2), res.getString(3));
				}
			}
			catch (Exception e) {
				
			}
		
			connection.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void doctorRegister() {
		System.out.print("\nREGISTER\n");
		
		String username = "";
		do {
			try {
				System.out.print("Enter the username: ");
				username = sc.next();
				if (doctors.containsKey(username)) {
					throw new Exception();
				}
			}
			catch (Exception e) {
				System.out.println("Username already exists! Please try a different name.");
				sc.nextLine();
			}
		} while (doctors.containsKey(username));
		sc.nextLine();
		
		System.out.print("Enter the password: ");
		String pass1 = sc.nextLine();
		
		System.out.print("Confirm the password: ");
		String pass2 = sc.nextLine();
		
		if (username.length() > 5 && pass1.length() > 5 && pass1.equals(pass2)) {
			writeDoctors(username, pass1);
			readDoctors();
			
			doctorLogin();
		} else {
			System.out.println("Register Failed...!\nNOTE: The Username & Password must be atleast 6 characters!");
			doctorRegister();
		}
	}
	
	
	public void patientRegister() {
		System.out.print("\nREGISTER\n");
		
		String username = "";
		do {
			try {
				System.out.print("Enter the username: ");
				username = sc.next();
				if (patients.containsKey(username)) {
					throw new Exception();
				}
			}
			catch (Exception e) {
				System.out.println("Username already exists! Please try a different name.");
				sc.nextLine();
			}
		} while (patients.containsKey(username));
		sc.nextLine();
		
		
		System.out.print("Enter the password: ");
		String pass1 = sc.nextLine();
		
		System.out.print("Confirm the password: ");
		String pass2 = sc.nextLine();
		
		if (username.length() > 5 && pass1.length() > 5 && pass1.equals(pass2)) {
			writePatients(username, pass1);
			readPatients();
			
			patientLogin();
		} else {
			System.out.println("Register Failed...!\nNOTE: The Username & Password must be atleast 6 characters!");
			patientRegister();
		}
	}

	
	public void doctorLogin() {
		System.out.print("\nLOGIN\n");
		
		System.out.print("Enter the Username: ");
		doctorName = sc.next();
		System.out.print("Enter the Password: ");
		doctorPassword = sc.next();
		if (doctors.containsKey(doctorName) && doctorPassword.equals(doctors.get(doctorName))) {
			System.out.println("\nLogin Successful!!!");
			System.out.println("\nHello " + doctorName);
			DoctorDetails obj = new Doctor();
			try {
				obj.docHome();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("OOPS! Credentials were wrong!");
			System.out.print("Already Registered?(Y/N): ");
			String reg = sc.next().toLowerCase();
			
			if (reg.equals("y")) {
				doctorLogin();
			} else if (reg.equals("n")) {
				doctorRegister();
			} else {
				System.out.println("Invalid!!!");
				start();
			}
		}
	}
	
	
	public void patientLogin() {
		System.out.print("\nLOGIN\n");
		
		System.out.print("Enter the Username: ");
		patientName = sc.next();
		System.out.print("Enter the Password: ");
		patientPassword = sc.next();
		if (patients.containsKey(patientName) && patientPassword.equals(patients.get(patientName))) {
			System.out.println("\nLogin Successful!!!");
			System.out.println("\nHello " + patientName);
			Patient obj = new Patient();
			obj.patientHome();
		} else {
			System.out.println("OOPS! Credentials were wrong!");
			System.out.print("Already Registered?(Y/N): ");
			String reg = sc.next().toLowerCase();
			
			if (reg.equals("y")) {
				patientLogin();
			} else if (reg.equals("n")) {
				patientRegister();
			} else {
				System.out.println("Invalid!!!");
				start();
			}
		}
	}
	
	
	public void logout() {
		start();
	}
	
	
	public void start() {
		try {
			DoctorDetails.diseaseDesc();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
			readPatients();
		readDoctors();
		new Patient().readPatientDetails();
		
		System.out.print("Welcome!\n(Doctor/Patient)? - (d/p/q-Quit): ");
		String dp = sc.next().toLowerCase();
		
		if (dp.equals("d")) {
			System.out.print("Welcome! Hello Doctor! Registered?(Y/N): ");
			String reg = sc.next().toLowerCase();
			
			if (reg.equals("y")) {
				doctorLogin();
			} else if (reg.equals("n")) {
				doctorRegister();
			} else {
				System.out.println("Invalid!!!");
				start();
			}
		} else if (dp.equals("p")) {
			System.out.print("Welcome! Hello Patient! Registered?(Y/N): ");
			String reg = sc.next().toLowerCase();
			
			if (reg.equals("y")) {
				patientLogin();
			} else if (reg.equals("n")) {
				patientRegister();
			} else {
				System.out.println("Invalid!!!");
				start();
			}
		} else if (dp.equals("q")) {
			System.exit(0);
		} else {
			System.out.println("Invalid!!!");
			start();
		}
	}
}
