package com.space.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 * @author zhuzhe
 * @date 2018/5/3 13:43
 */
public abstract class StringUtils extends org.apache.commons.lang3.StringUtils {
	
	/**
	 * str 为 null 或 "" 时返回false，否则返回true
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	/**
	 *   str == null     返回  ""
	 *   str == ""       返回  ""
	 *   str == " abc "  返回 "abc"
	 *   str == "a bc"   返回 "a bc"
	 * @param str
	 * @return
	 */
	public static String trimToEmpty(String str) {
		if (isEmpty(str)){
			return "";
		}
		return str.trim();
	}

	/**
	 * 使用正则表达式验证字符串
	 * @param checkTarget 验证的目标
	 * @param checkRegex 验证规则，是一个正则表达式
	 * @return 验证成功返回true,否则返回false
	 */
	public static boolean checkWithRegex(Object checkTarget,String checkRegex) {
		Matcher matcher = Pattern.compile(checkRegex)
				.matcher(StringUtils.trimToEmpty(String.valueOf(checkTarget)));
		return matcher.matches();
	}
}
