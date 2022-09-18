package com.moxuan.shop2u.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class FileUtil {
	private static String seperator = System.getProperty("file.separator");
	private static final SimpleDateFormat sDateFormat = new SimpleDateFormat(
			"yyyyMMddHHmmss"); // 时间格式化的格式
	private static final Random r = new Random();

	public static String getImgBasePath() {
		String os = System.getProperty("os.name");
		String basePath = "";
		if (os.toLowerCase().startsWith("win")) {
			basePath = "D:/mox/image/";
		} else {
			basePath = "/home/mox/";
		}
		basePath = basePath.replace("/", seperator);
		return basePath;
	}

	public static String getHeadLineImagePath() {
		String headLineImagePath = "/upload/images/item/headtitle/";
		headLineImagePath = headLineImagePath.replace("/", seperator);
		return headLineImagePath;
	}

	public static String getShopCategoryImagePath() {
		String shopCategoryImagePath = "/upload/images/item/shopcategory/";
		shopCategoryImagePath = shopCategoryImagePath.replace("/", seperator);
		return shopCategoryImagePath;
	}
	
	public static String getPersonInfoImagePath() {
		String personInfoImagePath = "/upload/images/item/personinfo/";
		personInfoImagePath = personInfoImagePath.replace("/", seperator);
		return personInfoImagePath;
	}

	public static String getShopImagePath(long shopId) {
		StringBuilder shopImagePathBuilder = new StringBuilder();
		shopImagePathBuilder.append("/upload/images/item/shop/");
		shopImagePathBuilder.append(shopId);
		shopImagePathBuilder.append("/");
		String shopImagePath = shopImagePathBuilder.toString().replace("/",
				seperator);
		return shopImagePath;
	}

	public static String getRandomFileName() {
		// gen random file name：current time from year to second +5 digit random number
		int rannum = (int) (r.nextDouble() * (99999 - 10000 + 1)) + 10000; // 获取随机数
		String nowTimeStr = sDateFormat.format(new Date()); // 当前时间
		return nowTimeStr + rannum;
	}

	public static void deleteFile(String storePath) {
		File file = new File(getImgBasePath() + storePath);
		if (file.exists()) {
			if (file.isDirectory()) {
				File files[] = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					files[i].delete();
				}
			}
			file.delete();
		}
	}
}

