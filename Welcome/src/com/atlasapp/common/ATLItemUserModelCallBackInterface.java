package com.atlasapp.common;

import java.util.ArrayList;

import com.atlasapp.atlas_database.EventProperties;
import com.atlasapp.atlas_database.ItemUserProperties;

public interface ATLItemUserModelCallBackInterface {
	void savedItemUserOnLocalDB(boolean success, ArrayList<ItemUserProperties> itemUser);

}
