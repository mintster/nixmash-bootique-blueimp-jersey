package com.nixmash.fileupload.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

public class WebUtils {


	public static String pluralize(String singular) {
		String plural = singular;
		int singularLength = StringUtils.length(singular);
		if (StringUtils.right(singular, 1).equals("y"))
			plural = StringUtils.left(singular, singularLength - 1) + "ies";
		else
			plural = singular + "s";
		return plural;
	}

	public static String lowerPluralize(String singular) {
		return StringUtils.uncapitalize(pluralize(singular));
	}

	public static boolean isInTestingMode() {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		List<StackTraceElement> list = Arrays.asList(stackTrace);
		for (StackTraceElement element : list) {
			if (element.getClassName().startsWith("org.junit.")) {
				return true;
			}
		}
		return false;
	}

}
