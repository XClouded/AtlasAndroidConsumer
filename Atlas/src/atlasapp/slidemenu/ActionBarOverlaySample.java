package atlasapp.slidemenu;

import android.os.Bundle;
import android.view.Window;

public class ActionBarOverlaySample extends ATLSlideMenu {

    @Override
    public void onCreate(Bundle inState) {
        requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        super.onCreate(inState);
    }

	
}
