package com.example.secrets;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class CaesarCode {
	private static final int shift = 23;
	private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ- ";
	
	public static String encrypt(String text){ //encrypting a string
		String ciphered="";
		for(int i=0;i<text.length();i++){

             int charPosition = ALPHABET.indexOf(text.charAt(i));
             int key = (shift+charPosition)%54;
             char replaceVal = ALPHABET.charAt(key);
             ciphered += replaceVal;

        }

        return ciphered;

  }
	
	public static String decrypt(String text){ //decrypting a string
          String deciphered="";

          for(int i=0;i<text.length();i++){

               int charPosition = ALPHABET.indexOf(text.charAt(i));
               int key = (charPosition-shift)%54;
               if(key<0){

                     key =ALPHABET.length() + key;

               }
               
               char replaceVal = ALPHABET.charAt(key);
               deciphered += replaceVal;

          }

          return deciphered;

    }
	
	
	public static void encryptFile(String fileName) throws IOException{  //method taking a file and returning it encrypted
		
		StringBuffer sb = new StringBuffer();
		try {
			FileInputStream inputStream;
			InputStreamReader inputStreamReader;
			BufferedReader bufferedReader;
			String line;
			
			inputStream = new FileInputStream(fileName);
			inputStreamReader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(inputStreamReader);

			while ((line = bufferedReader.readLine()) != null) {
				System.out.println(line);
				sb.append(encrypt(line+"-"));
				
			}
			sb.deleteCharAt(sb.length()-1);
			bufferedReader.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println(sb);
		
		FileOutputStream outputStream;
		try {
			outputStream = new FileOutputStream(new File(fileName));
		
			outputStream.write(sb.toString().getBytes());
			outputStream.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
	
	public static void decryptFile(String fileName){  //method taking a file and returning it decrypted
		StringBuffer sb = new StringBuffer();
		String lines[] = new String[1000];
		int total = 0;
		try {
			Scanner s = new Scanner(new File(fileName));
			String splitter = encrypt("-");
			lines = s.nextLine().split(splitter);
			int i=0;
			while(i<lines.length){
				sb.append(decrypt(lines[i])+"\n");
				i++;
				
			}
			s.close();
			total = lines.length;
		}catch(Exception e){
			
		}
		System.out.println(sb);
	
		
		FileOutputStream outputStream;
		try {
			
			outputStream = new FileOutputStream(new File(fileName));
			String separator = System.getProperty("line.separator");
			for(int j=0 ; j<total; j++){
				outputStream.write(decrypt(lines[j]).getBytes());
				outputStream.write(separator.getBytes());
			}
			outputStream.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	//the methods we want to use
	
public static void encryptFile(String sourceFile, String encryptedFile) throws IOException{ //takes first file and encrypts it intro the second file
		
		StringBuffer sb = new StringBuffer();
		try {
			FileInputStream inputStream;
			InputStreamReader inputStreamReader;
			BufferedReader bufferedReader;
			String line;
			
			inputStream = new FileInputStream(sourceFile);
			inputStreamReader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(inputStreamReader);

			while ((line = bufferedReader.readLine()) != null) {
				System.out.println(line);
				sb.append(encrypt(line+"-"));
				
			}
			sb.deleteCharAt(sb.length()-1);
			bufferedReader.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println(sb);
		
		FileOutputStream outputStream;
		try {
			outputStream = new FileOutputStream(new File(encryptedFile));
		
			outputStream.write(sb.toString().getBytes());
			outputStream.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
	
	public static void decryptFile(String encriptedFile, String decriptedFile){  //takes encripted file and decripts it into the second file
		StringBuffer sb = new StringBuffer();
		String lines[] = new String[1000];
		try {
			Scanner s = new Scanner(new File(encriptedFile));
			String splitter = encrypt("-");
			lines = s.nextLine().split(splitter);
			int i=0;
			while(i<lines.length){
				sb.append(decrypt(lines[i])+System.getProperty("line.separator"));
				i++;
				
			}
			s.close();
		}catch(Exception e){
			
		}
		System.out.println(sb);
	
		
		FileOutputStream outputStream;
		try {
			
			outputStream = new FileOutputStream(new File(decriptedFile));
			outputStream.write(sb.toString().getBytes());
			outputStream.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	
}
