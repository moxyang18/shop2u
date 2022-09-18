package com.moxuan.shop2u.service;

import java.util.List;

import com.moxuan.shop2u.dto.ProductCategoryExecution;
import com.moxuan.shop2u.entity.ProductCategory;

public interface ProductCategoryService {
	/**
	 * 
	 * @param long shopId
	 * @return List<ProductCategory>
	 */
	List<ProductCategory> getByShopId(long shopId);

	/**
	 * 
	 * @param productCategory
	 * @return
	 * @throws RuntimeException
	 */
	ProductCategoryExecution batchAddProductCategory(
			List<ProductCategory> productCategoryList) throws RuntimeException;

	/**
	 * 
	 * @param productCategoryId
	 * @param shopId
	 * @return
	 * @throws RuntimeException
	 */
	ProductCategoryExecution deleteProductCategory(long productCategoryId,
			long shopId) throws RuntimeException;
}