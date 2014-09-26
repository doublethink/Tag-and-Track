package nz.co.doublethink.tagandtrack;

import java.io.Serializable;

import android.net.Uri;

public class Specimen implements Serializable {

	public String uid;
	public String species;
	public String description;
	public String photo;
	public String tagid;
	public boolean isDynamic;
	private static final long serialVersionUID = 1L;
	
	public Specimen (String uid, String species, String description, String photo){
		this.uid = uid;
		this.species = species;
		this.description = description;
		this.photo = photo;
		this.tagid = "3141296";
		isDynamic = false;
	}
	
	public Specimen (String uid, String species, String description, Uri uri, String tagid){
		this.uid = uid;
		this.species = species;
		this.description = description;
		this.photo = uri.toString();
		this.tagid = tagid;
		isDynamic = false;
	}
	
	public Specimen (String uid, String species, String description, Uri uri, boolean isDynamic, String tagid){
		this.uid = uid;
		this.species = species;
		this.description = description;
		this.photo = uri.toString();
		this.isDynamic = isDynamic;
		this.tagid = tagid;
	}
	

}
