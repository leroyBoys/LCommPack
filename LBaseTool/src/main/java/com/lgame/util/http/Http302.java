package com.lgame.util.http;
import java.net.HttpURLConnection;
import java.net.URL;
/**
 * Created by Administrator on 2018/3/18.
 */
public class Http302 {
    public static void main(String[] args) {
        String location = get302("https://s.click.taobao.com/t?e=m%3D2%26s%3DvfDGbdOUB8yw%2Bv2O2yX1MeeEDrYVVa64LKpWJ%2Bin0XLjf2vlNIV67q4f%2FAzrd3UB18u9BjgaVz6FRVQo%2BDh00kJUHyd2dIQcYUJOPaqG8lgBUnXF4FHWjDerllUiS3naG6Ta2zz3Zu1pTbn5pyQbtKKYbEGbWAlocSpj5qSCmbA%3D&amp;pvid=26_117.136.0.42_1265_1521347779324&amp;ut_sk=1.utdid_null_1521347799180.TaoPassword-Outside.lianmeng-app&amp;spm=a211b4.24823497&amp;visa=13a09278fde22a2e&amp;disablePopup=true&amp;disableSJ=1");
        System.out.println(location);
        System.out.println(HttpTool.httpGet(location));
    }

    public static String get302(String url){

        try {
            System.out.println("访问地址:" + url);
            URL serverUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) serverUrl
                    .openConnection();
            conn.setRequestMethod("GET");
            // 必须设置false，否则会自动redirect到Location的地址
            conn.setInstanceFollowRedirects(false);

            conn.addRequestProperty("Accept-Charset", "UTF-8;");
            conn.addRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.8) Firefox/3.6.8");
            conn.connect();
            String location = conn.getHeaderField("Location");
            System.out.println("===>"+location);
            return location;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
