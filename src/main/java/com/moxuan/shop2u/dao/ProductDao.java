package com.moxuan.shop2u.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.moxuan.shop2u.entity.Product;

public interface ProductDao {
	/**
	 * 
	 * @param productCondition
	 * @param beginIndex
	 * @param pageSize
	 * @return
	 */
	List<Product> queryProductList(
			@Param("productCondition") Product productCondition,
			@Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

	/**
	 * 
	 * @param productCondition
	 * @return
	 */
	int queryProductCount(@Param("productCondition") Product productCondition);

	/**
	 * 
	 * @param productId
	 * @return
	 */
	Product queryProductByProductId(long productId);

	/**
	 * 
	 * @param product
	 * @return
	 */
	int insertProduct(Product product);

	/**
	 * 
	 * @param product
	 * @return
	 */
	int updateProduct(Product product);

	/**
	 * 
	 * @param productCategoryId
	 * @return
	 */
	int updateProductCategoryToNull(long productCategoryId);

	/**
	 * 
	 * @param productId
	 * @return
	 */
	int deleteProduct(@Param("productId") long productId,
			@Param("shopId") long shopId);
}
