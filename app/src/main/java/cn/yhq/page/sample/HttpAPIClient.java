package cn.yhq.page.sample;

import android.content.Context;

import cn.yhq.http.core.HttpRequester;

/**
 * Created by Yanghuiqiang on 2016/10/12.
 */

public class HttpAPIClient {

    public static void init(Context context) {
        HttpRequester.init(context);
        HttpRequester.registerXAPI("http://v5.pc.duomi.com", API.class);
    }

    public static API getAPI() {
        return HttpRequester.getAPI(API.class);
    }
}
