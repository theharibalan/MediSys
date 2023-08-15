package org.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Ex {
	public static void main(String[] args) throws Exception {
		LinkedHashMap<String, ArrayList<String>> hm = new LinkedHashMap<String, ArrayList<String>>();
		
		BufferedReader br = new BufferedReader(new FileReader(new File("C:\\Users\\HP\\Desktop\\JavaBasics\\ProjectMain\\DiseaseDesc.lookup")));
		int count = 0;
		String line;
		
		while ((line = br.readLine()) != null) {
			ArrayList<String> al = new ArrayList<String>();
			
			count++;
			if (count>1) {
				String[] arr = line.split("\\s");
				al.add(arr[1]);
				al.add(arr[2]);
				hm.put(arr[0], al);
			}
		}
		System.out.println(hm);
	}
}