package com.kubara.michal.quickfix.adminpanel.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StringUtils {

	public static String reverseString(String string) {
		List<Character> charList = new ArrayList<>();
		for(char c : string.toCharArray()) {
			charList.add(c);
		}
		
		Collections.reverse(charList);
		String reversedString = "";
		for(char c : charList) {
			reversedString += c;
		}
		return reversedString;
	}
	
}
