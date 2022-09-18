package com.moxuan.shop2u.enums;

public enum ShopStateEnum {
	CHECK(0, "Under Review"), OFFLINE(-1, "Illegal Shop"), SUCCESS(1, "Operation Success"), PASS(2, "Authentication Success"), INNER_ERROR(
			-1001, "Internal Error: Operation Failure"), NULL_SHOPID(-1002, "ShopId Empty"), NULL_SHOP_INFO(
			-1003, "Empty Info Passed In");

	private int state;

	private String stateInfo;

	private ShopStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}

	public int getState() {
		return state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public static ShopStateEnum stateOf(int index) {
		for (ShopStateEnum state : values()) {
			if (state.getState() == index) {
				return state;
			}
		}
		return null;
	}
}
