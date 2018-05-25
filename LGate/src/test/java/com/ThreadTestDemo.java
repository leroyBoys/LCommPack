package com;


import com.lgame.core.LQStart;
import com.lgame.mysql.impl.LQDataSource;

import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2017/3/31.
 */
public class ThreadTestDemo {
    public static Vector<Integer> numList = new Vector<>();
    public static LinkedBlockingDeque<Integer>  sdf = new LinkedBlockingDeque<> ();
    public static AtomicInteger _num = new AtomicInteger(0);
    public static  int threads = 5000;

    public static long startTime = 0;
    public static AtomicInteger _num22 = new AtomicInteger(0);

    public static void main(String[] args) {
        startTime = System.currentTimeMillis();

        final String sql = "DELETE from `tbl_player` where id = ?";
        final String sql2 = "insert into `tbl_player`(id,d_time) values (?,?)";
      //  final SqlPool sqlPool = new SqlPool(SqlPool.DataSourceType.Hikari);
       final LQDataSource jdbcTemplate = LQStart.getJdbcManager().getMaster();
        runTest(threads, new MainThread() {
            @Override
            public void run(final Integer id) {
                try {
                    jdbcTemplate.ExecuteUpdate(sql,id);
                    System.out.println(jdbcTemplate.ExecuteInsert(sql2,id,"2014-10-10")+" id:"+id);
                   /*  ps.executeUpdate(sql,id);
                    ps.executeUpdate(sql2,id,id);*/
                    _num22.incrementAndGet();
                }catch (Exception e){
                    e.printStackTrace();
                    System.out.println("=======================>");
                }finally {

                }


            }
        });

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("=list:"+numList.size()+"/"+(System.currentTimeMillis()-startTime)+" s");
    }

    private static void add_Num(){
        _num.incrementAndGet();

        if(threads == _num.get()){
            System.out.println((System.currentTimeMillis()-startTime) + " ms"+Math.ceil((System.currentTimeMillis()-startTime)/1000)+" s" +"  "+_num22);
        }
    }

    public static void runTest(final int threads,final MainThread mainThread){
        final CountDownLatch latch = new CountDownLatch( threads );
        for ( int i = 0; i < threads; ++i )
        {
            final int id = i;
            final Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        latch.countDown();
                        latch.await();

                        mainThread.run(id);
                        add_Num();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                 //   add_Num();
                }
            } );
            thread.start();

        }
    }


    interface MainThread{
        public void run(Integer id);
    }
}
