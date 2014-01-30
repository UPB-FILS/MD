package com.example.secrets;
import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.view.KeyEvent;

public class MainActivity extends Activity implements OnItemClickListener, OnCheckedChangeListener{

	private static final String EXTRA_MESSAGE = "com.example.secrets.MESSAGE";
	private static final int DIALOG_ALERT = 10;
	private static int authenticationTrials = 0;
	private boolean isEditEnabled = false;
	private boolean isDeleteEnabled = false;
	private AlertDialog passwordAlertDialog;
	private EditText passwordInput;
	private String password;
	private static String fileName = "mySecrets";
	private static String resultedFile = "resultedSecrets";
	private Context context;
	private File mySecretFile;
	private ListView secretsList;
	private SecretsList adapter;
	private Intent saveEditIntent;
	private static final String PREFS_PASS = "PassPref";
	private static boolean passwordIsChecked = false;
	private Bundle saveEditBundle;
	private int editPosition;
	private static final int lockTime = 2;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_main);
		secretsList = (ListView)findViewById(android.R.id.list);
		secretsList.setOnItemClickListener(this);
		adapter = new SecretsList(this);

		secretsList.setAdapter(adapter);
		
		context = (Context) this;

		String path = context.getFilesDir().getAbsolutePath() + "/" + fileName;
		mySecretFile = new File(path);
		//TODO make the following dialog (password prompt) appear only when you launch the application 
		Bundle extras = getIntent().getExtras();
		if(extras != null)
		{
			if(extras.containsKey("Secret"))
			{
				Log.d("mainbundle",extras.getString("Secret"));
				
			}
			else if(extras.containsKey("Cancelled"))
			{
				Log.d("mainbundle","cancelled");
			}
			saveEditBundle = extras;
			displayList();
		}
		else 
		{
			Log.d("mainbundle","cam golut");
		}
		if(extras != null)
		{
			if(extras.getString("Secret")!= null)
			{
				String r = extras.getString("Secret");
				Log.d("listtt", r);
				adapter.addNewSecret(r);
				displayList();
			}
		}
		if(savedInstanceState == null && saveEditBundle == null)
		{
			showDialog(DIALOG_ALERT);
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		adapter.contextMenu = menu;
		//Enable/Disable edit button
		MenuItem item = menu.findItem(R.id.action_edit);
		item.setEnabled(isEditEnabled);
		item.setVisible(isEditEnabled);
		//Enable/Disable delete button
		item = menu.findItem(R.id.action_delete);
		item.setEnabled(isDeleteEnabled);
		item.setVisible(isDeleteEnabled);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		//check which ActionBar button was pressed
		switch (item.getItemId()){
		case (R.id.action_add):
			openAdd();
			return true;
		case (R.id.action_edit):
			openEdit();
			return true;
		case (R.id.action_delete):
			deleteSecrets();
			return true;
		case (R.id.action_import_export):
			importExport();
		return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private void importExport() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
	      intent.setAction(Intent.ACTION_GET_CONTENT);
	      intent.setType("file/*");
	      intent.putExtra("FileName", fileName);
	      intent.putExtra("ResultedFile", resultedFile);
	      startActivity(intent);	
	}

	@Override
	//it saves smth=> bundle is not null
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		Log.d("dialog", "onSaveInstance called");
		Log.d("dialog", outState+"");
	}
	@Override
	//it is never called
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
	}


	private void openAdd(){
		Intent intent = new Intent(this, SaveEditActivity.class);
		intent.putExtra("ComingFrom", "0");
		intent.putExtra("FileName", fileName);
		Log.d("Seb", "openAdd()");
		startActivity(intent);
		Log.d("intent", "here");
		Log.d("intent", getIntent().getExtras()+"");

	}
	
	private void openEdit(){
		Intent intent = new Intent(this, SaveEditActivity.class);
		//get selected item's message
		Log.d("goin bananas",editPosition+"");
		Secret selectedSecret = (Secret)adapter.getItem(adapter.getCheckedItem(0));
		String intentMessage = selectedSecret.getDescription();
		intent.putExtra("ComingFrom", "1");
		intent.putExtra(EXTRA_MESSAGE, intentMessage);
		intent.putExtra("FileName", fileName);
		intent.putExtra("position", adapter.getCheckedItem(0));
		startActivity(intent);
	}
	
	private void deleteSecrets(){
		//Secret selectedSecret = (Secret)adapter.getItem(secretsList.getSelectedItemPosition());
		
		final ArrayList<Integer> selectedSecrets = adapter.checkedBoxIndexes;
		
        new AlertDialog.Builder(this)
        .setTitle(R.string.delete_dialog_title)
        .setMessage(R.string.delete_dialog_message)
        .setPositiveButton(R.string.delete_dialog_ok, new DialogInterface.OnClickListener() 
        {
        	public void onClick(DialogInterface dialog, int which) 
            {
        		ArrayList <String> lines = new ArrayList<String>();
        		
        		final StringBuffer finalContent= new StringBuffer();
				String separator = System.getProperty("line.separator");
				String line;
				BufferedReader reader = null;
				
				try {
					
					InputStream is = openFileInput(fileName);
					InputStreamReader isr = new InputStreamReader(is);
				    reader = new BufferedReader(isr);
				    
				    while ((line = reader.readLine()) != null) {
				        lines.add(line);
				    }

				    for (int i = 0; i < selectedSecrets.size(); i++){
						Log.d("selectedSecrets", "" + selectedSecrets.get(i));
					}
				    //Log.d("equalityCheck", "Lines: " + lines.size() + ", SelectedSecrets: " + selectedSecrets.size());
				    finalContent.append(lines.get(0)+separator);
				    for (int i = 1; i < lines.size(); i++){
				    	boolean found = false;
				    	for (int j = 0; j < selectedSecrets.size(); j++){
				    		//Log.d("equalityCheck", (i-1) + " = " + selectedSecrets.get(j).intValue());
				    		if (i - 1 == selectedSecrets.get(j).intValue()){
				    			found = true;
				    			break;
				    		}
				    	}
				    	if (!found){
				    		finalContent.append(lines.get(i)+separator);
				        	Log.d("finalContent", lines.get(i)+separator);
				    	}
				    }
				    
				    finalContent.deleteCharAt(finalContent.lastIndexOf(separator));
				} catch (Exception e) {
				    e.printStackTrace();
				} finally {
				    if (reader != null) {
				        try {
				            reader.close();
				        } catch (IOException e) {
				            e.printStackTrace();
				        }
				    }
				        
				}
					
				try {
					deleteFile(fileName);
					FileOutputStream outputStream;
					outputStream = openFileOutput(fileName, Context. MODE_APPEND);
					outputStream.write(finalContent.toString().getBytes());
					outputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Log.d("exception", "io "+e.getMessage());
				}
				displayList();
				Toast message = Toast.makeText(context, "You have deleted " + selectedSecrets.size() + " secrets", Toast.LENGTH_SHORT);
				message.show();
        		adapter.uncheckAll();
        		for(int i=0; i < secretsList.getChildCount(); i++){
        		    View itemLayout = secretsList.getChildAt(i);
        		    CheckBox cb = (CheckBox)itemLayout.findViewById(R.id.checkBox1);
        		    cb.setChecked(false);
        		}
        		isEditEnabled = false;
        		isDeleteEnabled = false;
        		invalidateOptionsMenu();
            }
         })
        .setNegativeButton(R.string.delete_dialog_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // do nothing
            }
         })
         .show();

	}

	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		// TODO Auto-generated method stub
		
		try {
			Log.d("Cosmin",""+ getLockTime());
			if (getLockTime() < lockTime) {
				
				Toast message = Toast.makeText(context, "You are locked for "
						+ (lockTime - getLockTime()) + " minutes", Toast.LENGTH_LONG);
				message.show();
				exitApplication();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		switch (id) {
		case DIALOG_ALERT:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			LayoutInflater inflater = this.getLayoutInflater();
			View dialogView = inflater.inflate(R.layout.dialog_password, null);
			passwordInput = (EditText) dialogView.findViewById(R.id.password);

			builder.setTitle(R.string.passwordAlertDialog).setView(dialogView)
					.setPositiveButton("Ok", new OkOnClickListener())
					.setNegativeButton("Cancel", new CancelOnClickListener())
.setOnKeyListener(new DialogInterface.OnKeyListener() {
		        @Override
		        public boolean onKey (DialogInterface dialog, int keyCode, KeyEvent event) {
		            if (keyCode == KeyEvent.KEYCODE_BACK && 
		                event.getAction() == KeyEvent.ACTION_UP && 
		                !event.isCanceled()) {
		                dialog.cancel();
		               exitApplication();
		                return true;
		            }
		            return false;
		        }
		    });

			passwordAlertDialog = builder.create();
			passwordAlertDialog.show();
		}
		return super.onCreateDialog(id);

	}

	private final class OkOnClickListener implements
			DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialog, int which) {
			password = passwordInput.getText().toString();
			checkPassword(password);
		}
	}

	private final class CancelOnClickListener implements
			DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialog, int which) {
			exitApplication();
		}
	}

	public void checkPassword(String password) {

		if (!mySecretFile.exists()) {
			try {
				
				FileOutputStream outputStream;
				outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
				outputStream.write(password.getBytes());
				outputStream.close();

				Toast message = Toast.makeText(context,	"Your password has been saved.", 100);
				message.show();
				this.passwordIsChecked = true;
				 SharedPreferences preferences = getPreferences(MODE_PRIVATE);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else {
			FileInputStream inputStream;
			InputStreamReader inputStreamReader;
			BufferedReader bufferedReader;
			StringBuilder sb = new StringBuilder();
			String line;
			try {
				
				inputStream = context.openFileInput(fileName);
				inputStreamReader = new InputStreamReader(inputStream);
				bufferedReader = new BufferedReader(inputStreamReader);

				if ((line = bufferedReader.readLine()) != null) {
					Log.d("pass", line+" fiiirst line");
					Log.d("pass", password+" paaassword");
					
					if (line.equalsIgnoreCase(password)) {
						this.passwordIsChecked = true;
						passwordAlertDialog.dismiss();
						displayList();
					} 
					else {
						Toast message = Toast.makeText(context,
								"Incorrect password!", 300);
						message.show();
						authenticationTrials++;

						if (authenticationTrials == 3) {
							try {
								rememberDate();
							} catch (IOException e) {
								e.printStackTrace();
							}
							exitApplication();
						} 
						else {
							showDialog(DIALOG_ALERT);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void encryptFile(){
		
		try {
			FileInputStream inputStream;
			InputStreamReader inputStreamReader;
			BufferedReader bufferedReader;
			StringBuilder sb = new StringBuilder();
			String line;
			
			inputStream = context.openFileInput(fileName);
			inputStreamReader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(inputStreamReader);
			int i = 0;
			while ((line = bufferedReader.readLine()) != null) {
				if(i != 0){
					adapter.addNewSecret(line);
				}
				else Log.d("pass", "firstLine "+line);
				i++;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void displayList(){
		
		adapter.removeSecrets();
		Log.d("listtt", adapter.getCount()+"");
		
		try {
			FileInputStream inputStream;
			InputStreamReader inputStreamReader;
			BufferedReader bufferedReader;
			StringBuilder sb = new StringBuilder();
			String line;
			
			inputStream = context.openFileInput(fileName);
			inputStreamReader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(inputStreamReader);
			int i = 0;
			while ((line = bufferedReader.readLine()) != null) {
				if(i != 0){
					adapter.addNewSecret(line);
				}
				else Log.d("pass", "firstLine "+line);
				i++;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void exitApplication(){
		MainActivity.this.finish();
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		this.authenticationTrials = 0;
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		  SharedPreferences preferences = getPreferences(MODE_PRIVATE);
		  this.passwordIsChecked = preferences.getBoolean("passwordIsChecked", false); // value to store    
		  Log.d("dialog", "on resume "+this.passwordIsChecked);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	Log.d("dialog", "back is pressed");
	        exitApplication();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View item, int position, long arg3) {
		// TODO Auto-generated method stub

			}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		Log.d("goin bananas", "checked");
		 if (buttonView.isChecked()) { 
		        Toast.makeText(getBaseContext(), "Checked", 
		        Toast.LENGTH_SHORT).show(); 
		      } 
		      else 
		      { 
		        Toast.makeText(getBaseContext(), "UnChecked", 
		        Toast.LENGTH_SHORT).show(); 
		      } 
		
	}


	private long getLockTime() throws IOException, Exception {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		File myFile = new File(getFilesDir(), "LockKey.txt");
		if (myFile.exists()) {
			BufferedReader br = new BufferedReader(new FileReader(myFile));
			String line = br.readLine();
			Date x = dateFormat.parse(line);
			br.close();
			long diff = getDateDiff(date, x, TimeUnit.MINUTES);
			return Math.abs(diff);
		}
		else return lockTime + 1;
	}
	
	public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
		long diffInMillies = date2.getTime() - date1.getTime();
		return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
	}
	
	private void rememberDate() throws IOException {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		File myFile = new File(getFilesDir() , "LockKey.txt");
		FileOutputStream fOut = new FileOutputStream(myFile);
		OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
		myOutWriter.append(dateFormat.format(date));
		myOutWriter.flush();
		myOutWriter.close();
	}

}