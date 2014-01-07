//  ==================================================================================================================
//  ATLAlertWebAccessCallBackInterface.java
//  AtlasConsumerAndroid
//  Copyright (c) 2012 AtlasConsumerAndroid Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================

package com.atlasapp.section_alerts;

import java.util.List;

import com.atlasapp.common.ATLConstants.AlertType;
import com.parse.ParseObject;

/**
 * @author Ryan Tan
 * 
 */
public interface ATLAlertWebAccessCallBackInterface {

	void didGetDataFinish(Object data, int alertType, String result);

	void didPostDataFinish(Object data, int alertType, String result);

	void didGetEventList(List<ParseObject> events, int alertType,
			String result);

	void didGetSenderName();

}
