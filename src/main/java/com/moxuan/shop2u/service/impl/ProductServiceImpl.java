package com.moxuan.shop2u.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.moxuan.shop2u.dao.ProductDao;
import com.moxuan.shop2u.dao.ProductImgDao;
import com.moxuan.shop2u.dto.ProductExecution;
import com.moxuan.shop2u.entity.Product;
import com.moxuan.shop2u.entity.ProductImg;
import com.moxuan.shop2u.enums.ProductStateEnum;
import com.moxuan.shop2u.service.ProductService;
import com.moxuan.shop2u.util.FileUtil;
import com.moxuan.shop2u.util.ImageUtil;
import com.moxuan.shop2u.util.PageCalculator;

@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	private ProductDao productDao;
	@Autowired
	private ProductImgDao productImgDao;

	@Override
	public ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize) {
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		List<Product> productList = productDao.queryProductList(productCondition, rowIndex, pageSize);
		int count = productDao.queryProductCount(productCondition);
		ProductExecution pe = new ProductExecution();
		pe.setProductList(productList);
		pe.setCount(count);
		return pe;
	}

	@Override
	public Product getProductById(long productId) {
		return productDao.queryProductByProductId(productId);
	}

	@Override
	@Transactional
	public ProductExecution addProduct(Product product, CommonsMultipartFile thumbnail,
			List<CommonsMultipartFile> productImgs) throws RuntimeException {
		if (product != null && product.getShop() != null && product.getShop().getShopId() != null) {
			product.setCreateTime(new Date());
			product.setLastEditTime(new Date());
			product.setEnableStatus(1);
			if (thumbnail != null) {
				addThumbnail(product, thumbnail);
			}
			try {
				int effectedNum = productDao.insertProduct(product);
				if (effectedNum <= 0) {
					throw new RuntimeException("Failed to create product");
				}
			} catch (Exception e) {
				throw new RuntimeException("Failed to create product:" + e.toString());
			}
			if (productImgs != null && productImgs.size() > 0) {
				addProductImgs(product, productImgs);
			}
			return new ProductExecution(ProductStateEnum.SUCCESS, product);
		} else {
			return new ProductExecution(ProductStateEnum.EMPTY);
		}
	}

	@Override
	@Transactional
	public ProductExecution modifyProduct(Product product, CommonsMultipartFile thumbnail,
			List<CommonsMultipartFile> productImgs) throws RuntimeException {
		if (product != null && product.getShop() != null && product.getShop().getShopId() != null) {
			product.setLastEditTime(new Date());
			if (thumbnail != null) {
				Product tempProduct = productDao.queryProductByProductId(product.getProductId());
				if (tempProduct.getImgAddr() != null) {
					FileUtil.deleteFile(tempProduct.getImgAddr());
				}
				addThumbnail(product, thumbnail);
			}
			if (productImgs != null && productImgs.size() > 0) {
				deleteProductImgs(product.getProductId());
				addProductImgs(product, productImgs);
			}
			try {
				int effectedNum = productDao.updateProduct(product);
				if (effectedNum <= 0) {
					throw new RuntimeException("Failed to update product info");
				}
				return new ProductExecution(ProductStateEnum.SUCCESS, product);
			} catch (Exception e) {
				throw new RuntimeException("Failed to update product info:" + e.toString());
			}
		} else {
			return new ProductExecution(ProductStateEnum.EMPTY);
		}
	}

	private void addProductImgs(Product product, List<CommonsMultipartFile> productImgs) {
		String dest = FileUtil.getShopImagePath(product.getShop().getShopId());
		List<String> imgAddrList = ImageUtil.generateNormalImgs(productImgs, dest);
		if (imgAddrList != null && imgAddrList.size() > 0) {
			List<ProductImg> productImgList = new ArrayList<ProductImg>();
			for (String imgAddr : imgAddrList) {
				ProductImg productImg = new ProductImg();
				productImg.setImgAddr(imgAddr);
				productImg.setProductId(product.getProductId());
				productImg.setCreateTime(new Date());
				productImgList.add(productImg);
			}
			try {
				int effectedNum = productImgDao.batchInsertProductImg(productImgList);
				if (effectedNum <= 0) {
					throw new RuntimeException("Failed to create product desc image");
				}
			} catch (Exception e) {
				throw new RuntimeException("Failed to create product desc image:" + e.toString());
			}
		}
	}

	private void deleteProductImgs(long productId) {
		List<ProductImg> productImgList = productImgDao.queryProductImgList(productId);
		for (ProductImg productImg : productImgList) {
			FileUtil.deleteFile(productImg.getImgAddr());
		}
		productImgDao.deleteProductImgByProductId(productId);
	}

	private void addThumbnail(Product product, CommonsMultipartFile thumbnail) {
		String dest = FileUtil.getShopImagePath(product.getShop().getShopId());
		String thumbnailAddr = ImageUtil.generateThumbnail(thumbnail, dest);
		product.setImgAddr(thumbnailAddr);
	}
}