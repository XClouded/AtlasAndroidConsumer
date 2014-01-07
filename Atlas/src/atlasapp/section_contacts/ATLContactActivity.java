package atlasapp.section_contacts;

import java.io.File;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import atlasapp.model.ATLContactModel;
import atlasapp.section_appentry.R;


/**
 * Gets contact ,picked by the user through address book, name,email,phone
 * number and photo (if exsits) check whether contact already on Atlas and shows
 * the available option to contact this contact (email/phone/atlas)
 * 
 * return to the latest activity (called) with the ATLContactModel invitee made
 * for farther processing
 * 
 * @author sharonnachum
 * 
 */
public class ATLContactActivity extends Activity {

	private static ATLContactModel contact;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact);

		Bundle extras = getIntent().getExtras();

		contact = extras.getParcelable("com.atlasapp.model.ATLContactModel");

		TextView contactName = (TextView) findViewById(R.id.contactName);
		contactName.setText(contact.displayName());

		RelativeLayout atlasLayout = (RelativeLayout) findViewById(R.id.atlasLayout);
		atlasLayout.setVisibility(View.INVISIBLE);
		RelativeLayout mobileLayout = (RelativeLayout) findViewById(R.id.mobileLayout);
		RelativeLayout emailLayout = (RelativeLayout) findViewById(R.id.emailLayout);

//		if (contact.getImage()!=null){
//		//	ImageView imageView = (ImageView) findViewById(R.id.contactImage);
//			
			
			setProfilePic();
			
			
//		//	imageView.setImageBitmap(contact.getImage());
//		}
		if (contact!=null)
		{
		// todo - work number too
		if (contact.getPhoneNumber() != null) {
			Button mobileBtn = (Button) findViewById(R.id.mobileBtn);
			mobileBtn.setText(("mobile : " + contact.getPhoneNumber()));
			mobileBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {   
					// sendSMSAlert(name,mobile);
				}
			});

		} else {
			mobileLayout.setVisibility(View.INVISIBLE);
		}
		
		// todo - work email too
		if (contact.getEmailAddress() != null) {
			Button emailBtn = (Button) findViewById(R.id.emailBtn);
			emailBtn.setText(("email : " + contact.getEmailAddress()));
			emailBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
				}
			});
		} else {
			emailLayout.setVisibility(View.INVISIBLE);
		}
		}
		
        ImageButton closeButton = (ImageButton) findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        
        ImageButton editButton = (ImageButton) findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
	}
	private void setProfilePic() {	
		
//		ImageView contactPhoto = (ImageView) findViewById(R.id.contactImage);
		File FRIENDS_IMAGE_DIR = new File(Environment.getExternalStorageDirectory()
	            + "/Android/data/com.atlastpowered/files/Pictures/friendPics");
		String profilePicName = (contact.getAtlasId()!=null && !contact.getAtlasId().equals(""))?
				contact.getAtlasId():"";
		String destinationImagePath= "/"+profilePicName+".png";	
		Bitmap image = null;	
		if (!profilePicName.equals(""))
		{
			// Bitmap storedBitmap = null;
			 File PROFILE_PIC_PATH= new File (FRIENDS_IMAGE_DIR,destinationImagePath) ;
				if(PROFILE_PIC_PATH.exists()){
				//String filePath = applicationController.IMAGE_DIR+"/"+profilePicName+".png";
					 image = BitmapFactory.decodeFile(PROFILE_PIC_PATH.getAbsolutePath());
					ImageView imageView = (ImageView) findViewById(R.id.contactImage);
				
				
				
				
				
				imageView.setImageBitmap(image);
				}

			//	contactPhoto.setImageBitmap(storedBitmap);
			//	invitee.setImage(storedBitmap);
		}
		}

}
