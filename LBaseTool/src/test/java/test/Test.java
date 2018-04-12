/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.lgame.util.comm.StringTool;
import com.lgame.util.http.HttpTool;

import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author leroy_boy
 */
public class Test {
    public static void main(String[] args) {
       String sj= HttpTool.httpGet("https://s.click.taobao.com/t?e=m%3D2%26s%3DvfDGbdOUB8yw%2Bv2O2yX1MeeEDrYVVa64LKpWJ%2Bin0XLjf2vlNIV67q4f%2FAzrd3UB18u9BjgaVz6FRVQo%2BDh00kJUHyd2dIQcYUJOPaqG8lgBUnXF4FHWjDerllUiS3naG6Ta2zz3Zu1pTbn5pyQbtKKYbEGbWAlocSpj5qSCmbA%3D&amp;pvid=26_117.136.0.42_1265_1521347779324&amp;ut_sk=1.utdid_null_1521347799180.TaoPassword-Outside.lianmeng-app&amp;spm=a211b4.24823497&amp;visa=13a09278fde22a2e&amp;disablePopup=true&amp;disableSJ=1");
        System.out.println(sj);
    }
}
