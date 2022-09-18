package com.moxuan.shop2u.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.moxuan.shop2u.entity.Shop;

public interface ShopDao {

	/**
	 * query shops by pages, conditions are: shopName, shopState, shopId, shopType, areaID
	 * 
	 * @param shopCondition
	 * @param rowIndex
	 * @param pageSize
	 * @return
	 */
	List<Shop> queryShopList(@Param("shopCondition") Shop shopCondition,
			@Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

	/**
	 * return the # of queried shops
	 * 
	 * @param shopCondition
	 * @return
	 */
	int queryShopCount(@Param("shopCondition") Shop shopCondition);

	/**
	 * find shops by employee id 
	 * 
	 * @param employeeId
	 * @return List<shop>
	 */
	List<Shop> queryByEmployeeId(long employeeId);

	/**
	 * find shops by owner id
	 * 
	 * @param shopId
	 * @return shop
	 */
	Shop queryByShopId(long shopId);

	/**
	 * add shop
	 * 
	 * @param shop
	 * @return effectedNum
	 */
	int insertShop(Shop shop);

	/**
	 * update shop
	 * 
	 * @param shop
	 * @return effectedNum
	 */
	int updateShop(Shop shop);

	/**
	 * can only delete the shops with no products published
	 * 
	 * @param shopName
	 * @return effectedNum
	 */
	int deleteShopByName(String shopName);	

}
