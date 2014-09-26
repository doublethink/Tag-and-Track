package nz.co.doublethink.tagandtrack;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


public class Add extends Activity {
	
	private Uri fileUri;
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	public static boolean hasBeenGenerated = false;
	private static ArrayList<Specimen> dynamicSpecimen = new ArrayList<Specimen>();
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);
		// Show the Up button in the action bar.
		setupActionBar();
	}
	
	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setIcon(R.drawable.add_icon);
		getActionBar().setTitle("Tag");
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
			Intent intent = new Intent(this, Database.class);
			startActivity(intent);
			break;
		case R.id.action_about:
			menu.menuAbout(this);
			break;
		case R.id.action_add:
			resetActivity(super.getCurrentFocus());
			break;
		case R.id.action_help:
			menu.menuHowTo(this,2);
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void resetActivity(final View view){
		AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
		 myAlertDialog.setTitle("Reset");
		 myAlertDialog.setMessage("Are you sure you would like to reset?\nYou will all your progress on this entry");
		 myAlertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

		  public void onClick(DialogInterface arg0, int arg1) {
		  	removeText(2, view.getContext());
		  }});
		 myAlertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		       
		  public void onClick(DialogInterface arg0, int arg1) {
		  // do nothing
		  }});
		 myAlertDialog.show();
	}
	
	public void confirmAdd(final View view){
		if (checkEntryNotComplete()){
			entryNotComplete();
			return;
		}
		AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
		 myAlertDialog.setTitle("Add");
		 myAlertDialog.setMessage("Add to database?");
		 myAlertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

		  public void onClick(DialogInterface arg0, int arg1) {
		  	removeText(1, view.getContext()); // for if statement inside method
		  }});
		 myAlertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		       
		  public void onClick(DialogInterface arg0, int arg1) {
		  // do nothing
		  }});
		 myAlertDialog.show();
	}
	
	// Helper method that clears data from all editTexts in activity as action not allowed inside onClickListener
	// input token is necessary to see what button is pushed
	public void removeText(int token, Context context){
		EditText textArray[] = {(EditText)findViewById(R.id.UID_text),(EditText)findViewById(R.id.species_text),(EditText)findViewById(R.id.description_text),(EditText)findViewById(R.id.tag_text)};
		String uid = "";
		try {
			uid = textArray[0].getEditableText().toString();
			String species = textArray[1].getEditableText().toString();
			String description = textArray[2].getEditableText().toString();
			String tagid = textArray[3].getEditableText().toString();
			dynamicSpecimen.add(new Specimen(uid,species,description,fileUri,true,tagid));
			if (token == 1)
				writeToFile(context);
		} catch (Exception e){
		} finally {
			clearTextEntries(context, textArray);
		}
		
		String toast;
		if (token == 1)
			toast = uid + " added to database";
		else
			toast = "Page reset";
		Toast.makeText(this, toast, Toast.LENGTH_LONG).show();
	}
	
	private void clearTextEntries(Context context, EditText[] textArray) {
		for(int i = 0; i < textArray.length; i++){	
			textArray[i].setText("");
		}
		ImageView image = (ImageView) findViewById(R.id.photo);
		image.setImageDrawable(getResources().getDrawable(R.drawable.image_not_available));
		
	}

	public void takePhoto(View view){
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		
	    if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
	        if (resultCode == RESULT_OK) {
	            // Image captured and saved to fileUri specified in the Intent
	        	try{
	        		fileUri = data.getData();
	        		Bitmap originalbitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), fileUri);
	        		@SuppressWarnings("static-access")
					Bitmap scaledbitmap = originalbitmap.createScaledBitmap(originalbitmap, 101, 170, true);
	        		ImageView image = (ImageView) findViewById(R.id.photo);
	        		image.setImageBitmap(scaledbitmap);
	        		
	        	} catch (Exception e){
	        		System.out.println("Failed to get image");
	        	}
	        } else if (resultCode == RESULT_CANCELED) {
	            // User cancelled the image capture
	        } else {
	            // Image capture failed, advise user
	        }
	    }
	}
	
	private void writeToFile(Context context){
		ArrayList<Specimen> animals = new ArrayList<Specimen>();
		animals = initialiseAnimalsArray(animals);
		FileOutputStream fos = null;
		ObjectOutputStream os = null;
		try {
			//context.deleteFile(FILENAME);
			fos = context.openFileOutput(Database.FILENAME, Context.MODE_PRIVATE);
			os = new ObjectOutputStream(fos);
			for (int i = 0; i < animals.size(); i++){
				os.writeObject(animals.get(i));
			}
			for (int j = 0; j < dynamicSpecimen.size(); j++){
				os.writeObject(dynamicSpecimen.get(j));
			}
			os.close();
		} catch (IOException e){
			e.printStackTrace();
		}
		hasBeenGenerated = true;
	}
	
	public ArrayList<Specimen> initialiseAnimalsArray(ArrayList<Specimen> animals){
		String[] uid = {"Dan", "Ralph", "Sarah", "Helen", "Robert", "Jessica", "Stephen", "Jeremy", "Jacob","Wendy"};
		String[] species = {"C. lupus","E. ferus","F. catus","P. leo","C. simum", "M. kaempferi", "P. tigris", "M. musculus", "D. carota", "F. pardalis"};
		String[] description = {"Dog", "Horse", "Cat", "Lion", "Rhinoceros", "Crab", "Tiger blood", "Mouse","Carrot", "Chameleon"};
		String[] photo = {Uri.parse("android.resource://nz.co.doublethink.tagandtrack/"+R.drawable.dog).toString(),Uri.parse("android.resource://nz.co.doublethink.tagandtrack/"+R.drawable.horse).toString(),Uri.parse("android.resource://nz.co.doublethink.tagandtrack/"+R.drawable.cat).toString(),Uri.parse("android.resource://nz.co.doublethink.tagandtrack/"+R.drawable.lion).toString(),
				Uri.parse("android.resource://nz.co.doublethink.tagandtrack/"+R.drawable.rhino).toString(),Uri.parse("android.resource://nz.co.doublethink.tagandtrack/"+R.drawable.crab).toString(),Uri.parse("android.resource://nz.co.doublethink.tagandtrack/"+R.drawable.tiger).toString(),
				Uri.parse("android.resource://nz.co.doublethink.tagandtrack/"+R.drawable.mouse).toString(),Uri.parse("android.resource://nz.co.doublethink.tagandtrack/"+R.drawable.carrot).toString(),Uri.parse("android.resource://nz.co.doublethink.tagandtrack/"+R.drawable.chameleon).toString(),};
		for (int i =0; i < uid.length; i++){
			animals.add(new Specimen(uid[i],species[i],description[i],photo[i]));
		}
		return animals;
	}
	
	private void entryNotComplete () {
		AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
		 myAlertDialog.setMessage("You must complete all sections");
		 myAlertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

		  public void onClick(DialogInterface arg0, int arg1) {
			//Do nothing  
		  }});
		 myAlertDialog.show();
	}
	
	// Debug method that carries out retreive operations, and parse text, if empty string exception returns true
	private boolean checkEntryNotComplete(){
		try {
			EditText textArray[] = {(EditText)findViewById(R.id.UID_text),(EditText)findViewById(R.id.species_text),(EditText)findViewById(R.id.description_text),(EditText)findViewById(R.id.tag_text)};
			
			String uid = textArray[0].getEditableText().toString();
			String species = textArray[1].getEditableText().toString();
			String description = textArray[2].getEditableText().toString();
			@SuppressWarnings("unused")
			Specimen newEntry = new Specimen(uid,species,description,fileUri.toString());
		} catch (Exception e){
			return true;
		}
		return false;
	}

}
