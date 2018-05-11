/*
package com.pro;

import com.alibaba.fastjson.JSON;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.lgame.util.PrintTool;
import com.lgame.util.json.JsonUtil;
import com.lgame.util.time.DateTimeTool;

import java.util.ArrayList;
import java.util.List;

*/
/**
 * Created by leroy:656515489@qq.com
 * 2018/5/11.
 *//*

public class Test {
    static int size = 1;

    public void test() throws Exception {
        protoBuffStf();
        protoBuffStf_read();
       // protoBuff();
      //  fastJson();
      */
/*
        protoBuffStf_read();
        protoBuffBaidu_read();*//*

        */
/*protoBuffStf();
        protoBuff();
        json_read();
        *//*

    }

 */
/*   public List<Products2.Products22> _deserializeProtoBufDataListToProducts22List(
            List<byte[]> bytesList) {
        long start = System.currentTimeMillis();
        List<Products2.Products22> list = new ArrayList<Products2.Products22>();
        for(byte[] b : bytesList) {
            try {
                list.add(Products2.Products22.parseFrom(b));
            } catch (InvalidProtocolBufferException e) {
                e.printStackTrace();
            }
        }
        long end = System.currentTimeMillis();
        return list;
    }*//*


 */
/*   public List<byte[]> _serializeProtoBufProductsList(
            List<Products2.Products22.Builder> builderList) {
        if(builderList == null) {
            System.out.println("【ProtoBufSerializeServiceImpl-serializeProtoBufProductsService】builderList==null");
        }
        List<byte[]> bytesList = new ArrayList<byte[]>();
        for(Products2.Products22.Builder p22Builder : builderList){
            Products2.Products22 p22 = p22Builder.build();
            byte[] bytes = p22.toByteArray();
            bytesList.add(bytes);
        }
        return bytesList;
    }

*//*

*/
/*    public void protoBuff(){
        Products2.Products22 products = getProDucts22();

        PrintTool.outTime("1","====");

        for(int i = 0;i<size;i++){
            byte[] bytes = products.toByteArray();
        }

        PrintTool.outTime("1","====over");
    }


    public void protoBuff_read() throws InvalidProtocolBufferException {
        Products2.Products22 products = getProDucts22();
        byte[] bytes = products.toByteArray();

        PrintTool.outTime("1","====");

        Products2.Products22 products22 = null;
        for(int i = 0;i<size;i++){
            products22 = Products2.Products22.parseFrom(bytes);
        }

        PrintTool.outTime("1","====over");
        System.out.println(products22.getL3());
    }*//*


    public void protoBuffStf_read() throws Exception {
        Products products = getProDucts();
        Schema<Products> schema = RuntimeSchema.getSchema(Products.class);
        byte[]  bytes = ProtostuffIOUtil.toByteArray(products, schema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
        PrintTool.outTime("1","====");

        Products product = null;
        for(int i = 0;i<size;i++){
            product = new Products();
            ProtostuffIOUtil.mergeFrom(bytes, product, schema);
        }
        PrintTool.outTime("1","====over");
        System.out.println(product.getL4());
    }


    public void protoBuffStf() throws Exception {
        Products products = getProDucts();
        byte[] bytes = null;
        Schema<Products> schema = RuntimeSchema.getSchema(Products.class);
        PrintTool.outTime("1","====");


        for(int i = 0;i<size;i++){
            bytes = ProtostuffIOUtil.toByteArray(products, schema, LinkedBuffer.allocate(4096));
        }

        PrintTool.outTime("1","====over");
        Products product = new Products();
        ProtostuffIOUtil.mergeFrom(bytes, product, schema);
        System.out.println(JsonUtil.getJsonFromBean(product));
    }

    public List<Products> _deserializeProtoStuffDataListToProductsList(
            List<byte[]> bytesList) {
        if(bytesList == null || bytesList.size() <= 0) {
            return null;
        }
        long start = System.currentTimeMillis() ;
        Schema<Products> schema = RuntimeSchema.getSchema(Products.class);
        List<Products> list = new ArrayList<Products>();
        for(byte[] bs : bytesList) {
            Products product = new Products();
            ProtostuffIOUtil.mergeFrom(bs, product, schema);
            list.add(product);
        }
        long end = System.currentTimeMillis() ;
        return list;
    }

    public List<byte[]> _serializeProtoStuffProductsList(List<Products> pList) {
        if(pList == null  ||  pList.size() <= 0) {
            return null;
        }
        List<byte[]> bytes = new ArrayList<byte[]>();
        Schema<Products> schema = RuntimeSchema.getSchema(Products.class);
        LinkedBuffer buffer = LinkedBuffer.allocate(4096);
        byte[] protostuff = null;
        for(Products p : pList) {
            try {
                protostuff = ProtostuffIOUtil.toByteArray(p, schema, buffer);
                bytes.add(protostuff);
            } finally {
                buffer.clear();
            }
        }
        long end = System.currentTimeMillis() ;
        return bytes;
    }

    private void json(){
        Products products = getProDucts();

        PrintTool.outTime("1","====");
        for(int i = 0;i<size;i++){
            String str = JsonUtil.getJsonFromBean(products);
        }

        PrintTool.outTime("1","====over");
    }


    private void json_read(){
        Products products = getProDucts();
        String str = JsonUtil.getJsonFromBean(products);
        PrintTool.outTime("1","====");
        for(int i = 0;i<size;i++){
            JsonUtil.getBeanFromJson(str,Products.class);
        }

        PrintTool.outTime("1","====over");
    }

    private void fastJson(){
        Products products = getProDucts();
        PrintTool.outTime("1","====");
        String jsonString = null;
        for(int i = 0;i<size;i++){
            jsonString = JSON.toJSONString(products);
        }

        PrintTool.outTime("1","=fastJson===over:"+jsonString);
    }

    private void fastJson_read(){
        Products products = getProDucts();
        String  jsonString = JSON.toJSONString(products);
        PrintTool.outTime("1","====");
        for(int i = 0;i<size;i++){
            products = JSON.parseObject(jsonString,Products.class);
        }

        PrintTool.outTime("1","=fastJson===over:"+products.getS5());
    }



    private Products getProDucts(){
        Products products = new Products();
        products.setB1(true);
        products.setB2(true);
        products.setB3(true);
        products.setB4(true);
        products.setB5(true);
        products.setB6(true);
        products.setB7(true);
        products.setB8(true);
        products.setB9(true);
        products.setI1(1);
        products.setI2(2);
        products.setI3(3);
        products.setI4(4);
        products.setI5(5);
        products.setI6(6);
        products.setI7(7);
        products.setI8(8);
        products.setI9(9);

        products.setS1("str1");
        products.setS2("str2");
        products.setS3("str3");
        products.setS4("str4");
        products.setS5("str5");
        products.setS6("str6");
        products.setS7("str7");
        products.setS8("str8");
        products.setS9("str9");

        products.setL1(100000l);
        products.setL2(200000l);
        products.setL3(300000l);
        products.setL4(400000l);
        products.setL5(500000l);
        products.setL6(600000l);
        products.setL7(700000l);
        products.setL8(800000l);
        products.setL9(900000l);


        Products products1 = new Products();
        products1.setI1(11111);
        products.setS1("1223232323232");
        products.setProducts(products1);

        products.setDate(DateTimeTool.getDateTime("2003-10-10 19:11:12"));
        return products;
    }
}
*/
