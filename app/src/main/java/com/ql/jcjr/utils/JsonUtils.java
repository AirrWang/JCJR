package com.ql.jcjr.utils;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * ClassName: Sender
 * Description: Json数据转换
 * Author: Liuchao
 * Date: Created on 2016/6/29.
 */
public class JsonUtils {

    // 组成json数据
    public static String setJson(Map map) {
        JSONObject json = new JSONObject();
        String key = null;
        String value = null;

        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            key = entry.getKey();
            value = entry.getValue();
            try {
                json.put(key, value);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        System.out.println("requst json@@@@" + json.toString());
        return json.toString();
    }

    /**
     * 获取JSONObject
     *
     * @param jsonStr json数据字符串
     * @return
     */
    public static JSONObject getJSONObject(String jsonStr) {
        if (StringUtils.isBlank(jsonStr)) {
            return null;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(jsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return jsonObject;
    }

    /**
     * json数据转HashMap
     *
     * @param jsonObject    JSON数据对象
     * @param resultHashMap 打包SON数据的HashMap
     */
    public static void JsonToHashMap(JSONObject jsonObject, HashMap<String, Object> resultHashMap) {
        if (jsonObject == null || resultHashMap == null) {
            return;
        }
        try {
            for (Iterator<String> keyStr = jsonObject.keys(); keyStr.hasNext(); ) {
                String key = keyStr.next().trim();
                Object value = jsonObject.get(key);
                if (value instanceof JSONObject) {
                    HashMap<String, Object> mapObj = new HashMap<String, Object>();
                    JsonToHashMap((JSONObject) value, mapObj);
                    resultHashMap.put(key, mapObj);
                    continue;
                }
                if (value instanceof JSONArray) {
                    if (isImageUrlsList(key)) {//处理ImageUrls(List<String>)
                        List<String> imgUrls = new ArrayList<>();
                        JSONArray jsonArray = (JSONArray) value;
                        getImgUrls(jsonArray, imgUrls);
                        resultHashMap.put(key, imgUrls);
                    } else {
                        List<HashMap<String, Object>>
                                hashMapList = new ArrayList<HashMap<String, Object>>();
                        JSONArrayToHasMap((JSONArray) value, hashMapList);
                        resultHashMap.put(key, hashMapList);
                    }
                    continue;
                }

                if (value != null && !"null".equals(value) && !jsonObject.isNull(key)) {
                    //基本数据
                    jsonToHasMap(key, value, resultHashMap);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * JSON数据
     *
     * @param key
     * @param value
     * @param rstList
     */
    public static void jsonToHasMap(String key, Object value, HashMap<String, Object> rstList) {
        if (value instanceof Integer) {
            rstList.put(key, (int) value);
        } else if (value instanceof String) {
            rstList.put(key, (String) value);
        } else {
            rstList.put(key, value);
        }
    }

    /**
     * JSONArray
     *
     * @param jsonArray
     * @param rstList
     */
    public static void JSONArrayToHasMap(JSONArray jsonArray,
            List<HashMap<String, Object>> rstList) {
        if (jsonArray == null || jsonArray.length() == 0) {
            return;
        }
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                Object object = jsonArray.get(i);
                if (object instanceof JSONObject) {
                    HashMap<String, Object> mapObj = new HashMap<String, Object>();
                    JsonToHashMap((JSONObject) object, mapObj);
                    rstList.add(mapObj);
                    continue;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从JSONArray中获取图片数组
     *
     * @param jsonArray
     * @param imgUrls
     */
    public static void getImgUrls(JSONArray jsonArray, List<String> imgUrls) {
        if (jsonArray == null || jsonArray.length() == 0) {
            return;
        }
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                Object object = jsonArray.get(i);
                if (object instanceof String) {
                    imgUrls.add((String) object);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断此Key对应的Value是一个List<String>集合
     *
     * @return
     */
    public static boolean isImageUrlsList(String key) {
        if (key.endsWith("ImageUrls") || key.contains("ImageUrls")
                || key.endsWith("imageUrls") || key.equals("imageUrls")) {
            return true;
        }
        return false;
    }

    /**
     * 判断HashMap是否有此Key
     *
     * @param hashMap
     * @return 没有返回false, 有返回true
     */
    public static boolean isContainsKey(HashMap hashMap, List<String> attributeNames) {
        boolean isContainsKey = false;
        for (int i = 0; i < attributeNames.size(); i++) {
            String attributeName = attributeNames.get(i);

            //迭代HashMap，比较HashMap中是否有此attributeName
            Iterator resIt = hashMap.entrySet().iterator();
            while (resIt.hasNext()) {
                Map.Entry entry = (Map.Entry) resIt.next();
                String originalKey = (String) entry.getKey();
                if (originalKey.equals(attributeName)) {
                    isContainsKey = true;
                    break;
                }
            }
        }

        return isContainsKey;
    }

    /**
     * 将HashMap转换成byte
     *
     * @param info
     * @return
     */
    public static byte[] convertHashMapToByte(HashMap<String, Object> info) {
        if (info != null) {
            try {
                ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(arrayOutputStream);
                objectOutputStream.writeObject(info);
                objectOutputStream.flush();
                byte data[] = arrayOutputStream.toByteArray();
                objectOutputStream.close();
                arrayOutputStream.close();
                return data;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new byte[0];
    }

    /**
     * 将byte转换成HashMap
     *
     * @param bytes
     * @return
     */
    public static HashMap<String, Object> getHashMap(byte[] bytes) {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();

        ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(bytes);
        try {
            ObjectInputStream inputStream = new ObjectInputStream(arrayInputStream);
            hashMap = (HashMap<String, Object>) inputStream.readObject();
            inputStream.close();
            arrayInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return hashMap;
    }

    public static String convertStream2Json(InputStream inputStream) {
        String jsonStr = "";
        // ByteArrayOutputStream相当于内存输出流
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        // 将输入流转移到内存输出流中
        try {
            while ((len = inputStream.read(buffer, 0, buffer.length)) != -1) {
                out.write(buffer, 0, len);
            }
            // 将内存流转换为字符串
            jsonStr = new String(out.toByteArray());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return jsonStr;
    }

}
