package com.zrk.data.pump.constants;

public enum OptType {

	New("New"),
	
	Update("Update"),
	
	Delete("Delete");
	

	private String actionName;
	
	OptType(String actionName){
		this.actionName = actionName;
	}

	public String getActionName() {
		return actionName;
	}

	public static OptType getActionByName(String actionName){
		if(New.getActionName().equals(actionName)){
			return New;
		}else if(Update.getActionName().equals(actionName)){
			return Update;
		}else if(Delete.getActionName().equals(actionName)){
			return Delete;
		}else{
			return null;
		}
	}
}
