package com.example.secrets;

import java.util.ArrayList;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class SecretsList extends BaseAdapter{
	
	private Activity context;
	ArrayList<Secret> secrets; 
	int checkedItems = 0;
	public Menu contextMenu;
	ArrayList<Integer> checkedBoxIndexes = new ArrayList<Integer>();  //indexes of the boxes that are checked
	
	public SecretsList(Activity context){
		this.context = context;
		secrets = new ArrayList<Secret>();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return secrets.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return secrets.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public int getCheckedItem(int position)
	{
		return checkedBoxIndexes.get(position);
	}
	
	public void uncheckAll(){
		checkedBoxIndexes = new ArrayList<Integer>();
		//checkedItems = 0;
		
		
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		View element;
		if (convertView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			element = inflater.inflate(R.layout.secret_row, null);
		} else
			element = convertView;
		final int a = position;
		CheckBox checkbox = (CheckBox)element.findViewById(R.id.checkBox1);
		checkbox.setTag(new Integer(position));
		checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() { 

		    @Override 
		    public void onCheckedChanged(CompoundButton buttonView, 
		                                            boolean isChecked) {
		    	
		    	Integer posSelected = (Integer)buttonView.getTag();  
		    	
		      // TODO Auto-generated method stub 
		    	if (buttonView.isChecked()) { 
			    	  //checkedBoxIndexes.add(posSelected);
			    	  checkedBoxIndexes.add(a);
			    	  notifyDataSetChanged();
			    	  Log.d("succes", "added succesfully");
			    	  checkedItems++;
			    	  Log.d("goin bananas", checkedItems+"");
			    	  contextMenu.findItem(R.id.action_delete).setEnabled(true);
			    	  contextMenu.findItem(R.id.action_delete).setVisible(true);
			    	  if(checkedItems==1){
			    		  Log.d("goin bananas", "it should edit");
			    		  contextMenu.findItem(R.id.action_edit).setEnabled(true);
			    		  contextMenu.findItem(R.id.action_edit).setVisible(true);
			    	  }
			    if(checkedItems>1){
		    		contextMenu.findItem(R.id.action_edit).setEnabled(false);
		    		contextMenu.findItem(R.id.action_edit).setVisible(false);
		    	}
		        Toast.makeText(context, "Checked item number: " + (posSelected + 1), 
		        Toast.LENGTH_SHORT).show(); 
		        
		      } 
		      else 
		      { 
		    	  checkedBoxIndexes.remove(posSelected);
		    	  notifyDataSetChanged();
		    	  Log.d("succes", "deleted succesfully");
		    	  checkedItems--;
		    	  Log.d("goin bananas", checkedItems+"");
		    	  if(checkedItems==0){
		    		  contextMenu.findItem(R.id.action_edit).setEnabled(false);
		    		  contextMenu.findItem(R.id.action_delete).setEnabled(false);
		    		  contextMenu.findItem(R.id.action_edit).setVisible(false);
		    		  contextMenu.findItem(R.id.action_delete).setVisible(false);
		    	  }
		    	  else{
		    		  contextMenu.findItem(R.id.action_delete).setEnabled(true);
		    		  contextMenu.findItem(R.id.action_delete).setVisible(true);
		    		  if(checkedItems==1){
				    		contextMenu.findItem(R.id.action_edit).setEnabled(true);
				    		contextMenu.findItem(R.id.action_edit).setVisible(true);
		    		  }
		    		  
		    	  }
		    	  Toast.makeText(context, "UnChecked", 
		    			  Toast.LENGTH_SHORT).show(); 
		      }
		      
		      
		    } 
		  }); 
		this.notifyDataSetChanged();
		TextView description = (TextView) element.findViewById(R.id.secret);
		description.setText(secrets.get(position).getDescription());
		return element;
	}
	
	public void editSecretAt(String description, int position){	
		secrets.get(position).setDescription(description);
		this.notifyDataSetChanged();
	}
	
	@Override
	public boolean isEnabled(int position) {
	    return false;
	}
	
	public void addNewSecret(String description){
		Secret secret = new Secret(description);
		secrets.add(secret);
		Log.d("lista", "added: "+secret.getDescription());
		this.notifyDataSetChanged();
	}
	
	public void removeSecrets(){
		secrets.clear();
		this.notifyDataSetChanged();
	}

}
