package com.example.secrets;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;

//import com.example.secrets.MainActivity.CancelOnClickListener;
//import com.example.secrets.MainActivity.OkOnClickListener;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.text.InputType;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;

public class SaveEditActivity extends Activity{
	
	private Button saveEdit, cancel;
	private EditText newSecret;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_save_edit);
		// Show the Up button in the action bar.
		setupActionBar();
		saveEdit = (Button)findViewById(R.id.saveEditButton);
		cancel = (Button)findViewById(R.id.cancelButton);
		newSecret = (EditText)findViewById(R.id.newSecret);
		final Intent sender = getIntent();
		final String option = sender.getExtras().getString("ComingFrom");
		if (option.contentEquals("1")){
			Log.d("Seb", sender.getExtras().getString("com.example.secrets.MESSAGE"));
			newSecret.setText(sender.getExtras().getString("com.example.secrets.MESSAGE"));
		}
		if(option.contentEquals("0")==true)
			saveEdit.setText("Add");
		else 
			saveEdit.setText("Edit");
		saveEdit.setOnClickListener(new OnClickListener(){

			final String newText = new String();
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				
				if(option.contentEquals("0")==true)
				{				

					String text = newSecret.getText().toString();
					try {
		
						if (!text.isEmpty()){
							FileOutputStream outputStream;
							outputStream = openFileOutput(sender.getExtras().getString("FileName"), Context. MODE_APPEND);
							String separator = System.getProperty("line.separator");
							outputStream.write(separator.getBytes());
							outputStream.write(text.getBytes());
							outputStream.close();
							Toast message = Toast.makeText(SaveEditActivity.this, "Your secret is safe with me", 100);
							message.show();
						}
						Intent intent = new Intent( SaveEditActivity.this, MainActivity.class);
						intent.putExtra("Secret", text);					
						startActivity(intent);
					
					} catch (IOException e) {
						// TODO Auto-generated catch block
						Log.d("exception", "io "+e.getMessage());
					}
				}
					//reading
				else
				{
					String text = newSecret.getText().toString();
					String aux = new String();
					if (!text.isEmpty()){
						Log.d("saveedit","editing");
						final StringBuffer finalContent= new StringBuffer();
						//editAction(sender); 
						//saveEdit.setText("Edit"); 
						String separator = System.getProperty("line.separator");
						String line;
						BufferedReader reader = null;
						
						try {
							
							//final String newText = new String();	
							
						    //File file = new File(sender.getExtras().getString("FileName"));
							InputStream is = openFileInput(sender.getExtras().getString("FileName"));
							Log.d("Seb", "edit try");
							InputStreamReader isr = new InputStreamReader(is);
						    reader = new BufferedReader(isr);
						    
						    //String secret;
						    //int position = Integer.parseInt(sender.getExtras().getString("position"));
						    int position = sender.getExtras().getInt("position");
						    position++;
						    int i=0;
		
						    while ((line = reader.readLine()) != null && i<position) {
						        i++;
						        finalContent.append(line+separator); //reched editing position
						        Log.d("finalContent", line+separator);
						    }
						      
						    Log.d("Seb",line);
						    finalContent.append(text + separator);
						    
						    
						    while ((line = reader.readLine()) != null){
						        finalContent.append(line+separator); //reched editing position
						    }
						    finalContent.deleteCharAt(finalContent.lastIndexOf(separator));
						    aux = newText;
						} catch (Exception e) {
						    e.printStackTrace();
						    Log.d("Seb",e.getMessage());
						} finally {
						    if (reader != null) {
						        try {
						            reader.close();
						        } catch (IOException e) {
						            e.printStackTrace();
						        }
						    }
						        
						}
							
						
						//newSecret.setText()//getting previous text from file
						
						Log.d("Seb", "mid Edit function");
						try {
							deleteFile(sender.getExtras().getString("FileName"));
							FileOutputStream outputStream;
							outputStream = openFileOutput(sender.getExtras().getString("FileName"), Context. MODE_APPEND);
							
							//outputStream.write(separator.getBytes());
							outputStream.write(finalContent.toString().getBytes());
							outputStream.close();
							Toast message = Toast.makeText(SaveEditActivity.this, "Your list has been updated", 100);
							message.show();
							
						
						} catch (IOException e) {
							// TODO Auto-generated catch block
							Log.d("exception", "io "+e.getMessage());
						}
					}
					Intent intent = new Intent( SaveEditActivity.this, MainActivity.class);
					intent.putExtra("Secret", aux);					
					startActivity(intent);
				}
			}
		});
		
	cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent( SaveEditActivity.this, MainActivity.class);
				intent.putExtra("Cancelled", "true");					
				startActivity(intent);
			}
		});
		
	}
	
	private void addAction(Intent intent){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = this.getLayoutInflater();
		View dialogView = inflater.inflate(R.layout.dialog_save_edit, null);

		builder.setTitle(R.string.saveEditDialog)
				.setView(dialogView)
				.setPositiveButton("Save", new OkOnClickListener())
				.setNegativeButton("Cancel", new CancelOnClickListener());

		
		
	}
	
	private final class OkOnClickListener implements	DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialog, int which) {
			
		
		}
	}

	private final class CancelOnClickListener implements DialogInterface.OnClickListener {
			public void onClick(DialogInterface dialog, int which) {
				//exitApplication();
				Intent backToMenu = new Intent();
				setResult(RESULT_OK,backToMenu);
				finish();
			}
	}

	
	private void editAction(Intent intent){
		
	}
	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			//getActionBar().setDisplayHomeAsUpEnabled(true);
			System.out.println("setupactionbar");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.save_edit, menu);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//switch (item.getItemId()) {
		//case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			//NavUtils.navigateUpFromSameTask(this);
			//System.out.println(item.getItemId());
			//return true;
		//}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
    protected void onRestart() {
		super.onRestart();
		Intent intent = new Intent(SaveEditActivity.this, MainActivity.class);
        startActivity(intent);
	}
}

	