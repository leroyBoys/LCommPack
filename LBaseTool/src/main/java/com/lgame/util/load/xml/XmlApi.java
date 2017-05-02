package com.lgame.util.load.xml;

import com.lgame.util.file.FileTool;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by leroy:656515489@qq.com
 * 2017/4/28.
 */
public class XmlApi {

    /**
     * 保存xml对象
     * @param obj 改对象必须注解XmlRootElement
     * @param pathName
     * @param isOverWrite 是否覆盖
     */
    public static void save(Object obj,String pathName,boolean isOverWrite){
        //将java对象转换为XML字符串
        JaxbUtil requestBinder = new JaxbUtil(obj.getClass(),
                JaxbUtil.CollectionWrapper.class);
        String retXml = requestBinder.toXml(obj, "utf-8");

        try {
            File file = new File(pathName);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(file, !isOverWrite);
            out.write(retXml.getBytes("utf-8"));
            out.close();
        }catch (Exception e){
        }
    }

    /**
     * 将xml文件读取成对象
     * @param cls
     * @param pathName
     * @param <T>
     * @return
     */
    public static <T> T readObjectFromXml(Class<T> cls,String pathName){
        JaxbUtil resultBinder = new JaxbUtil(cls,
        JaxbUtil.CollectionWrapper.class);
        return resultBinder.fromXml(FileTool.read(new File(pathName)));
    }

}
