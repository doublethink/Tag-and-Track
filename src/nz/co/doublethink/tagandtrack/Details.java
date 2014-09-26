package nz.co.doublethink.tagandtrack;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

public class Details extends Activity {
	
	Specimen specimen;
	GoogleMap map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);
		// Show the Up button in the action bar.
		setupActionBar();
		Intent intent = getIntent();
		specimen = (Specimen)intent.getSerializableExtra("CLICKED");
		propagateEntry();
		map = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
		setUpMap();
		
	}

	private void setUpMap() {
		if (map != null){
			map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
			map.setMyLocationEnabled(true);
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-41.289384,174.767461),(float)15.0));
			
			map.setOnMapLongClickListener(new OnMapLongClickListener(){
				
				@Override
				public void onMapLongClick(LatLng point){
					zoomMap();
					
				}
			});
			
		}
			
		
	}
	
	public void zoomMap(){
		
		Intent intent = new Intent(this, MapZoom.class);
		startActivity(intent);
	}

	private void propagateEntry() {
		
		TextView[] tvArray = {(TextView)findViewById(R.id.details_name),(TextView)findViewById(R.id.details_species),(TextView)findViewById(R.id.details_description),(TextView)findViewById(R.id.details_tagid)};
		tvArray[0].setText(specimen.uid);
		tvArray[1].setText(specimen.species);
		tvArray[2].setText(specimen.description);
		tvArray[3].setText(specimen.tagid);
		ImageView image = (ImageView)findViewById(R.id.details_photo);
		Uri uri = Uri.parse(specimen.photo);
		if (!specimen.isDynamic)
			image.setImageURI(uri);
		else 
			image.setImageBitmap(dynamicBitmap(image,uri));
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.details, menu);
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
			Intent addintent = new Intent(this, Add.class);
			startActivity(addintent);
			break;
		case R.id.action_help:
			menu.menuHowTo(this,4);
			break;
		case R.id.action_delete:
			delete();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@SuppressWarnings("static-access")
	private Bitmap dynamicBitmap(View view, Uri uri){
    	
    	Bitmap scaledbitmap = null;
	    try{
			Bitmap originalbitmap = MediaStore.Images.Media.getBitmap(view.getContext().getContentResolver(), uri);
			scaledbitmap = originalbitmap.createScaledBitmap(originalbitmap, 90, 160, true);
			
		} catch (Exception e){
			System.out.println("Failed to get image");
		}
	    return scaledbitmap;
    }
	
	private void delete() {
		AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
		 myAlertDialog.setTitle("Delete Entry?");
		 myAlertDialog.setMessage("Are you sure you would like to delete this entry?");
		 myAlertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

		  public void onClick(DialogInterface arg0, int arg1) {
			  deleteOK();
		  }});
		 myAlertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		       
		  public void onClick(DialogInterface arg0, int arg1) {
		  // do nothing
		  }});
		 myAlertDialog.show();
		 
	}
	
	private void deleteOK(){
		Toast.makeText(this, specimen.uid +" deleted", Toast.LENGTH_LONG).show();
		NavUtils.navigateUpFromSameTask(this);
	}

}
