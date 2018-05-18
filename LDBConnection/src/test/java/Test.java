import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.lgame.util.PrintTool;
import com.lgame.util.comm.StringTool;
import com.lgame.util.file.PropertiesTool;
import com.lgame.util.json.FastJsonTool;
import com.lgame.util.json.JsonUtil;
import com.lgame.util.thread.TaskIndieThread;
import com.lgame.util.thread.TaskPools;
import com.module.GameServer;
import com.mysql.compiler.ScanEntitysTool;
import com.mysql.entity.DBTable;
import com.mysql.entity.LQDBEnum;
import com.mysql.impl.JdbcTemplate;
import com.pro.Products;
import com.redis.entity.MapRedisSerializer;
import com.redis.entity.RedisKey;
import com.redis.impl.RedisConnectionImpl;
import com.test.TestData;
import com.test.TestEnum;
import com.test.TestEnum2;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.pro.Test.getProDucts;

/**
 * Created by Administrator on 2017/4/2.
 */
public class Test {


    public void go() {
        String url = "redis://@119.254.166.136:6379";
        String[] firstArray = url.split("//");
        String[] secondArray = firstArray[1].split("@");
        int db = 0;
        if(!secondArray[0].trim().isEmpty()){
            db = Integer.valueOf(secondArray[0]);
        }

        String[] threeArray = secondArray[1].split(":");
        String host = threeArray[0];
        int port = Integer.valueOf(threeArray[1].split("/")[0]);

        String password = null;
        if(threeArray[1].split("/").length>1){
            password = StringTool.trimToNull(threeArray[1].split("/")[1]);
        }

        System.out.println("db:"+db+"  ip:"+host+"  port:"+port+"  pwd:"+password);
    }
    @org.junit.Test
    public void thres() throws Exception {
    //    dbTest();
     //   redisTest();
        testJdkReflect();
        //RedisConnectionImpl redisConnectionManager = new RedisConnectionImpl("redis://0@192.168.11.128:3307");
       /* RedisConnectionImpl redisConnectionManager = new RedisConnectionImpl("redis://0@192.168.11.133:6378/123456");
        PrintTool.outTime("1","=PrintTool==>");

        redisConnectionManager.set("abc","sss");
        System.out.println(redisConnectionManager.get("abc"));
        PrintTool.outTime("1","=PrintTool==>");*/

     //   redisConnectionManager.test();
        //   go();
      //  testReflectAsm();
     //   tttDB22();

      /*  Map<String,String> map = new HashMap<>();
        map.put("id","ss");

        final ClassIntrospector ci = new ClassIntrospector();

        ObjectInfo res;

        res = ci.introspect(map);
        System.out.println( res.getDeepSize() );*/
    }

    private void redisTest() throws Exception {
     //   RedisConnectionImpl master = new RedisConnectionImpl("redis://0@192.168.11.133:6378/123456");
        RedisConnectionImpl master = new RedisConnectionImpl("redis://0@192.168.11.128:3307");
        Map<String,String> map = new HashMap<>();
        for(int i =0;i<100;i++){
            map.put("str"+i,"str"+i);
        }

        String key = "test-ls";
        master.hmset(key,map);



        ScanEntitysTool.scan("com.pro");
        Products products = getProDucts();

        DBTable dbTable = ScanEntitysTool.getDBTable(products.getClass());
        MapRedisSerializer mapRedisSerializer = (MapRedisSerializer) dbTable.getRedisSerializer();
       // map = mapRedisSerializer.serializer_single(products);
        System.out.println(JsonUtil.getJsonFromBean(map));

        String[] arr = new String[map.size()];
        map.keySet().toArray(arr);
        int  size = 1000000;
        String json = FastJsonTool.getJsonFromBean(products);
        PrintTool.outTime("1","=PrintTool==>");

        for(int i =0;i<size;i++){
         //   products = (Products) mapRedisSerializer.mergeFrom_single(arr,map,dbTable,products.getClass());
//            master.hgetAll(key);
           // mapRedisSerializer.serializer_single(products);
           // FastJsonTool.getBeanFromJson(json,Products.class);
            //master.hgetAll2(key,map);
          //  master.get("abd");
        }

        PrintTool.outTime("1","=PrintTool==>");




    }

    private void dbTest() throws Exception {
        ScanEntitysTool.scan("com.test");
        String sql = "SELECT * FROM `test_data`";
        JdbcTemplate db = new JdbcTemplate(JdbcTemplate.DataSourceType.Hikari,PropertiesTool.loadProperty("hikari_db.properties"));
        List<TestData> datas = db.ExecuteQuery(TestData.class,sql);
        TestData data = datas.get(1);
        data.setName("逗你玩");
        data.setTestEnum(TestEnum.Name);
        data.setTestEnum2(TestEnum2.More);
        db.ExecuteEntity(data);
    }


    public static void testJdkReflect() throws Exception {

        ScanEntitysTool.scan("com.test");
        String sql = "SELECT * FROM `test_data`";
        JdbcTemplate db = new JdbcTemplate(JdbcTemplate.DataSourceType.Hikari,PropertiesTool.loadProperty("hikari_db.properties"));
   //     RedisConnectionImpl redisConnectionManager = new RedisConnectionImpl("redis://0@127.0.0.1:6379");
      // RedisConnectionImpl redisConnectionManager = new RedisConnectionImpl("redis://0@192.168.11.128:3307");
        RedisConnectionImpl redisConnectionManager = new RedisConnectionImpl("redis://0@192.168.11.133:63781/123456");
        RedisConnectionImpl sleav1 = new RedisConnectionImpl("redis://0@192.168.11.133:63782/123456");

        RedisConnectionImpl master = new RedisConnectionImpl("redis://0@192.168.11.133:6378/123456");

        String key ="abd";
        master.set(key,"哈哈哈");
        System.out.println("master:"+master.get(key));
        Thread.sleep(100);
        System.out.println("sleav1:"+redisConnectionManager.get(key));
        Thread.sleep(100);
        System.out.println("sleav1:"+sleav1.get(key));



        RedisKey redisKey = new RedisKey() {
            @Override
            public boolean isSynFromDb() {
                return false;
            }

            @Override
            public Object queryFromDb(Object... paramters) {
                return null;
            }

            @Override
            public String getKey(Object... paramters) {
                return paramters[0]+""+paramters[1];
            }
        };

        PrintTool.outTime("1","===>");
        int size = 1;
      /*  for(int i = 0;i<size;i++){
            redisConnectionManager.hgetAll(redisKey.getKey("a",1));
       //     redisConnectionManager.hget("fiel哈哈d2"+i,redisKey,"a",1);
         //   redisConnectionManager.hset("fiel哈哈d2"+i, "va张三李四luess", redisKey,"a",1);
        //    redisConnectionManager.hset("a"+1,"fiel哈哈d2"+i, "va张三李四luess");
        }*/
     /*   System.out.println(redisConnectionManager.hget("field", redisKey,"a",1));

        String str = redisConnectionManager.hget(redisKey.getKey("a",1),"field");

        System.out.println(JsonUtil.getJsonFromBean(redisConnectionManager.hgetAll(redisKey.getKey("a",1))));*/
        PrintTool.outTime("1","=PrintTool==>");

 /*       for(int i = 0;i<5000;i++){
            List<TestData> testDatas =  db.ExecuteQuery(TestData.class,sql);
            //     System.out.println(JsonUtil.getJsonFromBean(testDatas));
            TestData data = testDatas.get(1);
            data.setName("张龙赵虎");
        }
*/
        List<TestData> testDatas =  db.ExecuteQuery(TestData.class,sql);

        System.out.println(FastJsonTool.getJsonFromBean(testDatas));
        //     System.out.println(JsonUtil.getJsonFromBean(testDatas));
     /*   TestData data = testDatas.get(1);
        data.setName("张2");*/
        PrintTool.outTime("1","==22=>");


        AtomicInteger integer = new AtomicInteger();
        for(int i = 0;i<size;i++){
            TaskPools.addTask(new TaskIndieThread() {
                @Override
                public void doExcute(Object... objects) {
                /*    redisConnectionManager.hset("fiel哈哈d2"+objects[0], "va张三李四luess", redisKey,"a",1);
                    redisConnectionManager.hget("fiel哈哈d2"+objects[0],redisKey,"a",1);*/
                   // redisConnectionManager.hgetAll(redisKey.getKey("a",1));
                    //redisConnectionManager.hgetAll(redisKey.getKey("a",1));
               //     db.Execute("update test_data  set num='99'   where id = "+((int)objects[0]+1453));
                 /*   TestData data = new TestData();
                    data.setName("张sdfsdfsdf1 ");
                    data.setId(1453);
                    data = redisConnectionManager.ExeuteQuery(TestData.class,1453);
                    //db.Execute("update test_data  set num='99'   where id = 22");
                    if(10 == (int)objects[0]){
                        System.out.println(FastJsonTool.getJsonFromBean(data));
                    }*/
                    integer.getAndAdd(1);
                }
            },i);

        }

        while (integer.get() !=size ){
            Thread.sleep(10);
        }

        PrintTool.outTime("1","===>");

    }

    public void testReflectAsm() throws Exception  {
        long now;
        long sum = 0;
        Class<?> c222 = GameServer.class;

        now = System.currentTimeMillis();

        for(int i = 0; i<500000000; ++i){
            GameServer t = new GameServer();
            t.setId(i);
            sum += t.getId();
        }
        System.out.println("get-set耗时"+(System.currentTimeMillis() - now) + "ms秒，和是" +sum);
        ttt();
        sum = 0;
        now = System.currentTimeMillis();

        for(int i = 0; i<5000000; ++i){
            Class<?> c = GameServer.class;
            GameServer o = (GameServer) c.newInstance();
            Class<?>[] argsType = new Class[1];
            argsType[0] = int.class;

            Method m = c.getMethod("setId",argsType);
            m.invoke(o, i);
            sum += o.getId();
        }
        System.out.println("标准反射耗时"+(System.currentTimeMillis() - now) + "ms，和是" +sum);
        ttt2();
        sum = 0;

        Map<String,Object> maps = new HashMap<>();
        maps.put("id",1);
        now = System.currentTimeMillis();

        for(int i = 0; i<500000000; ++i){
            sum +=  (int)maps.get("id");
        }
        System.out.println("map耗时"+(System.currentTimeMillis() - now) + "ms，和是" +sum);
        sum = 0;

        now = System.currentTimeMillis();

        for(int i = 0; i<500000000; ++i){
            Map<String,Object> map = new HashMap<>(1);
            map.put("id",i);
            sum +=  (int)map.get("id");
        }
        System.out.println("map222耗时"+(System.currentTimeMillis() - now) + "ms，和是" +sum);
        sum = 0;
    }

    private void ttt2() throws Exception {
        long sum = 0;
        Class<?>[] argsType = new Class[1];
        argsType[0] = int.class;
        Class<?> c = GameServer.class;
        Map<String,Method> methodMap = new HashMap<>();
        methodMap.put("setId",c.getMethod("setId",argsType));
        long now = System.currentTimeMillis();
        for(int i = 0; i<500000000; ++i){
            GameServer o = new GameServer();

            methodMap.get("setId").invoke(o, i);
            sum += o.getId();
        }
        System.out.println("缓存反射耗时2222:::"+(System.currentTimeMillis() - now) + "ms，和是" +sum);

    }
    private void ttt() throws Exception {
        long sum = 0;
        Class<?>[] argsType = new Class[1];
        argsType[0] = int.class;
        Class<?> c = GameServer.class;
        Method m = c.getMethod("setId",argsType);

        long now = System.currentTimeMillis();
        for(int i = 0; i<500000000; ++i){
            GameServer o = (GameServer) c.newInstance();

            m.invoke(o, i);
            sum += o.getId();
        }
        System.out.println("缓存反射耗时"+(System.currentTimeMillis() - now) + "ms，和是" +sum);

    }
    //redis://db@119.254.166.136:6379/pwd
    private void tttDB22() throws Exception {
       // RedisConnectionImpl redisConnectionManager = new RedisConnectionImpl("redis://0@192.168.11.128:3307");
      /*  RedisConnectionImpl redisConnectionManager = new RedisConnectionImpl("redis://0@127.0.0.1:6379");
        redisConnectionManager.hset("abc","abc",100+"");
        long sum = 0;

        long now = System.currentTimeMillis();
        for(int i = 0; i<50000; ++i){
            String obj = redisConnectionManager.hget("abc","abc");
            GameServer o = new GameServer();
            o.setId(i);
            sum += o.getId();
        }
        System.out.println("===========db耗时"+(System.currentTimeMillis() - now) + "ms，和是" +sum);


        sum = 0;
        now = System.currentTimeMillis();
        Cache<Integer,GameServer> gameServerCache = EchCacheManager.getCache("abc",Integer.class,GameServer.class);
        for(int i = 0; i<500; ++i){
            GameServer o = new GameServer();
            o.setId(i);
            gameServerCache.put(i,o);
        }
        System.out.println("========set cache时"+(System.currentTimeMillis() - now) + "ms，和是" +sum);
        now = System.currentTimeMillis();

        for(int i = 0; i<500; ++i){
            GameServer o = gameServerCache.get(i);
            if(o == null){
                continue;
            }
            System.out.println(o.getId());
            sum += o.getId();
        }
        System.out.println("========555===db耗时"+(System.currentTimeMillis() - now) + "ms，和是" +sum);*/

    }

    private void tttDB() throws Exception {
        JdbcTemplate db = new JdbcTemplate(PropertiesTool.loadProperty("druid_db.properties"));

        long sum = 0;

        long now = System.currentTimeMillis();
        for(int i = 0; i<5000; ++i){
            Object obj = db.ExecuteQueryOnlyValue("select id from test1");
            if(i < 2){
                System.out.println(obj);
            }
            GameServer o = new GameServer();
            o.setId(i);
            sum += o.getId();
        }
        System.out.println("===========db耗时"+(System.currentTimeMillis() - now) + "ms，和是" +sum);

    }

}
