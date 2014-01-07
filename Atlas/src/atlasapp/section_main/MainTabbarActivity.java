package atlasapp.section_main;







import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ViewFlipper;
import atlasapp.section_appentry.R;

public class MainTabbarActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_tabbar);
        
        
        
        
        
        
        setMainMenuListener();
        
        
        
    }
private void setMainMenuListener() {
		
		RadioGroup mainBtns = (RadioGroup)findViewById(R.id.main_btns_group);
		 
		
	//	holder.rdgCategory = (RadioGroup)row.findViewById(R.id.radiogroup);

        RadioGroup.OnCheckedChangeListener rdGrpCheckedListener = new RadioGroup.OnCheckedChangeListener(){

         @Override
         public void onCheckedChanged(RadioGroup group, int checkedId) {
        	 
        	 
        	 ViewFlipper vf = (ViewFlipper)findViewById(R.id.content_vf);
        	 ImageView mainTitle = (ImageView)findViewById(R.id.mainTitleLabel);
        	 	mainTitle.setVisibility(0);
        	 	 
             // TODO Auto-generated method stub
//               setCategoryinList(position, checkedId);
                switch (checkedId){ 
                case R.id.btnCal:mainTitle.setBackgroundResource(R.drawable.settings_title_bar_title);
                vf.setDisplayedChild(1);
                   
                    break;
                case R.id.btnNotes:
                	mainTitle.setBackgroundResource(R.drawable.settings_title_bar_title);
                	vf.setDisplayedChild(3);
                    break;
                case R.id.btnSettings:mainTitle.setBackgroundResource(R.drawable.settings_title_bar_title);
                vf.setDisplayedChild(4);
                
                break;
            case R.id.btnToday:
            	mainTitle.setBackgroundResource(R.drawable.settings_title_bar_title);
            	vf.setDisplayedChild(0);
                break;
            case R.id.btnToDo:
            	mainTitle.setBackgroundResource(R.drawable.settings_title_bar_title);
            	vf.setDisplayedChild(2);
                break;
                default:
                	mainTitle.setBackgroundResource(R.drawable.settings_title_bar_title);
                	vf.setDisplayedChild(0);
                    break;
                }
            }
        };

        mainBtns.setOnCheckedChangeListener(rdGrpCheckedListener);

		
	}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_tabbar, menu);
        return true;
    }
}
