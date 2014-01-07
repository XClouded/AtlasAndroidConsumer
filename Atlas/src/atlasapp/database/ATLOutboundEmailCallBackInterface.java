package atlasapp.database;

import atlasapp.common.ATLConstants.EmailTemplateType;

public interface ATLOutboundEmailCallBackInterface {
	
	
	public void savedNewOutboundEmailCallBack(boolean success, EmailTemplateType emailTemplateType);

}
