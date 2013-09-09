package com.octo.appaloosasdk.utils;

import java.security.MessageDigest;
import java.util.UUID;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;

/**
 * Utility class used to generate a constant "UDID", based on the IMEI or the mac address
 */
public class DeviceInfo {

	protected static String imeiNumber;
	protected static String wifiMacAddress;
	protected static String deviceID;
	
	public static String getDeviceInfo() {
	    return deviceID;
	}
	
	public static String getImei() {
	    return imeiNumber;
	}
	
	public static String getWifiMacAddress() {
	    return wifiMacAddress;
	}
	
	public static String getModel() {
	    return Build.MODEL;
	}
	
	public static String getOsVersion() {
	    return Build.VERSION.RELEASE;
	}
	
	public static String getDeviceId(Context context) {
	    String imei = getImei(context);
	    if (imei != null) return imei;
	    String tid = "unkown";
		try {
			tid = getWifiMacAddress(context);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return tid;
	}
	
	protected static String getWifiMacAddress(Context context) throws Exception {
	    WifiManager manager = (WifiManager) context
	            .getSystemService(Context.WIFI_SERVICE);
	    WifiInfo wifiInfo = manager.getConnectionInfo();
	    if (wifiInfo == null || wifiInfo.getMacAddress() == null)
	        return md5(UUID.randomUUID().toString());
	    else return wifiInfo.getMacAddress().replace(":", "").replace(".", "");
	}
	
	protected static String getImei(Context context) {
	    TelephonyManager m = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
	    String imei = m != null ? m.getDeviceId() : null;
	    return imei;
	}
	
	protected static String md5(String s) throws Exception {
	    MessageDigest md = MessageDigest.getInstance("MD5");
	
	    md.update(s.getBytes());
	
	    byte digest[] = md.digest();
	    StringBuffer result = new StringBuffer();
	
	    for (int i = 0; i < digest.length; i++) {
	        result.append(Integer.toHexString(0xFF & digest[i]));
	    }
	    return (result.toString());
	}
}