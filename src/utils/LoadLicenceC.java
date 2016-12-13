package utils;

import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.ptr.IntByReference;

import jdk.nashorn.internal.objects.NativeFunction;
import library.LicenseUtill;
import sun.misc.GC;

public class LoadLicenceC {
	public final static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(LoadLicenceC.class);

	public static boolean checkLic() {
		try {
			Native.setProtected(true);
			log.info("Checking license info with arg: 0, tc_gui, 16.4");
			String extractFromResourcePath = "";
			if (Platform.isLinux()) {
				extractFromResourcePath = Native.extractFromResourcePath("/resources/truechip_64.so").getAbsolutePath();
			} else {
				extractFromResourcePath = Native.extractFromResourcePath("/resources/truechip_64.dll")
						.getAbsolutePath();
			}
			log.info(extractFromResourcePath);
			// LicenseUtill loadLibrary = (LicenseUtill)
			// Native.loadLibrary("truechip_64", LicenseUtill.class);
			LicenseUtill loadLibrary = (LicenseUtill) Native.loadLibrary(extractFromResourcePath, LicenseUtill.class);
			//int check_out_lic = loadLibrary.check_out_lic(0, "tc_gui", "16.4");
			int check_out_lic = loadLibrary.check_out_lic(0, "tc_gui", "16.4");
			log.info("checking license completed returned: " + check_out_lic);
			if (check_out_lic == 0) {
				return true;
			} else {
				
				System.out.println(
						"LICENSING\n************************************************************************\n****** Truechip("
								+ check_out_lic
								+ "): No valid license checked out for Truechip_GUI ******\n**********************************************************************");
			}
			
			
		} catch (Exception e) {
			log.error(e);
		}
		return false;
	}
}
