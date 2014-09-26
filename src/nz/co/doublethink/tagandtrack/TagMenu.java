package nz.co.doublethink.tagandtrack;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class TagMenu {
	
	private String title;
	private String message;
	
	public TagMenu(){}
	
	public void menuAbout(Context context){
		new AlertDialog.Builder(context)
		.setTitle("About")
		.setMessage("Tag and Track is an application created to meet the needs of biodiversity researchers in the " +
				"field and the office. \nIf you would like to contribute contact me at stephen@doublethink.co.nz")
		.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		}).show();
	}
	
	public void menuHowTo(Context context, int token){
		if (token == 1){
			title = "Main Menu Help";
			message = "Navigate to the database by pressing the [Track] button or the magnifying glass button on the menu bar\n" +
					"To add a specimen to the database, press the [Tag] button or the + symbol on the menu bar";
		} 
		if (token == 2){
			title = "Add Help";
			message = "UID: is a unique identifier you decide to use to distinguish between specimen\nSpecies: may be the latin or" +
					" common species name\nPress the camera button to take a photo of the specimen\nUse the add button to add entry to the database";
		}
		if (token == 3){
			title = "Database Help";
			message = "Scroll with you finger to navigate the database\nClick on an entry to view it's details\nTo change search key, hold down on any of the entries and select an option";
		}
		if (token == 4){
			title = "Details Help";
			message = "Details of the chosen entry\nFor a larger view of the map, hold down on the map\nTo delete entry, expand the options menu and select [Delete]";
		}
		if (token == 5){
			title = "Map Zoom Help";
			message = "Figure it out yourself!";
		}
		
		new AlertDialog.Builder(context)
		.setTitle(title)
		.setMessage(message)
		.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		}).show();
	}

	
}