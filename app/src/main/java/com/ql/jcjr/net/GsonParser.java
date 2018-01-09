package com.ql.jcjr.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by Liuchao on 2016/9/23.
 */
public class GsonParser {

    public static <T> T getParsedObj(String json, Class<T> cls) {
        T t = null;
        try {
            Gson gson = new GsonBuilder().create();
            t = gson.fromJson(json, cls);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return t;
    }
}