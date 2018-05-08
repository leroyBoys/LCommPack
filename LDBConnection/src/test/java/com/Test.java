package com;

import com.lgame.util.PrintTool;
import com.module.GameServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by leroy:656515489@qq.com
 * 2018/4/28.
 */
public class Test{
   static final int size = 9999999;
    /**
     * @param args
     */
    @SuppressWarnings("static-access")
    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
        /*test1();
        test2();
        test22();*/

    }

    public static void test1(){
        Class cls = GameServer.class;
        Map<Class,Map<String,Integer>> map = new HashMap<>();
        Map<String,Integer> stringStringMap = new HashMap<>();
        stringStringMap.put("abc",1);
        map.put(cls,stringStringMap);

        int sum = 0;
        PrintTool.outTime("1","===>");
        for(int i=0;i<size;i++){
            stringStringMap = map.get(cls);
            if(stringStringMap != null){
                sum+=stringStringMap.get("abc");
            }
        }
        PrintTool.outTime("1",sum+"  ===>over:");
    }


    public static void test2(){
        Class cls = GameServer.class;
        Map<String,Integer> stringStringMap = new HashMap<>();
        stringStringMap.put(cls.getSimpleName()+"abc",1);
        String str;
        int sum = 0;
        PrintTool.outTime("11","===>");
        for(int i=0;i<size;i++){
            str = cls.getSimpleName()+"abc";
            sum+=stringStringMap.get(str);
        }
        PrintTool.outTime("11",sum+"   ===>over:");
    }


    public static void test22(){
        Class cls = GameServer.class;
        Map<String,Integer> stringStringMap = new HashMap<>();
        stringStringMap.put(cls.getSimpleName()+"abc",1);
        String clasName = cls.getSimpleName();

        String str;
        int sum = 0;
        PrintTool.outTime("121","===>");
        for(int i=0;i<size;i++){
            str = clasName+"abc";
            sum += stringStringMap.get(str);
        }
        PrintTool.outTime("121",sum+"  ===>over:");
    }
}