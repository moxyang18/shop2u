package com.moxuan.shop2u.enums;

public enum PersonInfoStateEnum {

	SUCCESS(0, "Create Success"), INNER_ERROR(-1001, "Op Fail"), EMPTY(-1002, "User info Empty");

	private int state;

	private String stateInfo;

	private PersonInfoStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}

	public int getState() {
		return state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public static PersonInfoStateEnum stateOf(int index) {
		for (PersonInfoStateEnum state : values()) {
			if (state.getState() == index) {
				return state;
			}
		}
		return null;
	}

}