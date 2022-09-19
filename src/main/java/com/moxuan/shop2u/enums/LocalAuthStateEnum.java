package com.moxuan.shop2u.enums;

public enum LocalAuthStateEnum {
	LOGINFAIL(-1, "pwd or username entered incorrectly"), SUCCESS(0, "op success"), NULL_AUTH_INFO(-1006,
			"register info void"), ONLY_ONE_ACCOUNT(-1007,"can only bind one account");

	private int state;

	private String stateInfo;

	private LocalAuthStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}

	public int getState() {
		return state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public static LocalAuthStateEnum stateOf(int index) {
		for (LocalAuthStateEnum state : values()) {
			if (state.getState() == index) {
				return state;
			}
		}
		return null;
	}

}