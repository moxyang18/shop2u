package com.moxuan.shop2u.enums;

public enum ShopCategoryStateEnum {
	SUCCESS(0, "create success"), INNER_ERROR(-1001, "operation fail"), EMPTY(-1002, "area info empty");

	private int state;

	private String stateInfo;

	private ShopCategoryStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}

	public int getState() {
		return state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public static ShopCategoryStateEnum stateOf(int index) {
		for (ShopCategoryStateEnum state : values()) {
			if (state.getState() == index) {
				return state;
			}
		}
		return null;
	}

}