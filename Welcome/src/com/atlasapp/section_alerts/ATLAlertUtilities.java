/**
 * 
 */
package com.atlasapp.section_alerts;

import com.atlasapp.common.ATLConstants.AlertType;
import com.atlasapp.section_calendar.ATLCalCellData;

/**
 * @author nghia
 * 
 */
public class ATLAlertUtilities {

	/**
	 * 
	 */
	public ATLAlertUtilities() {
		// TODO Auto-generated constructor stub
	}

	public void saveAlert(Object data, int alertType, boolean isSentSuccess) {
		switch (alertType) {
		case AlertType.eventInvited_Sent: {
			ATLCalCellData calCellData = (ATLCalCellData)data;
			if(isSentSuccess){
				
			}else{
				
			}

			break;
		}
		case AlertType.eventInvited_Received: {
			ATLCalCellData calCellData = (ATLCalCellData)data;
			
			break;
		}
		case AlertType.eventAccepted_Sent: {

			break;
		}
		case AlertType.eventAccepted_Received: {

			break;
		}
		case AlertType.taskAssigned_Sent: {

			break;
		}
		case AlertType.taskAssigned_Received: {

			break;
		}
		case AlertType.taskAccepted_Sent: {

			break;
		}
		case AlertType.taskAccepted_Received: {

			break;
		}
		default:
			break;
		}
	}

}
