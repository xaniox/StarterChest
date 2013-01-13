package com.matzefratze123.starterchest;

public enum Permissions {

	CAN_USE_STARTERCHEST("starterchest.use"),
	HAS_ADMIN_PERMISSIONS("starterchest.admin"),
	CAN_SHOW_HELP("starterchest.help"),
	CAN_SET_STARTERCHEST("starterchest.set"),
	CAN_REMOVE_STARTERCHEST("starterchest.remove"),
	CAN_LIST_STARTERCHESTS("starterchest.list"),
	CAN_GET_INFO_ABOUT_STARTERCHEST("starterchest.info");
	
	
	private final String permissionsString;
	
	private Permissions(String permissionsString) {
		this.permissionsString = permissionsString;
	}
	
	public String getPerm() {
		return permissionsString;
	}
	
}
