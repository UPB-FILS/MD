package com.example.lab3;
import android.app.Activity;
import android.content.Context;

import java.io.BufferedReader;
//import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

//import android.text.Editable;
import android.view.View;
//import android.R;
import android.os.Bundle;

import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends Activity {
EditText tf1, tf2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // Button Save=(Button) findViewById(R.id.button1);
        Button button1 = (Button) findViewById(R.id.button1);
        Button button2= (Button) findViewById(R.id.button2);
        tf1 = (EditText) findViewById(R.id.editText1);
        tf2 = (EditText) findViewById(R.id.editText2);
       // File f=new File(tf1.getText(),"str");
        
       
        /*private void writeToFile(String str){
        	try{
        		OutputStreamWriter osw = new OutputStreamWriter(openFileOutput("file.txt", Context.MODE_PRIVATE));
        	}
        }*/
        
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                writeToFile(tf1.getText().toString());
            	//tf2.setText("yay");
            }

			private void writeToFile(String str) {
				// TODO Auto-generated method stub
				try{
	        		OutputStreamWriter osw = new OutputStreamWriter(openFileOutput("file.txt", Context.MODE_PRIVATE));
	        		osw.write(str);
	        		osw.close();
	        	}
				catch(IOException e){
					System.out.println("Failed while writing to file");
					e.printStackTrace();
				}
			}
        });
        
        
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
					tf2.setText(""+readFromFile());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("aah!");
				}
            	
            	
            }

			private String readFromFile() throws IOException {
				// TODO Auto-generated method stub
				String res = "";
            	try{
                	InputStream is = openFileInput("file.txt");
                	if(is != null){
                		InputStreamReader isr = new InputStreamReader(is);
                		BufferedReader br = new BufferedReader(isr);
                		String recstr="";
                		StringBuilder sb = new StringBuilder();
                		while((recstr = br.readLine()) != null){
                			sb.append(recstr);
                		}
                		is.close();
                		res = sb.toString();
                	}
                }
            	catch(FileNotFoundException e){
            		System.out.println("The file has not been found!");
            	}
				return res;
			}
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
   
}
