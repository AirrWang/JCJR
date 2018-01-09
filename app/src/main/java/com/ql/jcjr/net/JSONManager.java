package com.ql.jcjr.net;

/**
 * Created by Liuchao on 2016/9/23.
 */
public class JSONManager {

    private static JSONManager mJSONManager;

    public JSONManager() {

    }

    public static JSONManager getInstance() {
        if (mJSONManager == null) {
            mJSONManager = new JSONManager();
        }
        return mJSONManager;
    }

    /**
     * Get photo list
     *
     * @return photolist

    public JSONObject getPhotoList(int type, int page, int topic) {
    JSONObject photoList = new JSONObject();
    try {
    photoList.put(AppConfig.FROM, AppConfig.PAGE_SIZE * (page - 1));
    photoList.put(AppConfig.PAGESIZE, AppConfig.PAGE_SIZE);
    photoList.put(AppConfig.SORT, type);
    photoList.put(AppConfig.TOPICID, topic);
    photoList.put(AppConfig.LANGUAGE, AppConfig.LAN_CHINESE);
    photoList.put(AppConfig.JSON_TAG, getTag());
    } catch (JSONException e) {
    e.printStackTrace();
    }

    return photoList;
    }*/
}
