package utils;

public class GenericUtils {
	
	
	public static Integer getSumOfElements(String[] intArray){
		Integer ouptut=0;
		try {
			for(String element:intArray){
				ouptut=ouptut+(new Integer(element));
			}
			return ouptut;
		} catch (Exception e) {
			return -1;
		}
	}
}
