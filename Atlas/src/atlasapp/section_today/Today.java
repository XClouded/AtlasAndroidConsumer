//package atlasapp.section_today;
//
//
//import android.content.Intent;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.support.v4.app.FragmentActivity;
//import android.text.format.DateFormat;
//import android.util.TypedValue;
//import android.view.View;
//import android.widget.ImageButton;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//
//import atlasapp.common.SlideOutActivity;
//import atlasapp.section_appentry.R;
//import atlasapp.section_calendar.CalendarDayView;
//import atlasapp.section_notes.ATLNoteListActivity;
//import atlasapp.section_settings.Settings;
//import atlasapp.section_tasks.ATLTaskListActivity;
//
//
//public class Today extends FragmentActivity {
//	private final DateFormat dateFormatter = new DateFormat();
//	private RadioButton todayBtn;
//	private static final String dateTemplate = "EEEE, MMMM d, yyyy";
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.today);
//
//		// ((ImageView)
//		// findViewById(R.id.mainTitleLabel)).setVisibility(View.VISIBLE);
//		// ((ImageButton)
//		// findViewById(R.id.topMenuBtn1)).setVisibility(View.VISIBLE);
//		// ((ImageButton)
//		// findViewById(R.id.topMenuBtn2)).setVisibility(View.VISIBLE);
//		// ((ImageButton)
//		// findViewById(R.id.topMenuBtn4)).setVisibility(View.VISIBLE);
//		// ImageView title =(ImageView) findViewById(R.id.mainTitleLabel);
//		// // title.setBackgroundResource(R.drawable.title_today);
//		// title.setImageResource(R.drawable.title_today);
//		//
//		// TextView todaysDateLabel =
//		// (TextView)findViewById(R.id.todaysLabelDate);
//		// todaysDateLabel.setText(dateFormatter.format(dateTemplate,
//		// Calendar.getInstance().getTime()));
//
//		ImageButton btnSort = (ImageButton) findViewById(R.id.sortBtn);
//		// btnSort.setVisibility(View.VISIBLE);
//		// btnSort.setBackgroundResource(R.drawable.);
//
//		btnSort.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				int width = (int) TypedValue.applyDimension(
//						TypedValue.COMPLEX_UNIT_DIP, 40, getResources()
//								.getDisplayMetrics());
//				SlideOutActivity.prepare(Today.this, R.id.today_content, width);
//				startActivity(new Intent(Today.this, TodaySort.class));
//				overridePendingTransition(0, 0);
//			}
//		});
//
//		ImageButton btnMonth = (ImageButton) findViewById(R.id.monthBtn);
//		// btnMonth.setVisibility(View.VISIBLE);
//		// btnMonth.setBackgroundResource(R.drawable.btn_month);
//
//		ImageButton btnAllies = (ImageButton) findViewById(R.id.alliesBtn);
//		// btnAllies.setVisibility(View.VISIBLE);
//		// btnAllies.setBackgroundResource(R.drawable.btn_allies);
//		//
//		btnAllies.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				int width = (int) TypedValue.applyDimension(
//						TypedValue.COMPLEX_UNIT_DIP, 40, getResources()
//								.getDisplayMetrics());
//				SlideOutActivity.prepare(Today.this, R.id.today_content, width);
//				Intent intent = new Intent(getBaseContext(), TodaySort.class);
//				intent.putExtra("rightSwipe", true);
//				startActivity(intent);
//				// startActivity(new Intent(Today.this,
//				// TodaySort.class));
//				overridePendingTransition(0, 0);
//			}
//		});
//
//		((RadioButton) findViewById(R.id.btnToday)).setVisibility(View.VISIBLE);
//		todayBtn = (RadioButton) findViewById(R.id.btnToday);
//
//		todayBtn.setChecked(true);
//		todayBtn.setTextColor(Color.BLACK);
//
//		setMainMenuListener();
//
//	}
//
//	@Override
//	protected void onResume() {
//		super.onResume();
//		todayBtn.setChecked(true);
//		todayBtn.setTextColor(Color.BLACK);
//	}
//
//	private void setMainMenuListener() {
//
//		((RadioGroup) findViewById(R.id.main_btns_group))
//				.setVisibility(View.VISIBLE);
//
//		RadioGroup mainBtns = (RadioGroup) findViewById(R.id.main_btns_group);
//
//		// holder.rdgCategory = (RadioGroup)row.findViewById(R.id.radiogroup);
//
//		RadioGroup.OnCheckedChangeListener rdGrpCheckedListener = new RadioGroup.OnCheckedChangeListener() {
//
//			@Override
//			public void onCheckedChanged(RadioGroup group, int checkedId) {
//
//				// ViewFlipper vf = (ViewFlipper)findViewById(R.id.content_vf);
//				// ImageView mainTitle =
//				// (ImageView)findViewById(R.id.mainTitleLabel);
//				// mainTitle.setVisibility(0);
//
//				// TODO Auto-generated method stub
//				// setCategoryinList(position, checkedId);
//				switch (checkedId) {
//				case R.id.btnCal:
//					Intent intentCalendar = new Intent(getBaseContext(),
//							CalendarDayView.class);
//					intentCalendar.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//					// intent.putExtra("EXTRA_SESSION_ID", sessionId);
//					startActivity(intentCalendar);
//
//					break;
//				case R.id.btnNotes:
//					Intent intentNotes = new Intent(getBaseContext(),
//							ATLNoteListActivity.class);
//					intentNotes.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//					// //intent.putExtra("EXTRA_SESSION_ID", sessionId);
//					startActivity(intentNotes);
//
//					break;
//				case R.id.btnSettings:
//					Intent intent = new Intent(getBaseContext(), Settings.class);
//					intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//					// //intent.putExtra("EXTRA_SESSION_ID", sessionId);
//					startActivity(intent);
//
//					break;
//				case R.id.btnToday:
//
//					break;
//				case R.id.btnToDo:
//					Intent intentTasks = new Intent(getBaseContext(),
//							ATLTaskListActivity.class);
//					intentTasks.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//					// //intent.putExtra("EXTRA_SESSION_ID", sessionId);
//					startActivity(intentTasks);
//
//					break;
//				default:
//
//					break;
//				}
//			}
//		};
//
//		mainBtns.setOnCheckedChangeListener(rdGrpCheckedListener);
//
//	}
//
//}