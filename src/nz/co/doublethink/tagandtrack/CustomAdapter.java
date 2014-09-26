package nz.co.doublethink.tagandtrack;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends ArrayAdapter<Specimen>{
	
	private ArrayList<Specimen> entries;
    private Activity activity;
    
    public CustomAdapter(Activity a, int textViewResourceId, ArrayList<Specimen> entries) {
        super(a, textViewResourceId, entries);
        this.entries = entries;
        this.activity = a;
    }
    
    public static class ViewHolder{
        public TextView item1;
        public TextView item2;
        public ImageView image;
        public Bitmap scaledbitmap = null;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder holder;
        if (v == null) {
            LayoutInflater vi =
                (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.grid_item, null);
            holder = new ViewHolder();
            holder.item1 = (TextView) v.findViewById(R.id.big);
            holder.item2 = (TextView) v.findViewById(R.id.small);
            holder.image = (ImageView) v.findViewById(R.id.image);
            v.setTag(holder);
        }
        else
            holder=(ViewHolder)v.getTag();
 
        final Specimen custom = entries.get(position);
        if (custom != null) {
            holder.item1.setText(custom.uid);
            holder.item2.setText(custom.species);
            Uri uri = Uri.parse(custom.photo);
            holder.image.setImageURI(null);
            if (!custom.isDynamic){
	            holder.image.setImageURI(uri);
            } else {
            	holder.image.setImageBitmap(dynamicBitmap(convertView,uri,holder));
            }
        }
        return v;
    }
    
    @SuppressWarnings("static-access")
	private Bitmap dynamicBitmap(View view, Uri uri,ViewHolder holder){
    	
    	if (holder.scaledbitmap==null){
	    try{
			Bitmap originalbitmap = MediaStore.Images.Media.getBitmap(view.getContext().getContentResolver(), uri);
			holder.scaledbitmap = originalbitmap.createScaledBitmap(originalbitmap, 90, 160, true);
			
		} catch (Exception e){
			System.out.println("Failed to get image");
		}
    	}
	    return holder.scaledbitmap;
    }

}
