package atlasapp.database;

import android.content.Context;

public interface ATLAlertControllerInterface {
	
	  
	
	public void refreshAllAlerts(Context context);
	
	public void getPendingAlert();
	public void getBookedAlert();
	public void getYourMove();
	
}
