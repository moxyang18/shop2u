package com.moxuan.shop2u.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.moxuan.shop2u.entity.ProductCategory;

public interface ProductCategoryDao {
	/**
	 * 
	 * @param long shopId
	 * @return List<ProductCategory>
	 */
	List<ProductCategory> queryByShopId(long shopId);

	/**
	 * Insert Product Type
	 * 
	 * @param ProductCategory
	 *            productCategory
	 * @return effectedNum
	 */
	int batchInsertProductCategory(List<ProductCategory> productCategoryList);

	/**
	 * Delete PD Type
	 * 
	 * @param productCategoryId
	 * @param shopId
	 * @return effectedNum
	 */
	int deleteProductCategory(
			@Param("productCategoryId") long productCategoryId,
			@Param("shopId") long shopId);
}