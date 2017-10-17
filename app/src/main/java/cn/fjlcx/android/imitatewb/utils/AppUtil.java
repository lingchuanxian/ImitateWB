package cn.fjlcx.android.imitatewb.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;

import java.io.File;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Enumeration;

/**
 * APP工具类
 *
 * @author ling_cx
 * @date 2017/8/14
 */

public class AppUtil {
	protected static final  String TAG = "AppUtil";
	/**
	 * 启动应用的设置
	 */
	public static void startAppSettings(Context context) {
		Intent intent = new Intent(
				Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
		intent.setData(Uri.parse("package:" + context.getPackageName()));
		context.startActivity(intent);
	}
	/**
	 * 获取App版本号
	 *
	 * @param context 上下文
	 * @return App版本号
	 */
	public static String getAppVersionName(Context context) {
		return getAppVersionName(context, context.getPackageName());
	}

	/**
	 * 获取App版本号
	 *
	 * @param context     上下文
	 * @param packageName 包名
	 * @return App版本号
	 */
	public static String getAppVersionName(Context context, String packageName) {
		if (isSpace(packageName)) return null;
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(packageName, 0);
			return pi == null ? null : pi.versionName;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static boolean isSpace(String s) {
		if (s == null) return true;
		for (int i = 0, len = s.length(); i < len; ++i) {
			if (!Character.isWhitespace(s.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断应用是否安装
	 * @param context
	 * @param packagename
	 * @return
	 */
	public static boolean isAppInstalled(Context context,String packagename)
	{
		PackageInfo packageInfo;
		try {
			packageInfo = context.getPackageManager().getPackageInfo(packagename, 0);
		}catch (PackageManager.NameNotFoundException e) {
			packageInfo = null;
		}
		if(packageInfo ==null){
			return false;
		}else{
			return true;
		}
	}

	/**
	 * APK安装
	 * @param file 文件
	 * @param mContext  上下文
	 */
	public static void installAPK(File file,Context mContext){
		if(!file.exists()){
			Log.d(TAG, "installAPK: ");
			return;
		}
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		mContext.startActivity(intent);
	}

	/**
	 * 判断是否使用vpn
	 * @return
	 */
	public static boolean isVpnUsed() {
		try {
			Enumeration<NetworkInterface> niList = NetworkInterface.getNetworkInterfaces();
			if(niList != null) {
				for (NetworkInterface intf : Collections.list(niList)) {
					if(!intf.isUp() || intf.getInterfaceAddresses().size() == 0) {
						continue;
					}
					Log.d(TAG, "isVpnUsed() NetworkInterface Name: " + intf.getName());
					if ("tun0".equals(intf.getName()) || "ppp0".equals(intf.getName())){
						return true; // The VPN is up
					}
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return false;
	}
}
