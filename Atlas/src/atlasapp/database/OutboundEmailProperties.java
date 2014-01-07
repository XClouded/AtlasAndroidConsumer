package atlasapp.database;

import com.parse.ParseObject;

public class OutboundEmailProperties 
{
	
	
	public String body="";
	public String from="";
	public String fromName="";
	public boolean hasError = false;
	public boolean isPending = false;
	public boolean isSent = false;
	public String replyTo = "";
	public String subject="";
	public String to="";
	
	
	
	public OutboundEmailProperties()
	{
		
	}
			
	public void setOutboundEmailProperties(
			String setBody, String setFrom,
			String setFromName, boolean setHasError,
			boolean setIsPending,boolean setIsSent,
			String setReplyTo , String setSubject, 
			String setTo)
	{
		body = (setBody!=null && !setBody.equals(""))? setBody:body;
		from = (setFrom!=null && !setFrom.equals(""))? setFrom:from;
		fromName = (setFromName!=null && !setFromName.equals(""))? setFromName:fromName;
		replyTo = (setReplyTo!=null && !setReplyTo.equals(""))? setReplyTo:replyTo;
		subject = (setSubject!=null && !setSubject.equals(""))? setSubject:subject;
		to = (setTo!=null && !setTo.equals(""))? setTo:to;
		hasError = setHasError;
		isPending = setIsPending;
		isSent = setIsSent;
	}
	public static OutboundEmailProperties getOutboundEmailPropertiesFromParseObjec(ParseObject outboundRecord)
	{
		OutboundEmailProperties outboundEmailProperties = null;
		if (outboundRecord!=null)
		{
			outboundEmailProperties  =new OutboundEmailProperties();
			outboundEmailProperties.body = outboundRecord.getString("body");
			outboundEmailProperties.from = outboundRecord.getString("from");
			outboundEmailProperties.fromName =outboundRecord.getString("from_name");
			outboundEmailProperties.replyTo = outboundRecord.getString("reply_to");
			outboundEmailProperties.subject =outboundRecord.getString("subject");
			outboundEmailProperties.to = outboundRecord.getString("to");
			outboundEmailProperties.hasError = outboundRecord.getBoolean("has_error");
			outboundEmailProperties.isPending = outboundRecord.getBoolean("is_pending");
			outboundEmailProperties.isSent = outboundRecord.getBoolean("is_sent");
		}
		return outboundEmailProperties;
	}
	

}
