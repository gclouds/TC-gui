package library;

import com.sun.jna.Library;

public interface LicenseUtill extends Library{
	
	//public int check_out_lic(int debug,String type, String version);
	public int check_out_lic(int debug,String type, String version);
}
