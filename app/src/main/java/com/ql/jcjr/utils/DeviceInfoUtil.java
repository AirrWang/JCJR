package com.ql.jcjr.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.ql.jcjr.application.JcbApplication;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by blanc on 2017/11/17.
 */

public class DeviceInfoUtil {
    public static String phone_model;
    public static String os_version;
    public static String imei;
    public static String channel;

//    public static String imsi;
//    public static String service_provider;
//    public static String mac;

    @SuppressLint("MissingPermission")
    public static void initDeviceInfo(Context context){
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        //手机型号
        String BRAND = android.os.Build.BRAND;
        if(null == BRAND){
            BRAND = "";
        }
        String MODEL = android.os.Build.MODEL;
        if(null == MODEL){
            MODEL = "";
        }
        phone_model = BRAND+"_"+MODEL;

        //操作系统版本
        os_version = android.os.Build.VERSION.RELEASE;
        if(null == os_version){
            os_version = "";
        }

        //imei
        try{
            imei = tm.getDeviceId();
            if(null == imei){
                imei = "";
            }
        }catch(Exception e){
            imei = "";
        }

        //渠道信息
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo appInfo = pm.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            channel = appInfo.metaData.getString("UMENG_CHANNEL");
        } catch (PackageManager.NameNotFoundException ignored) {
            channel = "";
        }

//        imsi = tm.getSubscriberId();
//        if(null == imsi){
//            imsi = "";
//        }
//        if (tm.getSimState() == TelephonyManager.SIM_STATE_READY) {
//            service_provider = tm.getSimOperatorName();
//            if(null == service_provider){
//                service_provider = "";
//            }
//        }
//        else{
//            service_provider = "";
//        }
//        if(isWifi()){
//            mac = getLocalMacAddress();
//            if(null == mac || mac.length()==0){
//                WifiManager wifi = (WifiManager)
//                        context.getSystemService(Context.WIFI_SERVICE);
//                WifiInfo info = wifi.getConnectionInfo();
//                mac = info.getMacAddress();
//                if(null == mac){
//                    mac = "";
//                }
//            }
//        }
//        else{
//            mac = "";
//        }
//        if(imei.equals("")){
//            imei = mac;
//        }
    }

    /********************************** Mac address 方法1 ****************************************************************/
//	public String getMacAddress() {
//		String result = "";
//		String Mac = "";
//		result = callCmd("busybox ifconfig", "HWaddr");
//
//		// 如果返回的result == null，则说明网络不可取
//		if (result == null) {
//			return "网络出错，请检查网络";
//		}
//
//		// 对该行数据进行解析
//		// 例如：eth0 Link encap:Ethernet HWaddr 00:16:E8:3E:DF:67
//		if (result.length() > 0 && result.contains("HWaddr") == true) {
//			Mac = result.substring(result.indexOf("HWaddr") + 6,
//					result.length() - 1);
//
//			if (Mac.length() > 1) {
//				Mac = Mac.replaceAll(" ", "");
//				result = "";
//				String[] tmp = Mac.split(":");
//				for (int i = 0; i < tmp.length; ++i) {
//					result += tmp[i];
//				}
//			}
//			// Log.i("test",result+" result.length: "+result.length());
//		}
//		return result;
//	}
//
//	public String callCmd(String cmd, String filter) {
//		String result = "";
//		String line = "";
//		try {
//			Process proc = Runtime.getRuntime().exec(cmd);
//			InputStreamReader is = new InputStreamReader(proc.getInputStream());
//			BufferedReader br = new BufferedReader(is);
//
//			// 执行命令cmd，只取结果中含有filter的这一行
//			while ((line = br.readLine()) != null
//					&& line.contains(filter) == false) {
//				// result += line;
//				// Log.i("test","line: "+line);
//			}
//
//			result = line;
//			// Log.i("test","result: "+result);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return result;
//	}

    /********************************** Mac address 方法2 ****************************************************************/
    /**
     * get the Mac Address from the file /proc/net/arp
     *
     * @param context
     * @attention the file /proc/net/arp need exit
     * @return Mac Address
     */
//	private static String getMacFromFile(Context context) {
//		List<String> mResult = readFileLines("/proc/net/arp");
//
//		Log.d("TAG_NETWORK", "=======  /proc/net/arp  =========");
//		for (int i = 0; i < mResult.size(); ++i)
//			Log.d("line", mResult.get(i));
//		Log.d("TAG_NETWORK", "===========================");
//
//		if (mResult != null && mResult.size() > 1) {
//			for (int j = 1; j < mResult.size(); ++j) {
//				List<String> mList = new ArrayList<String>();
//				String[] mType = mResult.get(j).split(" ");
//				for (int i = 0; i < mType.length; ++i) {
//					if (mType[i] != null && mType[i].length() > 0)
//						mList.add(mType[i]);
//				}
//
//				if (mList != null && mList.size() > 4) {
//					String result = "";
//					String[] tmp = mList.get(3).split(":");
//					for (int i = 0; i < tmp.length; ++i) {
//						result += tmp[i];
//					}
//					result = result.toUpperCase();
//					Log.i("TAG_NETWORK", "Mac address(file): " + result);
//					return result;
//				}
//			}
//		}
//		return null;
//	}

    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
//	private static List<String> readFileLines(String fileName) {
//		File file = new File(fileName);
//		BufferedReader reader = null;
//		String tempString = "";
//		List<String> mResult = new ArrayList<String>();
//		try {
//			Log.i("result", "以行为单位读取文件内容，一次读一整行：");
//			reader = new BufferedReader(new FileReader(file));
//			while ((tempString = reader.readLine()) != null) {
//				mResult.add(tempString);
//			}
//			reader.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			if (reader != null) {
//				try {
//					reader.close();
//				} catch (IOException e1) {
//				}
//			}
//		}
//
//		return mResult;
//	}

    /********************************** Mac address 方法3 ****************************************************************/
    public static String getLocalMacAddress() {
        String Mac = null;
        try {

            String path = "sys/class/net/wlan0/address";
            if ((new File(path)).exists()) {
                FileInputStream fis = new FileInputStream(path);
                byte[] buffer = new byte[8192];
                int byteCount = fis.read(buffer);
                if (byteCount > 0) {
                    Mac = new String(buffer, 0, byteCount, "utf-8");
                }
            }
//			Log.v("daming.zou***wifi**mac11**", "" + Mac);
            if (Mac == null || Mac.length() == 0) {
                path = "sys/class/net/eth0/address";
                FileInputStream fis_name = new FileInputStream(path);
                byte[] buffer_name = new byte[8192];
                int byteCount_name = fis_name.read(buffer_name);
                if (byteCount_name > 0) {
                    Mac = new String(buffer_name, 0, byteCount_name, "utf-8");
                }
            }
//			Log.v("daming.zou***eth0**mac11**", "" + Mac);

            if (Mac.length() == 0 || Mac == null) {
                return "";
            }
        } catch (Exception io) {
            Log.v("daming.zou**exception*", "" + io.toString());
        }
//		Log.v("xulongheng*Mac", Mac);
        return Mac.trim();
    }

    /**
     * 是否是wifi环境
     * @return
     */
    public static boolean isWifi() {
        ConnectivityManager connectivityManager = JcbApplication.getInstance().getConnectivity();
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    /**
     * 设备是否有网络
     * @return
     */
    public static boolean isConnect() {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        try {
            ConnectivityManager connectivity = JcbApplication.getInstance().getConnectivity();
            if (connectivity != null) {
                // 获取网络连接管理的对象
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    // 判断当前网络是否已经连接
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
//			System.out.println("获取网络状态异常");
            return false;
        }
        return false;
    }
}
