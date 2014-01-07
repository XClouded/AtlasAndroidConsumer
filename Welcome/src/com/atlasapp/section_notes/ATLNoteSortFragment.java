package com.atlasapp.section_notes;

import java.util.ArrayList;

import com.atlasapp.common.AtlasAndroidUser;
import com.atlasapp.section_appentry.R;
import com.atlasapp.section_main.ATLMultiSectionListView;
import com.ericharlow.DragNDrop.DragListener;
import com.ericharlow.DragNDrop.DragNDropAdapter;
import com.ericharlow.DragNDrop.DragNDropListView;
import com.ericharlow.DragNDrop.DropListener;
import com.ericharlow.DragNDrop.RemoveListener;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

interface ATLNoteFragmentDelegate {
	void didChangeSortOrder(int[] p);
}

public class ATLNoteSortFragment extends Fragment {

	static public final String NOTE_SORT_STARRED_STRING = "Starred";
	static public final String NOTE_SORT_DATECREATED_STRING = "Date Created";
	static public final String NOTE_SORT_LASTMODIFIED_STRING = "Last Modified";
	static public final String NOTE_SORT_SHARED_STRING = "Share";

	LayoutInflater mInflater;
	Context mContext;
	View mLayout;
	ATLMultiSectionListView alertsList;
	DragNDropListView noteTypeList;
	ImageButton reloadBtn;
	private FragmentActivity noteActivity;
	private DragNDropAdapter adapter;
	protected ATLNoteFragmentDelegate delegate;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		noteActivity = getActivity();
		mInflater = LayoutInflater.from(noteActivity);
		mLayout = (View) mInflater.inflate(R.layout.note_sort, null);
		initAttributes();
		setListener();

		return mLayout;
	}

	private void initAttributes() {
		// TODO Auto-generated method stub
		noteTypeList = (DragNDropListView) mLayout
				.findViewById(R.id.note_sort_list_view);
		ArrayList<String> content = new ArrayList<String>(mListContent.length);
		TextView userName = (TextView) mLayout
				.findViewById(R.id.note_sort_user_name_textview);
		userName.setText(AtlasAndroidUser.getUserNameDisplay());

		int arrSize = mListContent.length;
		for (int j = 0; j < arrSize; j++) {
			switch (ATLNoteSortSingleTon.sortIndex[j]) {
			case ATLNoteCellData.NOTE_SORT_STARRED:
				content.add(NOTE_SORT_STARRED_STRING);
				break;
			case ATLNoteCellData.NOTE_SORT_DATECREATED:
				content.add(NOTE_SORT_DATECREATED_STRING);
				break;
			case ATLNoteCellData.NOTE_SORT_LASTMODIFIED:
				content.add(NOTE_SORT_LASTMODIFIED_STRING);
				break;
			case ATLNoteCellData.NOTE_SORT_SHARED:
				content.add(NOTE_SORT_SHARED_STRING);
				break;
			}

		}

		// for (int i = 0; i < mListContent.length; i++) {
		// content.add(mListContent[i]);
		// }
		adapter = new DragNDropAdapter(noteActivity,
				new int[] { R.layout.dragitem }, new int[] { R.id.TextView01 },
				content);
		noteTypeList.setAdapter(adapter);// new DragNDropAdapter(this,content)

		if (noteTypeList instanceof DragNDropListView) {
			((DragNDropListView) noteTypeList).setDropListener(mDropListener);
			((DragNDropListView) noteTypeList)
					.setRemoveListener(mRemoveListener);
			((DragNDropListView) noteTypeList).setDragListener(mDragListener);
		}
	}

	private void setListener() {
		// TODO Auto-generated method stub
	}

	private DropListener mDropListener = new DropListener() {
		public void onDrop(int from, int to) {
			if (adapter instanceof DragNDropAdapter) {
				((DragNDropAdapter) adapter).onDrop(from, to);
				noteTypeList.invalidateViews();

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

				ATLNoteSortSingleTon.sortIndex = p;
			}
		}
	};

	private RemoveListener mRemoveListener = new RemoveListener() {
		public void onRemove(int which) {
			if (adapter instanceof DragNDropAdapter) {
				((DragNDropAdapter) adapter).onRemove(which);
				noteTypeList.invalidateViews();
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

	private static String[] mListContent = { NOTE_SORT_STARRED_STRING,
			NOTE_SORT_DATECREATED_STRING, NOTE_SORT_LASTMODIFIED_STRING,
			NOTE_SORT_SHARED_STRING };

}
