//  ==================================================================================================================
//  ATLTaskSortSingleton.java
//  ATLAS
//  Copyright (c) 2012 ATLAS Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================
//  2021-12-27 TAN:    Create class
//  ==================================================================================================================

package atlasapp.section_tasks;

public class ATLTaskSortSingleton {

	public static int[] sortIndex = { ATLTaskCellData.TASK_SORT_PRIORITY,
			ATLTaskCellData.TASK_SORT_CALENDAR,
			ATLTaskCellData.TASK_SORT_DUEDATE,
			ATLTaskCellData.TASK_SORT_DELEGATED };
	
	// public static int[] sortIndex = { ATLTaskCellData.TASK_SORT_CALENDAR,
	// ATLTaskCellData.TASK_SORT_DUEDATE,
	// ATLTaskCellData.TASK_SORT_PRIORITY,
	// ATLTaskCellData.TASK_SORT_DELEGATED };
	public static boolean isShowCompletedTasks = false;

	public static int getGroupKind(){
		return sortIndex[0];
	}
}
