package utils;

import java.io.IOException;

import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.ptr.IntByReference;

import jdk.nashorn.internal.objects.NativeFunction;
import library.LicenseUtill;
import sun.misc.GC;

public class CheckLicense {
	public final static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CheckLicense.class);
	public static LicenseUtill loadLibrary;

	public static LicenseUtill getLicenseUtill()  {
		try {
			//Native.setProtected(true);
			String extractFromResourcePath = "";
			if (Platform.isLinux()) {
				extractFromResourcePath = Native.extractFromResourcePath("/resources/truechip_64.so").getAbsolutePath();
			} else {
				extractFromResourcePath = Native.extractFromResourcePath("/resources/truechip_64.dll")
						.getAbsolutePath();
			}
			log.info(extractFromResourcePath);
			loadLibrary = (LicenseUtill) Native.loadLibrary(extractFromResourcePath, LicenseUtill.class);
			return loadLibrary;
		} catch (IOException e) {
			log.error(e);
			return null;
		}
	}
	
	public static boolean checkLic(LicenseUtill loadLibrary) {
		try {
			System.out.println("checking license...");
			log.info("Checking license info with arg: 0, tc_gui, 16.4");
			int check_out_lic = loadLibrary.check_out_lic(0, "tc_gui", "16.4");
			log.info("checking license completed returned: " + check_out_lic);
			if (check_out_lic == 0) {
				return true;
			} else {

				System.out.println(
						"************************************************************************\n***** Truechip("
								+ check_out_lic
								+ "): No valid license checked out for Truechip_GUI *****\n************************************************************************");
			}

		} catch (Exception e) {
			log.error(e);
		}
		return false;
	}
}
