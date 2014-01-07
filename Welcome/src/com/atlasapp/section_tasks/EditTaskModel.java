//  ==================================================================================================================
//  EditTaskModel.java
//  AtlasConsumerAndroid
//  Copyright (c) 2012 AtlasConsumerAndroid Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================
//  2012-11-12 TAN:     Init class : For contain data of edit task view
//  ==================================================================================================================

package com.atlasapp.section_tasks;


public class EditTaskModel {

	public static boolean isNewTask = false;
	private static EditTaskModel instance;
	private ATLTaskCellData taskCellData = new ATLTaskCellData();
	public static EditTaskModel getInstance(){
		if(instance == null){
			instance = new EditTaskModel();
		}
		return instance;
		
	}
	
	public void destroy(){
		if(instance != null){
			instance = null;
			taskCellData = null;
			isNewTask = true;
		}
	}
	
	public void setData(ATLTaskCellData taskCellData){
		isNewTask = false;
		ATLTaskCellData temp = new ATLTaskCellData();
		temp = taskCellData.copy();
		this.taskCellData = temp;
	}
	
	public ATLTaskCellData getTaskCellData(){
		return taskCellData;
	}
	
}
