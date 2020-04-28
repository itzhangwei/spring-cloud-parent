package com.learn.common.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.math.BigDecimal;
import java.util.UUID;
/**
 * @author zhang
 * @projectName spring-cloud-parent
 * @title StringUtil
 * @package com.learn.common.util
 * @description
 * @date 2020/4/17 6:56 下午
 */
public class StringUtil {
	private static final Log log = LogFactory.getLog(StringUtil.class);
	
	public static String getUUID32() {
		return UUID.randomUUID().toString().replace("-", "").toLowerCase();
	}
	
	public static String getUUID() {
		return UUID.randomUUID().toString();
	}
	
	public final static boolean isEmpty(Object s) {
		return s == null || s.toString().trim().equals("");
	}
	
	public static boolean isNotBlank(String str) {
		int length;
		if ((str == null) || ((length = str.length()) == 0))
			return false;
		for (int i = 0; i < length; ++i) {
			if (!(Character.isWhitespace(str.charAt(i)))) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 得到两位小数的金额字符串
	 */
	public static String getMoneyStr(String tranAmt) {
		char num[] = tranAmt.toCharArray();
		if (isDigital(tranAmt)) {
			tranAmt += ".00";
		} else if (isMoney(tranAmt)) {
			int pointIndex = tranAmt.indexOf(".");
			if (num.length - pointIndex >= 3) {
				tranAmt = tranAmt.substring(0, pointIndex + 3);
			} else {
				tranAmt = tranAmt + "0";
			}
		}
		return tranAmt;
	}
	
	/**
	 * @param str 判断字符串是金额格式：####.####
	 * @return true 是，false 否
	 */
	public static boolean isMoney(String str) {
		try {
			new BigDecimal(str);
		} catch (Exception e) {
			log.error("字符串格式有问题", e);
			return false;
		}
		return true;
	}
	
	/**
	 * 判断字符串是否为纯数字
	 * <p>
	 * Create Date: 2014年9月29日
	 * </p>
	 *
	 * @param str
	 * @return true 是，false 否
	 */
	public static boolean isDigital(String str) {
		if (isBlank(str))
			return false;
		
		return str.matches("\\d+");
	}
	
	public static boolean isBlank(String str) {
		int length;
		if ((str == null) || ((length = str.length()) == 0))
			return true;
		for (int i = 0; i < length; ++i) {
			if (!(Character.isWhitespace(str.charAt(i)))) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 生成四位数的随机数
	 *
	 * @return
	 */
	public static String buildRandomCode() {
		int a = (int) ((Math.random() * 9 + 1) * 1000);
		return a + "";
	}
	
}