package nz.co.doublethink.tagandtrack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    	super.onOptionsItemSelected(item);
    	TagMenu menu = new TagMenu();
    	switch(item.getItemId()){
    	case R.id.action_add :
    		Intent intent = new Intent(this, Add.class);
    		startActivity(intent);
    		break;
    	case R.id.action_search :
    		Intent intent2 = new Intent(this, Database.class);
    		startActivity(intent2);
    		break;
    	case R.id.action_about:
    		menu.menuAbout(this);
    		break;
    	case R.id.action_help:
    		menu.menuHowTo(this, 1);
    		break;
    	}
    	
    	return true;
    }
    
    // Called when add button pressed
    public void addButton (View view){
    	Intent intent = new Intent(this, Add.class);
    	startActivity(intent);
    }
    
    // Called when the search database button is pressed
    public void searchButton (View view){
    	Intent intent = new Intent(this, Database.class);
    	startActivity(intent);
    	
    }
    
}
