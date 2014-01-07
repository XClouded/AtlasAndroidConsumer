package atlasapp.common;

//import atlasapp.database.TaskAssign;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;

public class SendEmailActivity extends Activity{
	
	
	String name,atlasId,emailAddress;
	private String item;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
        	 name = extras.getString("name");
        	 atlasId = extras.getString("atlasId");
        	 emailAddress = extras.getString("emailAddress");
        	 item = extras.getString("item");
        	 if (name!=null && !name.equals("") && atlasId!=null && !atlasId.equals("") && emailAddress!=null && !emailAddress.equals(""))
        		 inviteByEmail();
        	 else
        		 finish();
        }
	   
	}
	
	private void inviteByEmail() {
		
		//String inviteToAppID = generateInviteID(email, name);
		
		
		String url = "www.getatlas.com/reply/"+item+".html?id="+atlasId;
		Html.fromHtml("<a href='http://"+url+"'>http://"+url+"</a>");
	      
		String messageInvite = "Hey "+ name+" , I just discovered Atlas and thought you'd like to check it out. \n" +
				"It's a really slick new mobile planner that makes it really easy to manage your time and collaborate " +
				"with friends and colleagues. \n It makes stressful things like scheduling an event with someone really simple " +
				"and easy. \n No more endless back-and-forth emails to find the best time to meet up. And it's a calendar, task list, " +
				"and notebook -- all in one. "

				+"[Oh… and it syncs with iCal, Google Cal, Yahoo, Hotmail, and Exchange]."

				+"\n Just check it out when you get a sec -- it's free and definitely worth a look."
				+"iPhone app or Android app. ";
				
				  
				
		String    subjectRext = "Invite to Atlas";
		    
        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
 
        emailIntent.setType("plain/text");
   
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{ emailAddress});

        
    //    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,email);
 
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subjectRext);
 
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, messageInvite+Html.fromHtml("<a href='http://"+url+"'>http://"+url+"</a>"));
        
        

        SendEmailActivity.this.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        
        finish();

		
		
		           
	}

}
