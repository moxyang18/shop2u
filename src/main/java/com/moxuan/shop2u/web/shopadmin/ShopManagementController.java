package com.moxuan.shop2u.web.shopadmin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moxuan.shop2u.dto.ShopExecution;
import com.moxuan.shop2u.entity.Area;
import com.moxuan.shop2u.entity.LocalAuth;
import com.moxuan.shop2u.entity.PersonInfo;
import com.moxuan.shop2u.entity.Shop;
import com.moxuan.shop2u.entity.ShopCategory;
import com.moxuan.shop2u.enums.ProductCategoryStateEnum;
import com.moxuan.shop2u.enums.ShopStateEnum;
import com.moxuan.shop2u.service.AreaService;
import com.moxuan.shop2u.service.LocalAuthService;
//import com.moxuan.shop2u.service.LocalAuthService;
import com.moxuan.shop2u.service.ShopCategoryService;
import com.moxuan.shop2u.service.ShopService;
import com.moxuan.shop2u.util.CodeUtil;
import com.moxuan.shop2u.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/shop")
public class ShopManagementController {
	@Autowired
	private ShopService shopService;
	@Autowired
	private ShopCategoryService shopCategoryService;
	@Autowired
	private AreaService areaService;
	@Autowired
	private LocalAuthService localAuthService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> list(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		PersonInfo user = (PersonInfo) request.getSession()
				.getAttribute("user");
		long employeeId = user.getUserId();
		if (hasAccountBind(request, employeeId)) {
			modelMap.put("hasAccountBind", true);
		} else {
			modelMap.put("hasAccountBind", false);
		}
		List<Shop> list = new ArrayList<Shop>();
		try {
			ShopExecution shopExecution = shopService
					.getByEmployeeId(employeeId);
			list = shopExecution.getShopList();
			modelMap.put("shopList", list);
			modelMap.put("user", user);
			modelMap.put("success", true);
			request.getSession().setAttribute("shopList", list);
		} catch (Exception e) {
			e.printStackTrace();
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
		}
		return modelMap;
	}

	@RequestMapping(value = "/getshopbyid", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopById(@RequestParam Long shopId,
			HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if (shopId != null && shopId > -1) {
			Shop shop = shopService.getByShopId(shopId);
			shop.getShopCategory().setShopCategoryName(
					shopCategoryService.getShopCategoryById(
							shop.getShopCategory().getShopCategoryId())
							.getShopCategoryName());
			shop.getParentCategory().setShopCategoryName(
					shopCategoryService.getShopCategoryById(
							shop.getParentCategory().getShopCategoryId())
							.getShopCategoryName());
			modelMap.put("shop", shop);
			request.getSession().setAttribute("currentShop", "shop");
			List<Area> areaList = new ArrayList<Area>();
			try {
				areaList = areaService.getAreaList();
			} catch (IOException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
			}
			modelMap.put("areaList", areaList);
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty shopId");
		}
		return modelMap;
	}

	@RequestMapping(value = "/getshopinitinfo", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopInitInfo() {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		List<ShopCategory> shopCategoryList = new ArrayList<ShopCategory>();
		List<Area> areaList = new ArrayList<Area>();
		try {
			shopCategoryList = shopCategoryService
					.getAllSecondLevelShopCategory();
			areaList = areaService.getAreaList();
		} catch (IOException e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
		}
		modelMap.put("shopCategoryList", shopCategoryList);
		modelMap.put("areaList", areaList);
		modelMap.put("success", true);
		return modelMap;
	}

	@RequestMapping(value = "/modifyshop", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyShop(HttpServletRequest request) {
		boolean statusChange = HttpServletRequestUtil.getBoolean(request,
				"statusChange");
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if (!statusChange && !CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "incorrect verification code");
			return modelMap;
		}
		ObjectMapper mapper = new ObjectMapper();
		Shop shop = null;
		String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
		MultipartHttpServletRequest multipartRequest = null;
		CommonsMultipartFile shopImg = null;
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (multipartResolver.isMultipart(request)) {
			multipartRequest = (MultipartHttpServletRequest) request;
			shopImg = (CommonsMultipartFile) multipartRequest
					.getFile("shopImg");
		}
		try {
			shop = mapper.readValue(shopStr, Shop.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		Shop currentShop = (Shop) request.getSession().getAttribute(
				"currentShop");
		shop.setShopId(currentShop.getShopId());
		if (shop != null && shop.getShopId() != null) {
			filterAttribute(shop);
			try {
				ShopExecution se = shopService.modifyShop(shop, shopImg);
				if (se.getState() == ProductCategoryStateEnum.SUCCESS
						.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", se.getStateInfo());
				}
			} catch (RuntimeException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}

		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "please enter shop info");
		}
		return modelMap;
	}

	@RequestMapping(value = "/registershop", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> registerShop(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if (!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "incorrect verification code");
			return modelMap;
		}
		ObjectMapper mapper = new ObjectMapper();
		Shop shop = null;
		String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
		MultipartHttpServletRequest multipartRequest = null;
		CommonsMultipartFile shopImg = null;
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (multipartResolver.isMultipart(request)) {
			multipartRequest = (MultipartHttpServletRequest) request;
			shopImg = (CommonsMultipartFile) multipartRequest
					.getFile("shopImg");
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "file to upload cannot be void");
			return modelMap;
		}
		try {
			shop = mapper.readValue(shopStr, Shop.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		if (shop != null && shopImg != null) {
			try {
				PersonInfo user = (PersonInfo) request.getSession()
						.getAttribute("user");
				shop.setOwnerId(user.getUserId());
				ShopExecution se = shopService.addShop(shop, shopImg);
				if (se.getState() == ShopStateEnum.CHECK.getState()) {
					modelMap.put("success", true);
					@SuppressWarnings("unchecked")
					List<Shop> shopList = (List<Shop>) request.getSession()
							.getAttribute("shopList");
					if (shopList != null && shopList.size() > 0) {
						shopList.add(se.getShop());
						request.getSession().setAttribute("shopList", shopList);
					} else {
						shopList = new ArrayList<Shop>();
						shopList.add(se.getShop());
						request.getSession().setAttribute("shopList", shopList);
					}
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", se.getStateInfo());
				}
			} catch (RuntimeException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}

		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "please enter shop info");
		}
		return modelMap;
	}

	private void filterAttribute(Shop shop) {

	}

	private boolean hasAccountBind(HttpServletRequest request, long userId) {
		if (request.getSession().getAttribute("bindAccount") == null) {
			LocalAuth localAuth = localAuthService.getLocalAuthByUserId(userId);
			if (localAuth != null && localAuth.getUserId() != null) {
				request.getSession().setAttribute("bindAccount", localAuth);
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}
}
