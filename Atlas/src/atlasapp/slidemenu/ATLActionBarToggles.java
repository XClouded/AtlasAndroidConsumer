package atlasapp.slidemenu;

import java.util.Random;
import com.actionbarsherlock.*;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;

public class ATLActionBarToggles extends ATLActionBar implements ActionBar.TabListener {
    private static final Random RANDOM = new Random();

    private int items = 0;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        for (int i = 0; i < items; i++) {
            menu.add("Text")
                .setIcon(R.drawable.ic_title_share_default)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Sherlock); //Used for theme switching in samples
        requestWindowFeature(Window.FEATURE_PROGRESS);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.feature_toggles);
        setSupportProgressBarIndeterminateVisibility(false);
        setSupportProgressBarVisibility(false);

//        getSupportActionBar().setCustomView(R.layout.custom_view);
        getSupportActionBar().setDisplayShowCustomEnabled(false);

        Context context = getSupportActionBar().getThemedContext();
        ArrayAdapter<CharSequence> listAdapter = ArrayAdapter.createFromResource(context, R.array.locations, R.layout.sherlock_spinner_item);
        listAdapter.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);

        getSupportActionBar().setListNavigationCallbacks(listAdapter, null);
    }
    
    
    public  void setProgressBar()
    {
    	  setSupportProgressBarVisibility(true);
          setSupportProgressBarIndeterminateVisibility(false);
          setSupportProgress(RANDOM.nextInt(8000) + 10);
    }
    public void hideProgressBar()
    {
    	 setSupportProgressBarVisibility(false);	
    }

      public void setProgressBarIndeterminate()
      {
    	  //Hack to hide the regular progress bar
          setSupportProgress(Window.PROGRESS_END);
          setSupportProgressBarIndeterminateVisibility(true);
      }
      public void hideProgressBarIndeterminate()
      {
    	  setSupportProgressBarIndeterminateVisibility(false);
      }
       public void setDisplayItem()
       {
    	   items = 0;
           invalidateOptionsMenu();
    	   
    	   
       }
       public void hideDisplayItem()
       {
    	   items += 1;
           invalidateOptionsMenu();   
       }
       /**
        * Sets the title subtitle string or hide it in case null 
        * @param subtitle
        */
        public void displaySubtitleShow(String subtitle)
        {
        	 getSupportActionBar().setSubtitle(subtitle);
        }
        
        
      public void showSubtitle(boolean show)
      {
    	  getSupportActionBar().setDisplayShowTitleEnabled(show);
      }

      public void showCustomView(boolean showCustom)
      {
    	  getSupportActionBar().setDisplayShowCustomEnabled(showCustom);
      }
     
      public void setStandardNavigationMode ()
      {
    	  getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
    	  
      }
      public void setListNavigatoin ()
      {
    	  getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
      }
      public void setTabNavigatoin ()
      {
    	  getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
      }
      
       public void displayHomeAsUpHow(boolean upShow)
       {
    	   getSupportActionBar().setDisplayHomeAsUpEnabled(upShow);
       }

        public void displayLogoShow(boolean logoShow)
        {
        	getSupportActionBar().setDisplayUseLogoEnabled(logoShow);
        }

       public void displayHomeShow(boolean show)
       {
    	   getSupportActionBar().setDisplayShowHomeEnabled(show);
       }

     public void displayActionBarShow(boolean show)
     {
    	 if (show)
    		 getSupportActionBar().show();
    	 else
    		 getSupportActionBar().hide();
     }
     public Tab addTab(TabListener listener)
     {
         ActionBar.Tab newTab = getSupportActionBar().newTab();

         if (RANDOM.nextBoolean()) {
             newTab.setCustomView(R.layout.tab_custom_view);
         } else {
             boolean icon = RANDOM.nextBoolean();
             if (icon) {
                 newTab.setIcon(R.drawable.ic_title_share_default);
             }
             if (!icon || RANDOM.nextBoolean()) {
                 newTab.setText("Text!");
             }
         }
         newTab.setTabListener(listener);
         getSupportActionBar().addTab(newTab);
         //newTab.performClick();
         return newTab;
     }

      

      public void displayTabSelected()
      {
    	  if (getSupportActionBar().getTabCount() > 0) {
              getSupportActionBar().selectTab(
                      getSupportActionBar().getTabAt(
                              RANDOM.nextInt(getSupportActionBar().getTabCount())
                      )
              );
          }
      }

       public void displayTabRemove()
       {
    	   if (getSupportActionBar().getTabCount() > 0) {
               getSupportActionBar().removeTabAt(getSupportActionBar().getTabCount() - 1);
           }
       }
        
       public void displayTabRemoveAll()
       {
    	   getSupportActionBar().removeAllTabs();
       }
       
       
      
    @Override
    public void onTabSelected(Tab tab, FragmentTransaction transaction) {}

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction transaction) {}

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction transaction) {}
}
