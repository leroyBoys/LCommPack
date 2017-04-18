/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.lgame.util.comm.StringTool;

import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author leroy_boy
 */
public class Test {
    public static void main(String[] args) {
//        System.out.println("-----d当前日期:"+DateUtils.currentDate());
//        List<String> aa = new ArrayList<>();
//        aa.add("11");
//        System.out.println("-----"+JsonUtil.getJsonFromBean(aa));
//        AtomicInteger sa=  new AtomicInteger(20);
//        for(int i = 0;i<10;i++){
//            System.out.println(sa.getAndIncrement());
//        }

        try {
            LinkedBlockingQueue<String> queue=new LinkedBlockingQueue(2);

            queue.take();
            queue.put("hello");
            System.out.println("==");
            queue.put("world");
            System.out.println("==world");
            queue.put("yes");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }


        int[] ll = new int[11];
        String sql = "{0},{1}";
        for(int i = 0;i<10;i++){
            System.out.println(StringTool.Format(sql,new Object[]{i,i+1}));
            System.out.println(sql);
            ll[i] = i+1;
            System.out.println("=========>"+i+"    "+(i & -i));
        }

 /*       KVData<Integer,KVData> obj = new KVData<>();
        obj.setK(11);
        obj.setV(new KVData(12,13));
        System.out.println(JsonTool.getJsonFromBean(obj));*/

        //Date data = new Date();
    /*    Date data = DateUtil.getDateTime("1987-09-10 0:0:0");
        System.out.println(DateUtil.getDateTime(DateUtil.getFirstDayOfMonth(data)));
        System.out.println(DateUtil.getDateTime(DateUtil.getLastDayOfMonth(data)));*/

        int im = 3;
        System.out.println(im<<1);
        System.out.println(im<<2);

    }
}
