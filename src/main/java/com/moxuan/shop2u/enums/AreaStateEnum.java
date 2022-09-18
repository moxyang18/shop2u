package com.moxuan.shop2u.enums;

public enum AreaStateEnum {
	OFFLINE(-1, "invalid area"), SUCCESS(0, "operation success"), INNER_ERROR(-1001, "operation failure"), EMPTY(
			-1002, "area info void");

	private int state;

	private String stateInfo;

	private AreaStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}

	public int getState() {
		return state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public static AreaStateEnum stateOf(int index) {
		for (AreaStateEnum state : values()) {
			if (state.getState() == index) {
				return state;
			}
		}
		return null;
	}	
}
