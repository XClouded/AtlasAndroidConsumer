package atlasapp.section_tasks;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import atlasapp.common.ATLAnimationUtils;
import atlasapp.common.ATLUser;
import atlasapp.section_appentry.AtlasApplication;
import atlasapp.section_appentry.R;
import atlasapp.section_main.ATLMultiSectionListView;

import com.ericharlow.DragNDrop.DragListener;
import com.ericharlow.DragNDrop.DragNDropAdapter;
import com.ericharlow.DragNDrop.DragNDropListView;
import com.ericharlow.DragNDrop.DropListener;
import com.ericharlow.DragNDrop.RemoveListener;

interface TasksFragmentDelegate {
	void didChangeSortOrder(int[] p);
}

public class TasksFragment extends Fragment {

	static public final String TASK_SORT_PRIORITY_STRING = "Priority";
	static public final String TASK_SORT_CALENDAR_STRING = "Calendar";
	static public final String TASK_SORT_DUEDATE_STRING = "Due Date";
	static public final String TASK_SORT_DELEGATED_STRING = "Delegated";

	LayoutInflater mInflater;
	Context mContext;
	View mLayout;
	ATLMultiSectionListView alertsList;
	DragNDropListView taskTypeList;
	ImageButton reloadBtn;
	private FragmentActivity taskActivity;
	private DragNDropAdapter adapter;
	protected TasksFragmentDelegate delegate;
	private CheckBox showCompletedCheckBox;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		taskActivity = getActivity();
		mInflater = LayoutInflater.from(taskActivity);
		mLayout = (View) mInflater.inflate(R.layout.task_sort, null);
		initAttributes();
		setListener();
		// bindingData();

		return mLayout;
	}

	private void initAttributes() {
		// TODO Auto-generated method stub
		taskTypeList = (DragNDropListView) mLayout
				.findViewById(R.id.task_sort_list_view);
		showCompletedCheckBox = (CheckBox) mLayout
				.findViewById(R.id.task_sort_show_completed_checkbox);
		showCompletedCheckBox
				.setChecked(ATLTaskSortSingleton.isShowCompletedTasks);
		ArrayList<String> content = new ArrayList<String>(mListContent.length);
		TextView userName = (TextView) mLayout
				.findViewById(R.id.task_sort_user_name_textview);
		userName.setText(ATLUser.getUserNameDisplay());
		
		//2013-01-09 TAN : add profile picture, temp solution
		Bitmap storedBitmap = null;
		File imgFile = AtlasApplication.PROFILE_PIC_PATH;
	    if(imgFile.exists())   		
			storedBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
		ImageView userAvatar = (ImageView) mLayout.findViewById(R.id.task_select_user_avatar);
		userAvatar.setImageBitmap(storedBitmap);
		//2013-01-09 TAN : add profile picture, temp end
		
		int arrSize = mListContent.length;
		for (int j = 0; j < arrSize; j++) {
			switch (ATLTaskSortSingleton.sortIndex[j]) {
			case ATLTaskCellData.TASK_SORT_CALENDAR:
				content.add(TASK_SORT_CALENDAR_STRING);
				break;
			case ATLTaskCellData.TASK_SORT_PRIORITY:
				content.add(TASK_SORT_PRIORITY_STRING);
				break;
			case ATLTaskCellData.TASK_SORT_DUEDATE:
				content.add(TASK_SORT_DUEDATE_STRING);
				break;
			case ATLTaskCellData.TASK_SORT_DELEGATED:
				content.add(TASK_SORT_DELEGATED_STRING);
				break;
			}

		}

		// for (int i = 0; i < mListContent.length; i++) {
		// content.add(mListContent[i]);
		// }
		adapter = new DragNDropAdapter(taskActivity,
				new int[] { R.layout.dragitem }, new int[] { R.id.TextView01 },
				content);
		taskTypeList.setAdapter(adapter);// new DragNDropAdapter(this,content)

		if (taskTypeList instanceof DragNDropListView) {
			((DragNDropListView) taskTypeList).setDropListener(mDropListener);
			((DragNDropListView) taskTypeList)
					.setRemoveListener(mRemoveListener);
			((DragNDropListView) taskTypeList).setDragListener(mDragListener);
		}
	}

	private void bindingData() {
		// TODO Auto-generated method stub
	}

	private void setListener() {
		// TODO Auto-generated method stub
		showCompletedCheckBox
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						ATLTaskSortSingleton.isShowCompletedTasks = isChecked;
					}
				});
	}

	private DropListener mDropListener = new DropListener() {
		public void onDrop(int from, int to) {
			if (adapter instanceof DragNDropAdapter) {
				//===================================================
				//2013-01-10 TAN: Handle Animation reorder list - start
				//===================================================
				
				if(from < to){
					for(int idx = from; idx < to; idx++ ){
						View rowAtIdx = taskTypeList.getChildAt(idx);
						rowAtIdx.startAnimation(ATLAnimationUtils.moveUp(rowAtIdx.getHeight()));
					}
				
				}else{
					for(int idx = from; idx > to; idx-- ){
						View rowAtIdx = taskTypeList.getChildAt(idx);
						rowAtIdx.startAnimation(ATLAnimationUtils.moveDown(rowAtIdx.getHeight()));
					}
				}
				
				View rowAtIdxFrom = taskTypeList.getChildAt(to);
				rowAtIdxFrom.startAnimation(ATLAnimationUtils.inFromRightAnimation());
				//===================================================
				//2013-01-10 TAN: Handle Animation reorder list - end
				//===================================================
				((DragNDropAdapter) adapter).onDrop(from, to);
				taskTypeList.invalidateViews();
				
				int[] p = new int[mListContent.length];
				int arrSize = mListContent.length;
				for (int i = 0; i < arrSize; i++) {
					String a = adapter.getItem(i);
					for (int j = 0; j < arrSize; j++) {
						if (a.equals(mListContent[j])) {
							p[i] = j;
						}
					}
				}

				ATLTaskSortSingleton.sortIndex = p;
			}
		}
	};

	private RemoveListener mRemoveListener = new RemoveListener() {
		public void onRemove(int which) {
			if (adapter instanceof DragNDropAdapter) {
				((DragNDropAdapter) adapter).onRemove(which);
				taskTypeList.invalidateViews();
			}
		}
	};

	private DragListener mDragListener = new DragListener() {

		int backgroundColor = 0xe0103010;
		int defaultBackgroundColor = Color.TRANSPARENT;

		public void onDrag(int x, int y, ListView listView) {
			// TODO Auto-generated method stub
		}

		public void onStartDrag(View itemView) {
			itemView.setVisibility(View.INVISIBLE);
			defaultBackgroundColor = itemView.getDrawingCacheBackgroundColor();
			itemView.setBackgroundColor(backgroundColor);
			ImageView iv = (ImageView) itemView.findViewById(R.id.ImageView01);
			if (iv != null)
				iv.setVisibility(View.INVISIBLE);
		}

		public void onStopDrag(View itemView) {
			itemView.setVisibility(View.VISIBLE);
			itemView.setBackgroundColor(defaultBackgroundColor);
			ImageView iv = (ImageView) itemView.findViewById(R.id.ImageView01);
			if (iv != null)
				iv.setVisibility(View.VISIBLE);
		}

	};

	private static String[] mListContent = { TASK_SORT_PRIORITY_STRING,
			TASK_SORT_CALENDAR_STRING, TASK_SORT_DUEDATE_STRING,
			TASK_SORT_DELEGATED_STRING };

}
