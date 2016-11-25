package utils;

import org.apache.commons.lang.StringUtils;

import constants.Matcher;

public class StringComparator {
	
	public static Matcher findMatch(String previousString, String currentString) throws Exception{
		
		if(!StringUtils.isBlank(previousString) && !StringUtils.isBlank(currentString)){
			
		 	if(previousString.equals(currentString)) return Matcher.EXACT_MATCH;
		 	
		 	else if(currentString.startsWith(previousString)) return Matcher.PREFIX_MATCH;
		 	
		 	else return Matcher.NO_MATCH;
		}
		throw new Exception("Does not any string!!!");
	}

	public static boolean validString(String str){
		if(str !=null && !str.contains("-") && str.length()>0)
			return true;
		else
			return false;
	}
}
