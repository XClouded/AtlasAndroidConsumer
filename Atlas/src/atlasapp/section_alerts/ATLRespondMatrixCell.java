package atlasapp.section_alerts;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import atlasapp.common.ATLUser;
import atlasapp.common.UtilitiesProject;
import atlasapp.database.DatabaseConstants.EVENT_STATUS;
import atlasapp.database.DatabaseConstants.ITEM_TYPE_PRIORITY_ORDER;
import atlasapp.database.DatabaseConstants.ITEM_USER_TASK_STATUS;
import atlasapp.database.EventProperties;
import atlasapp.database.ItemUserProperties;
import atlasapp.model.ATLContactModel;
import atlasapp.section_appentry.R;

public class ATLRespondMatrixCell extends RelativeLayout {
	Resources res;
	Drawable drawable;
	Context context;
	ImageView altOneImage,altTwoImage,altThreeImage,inviteeImage;
	private int decideOkImage;
	private Drawable drawableOK;
	private Drawable drawableIdeal;
	private Drawable drawableCant;
	private int altTimes = 3;
	private TextView inviteeName;
	////Respond Matrix decides
	public static enum EVENT_DECIDE { OK ("respond_matrix_decide_ok"), 
									IDEAL("respond_matrix_decide_ideal"),
									CANT("respond_matrix_decide_cant");
			private final String eventDecideName;  
	  
			private EVENT_DECIDE(String eventDecideName) {    
				this.eventDecideName = eventDecideName;  
			}  
	  
			public String getEventTypeName() {  
				return eventDecideName;  
			}  
		}
	
	public ATLRespondMatrixCell(Context context) {
		super(context);
		initView(context);
	}
	public ATLRespondMatrixCell(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	public ATLRespondMatrixCell(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}
	private void initView(Context context) {
		LayoutInflater.from(context).inflate(R.layout.atl_respond_matrix_cell, this, true);
		inviteeImage = (ImageView) findViewById(R.id.inviteeImage);
		inviteeName = (TextView)findViewById(R.id.inviteeName);
		altOneImage = (ImageView) findViewById(R.id.altOneImage);
		altTwoImage = (ImageView) findViewById(R.id.altTwoImgage);
		altThreeImage = (ImageView) findViewById(R.id.altThreeImage);
		
		
	
		this.context = context;
		res = getResources();
		
		
		int decideOk  = res.getIdentifier(EVENT_DECIDE.OK.getEventTypeName() , "drawable", context.getPackageName());
		int decideIdeal  = res.getIdentifier(EVENT_DECIDE.IDEAL.getEventTypeName() , "drawable", context.getPackageName());
		int decideCant  = res.getIdentifier(EVENT_DECIDE.CANT.getEventTypeName() , "drawable", context.getPackageName());
		drawableOK = res.getDrawable(decideOk );
		drawableIdeal = res.getDrawable(decideIdeal );
		drawableCant = res.getDrawable(decideCant );
	}
	
	
	public void setRespondMatrixCell(ArrayList<ItemUserProperties> itemUser,ArrayList<EventProperties> event)
	{
		if (itemUser!=null && itemUser.size()>0)
		{
			this.altTimes  = event.size();
			Bitmap pic;  
			ATLContactModel invitee = ATLContactModel.getContactByAtlasId(itemUser.get(0).atlasId);
			String name = "";
			if (invitee!=null && invitee.getFirstname()!=null)
				name =(invitee.getLastname()!=null)? invitee.getFirstname()+" "+invitee.getLastname():invitee.getFirstname();
			name = (itemUser.get(0).atlasId.equals(ATLUser.getParseUserID()))? "Me ":name;
			inviteeName.setText(name);
				
			 pic = (ATLUser.getParseUserID().equals(itemUser.get(0).atlasId))?UtilitiesProject.getUserProfilePic():
					 UtilitiesProject.getProfilePic(itemUser.get(0).atlasId);
			if (pic!=null)
				inviteeImage.setImageBitmap(pic);
//			
//			ITEM_TYPE_PRIORITY_ORDER priorityOrder = itemUser.priorityOrder;
//			priorityOrder = (priorityOrder!=null)? 
//				
//						priorityOrder : ITEM_TYPE_PRIORITY_ORDER.IDEAL;
//			int timeChosen =	(itemUser.status.equals(ITEM_USER_TASK_STATUS.DECLINED))? -2:
//				(itemUser.status.equals(ITEM_USER_TASK_STATUS.SENT))?-1:
//					(itemUser.atlasId.equals(ATLUser.getParseUserID()) && itemUser.getEventAssociated().atlasId.equals(ATLUser.getParseUserID()))?
//							(itemUser.getEventAssociated().status.equals(EVENT_STATUS.THE_ONE))?(itemUser.getEventAssociated().displayOrder):
//							(itemUser.getEventAssociated().status.equals(EVENT_STATUS.NOT_THE_ONE))?-2:
//											 -1:
//					(priorityOrder.equals(ITEM_TYPE_PRIORITY_ORDER.IDEAL))? itemUser.getEventAssociated().displayOrder:
//							 -1;
//					
			if (event.get(0).atlasId.equals(itemUser.get(0).atlasId))
				setInviterImages();  
			else
				
				setImages(itemUser);
			
		}
	}
	private void setInviterImages() {
		altOneImage.setImageDrawable(drawableOK);
		altTwoImage.setImageDrawable(drawableOK);
		altThreeImage.setImageDrawable(drawableOK);
		
	}
	private void setImages(ArrayList<ItemUserProperties> itemUserRecords) 
	{
		int altNum = 0;
		EventProperties event;
		
		for (ItemUserProperties itemUser:itemUserRecords)
		{
			event = itemUser.getEventAssociated();
			altNum = event.displayOrder;
			setImageVoteDrawable(altNum,getDrawableFromPriority(itemUser.priorityOrder));
			
		}
		
	}
	private Drawable getDrawableFromPriority(ITEM_TYPE_PRIORITY_ORDER decidePriority)
	{
		Drawable drawable = drawableOK;
		
		switch (decidePriority)
		{
		case IDEAL:drawable = drawableIdeal;
			break;
		case OK:drawable = drawableOK;
		break;
		case DECLINED:drawable = drawableCant;
		break;
		}
		
		  
		return drawable;
		
	}
	private void  setImageVoteDrawable(int altNum,Drawable decide )
	{
		
		
		switch (altNum)
		{
		case 0:altOneImage.setImageDrawable(decide);
		break;
		case 1:altTwoImage.setImageDrawable(decide);
		break;
		case 2:altThreeImage.setImageDrawable(decide);
		break;
		}   
		
	}
	private void setImages(int timeChosen,int altTimes)
	{
		
	
		switch (timeChosen)
		{
		case -2:
			/// declined
			altOneImage.setImageDrawable(drawableCant);
			altTwoImage.setImageDrawable(drawableCant);
			altThreeImage.setImageDrawable(drawableCant);
			break;
		case -1:
			//// Pending still
			altOneImage.setImageDrawable(drawableIdeal);
			altTwoImage.setImageDrawable(drawableIdeal);
			altThreeImage.setImageDrawable(drawableIdeal);
			break;
		case 0:
			altOneImage.setImageDrawable(drawableOK);
			altTwoImage.setImageDrawable(drawableCant);
			altThreeImage.setImageDrawable(drawableCant);
			
			break;
		case 1:
			altOneImage.setImageDrawable(drawableCant);
			altTwoImage.setImageDrawable(drawableOK);
			altThreeImage.setImageDrawable(drawableCant);
			break;
		case 2:
			altOneImage.setImageDrawable(drawableCant);
			altTwoImage.setImageDrawable(drawableCant);
			altThreeImage.setImageDrawable(drawableOK);
			break;
		
			
		}
		
		if (altTimes<3)
		{
			altOneImage.setVisibility(View.GONE);
			if (altTimes<2)
				altTwoImage.setVisibility(View.GONE);
		}
		

	}


}
