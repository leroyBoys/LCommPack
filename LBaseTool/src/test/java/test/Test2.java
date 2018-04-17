package test;

import com.lgame.util.excel.POIReadData;

/**
 * Created by Administrator on 2018/4/12.
 */
public class Test2 {
    public static void main(String[] args) throws InterruptedException {
       // new POIReadData().read("D:\\ww.xls");


        String str="adsf23(dsfsdl)";
        System.out.println(str.replaceAll("(?<=\\()(.*?)(?=\\))", "").replaceAll("(|)",""));
    }
}
