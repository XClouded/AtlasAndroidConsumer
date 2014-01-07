//  ==================================================================================================================
//  ATLAS.java
//  AtlasConsumerAndroid
//  Copyright (c) 2012 ATLAS Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================
//  2012-11-29 TAN:     Create class
//  ==================================================================================================================

package com.atlasapp.common;

/**
 * = author Ryan Tan
 *
 */
public class ATLProtoMessConfig {

	static public final String URL_PROTOMESS_BASE = "http://ami.atlaspowered.com/protomess?";

	static public final String URL_PROTOMESS_POST_TASK_REQUEST_HEADER    = "DoAction=POST&messtype=TASKREQUEST"; 
	static public final String URL_PROTOMESS_GET_TAST_REQUEST_HEADER     = "DoAction=GET&messtype=TASKREQUEST";
	static public final String URL_PROTOMESS_POST_TASK_RESPONSE_HEADER   = "DoAction=POST&messtype=TASKRESPONSE" ;
	static public final String URL_PROTOMESS_GET_TAST_RESPONSE_HEADER    = "DoAction=GET&messtype=TASKRESPONSE";
	static public final String URL_PROTOMESS_POST_TASK_COMPLETE_HEADER   = "DoAction=POST&messtype=TASKCOMPLETE" ;
	static public final String URL_PROTOMESS_GET_TAST_COMPLETE_HEADER    = "DoAction=GET&messtype=TASKCOMPLETE";

}
