package nz.co.doublethink.tagandtrack;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class Database extends Activity {
	
	public static String FILENAME = "specimen_data.ser";
	private ListView lv;
    private CustomAdapter adapter;
    ArrayList<Specimen> animals;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_database);
		
		
		// Show the Up button in the action bar.
		setupActionBar();
		
		// If add has not already generated the file
		if (!Add.hasBeenGenerated)
			generateDatabase(this);
		displayAdapter();
		
		ListView listv = (ListView)findViewById(R.id.list_view);
		registerForContextMenu(listv);
		
	}
	
	@Override
	public void onCreateContextMenu(
	        ContextMenu menu,
	        View v,
	        ContextMenuInfo menuInfo) {
	 
	    super.onCreateContextMenu(menu, v, menuInfo);
	    
	    menu.setHeaderTitle("Sort by:");	 
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.database, menu);
	}
	
	public void displayAdapter(){
		animals = getArrayList(this);
		lv = (ListView) findViewById(R.id.list_view);
		adapter = new CustomAdapter(Database.this, R.id.list_view, animals);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener(){

		    @Override
		    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {   
		         entryClicked((int)id,animals);
		    }
		 });
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setIcon(R.drawable.edit_find);
		getActionBar().setTitle("Track");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		TagMenu menu = new TagMenu();
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.action_search:
			// do nothing
			break;
		case R.id.action_about:
			menu.menuAbout(this);
			break;
		case R.id.action_add:
			Intent intent = new Intent(this, Add.class);
			startActivity(intent);
			break;
		case R.id.action_help:
			menu.menuHowTo(this,3);
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public ArrayList<Specimen> generateDatabase(Context context){
		ArrayList<Specimen> animals = new ArrayList<Specimen>();
		animals = initialiseAnimalsArray(animals);
		FileOutputStream fos = null;
		ObjectOutputStream os = null;
		try {
			fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
			os = new ObjectOutputStream(fos);
			for (int i = 0; i < animals.size(); i++){
				os.writeObject(animals.get(i));
			}
			os.close();
		} catch (IOException e){
			e.printStackTrace();
		}
		return animals;
		
	}
	
	private ArrayList<Specimen> getArrayList(Context context) {
		ArrayList<Specimen> animals = new ArrayList<Specimen>();
		FileInputStream fos = null;
		ObjectInputStream os = null;
		try {
			fos = context.openFileInput(FILENAME);
			os = new ObjectInputStream(fos);
			Specimen current;
			while ((current= (Specimen)os.readObject()) != null){
				animals.add(current);
			}
			os.close();
		} catch (Exception e){
			e.printStackTrace();
		}
		return animals;
	}
	
	public ArrayList<Specimen> initialiseAnimalsArray(ArrayList<Specimen> animals){
		String[] uid = {"Dan", "Ralph", "Sarah", "Helen", "Robert", "Jessica", "Stephen", "Jeremy", "Jacob","Wendy"};
		String[] species = {"C. lupus","E. ferus","F. catus","P. leo","C. simum", "M. kaempferi", "P. tigris", "M. musculus", "D. carota", "F. pardalis"};
		String[] description = {"Dog\nHairy", "Horse", "Cat", "Lion", "Rhinoceros", "Crab", "Tiger blood", "Mouse","Carrot", "Chameleon"};
		String[] photo = {Uri.parse("android.resource://nz.co.doublethink.tagandtrack/"+R.drawable.dog).toString(),Uri.parse("android.resource://nz.co.doublethink.tagandtrack/"+R.drawable.horse).toString(),Uri.parse("android.resource://nz.co.doublethink.tagandtrack/"+R.drawable.cat).toString(),Uri.parse("android.resource://nz.co.doublethink.tagandtrack/"+R.drawable.lion).toString(),
				Uri.parse("android.resource://nz.co.doublethink.tagandtrack/"+R.drawable.rhino).toString(),Uri.parse("android.resource://nz.co.doublethink.tagandtrack/"+R.drawable.crab).toString(),Uri.parse("android.resource://nz.co.doublethink.tagandtrack/"+R.drawable.tiger).toString(),
				Uri.parse("android.resource://nz.co.doublethink.tagandtrack/"+R.drawable.mouse).toString(),Uri.parse("android.resource://nz.co.doublethink.tagandtrack/"+R.drawable.carrot).toString(),Uri.parse("android.resource://nz.co.doublethink.tagandtrack/"+R.drawable.chameleon).toString(),};
		for (int i =0; i < uid.length; i++){
			animals.add(new Specimen(uid[i],species[i],description[i],photo[i]));
		}
		return animals;
	}
	
	private void entryClicked(int id,ArrayList<Specimen> animals){
		
		Specimen clicked = animals.get(id);
		Intent intent = new Intent(this, Details.class);
		intent.putExtra("CLICKED", clicked);
		startActivity(intent);
		
	}

}
