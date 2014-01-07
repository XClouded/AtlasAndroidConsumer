package com.atlasapp.section_settings;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.atlasapp.section_appentry.AtlasApplication;
import com.atlasapp.section_appentry.R;

public class ATLEmailInviteToApp extends Activity {
    Button send;
    EditText address, subject, emailtext;
	private String emailAddress;

    
    @SuppressLint("NewApi")
	@Override
public void onCreate(Bundle savedInstanceState) {
       
    	
    	super.onCreate(savedInstanceState);
    setContentView(R.layout.email_app_invite);
    send=(Button) findViewById(R.id.emailsendbutton);
    address=(EditText) findViewById(R.id.emailaddress);
    subject=(EditText) findViewById(R.id.emailsubject);
    emailtext=(EditText) findViewById(R.id.emailtext);
   
	 Bundle extras = getIntent().getExtras();

    
    if (extras != null) {
    	emailAddress = extras.getString("email");
    }
    
    
    emailtext.setText("Atlas is a mobile planner that allows you not only to manage your own time but to collaborate with anyone on any platform with calendars, tasks, notes and contacts.");
    address.setText(emailAddress);
    subject.setText("Invite to Atlas");
    
    send.setOnClickListener(new View.OnClickListener() {
                   
                    @Override
                    public void onClick(View v) {
                            // TODO Auto-generated method stub
                             
                                  final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                           
                                  emailIntent.setType("plain/text");
                             
                                  emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{ address.getText().toString()});
                           
                                  emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject.getText());
                           
                                  emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, emailtext.getText());
                                  
                                  
                   
                                  ATLEmailInviteToApp.this.startActivity(Intent.createChooser(emailIntent, "Send mail..."));

                    }
            });
    send.callOnClick(); 
}
}