package atlasapp.slidemenu;

import android.os.Bundle;

public class ATLTopBarSingleton extends ATLSlideMenu{
	
	
	
	private static ATLTopBarSingleton atlTopBarSingleton ;
	
	
	private ATLTopBarSingleton(){};
	
	
	@Override
    public void onCreate(Bundle inState) {
		super.onCreate(inState);
		
	}
	
	
	public static ATLTopBarSingleton getATLTopBarSingleton()
	{
		if (atlTopBarSingleton==null)
		{
			atlTopBarSingleton = new ATLTopBarSingleton();
		}
		else
		{
			
		}
		return atlTopBarSingleton;
	}

}
