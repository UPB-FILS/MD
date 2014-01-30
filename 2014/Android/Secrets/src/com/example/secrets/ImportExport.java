package com.example.secrets;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class ImportExport extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_import_export);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.import_export, menu);
		return true;
	}
	
	public void importFunction(View v){
		final Intent sender = getIntent();
		final String fileName = sender.getExtras().getString("FileName");
		final String resultedFileName = sender.getExtras().getString("ResultedFile");
		CaesarCode.decryptFile(fileName, resultedFileName);
		Toast message = Toast.makeText(ImportExport.this, "Your file has been decrypted", Toast.LENGTH_SHORT);
		message.show();
	}
	
	public void exportFunction(View v){
		final Intent sender = getIntent();
		final String fileName = sender.getExtras().getString("FileName");
		final String resultedFileName = sender.getExtras().getString("ResultedFile");
		try {
			CaesarCode.encryptFile(fileName, resultedFileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Toast message = Toast.makeText(ImportExport.this, "Your file has been encrypted", Toast.LENGTH_SHORT);
		message.show();
	}
	
	public void IEBackFunction(View v){
		Intent intent = new Intent(ImportExport.this, MainActivity.class);
		startActivity(intent);
		
	}

}
