package atlasapp.model;

import java.util.ArrayList;

import atlasapp.database.EventProperties;
import atlasapp.database.ItemUserProperties;

public interface ATLItemUserModelCallBackInterface {
	void savedItemUserOnLocalDB(boolean success, ArrayList<ItemUserProperties> itemUser);

	void updatedItemUserOnLocalDB(boolean success);

	void writeItemUserOnLocalDB(boolean success);

}
