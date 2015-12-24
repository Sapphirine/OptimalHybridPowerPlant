package com.me.test;

import java.io.FileWriter;
import java.io.IOException;

public class BruteForceWriter {

	
	
	
	private static final String COMMA_DELIMITER = ",";
	private static final String NEW_LINE_SEPARATOR = "\n";

	private static final int Nps=5000;
	private static final int Npe=5500;
	private static final int Nws=5000;
	private static final int Nwe=5500;
	
	
	public static void main(String[] args) {
		FileWriter write =null;
		try {
			write =new FileWriter("OPTIMAL_VAL.csv");
			
			for (int i=Nps; i<=Npe; i++){
				for (int j=Nws; j<=Nwe; j++){
					write.append(String.valueOf(i));
					write.append(COMMA_DELIMITER);
					write.append(String.valueOf(j));
					write.append(NEW_LINE_SEPARATOR);
					
				}
				
			}
			System.out.println("Done!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
				try {
					write.flush();
					write.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
			
	}
	
}