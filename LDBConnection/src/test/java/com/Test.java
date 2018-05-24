package com;


import com.lgame.core.LQSpringScan;
import com.lgame.mysql.impl.DataSourceImpl;
import com.lgame.util.LqUtil;
import com.test.TestData;

import java.util.List;

/**
 * Created by leroy:656515489@qq.com
 * 2018/5/16.
 */
public class Test {


    public void mai(){
        String k = "datasource.redis.slave.01.02.timeOut";
        System.out.println(k.lastIndexOf("."));
        System.out.println(k.indexOf("redis")+5);

        String key = k.replace("slave","-");
        System.out.println(key);
        String msConfigKey = key.substring(0,key.indexOf(".",key.indexOf("-")+2));
        System.out.println(msConfigKey);
    }
    @org.junit.Test
    public void test() throws Exception {
        LQSpringScan.instance("com");

        LQSpringScan.initConnectionManager(LqUtil.loadProperty("db.properties"));

    //    LqJdbcPool jdbcPool = new LqJdbcPool(LqJdbcPool.DataSourceType.Hikari, LqUtil.loadProperty("hikari_db.properties"));
        DataSourceImpl jdbcPool = LQSpringScan.getJdbcManager().getMaster();
        String sql = "SELECT test_data.* ,test1.`id` AS tid,test1.`name` AS tname FROM `test_data` RIGHT JOIN test1 ON test_data.`id` = test1.`id`";


        List<TestData> testDataList = jdbcPool.ExecuteQueryList(TestData.class,sql);

        System.out.println(testDataList.size());

        TestData testData = testDataList.get(1);
        testData.setName("tomess");
        jdbcPool.ExecuteEntity(testData);

        System.out.println(testData.getName());


        testData.setId(0);
        testData.setName("新增");
        jdbcPool.ExecuteEntity(testData);

        System.out.println(testData.getName()+"   "+testData.getId());
    }
}
