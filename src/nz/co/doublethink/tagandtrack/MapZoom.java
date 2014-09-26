package nz.co.doublethink.tagandtrack;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

public class MapZoom extends Activity {

	ImageView iv;
	LayoutParams params;
	LinearLayout layout;
	TextView tv;
	PopupWindow popUp;
	boolean click = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		popUp = new PopupWindow(this);
		layout = new LinearLayout(this);
		tv = new TextView(this);
		params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		layout.setOrientation(LinearLayout.VERTICAL);
		tv.setText("Latitude: -41.3 Longitude: 174.7\nDate: 5 June 2013\nTime: 0813 hours");
		tv.setBackgroundColor(Color.parseColor("#91c8f4"));
		layout.addView(tv, params);
		popUp.setContentView(layout);
		
		setContentView(R.layout.activity_map_zoom);
		// Show the Up button in the action bar.
		setupActionBar();
		
		iv = (ImageView)findViewById(R.id.map_zoom_image);
		iv.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v){
				if (click){
					popUp.showAtLocation(layout, Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 10, 10);
		        	popUp.update(150, -150, 300, 145);
		        	click = false;
				} else {
					popUp.dismiss();
					click = true;
				}
			}
		});
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle("Details");

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
			//NavUtils.navigateUpFromSameTask(this);
			finish();
			return true;
		case R.id.action_search:
			Intent intent = new Intent(this, Database.class);
			startActivity(intent);
			finish();
			break;
		case R.id.action_about:
			menu.menuAbout(this);
			break;
		case R.id.action_add:
			Intent addintent = new Intent(this, Add.class);
			startActivity(addintent);
			finish();
			break;
		case R.id.action_help:
			menu.menuHowTo(this,5);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
