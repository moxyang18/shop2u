package com.moxuan.shop2u.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.moxuan.shop2u.dao.ShopCategoryDao;
import com.moxuan.shop2u.dao.ShopDao;
import com.moxuan.shop2u.dto.ShopExecution;
import com.moxuan.shop2u.entity.Shop;
import com.moxuan.shop2u.entity.ShopCategory;
import com.moxuan.shop2u.enums.ShopStateEnum;
import com.moxuan.shop2u.service.ShopService;
import com.moxuan.shop2u.util.FileUtil;
import com.moxuan.shop2u.util.ImageUtil;

public class ShopServiceImpl implements ShopService {

	@Autowired
	private ShopDao shopDao;
	@Autowired
	private ShopAuthMapDao shopAuthMapDao;
	@Autowired
	private ShopCategoryDao shopCategoryDao;

	@Override
	public ShopExecution getShopList(Shop shopCondition, int pageIndex,
			int pageSize) {
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		List<Shop> shopList = shopDao.queryShopList(shopCondition, rowIndex,
				pageSize);
		int count = shopDao.queryShopCount(shopCondition);
		ShopExecution se = new ShopExecution();
		if (shopList != null) {
			se.setShopList(shopList);
			se.setCount(count);
		} else {
			se.setState(ShopStateEnum.INNER_ERROR.getState());
		}
		return se;
	}

	@Override
	public ShopExecution getByEmployeeId(long employeeId)
			throws RuntimeException {
		List<Shop> shopList = shopDao.queryByEmployeeId(employeeId);
		ShopExecution se = new ShopExecution();
		se.setShopList(shopList);
		return se;
	}

	@Override
	public Shop getByShopId(long shopId) {
		return shopDao.queryByShopId(shopId);
	}

	@Override
	@Transactional
	/**
	 */
	public ShopExecution addShop(Shop shop, CommonsMultipartFile shopImg)
			throws RuntimeException {
		if (shop == null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOP_INFO);
		}
		try {
			shop.setEnableStatus(0);
			shop.setCreateTime(new Date());
			shop.setLastEditTime(new Date());
			if (shop.getShopCategory() != null) {
				Long shopCategoryId = shop.getShopCategory()
						.getShopCategoryId();
				ShopCategory sc = new ShopCategory();
				sc = shopCategoryDao.queryShopCategoryById(shopCategoryId);
				ShopCategory parentCategory = new ShopCategory();
				parentCategory.setShopCategoryId(sc.getParentId());
				shop.setParentCategory(parentCategory);
			}
			int effectedNum = shopDao.insertShop(shop);
			if (effectedNum <= 0) {
				throw new RuntimeException("店铺创建失败");
			} else {
				try {
					if (shopImg != null) {
						addShopImg(shop, shopImg);
						effectedNum = shopDao.updateShop(shop);
						if (effectedNum <= 0) {
							throw new RuntimeException("创建图片地址失败");
						}
					}
				} catch (Exception e) {
					throw new RuntimeException("addShopImg error: "
							+ e.getMessage());
				}
				// 执行增加shopAuthMap操作
				ShopAuthMap shopAuthMap = new ShopAuthMap();
				shopAuthMap.setEmployeeId(shop.getOwnerId());
				shopAuthMap.setShopId(shop.getShopId());
				shopAuthMap.setName("");
				shopAuthMap.setTitle("Owner");
				shopAuthMap.setTitleFlag(1);
				shopAuthMap.setCreateTime(new Date());
				shopAuthMap.setLastEditTime(new Date());
				shopAuthMap.setEnableStatus(1);
				try {
					effectedNum = shopAuthMapDao.insertShopAuthMap(shopAuthMap);
					if (effectedNum <= 0) {
						throw new RuntimeException("Authorization fail");
					} else {// 创建成功
						return new ShopExecution(ShopStateEnum.CHECK, shop);
					}
				} catch (Exception e) {
					throw new RuntimeException("insertShopAuthMap error: "
							+ e.getMessage());
				}

			}
		} catch (Exception e) {
			throw new RuntimeException("insertShop error: " + e.getMessage());
		}
	}

	@Override
	@Transactional
	public ShopExecution modifyShop(Shop shop, CommonsMultipartFile shopImg)
			throws RuntimeException {
		if (shop == null || shop.getShopId() == null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOPID);
		} else {
			try {
				if (shopImg != null) {
					Shop tempShop = shopDao.queryByShopId(shop.getShopId());
					if (tempShop.getShopImg() != null) {
						FileUtil.deleteFile(tempShop.getShopImg());
					}
					addShopImg(shop, shopImg);
				}
				shop.setLastEditTime(new Date());
				int effectedNum = shopDao.updateShop(shop);
				if (effectedNum <= 0) {
					return new ShopExecution(ShopStateEnum.INNER_ERROR);
				} else {// 
					shop = shopDao.queryByShopId(shop.getShopId());
					return new ShopExecution(ShopStateEnum.SUCCESS, shop);
				}
			} catch (Exception e) {
				throw new RuntimeException("modifyShop error: "
						+ e.getMessage());
			}
		}
	}

	private void addShopImg(Shop shop, CommonsMultipartFile shopImg) {
		String dest = FileUtil.getShopImagePath(shop.getShopId());
		String shopImgAddr = ImageUtil.generateThumbnail(shopImg, dest);
		shop.setShopImg(shopImgAddr);
	}	

}
