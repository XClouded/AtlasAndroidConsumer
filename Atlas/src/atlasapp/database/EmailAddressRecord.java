package atlasapp.database;

import com.parse.ParseObject;


public class EmailAddressRecord {
	
	
	public  String objectId = "";
	public  String atlasId = "";
	public  String emailAddress = "";
	public  String emailAddressId = "";
	public  boolean  isVerified = false;
	
	
	EmailAddressRecord(String objectId, String atlasId, String emailAddress, String emailAddressId ,boolean  isVarified )
	{
		this.objectId = (objectId!=null)? objectId: "";
		this.atlasId = (atlasId!=null)? atlasId: "";
		this.emailAddress = (emailAddress!=null)? emailAddress: "";
		this.emailAddressId = (emailAddressId!=null)? emailAddressId: "";
		this.isVerified = isVarified;
		
	}
	/**
	 * Gets a row record from the Parse email_address table (as a ParseObject)
	 * & return an object EmailAddressRecord
	 * @param parseEmailAddressRecord
	 * @return
	 */
	public static EmailAddressRecord fromParseObject(ParseObject parseEmailAddressRecord)
	{
		EmailAddressRecord emailAddressRecord = null;
		if (parseEmailAddressRecord!=null)
		{
			 emailAddressRecord = new EmailAddressRecord(parseEmailAddressRecord.getObjectId(),
					 parseEmailAddressRecord.getString("atlas_id"),
					 parseEmailAddressRecord.getString("email_address"),
					 parseEmailAddressRecord.getString("email_address_id"),
					 parseEmailAddressRecord.getBoolean("is_verified"));
			
		}
		
		
		return emailAddressRecord;
	}
	

}
