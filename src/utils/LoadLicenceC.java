package utils;

import logger.Logger;

public class LoadLicenceC {
	public native int check_out_lic();
	static{
		try {
			
			String property = System.getProperty("java.library.path");
			Logger.info(property);
			System.loadLibrary("truechip_64");
			//System.load();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		//RlmEZ.checkout(product, version);
		
		//int out=l.check_out_lic();
		
		//Logger.info(out);
	}
	

}
