package atlasapp.common;

import java.util.ArrayList;

import atlasapp.database.AtlasServerConnect;
import atlasapp.database.DatabaseConstants.TABLE_ALERTS;
import atlasapp.model.ATLAlertModel;
import atlasapp.section_appentry.AtlasApplication;
import com.parse.ParseObject;

import android.content.Context;

/**
 * 
 * @author sharonnachum
 *
 */
public class ATLCurrentsUserAlerts {
	
	
	private static Context mContext;
	
	public static ArrayList<ATLAlertModel> currentUserEventAlerts,currentUserTaskAlerts,allCurrentUserAlerts;
	private static ATLCurrentsUserAlerts atlCurrentsUserAlerts;

	private static AtlasApplication applicationController;

	private static AtlasServerConnect parseConnect;

  
	private ATLCurrentsUserAlerts()
	{
		
	}
	public void setCurrentAllUserAlerts(ArrayList<ATLAlertModel> allCurrentUserAlerts)
	{
		if (allCurrentUserAlerts!=null && allCurrentUserAlerts.size()>0)
			if (atlCurrentsUserAlerts.allCurrentUserAlerts.size()==0)
				atlCurrentsUserAlerts.allCurrentUserAlerts = allCurrentUserAlerts;
			else
				atlCurrentsUserAlerts.allCurrentUserAlerts.addAll(allCurrentUserAlerts);
	}
	public ArrayList<ATLAlertModel> getAllCurrentUserAlert ()
	{
		return atlCurrentsUserAlerts.allCurrentUserAlerts;
	}
	
	
	
	public void setCurrentUserEventAlerts(ArrayList<ATLAlertModel> currentUserEventAlerts)
	{
		if (currentUserEventAlerts!=null && currentUserEventAlerts.size()>0)
		atlCurrentsUserAlerts.currentUserEventAlerts = currentUserEventAlerts;
	}
	public ArrayList<ATLAlertModel> getCurrentUserEventAlert ()
	{
		return atlCurrentsUserAlerts.currentUserEventAlerts;
	}
	public void setCurrentUserTaskAlerts(ArrayList<ATLAlertModel> currentUserTaskAlerts)
	{
		if (currentUserEventAlerts!=null && currentUserEventAlerts.size()>0)

		atlCurrentsUserAlerts.currentUserTaskAlerts = currentUserTaskAlerts;
	}
	public ArrayList<ATLAlertModel> getCurrentUserTaskAlert ()
	{
		return atlCurrentsUserAlerts.currentUserTaskAlerts;
	}
	
	public static ATLCurrentsUserAlerts getSingletonObject(Context context)
	{
	      if (atlCurrentsUserAlerts == null)
	      {
	    	  atlCurrentsUserAlerts = new ATLCurrentsUserAlerts();
	    	  ATLCurrentsUserAlerts.mContext = context;
	          applicationController = (AtlasApplication)context;
	          parseConnect =   AtlasServerConnect.getSingletonObject(null);
	          currentUserEventAlerts = new ArrayList<ATLAlertModel>();
	          currentUserTaskAlerts = new ArrayList<ATLAlertModel>();
	          allCurrentUserAlerts = new ArrayList<ATLAlertModel>();
	          // 
	    	
	      }
	      return atlCurrentsUserAlerts;
	}
	/**
	 * Refresh the currentUserEventAlerts and currentUserTaskAlerts
	 * from Parse database
	 */
	public void refresAllUserAlerts()  
	{
		atlCurrentsUserAlerts.parseConnect.setAlerts(TABLE_ALERTS.EVENT);
		atlCurrentsUserAlerts.parseConnect.setAlerts(TABLE_ALERTS.TASK);
		atlCurrentsUserAlerts.allCurrentUserAlerts = new ArrayList<ATLAlertModel>();
		if (getCurrentUserEventAlert()!=null)
		{
			atlCurrentsUserAlerts.allCurrentUserAlerts = getCurrentUserEventAlert();
	//	atlCurrentsUserAlerts.allCurrentUserAlerts.addAll(currentUserEventAlerts);
			//atlCurrentsUserAlerts.allCurrentUserAlerts.addAll(currentUserTaskAlerts);
		}
		if (getCurrentUserTaskAlert()!=null)
		{
		atlCurrentsUserAlerts.allCurrentUserAlerts.addAll(currentUserTaskAlerts);
		}
	}
	public void refreshEventsAlerts()
	{
		atlCurrentsUserAlerts.parseConnect.setAlerts(TABLE_ALERTS.EVENT);
		atlCurrentsUserAlerts.allCurrentUserAlerts = new ArrayList<ATLAlertModel>();
		atlCurrentsUserAlerts.allCurrentUserAlerts.addAll(currentUserEventAlerts);
		atlCurrentsUserAlerts.allCurrentUserAlerts.addAll(currentUserTaskAlerts);
	}
	public void refreshTaskAlerts()
	{
		atlCurrentsUserAlerts.parseConnect.setAlerts(TABLE_ALERTS.TASK);
		atlCurrentsUserAlerts.allCurrentUserAlerts = new ArrayList<ATLAlertModel>();
		atlCurrentsUserAlerts.allCurrentUserAlerts.addAll(currentUserEventAlerts);
		atlCurrentsUserAlerts.allCurrentUserAlerts.addAll(currentUserTaskAlerts);
	}
}
