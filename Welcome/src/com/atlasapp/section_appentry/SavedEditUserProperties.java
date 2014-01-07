package com.atlasapp.section_appentry;

import com.atlasapp.atlas_database.AtlasServerConnect;

public class SavedEditUserProperties {
	
	public static String email = "";
	public static String firstName = "";
	public static String lastName = "";
	public static String password = "";
	private static SavedEditUserProperties savedEditUserProperties;
	
	private SavedEditUserProperties()
	{
		
	}
	public static SavedEditUserProperties getSingletonObject()
	{
		  if (savedEditUserProperties == null)
	      {
			  savedEditUserProperties = new SavedEditUserProperties();
	    	
	      }
	      return savedEditUserProperties;
	}
	

}
