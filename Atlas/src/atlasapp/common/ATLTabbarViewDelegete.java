//  ==================================================================================================================
//  ATLTabbarViewDelegete.java
//  ATLAS
//  Copyright (c) 2012 ATLAS Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================

package atlasapp.common;

public interface ATLTabbarViewDelegete {
	void didTabToTabIndex(int index);

	public static interface TabbarIndex {
		public static final int tabbar_icon_contacts = 0;
		public static final int tabbar_icon_calendar = 1;
		public static final int tabbar_icon_home = 2;
		public static final int tabbar_icon_tasks = 3;
		public static final int tabbar_icon_settings = 4;
	}

}
