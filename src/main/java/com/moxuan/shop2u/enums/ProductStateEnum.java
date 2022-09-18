package com.moxuan.shop2u.enums;

public enum ProductStateEnum {
	OFFLINE(-1, "Invalid Product"), SUCCESS(0, "Op Success"), PASS(2, "Verified"), INNER_ERROR(
			-1001, "Op Failure"),EMPTY(-1002, "Void Product");

	private int state;

	private String stateInfo;

	private ProductStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}

	public int getState() {
		return state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public static ProductStateEnum stateOf(int index) {
		for (ProductStateEnum state : values()) {
			if (state.getState() == index) {
				return state;
			}
		}
		return null;
	}

}